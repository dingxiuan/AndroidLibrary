//package com.dxa.android.adapter.recycler;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.dxa.android.R;
//
//import java.util.Collection;
//
//public class LoadMoreRecyclerAdapter<T, VH extends RecyclerAdapter.ViewHolder> extends RecyclerAdapter<T, VH> {
//
//    private static final int ITEM_TYPE_LOADMORE = 1 << 12;
//    private RecyclerView.ViewHolder mLoadMoreItemHolder;
//
//    public LoadMoreRecyclerAdapter(Context context) {
//        super(context);
//    }
//
//    public LoadMoreRecyclerAdapter(Context context, Collection<T> data) {
//        super(context, data);
//    }
//
//    @Override
//    public VH onCreateViewHolder(LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
//        if (viewType == ITEM_TYPE_LOADMORE) {
//            View loadMoreView = createLoadMoreView(parent);
//            return (VH) (mLoadMoreItemHolder = new RecyclerView.ViewHolder(loadMoreView) {
//            });
//        }
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull VH holder, int position) {
//        // 非'加载更多'item，进行绑定
//        if (position != getItemCount() - 1) {
//            super.onBindViewHolder(holder, position);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return super.getItemCount() + 1;
//    }
//
//    @Override
//    public T getItem(int position) {
//        return super.getItem(position);
//    }
//
//    protected View createLoadMoreView(View parent) {
//        TextView textView = new TextView(getContext());
//        textView.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        textView.setGravity(Gravity.CENTER);
//        return textView;
//    }
//
//    // 显示'加载更多'item
//    protected void showLoadMoreItem() {
//        if (mLoadMoreItemHolder == null) {
//            return;
//        }
//        if (mLoadMoreItemHolder.itemView.getVisibility() != View.VISIBLE) {
//            mLoadMoreItemHolder.itemView.setVisibility(View.VISIBLE);
//        }
//    }
//
//    // 隐藏'加载更多'item
//    protected void hideLoadMoreItem() {
//        if (mLoadMoreItemHolder == null) {
//            return;
//        }
//        if (mLoadMoreItemHolder.itemView.getVisibility() != View.INVISIBLE) {
//            mLoadMoreItemHolder.itemView.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    // '加载更多'item是否显示
//    protected boolean isLoadMoreShown() {
//        return mLoadMoreItemHolder != null && mLoadMoreItemHolder.itemView.getVisibility() == View.VISIBLE;
//    }
//
//    // 显示 '加载更多' 或者 '努力加载中'
//    protected void switchLoadMoreState(boolean loading) {
//        if (mLoadMoreItemHolder == null) {
//            return;
//        }
//        mLoadMoreItemHolder.itemView.setVisibility(loading ? View.GONE : View.VISIBLE);
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (position == getItemCount() - 1) {
//            return ITEM_TYPE_LOADMORE;
//        }
//        return 0;
//    }
//
//
//    protected class RecyclerScrollListener extends RecyclerView.OnScrollListener {
//
//        private LoadMoreRecyclerAdapter mAdapter;
//        private RecyclerView.LayoutManager mManager;
//
//        public RecyclerScrollListener(RecyclerView recyclerView, LoadMoreRecyclerAdapter adapter) {
//            mManager = recyclerView.getLayoutManager();
//            mAdapter = adapter;
//        }
//
//        @Override
//        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//            if (!(mManager instanceof LinearLayoutManager)) {
//                return;
//            }
//            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mManager;
//            int lastCompletePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//            int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
//            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                if (mLoadingMore) {
//                    return;
//                }
//                if (lastCompletePosition == mAdapter.getItemCount() - 2) {
//                    View child = linearLayoutManager.findViewByPosition(lastCompletePosition);
//                    if (child == null) {
//                        return;
//                    }
//                    int deltaY = (recyclerView.getBottom() - recyclerView.getPaddingBottom()) - child.getBottom();
//                    if (deltaY > 0) {
//                        recyclerView.smoothScrollBy(0, -deltaY);
//                    }
//                } else if (lastCompletePosition == mAdapter.getItemCount() - 1) {
//                    mLoadingMore = true;
//                    mAdapter.switchLoadMoreState(true);
//                    if (mListener != null) {
//                        mListener.startLoadMore();
//                    }
//                }
//            }
//        }
//
//        @Override
//        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//            if (!(mManager instanceof LinearLayoutManager) || mLoadingMore) {
//                return;
//            }
//            int newState = recyclerView.getScrollState();
//            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mManager;
//            int lastCompletePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//            int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
//            if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
//                if (lastVisibleItemPosition == mAdapter.getItemCount() - 1 && lastCompletePosition == mAdapter.getItemCount() - 2) {
//                    mAdapter.showLoadMoreItem();
//                }
//            }
//        }
//    }
//}
