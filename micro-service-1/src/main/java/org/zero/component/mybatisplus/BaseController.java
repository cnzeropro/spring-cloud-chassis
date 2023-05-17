package org.zero.component.mybatisplus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.zero.model.po.BasePO;
import org.zero.model.vo.Result;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.zero.model.dto.Page.DEFAULT_PAGE_SIZE_STR;

/**
 * @author zero
 * @since 2023/5/16
 */
@Slf4j
public abstract class BaseController<S extends IService<T>, T extends BasePO> {
    protected S baseService;

    /**
     * 如果有多个类型的业务类在spring容器中，请重写该set方法用于注入指定类型
     */
    @Autowired
    public void setBaseService(S baseService) {
        this.baseService = baseService;
    }

    public S getBaseService() {
        return baseService;
    }

    /* ====================================================================== R(Read) ====================================================================== */
    /* ********************************************************************** get ********************************************************************** */
    @GetMapping(path = "/get", params = {"id"})
    public Result<T> get(@RequestParam Serializable id) {
        return getById(id);
    }

    @GetMapping("/getById")
    public Result<T> getById(@RequestParam Serializable id) {
        T dbRecord = baseService.getById(id);
        if (Objects.isNull(dbRecord)) {
            return Result.error("数据不存在！");
        }
        return Result.ok(dbRecord);
    }

    @GetMapping("/get-by-id")
    public Result<T> getByIdWithKebabCase(@RequestParam Serializable id) {
        return getById(id);
    }

    @GetMapping("/getById/{id}")
    public Result<T> getByIdWithPathVariable(@PathVariable Serializable id) {
        return getById(id);
    }

    @GetMapping("/get-by-id/{id}")
    public Result<T> getByIdWithPathVariableAndKebabCase(@PathVariable Serializable id) {
        return getById(id);
    }

    /* ********************************************************************** list ********************************************************************** */
    @GetMapping("/listByIds")
    public Result<List<T>> listByIds(@RequestParam Collection<? extends Serializable> ids) {
        return Result.ok(baseService.listByIds(ids));
    }

    @GetMapping("/list-by-ids")
    public Result<List<T>> listByIdsWithKebabCase(@RequestParam Collection<? extends Serializable> ids) {
        return listByIds(ids);
    }

    @GetMapping("/listByIds/{ids}")
    public Result<List<T>> listByIdsWithPathVariable(@PathVariable Collection<? extends Serializable> ids) {
        return listByIds(ids);
    }

    @GetMapping("/list-by-ids/{ids}")
    public Result<List<T>> listByIdsWithPathVariableAndKebabCase(@PathVariable Collection<? extends Serializable> ids) {
        return listByIds(ids);
    }

    @GetMapping("/list")
    public Result<List<T>> list() {
        return Result.ok(baseService.list());
    }

    /* ********************************************************************** page ********************************************************************** */
    @GetMapping("/page")
    public Result<IPage<T>> page(@RequestParam(defaultValue = "1") long pageNum,
                                 @RequestParam(defaultValue = DEFAULT_PAGE_SIZE_STR) long pageSize) {
        return Result.ok(baseService.page(Page.of(pageNum, pageSize)));
    }

    /* ********************************************************************** count ********************************************************************** */
    @GetMapping("/count")
    public Result<Long> count() {
        return Result.ok(baseService.count());
    }

    /* ====================================================================== C(Create) ====================================================================== */
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody T entity) {
        return Result.ok(baseService.save(entity));
    }

    @PostMapping("/saveBatch")
    public Result<Boolean> saveBatch(@RequestBody Collection<T> entities) {
        return Result.ok(baseService.saveBatch(entities));
    }

    @PostMapping("/save-batch")
    public Result<Boolean> saveBatchWithKebabCase(@RequestBody Collection<T> entities) {
        return saveBatch(entities);
    }

    /* ====================================================================== U(Update) ====================================================================== */
    @PutMapping("/updateById")
    public Result<Boolean> updateById(@RequestBody T entity) {
        return Result.ok(baseService.updateById(entity));
    }

    @PutMapping("/update-by-id")
    public Result<Boolean> updateByIdWithKebabCase(@RequestBody T entity) {
        return updateById(entity);
    }

    @PutMapping("/updateBatchById")
    public Result<Boolean> updateBatchById(@RequestBody Collection<T> entities) {
        return Result.ok(baseService.updateBatchById(entities));
    }

    @PutMapping("/update-batch-by-id")
    public Result<Boolean> updateBatchByIdWithKebabCase(@RequestBody Collection<T> entities) {
        return updateBatchById(entities);
    }

    /* ====================================================================== D(Delete) ====================================================================== */
    @DeleteMapping("/deleteById")
    public Result<Boolean> deleteById(@RequestParam Serializable id) {
        return Result.ok(baseService.removeById(id));
    }

    @DeleteMapping("/delete-by-id")
    public Result<Boolean> deleteByIdWithKebabCase(@RequestParam Serializable id) {
        return deleteById(id);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result<Boolean> deleteByIdWithPathVariable(@PathVariable Serializable id) {
        return deleteById(id);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public Result<Boolean> deleteByIdWithPathVariableAndKebabCase(@PathVariable Serializable id) {
        return deleteById(id);
    }

    @DeleteMapping("/deleteByIds")
    public Result<Boolean> deleteByIds(@RequestParam Collection<? extends Serializable> ids) {
        return Result.ok(baseService.removeByIds(ids));
    }

    @DeleteMapping("/delete-by-ids")
    public Result<Boolean> deleteByIdsWithKebabCase(@RequestParam Collection<? extends Serializable> ids) {
        return deleteByIds(ids);
    }

    @DeleteMapping("/deleteByIds/{ids}")
    public Result<Boolean> deleteByIdsWithPathVariable(@PathVariable Collection<? extends Serializable> ids) {
        return deleteByIds(ids);
    }

    @DeleteMapping("/delete-by-ids/{ids}")
    public Result<Boolean> deleteByIdsWithPathVariableAndKebabCase(@PathVariable Collection<? extends Serializable> ids) {
        return deleteByIds(ids);
    }
}
