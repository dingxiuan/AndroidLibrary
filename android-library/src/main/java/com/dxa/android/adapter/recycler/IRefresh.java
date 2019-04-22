package com.dxa.android.adapter.recycler;

import java.util.Collection;
import java.util.List;

/**
 * RecyclerAdapter接口
 */

public interface IRefresh<T> {
    /**
     * 获得Item的条数
     */
    int getCount();

    /**
     * 获得所有的Item
     */
    List<T> getItems();

    /**
     * 根据位置获得Item
     */
    T getItem(int position);

    /**
     * 添加对象
     */
    boolean add(T t);

    /**
     * 添加数组
     */
    boolean addAll(T[] array);

    /**
     * 添加一个集合的数据
     *
     * @param c 集合
     * @return 是否添加
     */
    boolean addAll(Collection<T> c);

    /**
     * 移除
     *
     * @param position 移除的位置
     * @return 移除的对象
     */
    T remove(int position);

    /**
     * 移除
     *
     * @param from 开始移除的位置
     * @param to   结束移除的位置
     * @return 是否移除
     */
    boolean remove(int from, int to);

    /**
     * 移除全部对象
     *
     * @param subItems 子集合
     * @return 是否移除
     */
    boolean removeAll(Collection<T> subItems);

    /**
     * 此位置上是否包含对象
     *
     * @param position 对象的位置
     * @return 包含返回true，否则false
     */
    boolean contains(int position);

    /**
     * 是否包含对象
     *
     * @param t 对象
     * @return 包含返回true，否则false
     */
    boolean contains(T t);

    /**
     * 清理
     */
    void clear();

    /**
     * 设置集合中的对象是否允许重复
     */
    void setRepeat(boolean repeat);

    /**
     * 获取集合中的对象是否允许重复
     */
    boolean isRepeat();
}
