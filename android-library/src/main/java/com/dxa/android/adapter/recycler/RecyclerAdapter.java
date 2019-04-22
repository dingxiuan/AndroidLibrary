package com.dxa.android.adapter.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dxa.android.adapter.recycler.IRefresh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * RecyclerView的Adapter
 */
public abstract class RecyclerAdapter<T, VH extends RecyclerAdapter.ViewHolder>
        extends RecyclerView.Adapter<VH> implements IRefresh<T> {

    private final Context context;
    private final ArrayList<T> items;
    private final LayoutInflater inflater;

    private OnItemClickListener<T> listener;

    /**
     * 是否包含重复对象，默认不包含
     */
    private boolean repeat = false;

    public RecyclerAdapter(Context context) {
        if (context == null) {
            throw new NullPointerException("Context对象不能为null !");
        }
        this.context = context;
        this.items = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    public RecyclerAdapter(Context context, Collection<T> data) {
        this(context);
        if (data != null && data.size() > 0) {
            items.addAll(data);
        }
    }

    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return onCreateViewHolder(inflater, parent, viewType);
    }

    public abstract VH onCreateViewHolder(LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        holder.setData(getItem(position), position, listener);
    }

    @Override
    public final void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    /**
     * 获得上下文对象
     */
    protected Context getContext() {
        return context;
    }

    /**
     * 获得布局加载器
     */
    protected LayoutInflater getInflater() {
        return inflater;
    }


    @SuppressWarnings("unchecked")
    protected <V extends View> V inflateView(@LayoutRes int resId, ViewGroup parent) {
        return (V) getInflater().inflate(resId, parent, false);
    }

    @Override
    public List<T> getItems() {
        return items;
    }

    /**
     * 根据位置获得Item
     */
    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    /**
     * 添加对象
     */
    @Override
    public boolean add(T t) {
        if (t == null || (!repeat && contains(t))) {
            return false;
        }
        int itemCount = getCount();
        boolean add = items.add(t);
        notifyItemInserted(itemCount);
        return add;
    }

    @Override
    public boolean addAll(T[] array) {
        return !(array == null || array.length < 1) && addAll(Arrays.asList(array));
    }

    /**
     * 添加一个集合的数据
     *
     * @param c 集合
     * @return 是否添加
     */
    @Override
    public boolean addAll(Collection<T> c) {
        if (c == null || c.isEmpty()) {
            return false;
        }

        if (!repeat) {
            ArrayList<T> tempList = new ArrayList<>();
            for (T t : c) {
                if (!contains(t)) {
                    tempList.add(t);
                }
            }
            c = tempList;

            if (c.isEmpty()) {
                return false;
            }
        }
        int itemCount = getCount();
        int size = c.size();
        boolean add = items.addAll(c);
        notifyItemRangeInserted(itemCount, size);
        return add;
    }

    /**
     * 移除
     *
     * @param position 移除的位置
     * @return 移除的对象
     */
    @Override
    public T remove(int position) {
        T t = null;
        if (contains(position)) {
            t = items.remove(position);
            notifyItemRemoved(position);
        }
        return t;
    }

    /**
     * 移除
     *
     * @param from 开始移除的位置
     * @param to   结束移除的位置
     * @return 是否移除
     */
    @Override
    public boolean remove(int from, int to) {
        if (from < to && contains(from) && contains(to)) {
            ArrayList<T> removedList = new ArrayList<>();
            for (int i = from; i < to; i++) {
                removedList.add(items.get(i));
            }
            items.removeAll(removedList);
            notifyItemMoved(from, to);
            return true;
        }
        return false;
    }

    /**
     * 移除全部对象
     *
     * @param subItems 子集合
     * @return 是否移除
     */
    @Override
    public boolean removeAll(Collection<T> subItems) {
        if (subItems != null && subItems.size() > 0) {
            boolean removeAll = items.removeAll(subItems);
            notifyDataSetChanged();
            return removeAll;
        }
        return false;
    }

    /**
     * 此位置上是否包含对象
     *
     * @param index 对象的位置
     * @return 包含返回true，否则false
     */
    @Override
    public boolean contains(int index) {
        return index >= 0 && getCount() > index;
    }

    /**
     * 是否包含对象
     *
     * @param t 对象
     * @return 包含返回true，否则false
     */
    @Override
    public boolean contains(T t) {
        return t != null && items.contains(t);
    }

    @Override
    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    @Override
    public boolean isRepeat() {
        return repeat;
    }

    /**
     * 添加当Item被点击时的监听
     */
    public void addOnItemClickListener(OnItemClickListener<T> listener) {
        this.listener = listener;
    }

    /**
     * 当Item被点击时的监听
     */
    public interface OnItemClickListener<T> {
        /**
         * 当Item被点击时的回调
         *
         * @param parentView 点击的View
         * @param t          对应的Item
         * @param position   View位置
         */
        void onItemClick(View parentView, T t, int position);
    }


    public static class ViewHolder<T> extends RecyclerView.ViewHolder {

        private OnItemClickListener<T> listener;
        private T item;
        private int position;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(v, item, position);
                }
            });
        }

        public void setListener(OnItemClickListener<T> listener) {
            this.listener = listener;
        }

        /**
         * 设置Item的数据
         *
         * @param item     ITEM
         * @param position 位置
         */
        public final void setData(T item, int position, OnItemClickListener<T> listener) {
            this.listener = listener;
            this.item = item;
            this.position = position;
            setData(item, position);
        }

        public void setData(T item, int position) {
            // ~
        }

    }

}
