package org.zero.common.api.extra.loki.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zero
 * @since 2023/9/14
 */
@Data
public class LokiPushDTO {
    private List<Stream> streams;

    public static class Stream {
        private StreamIn stream;
        private List<List<String>> values;

        public static class StreamIn {
            private String label;
        }
    }
}
