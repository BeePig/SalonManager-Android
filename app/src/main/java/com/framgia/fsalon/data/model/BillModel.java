package com.framgia.fsalon.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.fsalon.BR;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MyPC on 03/08/2017.
 */
public interface BillModel {
    /**
     * Bill
     *
     * @param <T>
     */
    class Bill<T extends BillItem> extends BaseObservable {
        @Expose
        @SerializedName("customer_id")
        protected int mCustomerId;
        @Expose
        @SerializedName("phone")
        protected String mPhone;
        @Expose
        @SerializedName("status")
        protected int mStatus;
        @Expose
        @SerializedName("customer_name")
        protected String mCustomerName;
        @Expose
        @SerializedName("order_booking_id")
        protected int mOrderBookingId;
        @Expose
        @SerializedName("grand_total")
        protected float mGrandTotal;
        @Expose
        @SerializedName("bill_items")
        protected List<T> mBillItems;
        @Expose
        @SerializedName("image_url")
        protected String mImageUrl = "http://hinhnendep.pro/wp-content/uploads/2016/05/" +
            "nhung-hinh-anh-avatar-trai-dep-cuc-hot-khien-ban-ngay-ngat-6.jpg";

        public int getCustomerId() {
            return mCustomerId;
        }

        public void setCustomerId(int customerId) {
            mCustomerId = customerId;
        }

        public String getPhone() {
            return mPhone;
        }

        public void setPhone(String phone) {
            mPhone = phone;
        }

        @Bindable
        public int getStatus() {
            return mStatus;
        }

        public void setStatus(int status) {
            mStatus = status;
            notifyPropertyChanged(BR.status);
        }

        @Bindable
        public String getCustomerName() {
            return mCustomerName;
        }

        public void setCustomerName(String customerName) {
            mCustomerName = customerName;
            notifyPropertyChanged(BR.customerName);
        }

        public int getOrderBookingId() {
            return mOrderBookingId;
        }

        public void setOrderBookingId(int orderBookingId) {
            mOrderBookingId = orderBookingId;
        }

        @Bindable
        public float getGrandTotal() {
            return mGrandTotal;
        }

        public void setGrandTotal(float grandTotal) {
            mGrandTotal = grandTotal;
            notifyPropertyChanged(BR.grandTotal);
        }

        public List<T> getBillItems() {
            return mBillItems;
        }

        public void setBillItems(List<T> billItems) {
            mBillItems = billItems;
        }

        public String getImageUrl() {
            return mImageUrl;
        }

        public void setImageUrl(String imageUrl) {
            mImageUrl = imageUrl;
        }
    }

    /**
     * BillItem
     */
    class BillItem {
        @Expose
        @SerializedName("id")
        protected int mId;
        @Expose
        @SerializedName("service_product_id")
        protected int mServiceProductId;
        @Expose
        @SerializedName("stylist_id")
        protected int mStylistId;
        @Expose
        @SerializedName("price")
        protected float mPrice;
        @Expose
        @SerializedName("qty")
        protected int mQty;
        @Expose
        @SerializedName("row_total")
        protected float mRowTotal;

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            mId = id;
        }

        public int getServiceProductId() {
            return mServiceProductId;
        }

        public void setServiceProductId(int serviceProductId) {
            mServiceProductId = serviceProductId;
        }

        public int getStylistId() {
            return mStylistId;
        }

        public void setStylistId(int stylistId) {
            mStylistId = stylistId;
        }

        public float getPrice() {
            return mPrice;
        }

        public void setPrice(float price) {
            mPrice = price;
        }

        public int getQty() {
            return mQty;
        }

        public void setQty(int qty) {
            mQty = qty;
        }

        public float getRowTotal() {
            return mRowTotal;
        }

        public void setRowTotal(float rowTotal) {
            mRowTotal = rowTotal;
        }
    }
}
