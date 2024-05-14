package com.zznode.dhmp.export.support.filler;

import org.springframework.util.ObjectUtils;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 对ArrayList的一层封装，用于判断列表中是否所有元素都是空
 *
 * @author 王俊
 */
public class ExportRow<T> extends ArrayList<T> {
    @Serial
    private static final long serialVersionUID = 6614240212914049033L;

    /**
     * 是否所有元素都是空
     */
    private boolean emptyRow = true;

    public boolean isEmptyRow() {
        return emptyRow;
    }

    private void checkElements(Collection<? extends T> c) {
        // 如果已经有非空元素，则直接返回
        if (!emptyRow) return;
        for (T t : c) {
            checkElement(t);
        }
    }

    private void checkElement(T element) {
        // 如果已经有非空元素，则直接返回
        if (!emptyRow) return;
        if (!ObjectUtils.isEmpty(element)) {
            emptyRow = false;
        }
    }

    @Override
    public boolean add(T t) {
        boolean add = super.add(t);
        checkElement(t);
        return add;
    }


    @Override
    public void add(int index, T element) {
        checkElement(element);
        super.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        checkElements(c);
        return super.addAll(c);
    }


    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        checkElements(c);
        return super.addAll(index, c);
    }


    @Override
    public T set(int index, T element) {
        checkElement(element);
        return super.set(index, element);
    }

}
