package org.zero.demo.spring.boot.web.mvc.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;
import org.zero.common.data.util.java.io.ByteArrayOutputStreamWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * 文件下载示例
 *
 * @author zero
 * @since 2019/10/18
 */
@Slf4j
@RestController
@RequestMapping("download")
public class DownloadController {
    /**
     * 全量下载 -
     * 直接写入HttpServletResponse输出流中
     */
    @SneakyThrows
    @GetMapping("/d1")
    public void d1(@RequestParam(required = false, defaultValue = "true") boolean isXlsx, HttpServletResponse response) {
        // TODO: 换成实际的输入流（需要响应的数据）
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // 此处使用FastByteArrayOutputStream倒腾一下：
        // 1、方便获取ContentLength
        // 2、方便计算摘要值
        // 3、可以把这块数据生成以及copy移到Service层，并以FastByteArrayOutputStream作为方法返回
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        long copied = IoUtil.copy(in, out);
        IoUtil.close(in);

        // 设置响应状态码
        response.setStatus(HttpServletResponse.SC_OK);
        // 设置相关响应头
        String fileName = "";
        String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
        response.setContentType(isXlsx ? ExcelUtil.XLSX_CONTENT_TYPE : ExcelUtil.XLS_CONTENT_TYPE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
        response.setHeader("Cache-Control", "no-store");
        // response.setContentLength(out.size());
        response.setContentLength((int) copied);

        // 写入到响应的输出流
        ServletOutputStream output = response.getOutputStream();
        out.writeTo(output);
        IoUtil.close(out);
        IoUtil.close(output);
    }

    /**
     * 全量下载 -
     * 使用Spring提供的ResponseEntity
     */
    @SneakyThrows
    @GetMapping("/d2")
    public ResponseEntity<Resource> d2(@RequestParam(required = false, defaultValue = "true") boolean isXlsx) {
        // TODO: 换成实际的输入流（需要响应的数据）
        InputStream in = new ByteArrayInputStream(new byte[0]);

        byte[] data = IoUtil.readBytes(in);
        IoUtil.close(in);
        // 生成文件名
        String fileName = "";
        String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
        // 构建Resource
        Resource resource = new ByteArrayResource(data);
        // 构建ResponseEntity并返回
        return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                // .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentType(MediaType.parseMediaType(isXlsx ? ExcelUtil.XLSX_CONTENT_TYPE : ExcelUtil.XLS_CONTENT_TYPE))
                .allow(HttpMethod.GET)
                .cacheControl(CacheControl.noStore())
                // 可以将其解析为绝对文件路径的资源才能使用Resource.lastModified()，否则会抛出FileNotFoundException
                // .lastModified(resource.lastModified())
                .lastModified(Instant.now())
                .header("Content-Disposition", "attachment;filename=" + encodedFileName)
                .body(resource);
    }

    /**
     * 分片下载 -
     * 直接写入HttpServletResponse输出流中
     */
    @SneakyThrows
    @GetMapping("/d3")
    public void d3(@Header(value = "Range", required = false) String range, HttpServletResponse response) {
        // 解析生成Range请求头信息
        RangeInfo rangeInfo = RangeInfo.parse(range);
        // 不存在Range请求头时全量下载
        if (Objects.isNull(rangeInfo)) {
            // TODO: 换成实际的输入流（需要响应的数据）
            InputStream in = new ByteArrayInputStream(new byte[0]);
            FastByteArrayOutputStream out = new FastByteArrayOutputStream();
            long copied = IoUtil.copy(in, out);
            IoUtil.close(in);
            response.setStatus(HttpServletResponse.SC_OK);
            String fileName = "";
            String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
            response.setHeader("Cache-Control", "no-store");
            response.setContentLength((int) copied);
            ServletOutputStream output = response.getOutputStream();
            out.writeTo(output);
            IoUtil.close(out);
            IoUtil.close(output);
            return;
        }

        // TODO: 获取数据总大小
        // 使用InputStream.available()方法获取流中总字节数可能会存在某些问题，特别是网络流
        // int totalByte = IoUtil.toAvailableStream(in).available();
        long totalByte = 0L;
        rangeInfo.setTotalByte(totalByte);
        List<RangeInfo.Slice> slices = rangeInfo.getSlices();
        // 单一范围
        if (slices.size() == 1) {
            RangeInfo.Slice slice = slices.get(0);
            String dataUnit = rangeInfo.getDataUnit();
            long total = rangeInfo.getTotal();
            long end = slice.getEnd();
            long startByte = slice.getStartByte();
            long start = slice.getStart();
            long endByte = slice.getEndByte();

            // 边界位置不对或者请求的范围越界（范围值超过了资源的大小），返回416 Requested Range Not Satisfiable
            if (startByte > endByte || startByte >= totalByte || endByte > totalByte) {
                // TODO: 此处建议直接抛出异常交给全局异常捕捉处理或者响应成统一JSON返回体
                // response.setHeader("Content-Range", String.format("%s */%d", dataUnit, total));
                // response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                return;
            }

            // TODO: 换成实际的输入流（需要响应的数据）
            InputStream in = new ByteArrayInputStream(new byte[0]);

            // 跳过开始位置之前的字节数据
            in.skip(startByte);
            long length = endByte - startByte + 1;
            final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
            long copied = IoUtil.copy(in, out, IoUtil.DEFAULT_BUFFER_SIZE, length, null);
            IoUtil.close(in);

            // 设置响应状态码
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            // 设置相关响应头
            String fileName = "";
            String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
            response.setHeader("Cache-Control", "no-store");
            // 从正常情况来时，copied=length=out.size()，三选一即可
            // response.setContentLength((int) length);
            // response.setContentLength(out.size());
            response.setContentLength((int) copied);
            response.setHeader("Accept-Ranges", dataUnit);
            response.setHeader("Content-Range", String.format("%s %d-%d/%d", dataUnit, start, end, total));

            // 写入到响应的输出流
            ServletOutputStream output = response.getOutputStream();
            out.writeTo(output);
            IoUtil.close(out);
            IoUtil.close(output);
        }

        // 多重范围
        ServletOutputStream output = response.getOutputStream();
        long len = 0L;
        for (RangeInfo.Slice slice : slices) {
            String dataUnit = rangeInfo.getDataUnit();
            long total = rangeInfo.getTotal();
            long end = slice.getEnd();
            long startByte = slice.getStartByte();
            long start = slice.getStart();
            long endByte = slice.getEndByte();

            // 边界位置不对或者请求的范围越界（范围值超过了资源的大小），返回416 Requested Range Not Satisfiable
            if (startByte > endByte || startByte >= totalByte || endByte > totalByte) {
                // TODO: 此处建议直接抛出异常交给全局异常捕捉处理或者响应成统一JSON返回体
                return;
            }

            // TODO: 换成实际的输入流（需要响应的数据）
            InputStream in = new ByteArrayInputStream(new byte[0]);

            // 跳过开始位置之前的字节数据
            in.skip(startByte);
            long length = endByte - startByte + 1;
            final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
            long copied = IoUtil.copy(in, out, IoUtil.DEFAULT_BUFFER_SIZE, length, null);
            IoUtil.close(in);

            // 数据写入到响应的输出流
            output.println();
            output.println("--MULTIPART_BYTERANGES");
            output.println("Content-Type: application/octet-stream;charset=UTF-8");
            output.println("Content-Length: " + copied);
            output.println(String.format("Content-Range: %s %d-%d/%d", dataUnit, start, end, total));
            out.writeTo(output);
            IoUtil.close(out);
            len += copied;
        }
        // 设置响应状态码
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        // 设置相关响应头
        response.setContentType("multipart/byteranges; boundary=MULTIPART_BYTERANGES");
        response.setHeader("Accept-Ranges", rangeInfo.getDataUnit());
        response.setContentLength((int) len);

        // 数据写入到响应的输出流
        output.println();
        output.println("--MULTIPART_BYTERANGES--");
        IoUtil.close(output);
    }

    /**
     * 分片下载 -
     * 使用Spring提供的ResponseEntity
     */
    @GetMapping("/d4")
    @SneakyThrows
    public ResponseEntity<Resource> d4(@Header(value = "Range", required = false) String range) {
        // 生成Range请求头信息
        RangeInfo rangeInfo = RangeInfo.parse(range);
        // 不存在Range请求头时全量下载
        if (Objects.isNull(rangeInfo)) {
            // TODO: 换成实际的输入流（需要响应的数据）
            InputStream in = new ByteArrayInputStream(new byte[0]);
            byte[] data = IoUtil.readBytes(in);
            IoUtil.close(in);
            String fileName = "";
            String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
            Resource resource = new ByteArrayResource(data);
            return ResponseEntity.ok()
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .allow(HttpMethod.GET)
                    .cacheControl(CacheControl.noStore())
                    .lastModified(Instant.now())
                    .header("Content-Disposition", "attachment;filename=" + encodedFileName)
                    .body(resource);
        }

        // TODO: 获取数据总大小
        // 使用InputStream.available()方法获取流中总字节数可能会存在某些问题，特别是网络流
        // int totalByte = IoUtil.toAvailableStream(in).available();
        long totalByte = 0L;
        rangeInfo.setTotalByte(totalByte);
        List<RangeInfo.Slice> slices = rangeInfo.getSlices();
        // 单一范围
        if (slices.size() == 1) {
            RangeInfo.Slice slice = slices.get(0);
            String dataUnit = rangeInfo.getDataUnit();
            long total = rangeInfo.getTotal();
            long end = slice.getEnd();
            long startByte = slice.getStartByte();
            long start = slice.getStart();
            long endByte = slice.getEndByte();

            // 边界位置不对或者请求的范围越界（范围值超过了资源的大小），返回416 Requested Range Not Satisfiable
            if (startByte > endByte || startByte >= totalByte || endByte > totalByte) {
                // TODO: 此处建议直接抛出异常交给全局异常捕捉处理或者响应成统一JSON返回体
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                        .header("Content-Range", String.format("%s */%d", dataUnit, total))
                        .build();
            }

            // TODO: 换成实际的输入流（需要响应的数据）
            InputStream in = new ByteArrayInputStream(new byte[0]);

            // 跳过开始位置之前的字节数据
            in.skip(startByte);
            long length = endByte - startByte + 1;
            final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
            IoUtil.copy(in, out, IoUtil.DEFAULT_BUFFER_SIZE, length, null);
            IoUtil.close(in);

            Resource resource = new ByteArrayResource(out.toByteArray());
            IoUtil.close(out);
            String fileName = "";
            String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .allow(HttpMethod.GET)
                    .cacheControl(CacheControl.noStore())
                    .lastModified(Instant.now())
                    .header("Accept-Ranges", dataUnit)
                    .header("Content-Range", String.format("%s %d-%d/%d", dataUnit, start, end, total))
                    .header("Content-Disposition", "attachment;filename=" + encodedFileName)
                    .body(resource);
        }

        // 多重范围
        String dataUnit = rangeInfo.getDataUnit();
        ByteArrayOutputStreamWriter output = new ByteArrayOutputStreamWriter();
        long len = 0L;
        for (RangeInfo.Slice slice : slices) {
            long total = rangeInfo.getTotal();
            long end = slice.getEnd();
            long startByte = slice.getStartByte();
            long start = slice.getStart();
            long endByte = slice.getEndByte();

            // 边界位置不对或者请求的范围越界（范围值超过了资源的大小），返回416 Requested Range Not Satisfiable
            if (startByte > endByte || startByte >= totalByte || endByte > totalByte) {
                // TODO: 此处建议直接抛出异常交给全局异常捕捉处理或者响应成统一JSON返回体
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                        .header("Content-Range", String.format("%s */%d", dataUnit, total))
                        .build();
            }

            // TODO: 换成实际的输入流（需要响应的数据）
            InputStream in = new ByteArrayInputStream(new byte[0]);

            // 跳过开始位置之前的字节数据
            in.skip(startByte);
            long length = endByte - startByte + 1;
            final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
            long copied = IoUtil.copy(in, out, IoUtil.DEFAULT_BUFFER_SIZE, length, null);

            // 数据写入到响应的输出流
            output.println();
            output.println("--MULTIPART_BYTERANGES");
            output.println("Content-Type: application/octet-stream;charset=UTF-8");
            output.println("Content-Length: " + copied);
            output.println(String.format("Content-Range: %s %d-%d/%d", dataUnit, start, end, total));
            out.writeTo(output);
            IoUtil.close(out);
            len += copied;
        }
        // 数据写入到响应的输出流
        output.println();
        output.println("--MULTIPART_BYTERANGES--");

        // 构建Resource
        Resource resource = new ByteArrayResource(output.toByteArray());
        IoUtil.close(output);
        // 文件名称处理
        String fileName = "";
        String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
        // 构建ResponseEntity并返回
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentLength(len)
                .contentType(MediaType.parseMediaType("multipart/byteranges;boundary=MULTIPART_BYTERANGES"))
                .allow(HttpMethod.GET)
                .cacheControl(CacheControl.noStore())
                .lastModified(Instant.now())
                .header("Accept-Ranges", dataUnit)
                .header("Content-Disposition", "attachment;filename=" + encodedFileName)
                .body(resource);
    }
}
