package org.zero.log.common.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.experimental.UtilityClass;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author zero
 * @since 2022/8/25
 */
@UtilityClass
public class LogUtil {
    public void excludeInJson(JsonNode jsonNode, String[] excludeKeys) {
        if (ObjectUtils.isEmpty(excludeKeys) || Objects.isNull(jsonNode)) {
            return;
        }

        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            for (String excludeKey : excludeKeys) {
                objectNode.remove(excludeKey);
            }
            objectNode.fields().forEachRemaining(entry -> excludeInJson(entry.getValue(), excludeKeys));
        } else if (jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                excludeInJson(node, excludeKeys);
            }
        }
    }

    public Map<String, String[]> excludeInParamMap(Map<String, String[]> paramMap, String[] excludeKeys) {
        if (Objects.isNull(paramMap)) {
            return MapUtil.empty();
        }

        if (ObjectUtils.isEmpty(excludeKeys)) {
            return paramMap;
        }

        return MapUtil.filter(paramMap, entry -> ArrayUtil.contains(excludeKeys, entry.getKey()));
    }

    public String excludeInQueryStr(String queryStr, String[] excludeKeys) {
        if (CharSequenceUtil.isBlank(queryStr)) {
            return "";
        }

        if (ObjectUtils.isEmpty(excludeKeys)) {
            return queryStr;
        }

        String result = queryStr;
        for (String excludeKey : excludeKeys) {
            String regex = excludeKey + "=[^&]*&?";
            result = result.replaceAll(regex, "");
        }
        // 去除最后一个可能的"&"
        result = result.replaceAll("&$", "");
        return result;
    }
}
