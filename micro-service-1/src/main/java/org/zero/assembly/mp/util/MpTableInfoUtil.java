package org.zero.assembly.mp.util;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.experimental.UtilityClass;
import org.zero.model.qo.BaseQO;

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
    public static String toColumn(final String field) {
        return StrUtil.toUnderlineCase(field);
    }

    public static String toColumn(Class<?> clazz, final String field) {
        return getTableFieldInfos(clazz).stream()
                .filter(tableFieldInfo -> Objects.equals(tableFieldInfo.getProperty(), field))
                .map(TableFieldInfo::getColumn)
                .findFirst()
                .orElse(null);
    }

    public static List<String> toColumns(Class<?> clazz, final String[] fields) {
        return getTableFieldInfos(clazz).stream()
                .filter(tableFieldInfo -> ArrayUtil.contains(fields, tableFieldInfo.getProperty()))
                .map(TableFieldInfo::getColumn)
                .collect(Collectors.toList());
    }

    public static List<TableFieldInfo> getTableFieldInfos(Class<?> clazz) {
        return Optional.ofNullable(TableInfoHelper.getTableInfo(clazz)).map(TableInfo::getFieldList).orElse(Collections.emptyList());
    }

    public static void setSelect(QueryWrapper<?> queryWrapper, Class<?> clazz, final String[] fields) {
        String[] columns = toColumns(clazz, fields).toArray(new String[0]);
        setSelect(queryWrapper, columns);
    }

    public static void setSelect(QueryWrapper<?> queryWrapper, final String[] columns) {
        queryWrapper.select(columns);
    }

    public static void setGroup(QueryWrapper<?> queryWrapper, Class<?> clazz, final String[] fields) {
        String[] columns = toColumns(clazz, fields).toArray(new String[0]);
        setGroup(queryWrapper, columns);
    }

    public static void setGroup(QueryWrapper<?> queryWrapper, final String[] columns) {
        queryWrapper.groupBy(ListUtil.toList(columns));
    }

    public static void setOrder(QueryWrapper<?> queryWrapper, Class<?> clazz, final BaseQO.Collation[] collations) {
        // 可替换为 'peek'
        setOrder(queryWrapper, Arrays.stream(collations).map(collation -> {
            String column = toColumn(clazz, collation.getColumn());
            collation.setColumn(column);
            return collation;
        }).toArray(BaseQO.Collation[]::new));
    }

    public static void setOrder(QueryWrapper<?> queryWrapper, final BaseQO.Collation[] collations) {
        for (BaseQO.Collation collation : collations) {
            queryWrapper.orderBy(StrUtil.isNotEmpty(collation.getColumn()), collation.isAsc(), collation.getColumn());
        }
    }

    public static void setIn(QueryWrapper<?> queryWrapper, Class<?> clazz, final BaseQO.MultiVal[] multiVals) {
        // 可替换为 'peek'
        setIn(queryWrapper, Arrays.stream(multiVals).map(multiVal -> {
            String column = toColumn(clazz, multiVal.getColumn());
            multiVal.setColumn(column);
            return multiVal;
        }).toArray(BaseQO.MultiVal[]::new));
    }

    public static void setIn(QueryWrapper<?> queryWrapper, final BaseQO.MultiVal[] multiVals) {
        for (BaseQO.MultiVal multiVal : multiVals) {
            queryWrapper.in(StrUtil.isNotEmpty(multiVal.getColumn()) && ArrayUtil.isNotEmpty(multiVal.getValues()), multiVal.getColumn(), multiVal.getValues());
        }
    }

    public static void setBetween(QueryWrapper<?> queryWrapper, Class<?> clazz, final BaseQO.Range[] ranges) {
        // 可替换为 'peek'
        setBetween(queryWrapper, Arrays.stream(ranges).peek(range -> {
            String column = toColumn(clazz, range.getColumn());
            range.setColumn(column);
        }).toArray(BaseQO.Range[]::new));
    }

    public static void setBetween(QueryWrapper<?> queryWrapper, final BaseQO.Range[] ranges) {
        for (BaseQO.Range range : ranges) {
            queryWrapper.between(StrUtil.isNotEmpty(range.getColumn()), range.getColumn(), range.getStartVal(), range.getEndVal());
        }
    }
}
