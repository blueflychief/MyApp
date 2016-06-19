package com.infinite.myapp.widget.recycler;

/**
 * Created by Lsq on 6/17/2016.--8:02 PM
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 刷新和加载的适配器
 *
 * @param <VH> ViewHolder的子类
 */
public abstract class RefreshAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
    private RefreshRecycleView refreshView;
    // 内容类型
    protected final int TYPE_CONTENT = 1;
    // 底部加载更多
    private final int TYPE_FOOTER = TYPE_CONTENT + 1;
    // 头部
    private final int TYPE_HEADER = TYPE_CONTENT + 2;

    private List<Integer> viewTypes; // 子视图类型
    private List mAllDatas;

    protected LayoutInflater inflater;

    /**
     * 创建适配器
     */
    public RefreshAdapter(Context context, List list, RefreshRecycleView refreshView) {
        this.refreshView = refreshView;
        mAllDatas = list;
        viewTypes = new ArrayList<>();
//            setItemTypes(viewTypes);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CONTENT || viewTypes.contains(viewType)) {
            return onCreateHolder(parent, viewType);
        } else if (viewType == TYPE_HEADER) {
            refreshView.setFullSpan(refreshView.getHeader());
            return new RecyclerView.ViewHolder(refreshView.getHeader()) {
            };
        } else if (viewType == TYPE_FOOTER) {
            refreshView.setFullSpan(refreshView.getFooter());
            return new RecyclerView.ViewHolder(refreshView.getFooter()) {
            };
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (viewType == TYPE_CONTENT || viewTypes.contains(viewType)) {
            onBindHolder((VH) holder, refreshView.getHeader() != null ? position - 1 : position);
        }
    }


    /**
     * 创建ViewHolder, 用来代替onCreateViewHolder()方法, 用法还是一样的
     *
     * @param parent   父控件
     * @param viewType 类型
     */
    public abstract VH onCreateHolder(ViewGroup parent, int viewType);

    /**
     * 给ViewHolder绑定数据, 用来代替onBindViewHolder(), 用法一样
     *
     * @param holder   ViewHolder的子类实例
     * @param position 位置
     */
    public abstract void onBindHolder(VH holder, int position);

    @Override
    public int getItemCount() {
        int count = mAllDatas.size();
        //还有更多，且Footer显示
        if (refreshView.getLoadMoreEnable() && refreshView.getShowFooterWithNoMore() && refreshView.getFooter() != null)
            count += 1;
        //没有更多，但是还是需要显示Footer
        if (!refreshView.getLoadMoreEnable() && refreshView.getShowFooterWithNoMore() && refreshView.getFooter() != null)
            count += 1;
        if (refreshView.getHeader() != null) count += 1;
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && refreshView.getHeader() != null) {
            return TYPE_HEADER;
        }
        //此情况是没有更多时候显示footer，例如显示“没有更多”
        else if (!refreshView.getLoadMoreEnable()
                && refreshView.getShowFooterWithNoMore()
                && refreshView.getFooter() != null
                && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        //此情况是有更多时候显示footer
        else if (refreshView.getLoadMoreEnable()
                && refreshView.getFooter() != null
                && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            int viewType = getItemType(refreshView.getHeader() != null ? position - 1 : position);
            return viewType == -1 ? TYPE_CONTENT : viewType;
        }
    }

    /**
     * 自定义获取子视图类型的方法
     *
     * @param position 位置
     * @return 类型
     */
    public int getItemType(int position) {
        return -1;
    }

    /**
     * 设置子视图类型, 如果有新的子视图类型, 直接往参数viewTypes中添加即可, 每个类型的值都要>3, 且不能重复
     *
     * @param viewTypes 子视图类型列表
     */
    public void setItemTypes(List<Integer> viewTypes) {
        if (viewTypes != null) {
            this.viewTypes.addAll(viewTypes);
        }
    }

    /**
     * @param position 位置
     * @return 是否为脚部布局
     */
    public boolean isFooter(int position) {
        return TYPE_FOOTER == getItemViewType(position);
    }

    /**
     * @param position 位置
     * @return 是否为头部布局
     */
    public boolean isHeader(int position) {
        return TYPE_HEADER == getItemViewType(position);
    }

}


