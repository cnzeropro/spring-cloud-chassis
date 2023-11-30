package org.zero.demo.spring.boot.web.mvc.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.unit.DataSize;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author zero
 * @since 2022/11/21
 */
@Getter
public class RangeInfo {
    // public static final String RANGE_REGEX = "^bytes=((?:\\d+-\\d*|-?\\d+)(?:,(?:\\d+-\\d*|-?\\d+))*)$";
    public static final String RANGE_REGEX = "^.*bytes=((?:\\d+-\\d*|-?\\d+)(?:,(?:\\d+-\\d*|-?\\d+))*)$";
    public static final String BYTES = "Bytes";
    public static final String KILOBYTES = "Kilobytes";
    public static final String MEGABYTES = "Megabytes";
    public static final String GIGABYTES = "Gigabytes";
    public static final String TERABYTES = "Terabytes";

    private static final Pattern RANGE_PATTERN = Pattern.compile(RANGE_REGEX);

    private final String dataUnit;
    private long total = 0L;
    private long totalByte = 0L;
    private List<Slice> slices = new ArrayList<>();

    public static RangeInfo parse(String range) {
        // 不存在Range请求头
        if (StrUtil.isBlank(range)) {
            return null;
        }
        // Range请求头内容格式不匹配（如果前端按照RFC 7233规范传入则一般不会）
        if (!RANGE_PATTERN.matcher(range).matches()) {
            throw new IllegalArgumentException("Invalid range: " + range);
        }
        // Range请求头解析
        // Range请求头示例：bytes=0-50,-150,300-
        String dataUnit = StrUtil.subBefore(range, "=", false);
        String dataRangeStr = StrUtil.subAfter(range, "=", false);
        List<String> dataRanges = StrUtil.split(dataRangeStr, ",");
        // 数据范围未指定（已做正则匹配，基本不可能出现这种情况）
        if (CollUtil.isEmpty(dataRanges)) {
            throw new IllegalArgumentException("Invalid range: " + range);
        }
        // 生成Range信息
        RangeInfo rangeInfo = new RangeInfo(dataUnit);
        List<Slice> slices = dataRanges.stream()
                .map(dataRange -> {
                    // 数据范围解析
                    String startStr = StrUtil.subBefore(dataRange, "-", false);
                    String endStr = StrUtil.subAfter(dataRange, "-", false);
                    // 开始和结束位置均未指定（”-“的情况）（已做正则匹配，基本不可能出现这种情况）
                    if (StrUtil.isBlank(startStr) && StrUtil.isBlank(endStr)) {
                        throw new IllegalArgumentException("Invalid range slice");
                    }
                    // 开始位置
                    long start = 0L;
                    if (StrUtil.isNotBlank(startStr)) {
                        start = Long.parseLong(startStr);
                    }
                    // 结束位置
                    long end = -1L;
                    if (StrUtil.isNotBlank(endStr)) {
                        end = Long.parseLong(endStr);
                    }
                    return rangeInfo.new Slice(start, end);
                })
                .collect(Collectors.toList());
        rangeInfo.setSlices(slices);
        return rangeInfo;
    }

    private RangeInfo(String dataUnit) {
        this.dataUnit = dataUnit;
    }

    private void setSlices(List<Slice> slices) {
        this.slices = slices;
    }

    public RangeInfo setTotalByte(long totalByte) {
        this.totalByte = totalByte;
        return setTotalFromByte(totalByte);
    }

    private RangeInfo setTotalFromByte(long totalByte) {
        // HTTP规范中主要使用"bytes"作为单位，其他虽然没有限制，但并不推荐
        DataSize dataSize = DataSize.ofBytes(totalByte);
        if (BYTES.equalsIgnoreCase(dataUnit)) {
            this.total = dataSize.toBytes();
        } else if (KILOBYTES.equalsIgnoreCase(dataUnit)) {
            this.total = dataSize.toKilobytes();
        } else if (MEGABYTES.equalsIgnoreCase(dataUnit)) {
            this.total = dataSize.toGigabytes();
        } else if (GIGABYTES.equalsIgnoreCase(dataUnit)) {
            this.total = dataSize.toGigabytes();
        } else if (TERABYTES.equalsIgnoreCase(dataUnit)) {
            this.total = dataSize.toTerabytes();
        } else {
            // this.total = totalByte;
            throw new IllegalArgumentException("Unsupported DataUnit: " + dataUnit);
        }
        return this;
    }

    private long getByte(long length) {
        DataSize dataSize;
        // HTTP规范中主要使用"bytes"作为单位，其他虽然没有限制，但并不推荐
        if (BYTES.equalsIgnoreCase(dataUnit)) {
            dataSize = DataSize.ofBytes(length);
        } else if (KILOBYTES.equalsIgnoreCase(dataUnit)) {
            dataSize = DataSize.ofKilobytes(length);
        } else if (MEGABYTES.equalsIgnoreCase(dataUnit)) {
            dataSize = DataSize.ofMegabytes(length);
        } else if (GIGABYTES.equalsIgnoreCase(dataUnit)) {
            dataSize = DataSize.ofGigabytes(length);
        } else if (TERABYTES.equalsIgnoreCase(dataUnit)) {
            dataSize = DataSize.ofTerabytes(length);
        } else {
            // dataSize = DataSize.ofBytes(length);
            throw new IllegalArgumentException("Unsupported DataUnit: " + dataUnit);
        }
        return dataSize.toBytes();
    }

    public class Slice {
        @Getter
        private long start = 0L;
        private long end = -1L;

        public long getStartByte() {
            return getByte(start);
        }

        public long getEnd() {
            // 如果结束位置未指定，默认使用资源的总大小
            if (end == -1L) {
                end = getTotal();
            }
            return end;
        }

        public long getEndByte() {
            // 如果结束位置未指定，默认使用资源的总大小
            if (end == -1L) {
                return getTotalByte();
            }
            return getByte(end);
        }

        private Slice(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }
}
