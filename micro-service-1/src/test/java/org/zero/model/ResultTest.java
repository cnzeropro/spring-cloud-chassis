package org.zero.model;

import cn.hutool.core.collection.ListUtil;
import org.junit.jupiter.api.Test;
import org.zero.constant.SysError;
import org.zero.model.dto.Page;
import org.zero.model.vo.Result;

import java.util.Date;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/29
 */
class ResultTest {

    @Test
    void succeed() {
        Result<Void> okVoid = Result.ok();
        System.out.println(okVoid);
        Result<Integer> okInt = Result.ok(10);
        System.out.println(okInt);
        Result<Page<StudentPO>> okPageStudent = Result.ok("分页查询成功", Page.<StudentPO>of()
                .setPageSize(20L)
                .setCurrentPage(12L)
                .setRecordCount(107L)
                .setRecords(ListUtil.of(StudentPO.builder()
                                .id(1L)
                                .sid("s00001")
                                .name("小明")
                                .build(),
                        StudentPO.builder()
                                .id(2L)
                                .sid("s00002")
                                .name("小红")
                                .build())));
        System.out.println(okPageStudent);
    }

    @Test
    void error() {
        Result<Void> error = Result.error("登录失败", "A10001", "用户名或密码错误");
        System.out.println(error);
    }

    @Test
    void fail() {
        Result<Void> failVoid = Result.fail();
        System.out.println(failVoid);
        Result<Double> failDouble = Result.fail("fail");
        System.out.println(failDouble);
        Result<Date> failDate = Result.fail("fail");
        System.out.println(failDate);
    }

    @Test
    void of() {
        Result<Void> of = Result.fail(404, "资源未找到", SysError.ERROR);
        System.out.println(of);
    }
}