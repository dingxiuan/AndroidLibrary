package com.dxa.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 抽象的BaseAdapter子类，主要用于ListView和GridView及其子类；
 * 继承自此抽象类的子类只需要实现BaseAdapter的getView()方法即可。
 * 当创建子类时，如果没有传入List集合，则默认使用ArrayList，期间也可以设置其他的List对象。
 * @function: 可以进行增删改查或者清空等基本操作，添加之后会自动刷新视图.
 */
public abstract class AbstractBaseAdapter<T> extends BaseAdapter  {

    private final Context mContext;
    private final List<T> items;
    private final LayoutInflater mInflater;

    /**
     * 是否包含同于个对象
     */
    private boolean repeat = false;

    public AbstractBaseAdapter(Context context) {
        if (context == null)
            throw new NullPointerException("Context's object must not null !");

        this.mContext = context;
        this.items = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
    }

    public AbstractBaseAdapter(Context context, List<T> list) {
        this(context);
        if (list != null) {
            this.items.addAll(list);
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    protected Context getContext() {
        return mContext;
    }

    /**
     * 获取布局加载器
     *
     * @return
     */
    protected LayoutInflater getInflater() {
        return mInflater;
    }


    /**
     * 添加一个Item
     *
     * @param t
     */
    public void add(T t) {
        if (!repeat && notContains(t))
            return;

        items.add(t);
        notifyDataSetChanged();
    }

    /**
     * 添加一个集合
     */
    public void addAll(List<T> list) {
        if (isEmpty(list))
            return;

        if (!repeat) {
            ArrayList<T> ts = new ArrayList<>();
            for (T t : list) {
                if (notContains(t))
                    ts.add(t);
            }
            list = ts;

            if (list.isEmpty())
                return;
        }

        items.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 移除一个Item根据位置
     */
    public T remove(int position) {
        T remove = null;
        if (position >= 0 && position < getCount()) {
            remove = items.remove(position);
            notifyDataSetChanged();
        }
        return remove;
    }

    /**
     * 移除一个Item对象
     */
    public boolean remove(T t) {
        boolean remove = false;
        if (t != null && items.contains(t)) {
            remove = items.remove(t);
            notifyDataSetChanged();
        }
        return remove;
    }

    /**
     * 移除一个Item集合
     */
    public boolean removeAll(List<T> list) {
        if (list == null || list.isEmpty())
            return false;

        boolean removeAll = items.removeAll(list);
        notifyDataSetChanged();
        return removeAll;
    }

    /**
     * 获取集合
     */
    public List<T> getItemAll() {
        return items;
    }

    /**
     * 往某个位置插入一条数据
     *
     * @param position
     * @param t
     */
    public void insertTo(int position, T t) {
        if (position >= 0 && position < getCount()) {
            items.add(position, t);
            notifyDataSetChanged();
        }
    }

    /**
     * 是否包含该对象
     */
    public boolean contains(T t) {
        return t != null && items.contains(t);
    }

    /**
     * 不包含此对象
     */
    public boolean notContains(T t) {
        return t == null || !items.contains(t);
    }

    /**
     * 清除所有的数据
     */
    public void clear() {
        if (isEmpty())
            return;

        items.clear();
        notifyDataSetChanged();
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    private static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }
}
