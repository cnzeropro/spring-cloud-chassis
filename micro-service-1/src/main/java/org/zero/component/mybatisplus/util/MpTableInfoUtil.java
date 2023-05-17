package org.zero.component.mybatisplus.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.experimental.UtilityClass;
import org.zero.model.qo.BaseQO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2021/03/10
 */
@UtilityClass
public class MpTableInfoUtil {
    /**
     * 属性名（实体-驼峰命名法）转化为字段名（数据库-蛇形命名法）
     */
    public static String toColumn(final String property) {
        return StringUtils.camelToUnderline(property);
    }

    /**
     * 属性名（实体-驼峰命名法）转化为MP保存的字段名（数据库-蛇形命名法）
     */
    public static String toColumn(final Class<?> clazz, final String property) {
        return getTableFieldInfos(clazz).stream()
                .filter(tableFieldInfo -> Objects.equals(tableFieldInfo.getProperty(), property))
                .map(TableFieldInfo::getColumn)
                .findFirst()
                .orElse(null);
    }

    /**
     * 属性名（实体-驼峰命名法）转化为MP保存的字段名（数据库-蛇形命名法）
     */
    public static List<String> toColumns(final Class<?> clazz, final String[] properties) {
        if (ArrayUtils.isEmpty(properties)) {
            return new ArrayList<>();
        }

        return getTableFieldInfos(clazz).stream()
                .filter(tableFieldInfo -> Arrays.binarySearch(properties, tableFieldInfo.getProperty()) >= 0)
                .map(TableFieldInfo::getColumn)
                .collect(Collectors.toList());
    }

    /**
     * 获取MP保存的表字段信息
     */
    public static List<TableFieldInfo> getTableFieldInfos(final Class<?> clazz) {
        return Optional.ofNullable(TableInfoHelper.getTableInfo(clazz)).map(TableInfo::getFieldList).orElse(Collections.emptyList());
    }

    public static void setSelect(final QueryWrapper<?> queryWrapper, final Class<?> clazz, final String[] properties) {
        setSelect(queryWrapper, toColumns(clazz, properties));
    }

    public static void setSelect(final QueryWrapper<?> queryWrapper, final String[] columns) {
        setSelect(queryWrapper, Arrays.asList(columns));
    }

    public static void setSelect(final QueryWrapper<?> queryWrapper, final List<String> columns) {
        queryWrapper.select(columns);
    }

    public static void setGroup(final QueryWrapper<?> queryWrapper, final Class<?> clazz, final String[] properties) {
        setGroup(queryWrapper, toColumns(clazz, properties));
    }

    public static void setGroup(final QueryWrapper<?> queryWrapper, final String[] columns) {
        setGroup(queryWrapper, Arrays.asList(columns));
    }

    public static void setGroup(final QueryWrapper<?> queryWrapper, final List<String> columns) {
        queryWrapper.groupBy(columns);
    }

    public static void setOrder(final QueryWrapper<?> queryWrapper, final Class<?> clazz, final BaseQO.Collation[] collations) {
        setOrder(queryWrapper, Arrays.stream(collations)
                // 可替换为 'peek'
                .map(collation -> {
                    String column = toColumn(clazz, collation.getColumn());
                    collation.setColumn(column);
                    return collation;
                })
                .toArray(BaseQO.Collation[]::new));
    }

    public static void setOrder(final QueryWrapper<?> queryWrapper, final BaseQO.Collation[] collations) {
        for (BaseQO.Collation collation : collations) {
            queryWrapper.orderBy(StringUtils.isNotBlank(collation.getColumn()), collation.isAsc(), collation.getColumn());
        }
    }

    public static void setIn(final QueryWrapper<?> queryWrapper, Class<?> clazz, final BaseQO.MultiVal[] multiVals) {
        setIn(queryWrapper, Arrays.stream(multiVals)
                .map(multiVal -> {
                    String column = toColumn(clazz, multiVal.getColumn());
                    multiVal.setColumn(column);
                    return multiVal;
                })
                .toArray(BaseQO.MultiVal[]::new));
    }

    public static void setIn(final QueryWrapper<?> queryWrapper, final BaseQO.MultiVal[] multiVals) {
        for (BaseQO.MultiVal multiVal : multiVals) {
            queryWrapper.in(StringUtils.isNotBlank(multiVal.getColumn()) && ArrayUtils.isNotEmpty(multiVal.getValues()), multiVal.getColumn(), multiVal.getValues());
        }
    }

    public static void setBetween(final QueryWrapper<?> queryWrapper, Class<?> clazz, final BaseQO.Range[] ranges) {
        setBetween(queryWrapper, Arrays.stream(ranges)
                .peek(range -> {
                    String column = toColumn(clazz, range.getColumn());
                    range.setColumn(column);
                })
                .toArray(BaseQO.Range[]::new));
    }

    public static void setBetween(final QueryWrapper<?> queryWrapper, final BaseQO.Range[] ranges) {
        for (BaseQO.Range range : ranges) {
            queryWrapper.between(StringUtils.isNotBlank(range.getColumn()), range.getColumn(), range.getStartVal(), range.getEndVal());
        }
    }
}
