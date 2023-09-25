package com.zznode.dhmp.data.page.domain;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zznode.dhmp.data.page.Select;
import com.zznode.dhmp.data.page.parser.DefaultOrderByParser;
import com.zznode.dhmp.data.page.parser.OrderByParser;
import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * 使用代理模式增强{@link org.springframework.data.domain.PageRequest }。
 * <p>提供便捷方法，在开发中更加方便地使用分页
 * <p>虽然{@link org.springframework.data.domain.PageRequest }是0基分页，不影响。我们还是使用的1基分页
 *
 * @author 王俊
 * @date create in 2023/7/3 10:54
 */
public class PageRequest implements Pageable {

    private final Pageable delegate;
    private OrderByParser orderByParser;

    private final static OrderByParser DEFAULT_ORDER_BY_PARSER = new DefaultOrderByParser();

    public void setParser(OrderByParser orderByParser) {
        Assert.notNull(orderByParser, "orderByParser cannot be null");
        this.orderByParser = orderByParser;
    }


    @SuppressWarnings("unused")
    private PageRequest() throws IllegalAccessException {
        throw new IllegalAccessException("construct cannot be called");
    }

    private PageRequest(Pageable page) {
        this(page, DEFAULT_ORDER_BY_PARSER);
    }

    private PageRequest(Pageable page, OrderByParser orderByParser) {
        Assert.notNull(page, "param page cannot be null");
        this.delegate = page;
        this.orderByParser = orderByParser;
    }

    public static PageRequest with(Pageable page) {
        return new PageRequest(page);
    }

    /**
     * 开始分页
     *
     * @param <E> 实体类型
     * @return 分页信息
     * @see PageHelper#startPage(int, int)
     */
    public <E> Page<E> startPage() {
        return startPageAndSort(Sort.unsorted());
    }


    /**
     * 分页并排序, 根据前端传的参数排序，如果前端没有传，使用defaultSort参数排序
     *
     * @param <E>         实体类型
     * @param defaultSort 排序，当{@link #getSort()}获取到的Sort对象中{@link Sort#isUnsorted() isUnsorted}为true时，使用的默认排序
     * @return 分页信息
     */
    public <E> Page<E> startPageAndSort(Sort defaultSort) {
        return PageHelper.startPage(getPageNumber(), getPageSize(), getOrderBy(defaultSort));
    }

    /**
     * 执行分页，返回分页结果
     *
     * @param select 查询
     * @param <E>    实体类型
     * @return PageResult
     */
    public <E> PageResult<E> doPage(Select<E> select) {
        Page<E> page = startPage();
        select.doSelect();
        return PageResult.of(page);
    }


    /**
     * 执行分页并排序，返回分页结果
     *
     * @param select      查询
     * @param <E>         实体类型
     * @param defaultSort 排序，当{@link #getSort()}获取到的Sort对象中{@link Sort#isUnsorted() isUnsorted}为true时，使用的默认排序
     * @return PageResult
     */
    public <E> PageResult<E> doPageAndSort(Select<E> select, Sort defaultSort) {
        Page<E> page = startPageAndSort(defaultSort);
        select.doSelect();
        return PageResult.of(page);
    }

    /**
     * 获取排序sql
     *
     * @param defaultSort 默认的sort
     * @return 排序sql
     */
    public String getOrderBy(Sort defaultSort) {
        Sort sort = getSortOr(defaultSort);
        return orderByParser.parseToOrderBy(sort);
    }

    @Override
    public boolean isPaged() {
        return delegate.isPaged();
    }

    @Override
    public boolean isUnpaged() {
        return delegate.isUnpaged();
    }

    @Override
    public int getPageNumber() {
        return delegate.getPageNumber();
    }

    @Override
    public int getPageSize() {
        return delegate.getPageSize();
    }

    @Override
    public long getOffset() {
        return delegate.getOffset();
    }

    @Override
    public Sort getSort() {
        return delegate.getSort();
    }

    @Override
    public Sort getSortOr(Sort sort) {
        return delegate.getSortOr(sort);
    }

    @Override
    public Pageable next() {
        return delegate.next();
    }

    @Override
    public Pageable previousOrFirst() {
        return delegate.previousOrFirst();
    }

    @Override
    public Pageable first() {
        return delegate.first();
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return delegate.withPage(pageNumber);
    }

    @Override
    public boolean hasPrevious() {
        return delegate.hasPrevious();
    }

    @Override
    public Optional<Pageable> toOptional() {
        return delegate.toOptional();
    }

    @Override
    public OffsetScrollPosition toScrollPosition() {
        return delegate.toScrollPosition();
    }
}
