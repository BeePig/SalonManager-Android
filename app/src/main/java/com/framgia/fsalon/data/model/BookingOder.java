package com.framgia.fsalon.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.framgia.fsalon.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by framgia on 7/21/17.
 */
public class BookingOder implements Parcelable {
    public static final int STATUS_CANCELED = 0;
    public static final int STATUS_WATTING = 1;
    public static final int STATUS_FINISHED = 2;
    public static final int STATUS_IN_LATE = 3;
    @SerializedName("id")
    @Expose
    private int mId;
    @SerializedName("render_booking_id")
    @Expose
    private int mRenderBookingId;
    @SerializedName("user_id")
    @Expose
    private int mUserId;
    @SerializedName("phone")
    @Expose
    private String mPhone;
    @SerializedName("name")
    @Expose
    private String mName;
    @SerializedName("stylist_id")
    @Expose
    private int mStylistId;
    @SerializedName("grand_total")
    @Expose
    private int mGandTotal;
    @SerializedName("created_at")
    @Expose
    private Date mCreatedAt;
    @SerializedName("updated_at")
    @Expose
    private Date mUpdatedAt;
    /**
     * status for order: waiting, finished or canceled
     */
    @SerializedName("status")
    @Expose
    private int mStatus;
    @SerializedName("stylist")
    @Expose
    private Stylist mStylist;
    @SerializedName("render_booking")
    @Expose
    private BookingRender mRender;
    @SerializedName("department")
    @Expose
    private Salon mDepartment;
    @SerializedName("time_start")
    @Expose
    private Date mTimeStart;
    @SerializedName("images")
    @Expose
    private List<ImageResponse> mImages;

    protected BookingOder(Parcel in) {
        mId = in.readInt();
        mRenderBookingId = in.readInt();
        mUserId = in.readInt();
        mPhone = in.readString();
        mName = in.readString();
        mStylistId = in.readInt();
        mGandTotal = in.readInt();
        mStatus = in.readInt();
        mStylist = in.readParcelable(Stylist.class.getClassLoader());
        mRender = in.readParcelable(BookingRender.class.getClassLoader());
        mDepartment = in.readParcelable(Salon.class.getClassLoader());
    }

    public BookingOder(Date timeStart) {
        mTimeStart = timeStart;
    }

    public static final Creator<BookingOder> CREATOR = new Creator<BookingOder>() {
        @Override
        public BookingOder createFromParcel(Parcel in) {
            return new BookingOder(in);
        }

        @Override
        public BookingOder[] newArray(int size) {
            return new BookingOder[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getRenderBookingId() {
        return mRenderBookingId;
    }

    public void setRenderBookingId(int renderBookingId) {
        mRenderBookingId = renderBookingId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getName() {
        return mName;
    }

    public Salon getDepartment() {
        return mDepartment;
    }

    public void setDepartment(Salon department) {
        mDepartment = department;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getStylistId() {
        return mStylistId;
    }

    public void setStylistId(int stylistId) {
        mStylistId = stylistId;
    }

    public int getGandTotal() {
        return mGandTotal;
    }

    public void setGandTotal(int gandTotal) {
        mGandTotal = gandTotal;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
    }

    public Date getMpdatedAt() {
        return mUpdatedAt;
    }

    public void setMpdatedAt(Date mpdatedAt) {
        this.mUpdatedAt = mpdatedAt;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public Date getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public Stylist getStylist() {
        return mStylist;
    }

    public void setStylist(Stylist stylist) {
        mStylist = stylist;
    }

    public BookingRender getRender() {
        return mRender;
    }

    public void setRender(BookingRender render) {
        mRender = render;
    }

    public Date getTimeStart() {
        return mTimeStart;
    }

    public void setTimeStart(Date timeStart) {
        mTimeStart = timeStart;
    }

    public List<ImageResponse> getImages() {
        return mImages;
    }

    public void setImages(List<ImageResponse> images) {
        mImages = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mRenderBookingId);
        dest.writeInt(mUserId);
        dest.writeString(mPhone);
        dest.writeString(mName);
        dest.writeInt(mStylistId);
        dest.writeInt(mGandTotal);
        dest.writeInt(mStatus);
        dest.writeParcelable(mStylist, flags);
        dest.writeParcelable(mRender, flags);
        dest.writeParcelable(mDepartment, flags);
    }

    public int getStatusBooking() {
        switch (mStatus) {
            case STATUS_CANCELED:
                return R.drawable.ic_broken_heart_24dp;
            case STATUS_WATTING:
                return R.drawable.ic_waiting_room_24_dp;
            case STATUS_FINISHED:
                return R.drawable.ic_smile_24dp;
            case STATUS_IN_LATE:
                return R.drawable.ic_sad_24dp;
            default:
                return R.drawable.ic_waiting_room_24_dp;
        }
    }
}
