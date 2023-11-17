package org.zero.demo.spring.boot.web.mvc.controller;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.poi.excel.ExcelUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * @author zero
 * @since 2019/10/18
 */
@Slf4j
@RestController
@RequestMapping("download")
public class DownloadController {
    @GetMapping("/d1")
    @SneakyThrows
    public void d1(@RequestParam(required = false, defaultValue = "true") boolean isXlsx, HttpServletResponse response) {
        response.setContentType(isXlsx ? ExcelUtil.XLSX_CONTENT_TYPE : ExcelUtil.XLS_CONTENT_TYPE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String fileName = "";
        String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
        response.setHeader("Cache-Control", "no-store");
        // 构建FastByteArrayOutputStream
        try (FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
             ServletOutputStream servletOutputStream = response.getOutputStream()) {
            response.setContentLength(outputStream.size());
            outputStream.writeTo(servletOutputStream);
        }
    }

    @GetMapping("/d2")
    public ResponseEntity<ByteArrayResource> d2(@RequestParam(required = false, defaultValue = "true") boolean isXlsx) {
        // 构建ByteArrayResource
        ByteArrayResource byteArrayResource = new ByteArrayResource(new byte[0]);
        String fileName = "";
        String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentLength(byteArrayResource.contentLength())
                // .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentType(MediaType.parseMediaType(isXlsx ? ExcelUtil.XLSX_CONTENT_TYPE : ExcelUtil.XLS_CONTENT_TYPE))
                .allow(HttpMethod.GET)
                .cacheControl(CacheControl.noStore())
                .eTag(DigestUtil.sha256Hex(byteArrayResource.getByteArray()))
                .lastModified(Instant.now())
                .header("Content-Disposition", "attachment;filename=" + encodedFileName)
                .body(byteArrayResource);
    }
}
