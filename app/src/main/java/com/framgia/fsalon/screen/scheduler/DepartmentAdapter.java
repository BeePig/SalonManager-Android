package com.framgia.fsalon.screen.scheduler;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.fsalon.BR;
import com.framgia.fsalon.BaseRecyclerViewAdapter;
import com.framgia.fsalon.R;
import com.framgia.fsalon.data.model.Salon;
import com.framgia.fsalon.databinding.ItemDepartmentBinding;
import com.framgia.fsalon.utils.OnDepartmentItemClick;

import java.util.List;

/**
 * Created by MyPC on 08/08/2017.
 */
public class DepartmentAdapter
    extends BaseRecyclerViewAdapter<Salon, DepartmentAdapter.ViewHolder> {
    private List<Salon> mData;
    private int mSelectedPosition = -1;
    private OnDepartmentItemClick mOnDepartmentItemClick;

    public DepartmentAdapter(@NonNull Context context, List<Salon> data,
                                OnDepartmentItemClick onDepartmentItemClick) {
        super(context);
        mData = data;
        mOnDepartmentItemClick = onDepartmentItemClick;
    }

    public void selectedPosition(int position) {
        if (position < 0) {
            return;
        }
        mSelectedPosition = position;
        notifyDataSetChanged();
    }

    public Salon getItem(int position) {
        return position < 0 ? null : mData.get(position);
    }

    @Override
    public void onUpdatePage(List<Salon> data) {
        if (data == null) {
            return;
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public DepartmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDepartmentBinding binding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_department, parent, false);
        return new DepartmentAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemDepartmentBinding mBinding;

        public ViewHolder(ItemDepartmentBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindData(Salon salon) {
            if (salon == null) {
                return;
            }
            ViewHolderModel
                model = new ViewHolderModel(salon, getAdapterPosition(),
                mSelectedPosition == getAdapterPosition(), mOnDepartmentItemClick);
            mBinding.setViewHolderModel(model);
            mBinding.executePendingBindings();
        }
    }

    /**
     * ViewHolderModel
     */
    public class ViewHolderModel extends BaseObservable {
        private Salon mSalon;
        private int mPosition;
        private boolean mIsSelected;
        private OnDepartmentItemClick mOnItemClick;

        public ViewHolderModel(Salon salon, int position, boolean isSelected,
                               OnDepartmentItemClick itemClick) {
            mSalon = salon;
            mPosition = position;
            mIsSelected = isSelected;
            mOnItemClick = itemClick;
        }

        @Bindable
        public Salon getSalon() {
            return mSalon;
        }

        public void setSalon(Salon salon) {
            mSalon = salon;
            notifyPropertyChanged(BR.salon);
        }

        @Bindable
        public int getPosition() {
            return mPosition;
        }

        public void setPosition(int position) {
            mPosition = position;
            notifyItemChanged(BR.position);
        }

        @Bindable
        public boolean isSelected() {
            return mIsSelected;
        }

        public void setSelected(boolean selected) {
            mIsSelected = selected;
            notifyPropertyChanged(BR.selected);
        }

        @Bindable
        public OnDepartmentItemClick getOnItemClick() {
            return mOnItemClick;
        }

        public void setOnItemClick(OnDepartmentItemClick onItemClick) {
            mOnItemClick = onItemClick;
            notifyDataSetChanged();
        }
    }
}
