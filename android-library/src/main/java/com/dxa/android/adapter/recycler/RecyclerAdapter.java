package com.dxa.android.adapter.recycler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * RecyclerView的Adapter
 */
public abstract class RecyclerAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements IRefresh<T> {

    private final Context context;
    private final ArrayList<T> items;
    private final LayoutInflater inflater;

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
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(VH holder, int position);

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
    public boolean addAll(T[] ts) {
        return !(ts == null || ts.length < 1) && addAll(Arrays.asList(ts));
    }

    /**
     * 添加一个集合的数据
     *
     * @param list 集合
     * @return 是否添加
     */
    @Override
    public boolean addAll(Collection<T> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }

        if (!repeat) {
            ArrayList<T> tempList = new ArrayList<>();
            for (T t : list) {
                if (!contains(t))
                    tempList.add(t);
            }
            list = tempList;

            if (list.isEmpty()) {
                return false;
            }
        }
        int itemCount = getCount();
        int size = list.size();
        boolean add = items.addAll(list);
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
        if (from > to && contains(from) && contains(to)) {
            for (int i = from; i < to; i++) {
                items.remove(from);
            }
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
     * @param position 对象的位置
     * @return 包含返回true，否则false
     */
    @Override
    public boolean contains(int position) {
        return position >= 0 && getCount() > position;
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
     * 当Item被点击时的回调
     */
    public interface OnItemClickListener {
    	/**
    	 * 当Item被点击时的回调
    	 * @param parent 父容器
    	 * @param view 点击的View
    	 * @param position View位置
    	 */
    	void onItemClick(ViewGroup parent, View view, int position);
    }
}
