package com.framgia.fsalon.screen.listbill;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IdRes;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.framgia.fsalon.FSalonApplication;
import com.framgia.fsalon.R;
import com.framgia.fsalon.data.model.ListBillRespond;
import com.framgia.fsalon.data.model.Salon;
import com.framgia.fsalon.data.model.User;
import com.framgia.fsalon.screen.bill.BillActivity;
import com.framgia.fsalon.screen.scheduler.DepartmentAdapter;
import com.framgia.fsalon.utils.OnDepartmentItemClick;
import com.framgia.fsalon.utils.Utils;
import com.framgia.fsalon.utils.navigator.Navigator;
import com.framgia.fsalon.wiget.StickyHeaderLayoutManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.framgia.fsalon.data.source.remote.ManageBookingRemoteDataSource.FILTER_DAY;
import static com.framgia.fsalon.data.source.remote.ManageBookingRemoteDataSource.FILTER_SPACE;
import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_END_DATE;
import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_SELECT_DATE;
import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_START_DATE;
import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_TODAY;
import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_TOMORROW;
import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_YESTERDAY;
import static com.framgia.fsalon.utils.Constant.FIRST_ITEM;
import static com.framgia.fsalon.utils.Constant.OUT_OF_INDEX;

/**
 * Exposes the data to be used in the Listbill screen.
 */
public class ListBillViewModel extends BaseObservable
    implements ListBillContract.ViewModel, OnDepartmentItemClick,
    DatePickerDialog.OnDateSetListener,
    DialogInterface.OnCancelListener {
    public static final int STATUS_WAITING = 0;
    public static final int STATUS_COMPLETED = 1;
    public static final int STATUS_CANCEL = 2;
    public static final String STT_WAITING = "Waiting";
    public static final String STT_COMPLETED = "Completed";
    public static final String STT_CANCEL = "Cancel";
    private ListBillContract.Presenter mPresenter;
    private Activity mActivity;
    private Context mContext;
    private Navigator mNavigator;
    private StickyHeaderLayoutManager mLayoutManager = new StickyHeaderLayoutManager();
    private ListBillAdapter mAdapter;
    private int mVisibleProgressBar = GONE;
    private int mRadioButtonId;
    private DepartmentAdapter mDepartmentAdapter;
    private User mCustomer;
    private String mCustomerNameError;
    private String mTitleFragment;
    private boolean mIsWaiting;
    private boolean mIsCompleted;
    private boolean mIsCanceled;
    private Calendar mCalendar;
    private int mStartDate = Utils.createTimeStamp(TAB_TODAY);
    private int mEndDate = OUT_OF_INDEX;
    private DatePickerDialog mDatePickerDialog;
    private int mTypeSelectDate;
    private String mTypeFilter;
    private String mStatusId = "";
    private int mDepartmentId = OUT_OF_INDEX;
    private int mCustomerId = OUT_OF_INDEX;
    private FragmentManager mFragmentManager;
    private String mSpaceTime = "";
    private String mStatus;
    private int mStatusColor;
    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(View drawerView) {
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            makeStatusFilter();
            mPresenter
                .filterBills(mTypeFilter, mStartDate, mEndDate, mStatusId, mDepartmentId,
                    mCustomerId);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };

    private void makeStatusFilter() {
        mStatusId = "";
        if (mIsCanceled) {
            mStatusId = mStatusId.concat(STATUS_CANCEL + ",");
        }
        if (mIsCompleted) {
            mStatusId = mStatusId.concat(STATUS_COMPLETED + ",");
        }
        if (mIsWaiting) {
            mStatusId = mStatusId.concat(STATUS_WAITING + ",");
        }
        mStatusId = mStatusId.substring(0, mStatusId.length() - 1);
    }

    private RadioGroup.OnCheckedChangeListener mChangeListener = new RadioGroup
        .OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.filter_today:
                    mTypeFilter = FILTER_DAY;
                    mEndDate = OUT_OF_INDEX;
                    mStartDate = Utils.createTimeStamp(TAB_TODAY);
                    setTitleFragment(FSalonApplication.getInstant().getResources()
                        .getString(R.string.title_today));
                    setRadioButtonId(R.id.filter_today);
                    break;
                case R.id.filter_yesterday:
                    mEndDate = OUT_OF_INDEX;
                    mTypeFilter = FILTER_DAY;
                    mStartDate = Utils.createTimeStamp(TAB_YESTERDAY);
                    setTitleFragment(FSalonApplication.getInstant().getResources()
                        .getString(R.string.title_yesterday));
                    setRadioButtonId(R.id.filter_yesterday);
                    break;
                case R.id.filter_tomorrow:
                    mEndDate = OUT_OF_INDEX;
                    mTypeFilter = FILTER_DAY;
                    mStartDate = Utils.createTimeStamp(TAB_TOMORROW);
                    setTitleFragment(FSalonApplication.getInstant().getResources()
                        .getString(R.string.title_tomorrow));
                    setRadioButtonId(R.id.filter_tomorrow);
                    break;
                default:
                    break;
            }
        }
    };

    public ListBillViewModel(Activity activity) {
        mActivity = activity;
        mContext = activity.getApplicationContext();
        mNavigator = new Navigator(mActivity);
        mFragmentManager = mActivity.getFragmentManager();
        setAdapter(new ListBillAdapter(this, new ArrayList<ListBillRespond>()));
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
        mDatePickerDialog =
            DatePickerDialog.newInstance(this, mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.setOnDateSetListener(this);
        mDatePickerDialog.setOnCancelListener(this);
        setRadioButtonId(R.id.filter_today);
        setWaiting(true);
        setCompleted(true);
        setCanceled(true);
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(ListBillContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreateBillClick() {
        mNavigator.startActivity(BillActivity.getInstance(mContext));
    }

    @Override
    public void onFilterSuccessfully(List<ListBillRespond> bills) {
        mAdapter.clear();
        mAdapter.updateData(bills);
    }

    @Override
    public void onHideProgressBar() {
        setVisibleProgressBar(GONE);
    }

    @Override
    public void onShowProgressBar() {
        setVisibleProgressBar(VISIBLE);
    }

    @Override
    public void onFilterClick(DrawerLayout layout) {
        if (layout.isDrawerOpen(Gravity.START)) {
            layout.closeDrawer(Gravity.START);
            return;
        }
        layout.openDrawer(Gravity.START);
    }

    @Override
    public void onInputCustomerNameError() {
        setCustomerNameError(mNavigator.getStringById(R.string.msg_error_empty));
    }

    @Override
    public void onHideCustomerNameError() {
        setCustomerNameError(null);
    }

    @Override
    public void onSpaceTimeClick() {
        mTypeFilter = FILTER_SPACE;
        mTypeSelectDate = TAB_START_DATE;
        showDatePickerDialog();
    }

    @Override
    public void onSelectDateClick() {
        mEndDate = OUT_OF_INDEX;
        mTypeFilter = FILTER_DAY;
        mTypeSelectDate = TAB_SELECT_DATE;
        showDatePickerDialog();
    }

    private void showDatePickerDialog() {
        switch (mTypeSelectDate) {
            case TAB_SELECT_DATE:
                mDatePickerDialog.setTitle(FSalonApplication.getInstant().getResources()
                    .getString(R.string.title_select_date));
                break;
            case TAB_START_DATE:
                mDatePickerDialog.setTitle(FSalonApplication.getInstant().getResources()
                    .getString(R.string.title_select_start_day));
                break;
            case TAB_END_DATE:
                mDatePickerDialog.setTitle(FSalonApplication.getInstant().getResources()
                    .getString(R.string.title_select_end_day));
                break;
            default:
                break;
        }
        mDatePickerDialog.show(mFragmentManager, "");
    }

    @Override
    public void onGetSalonsSuccess(List<Salon> salons) {
        setDepartmentAdapter(new DepartmentAdapter(mContext, salons, this));
        mDepartmentAdapter.selectedPosition(FIRST_ITEM);
    }

    public StickyHeaderLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public void setLayoutManager(StickyHeaderLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    public ListBillAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(ListBillAdapter adapter) {
        mAdapter = adapter;
    }

    public int getVisibleProgressBar() {
        return mVisibleProgressBar;
    }

    public void setVisibleProgressBar(int visibleProgressBar) {
        mVisibleProgressBar = visibleProgressBar;
    }

    public DrawerLayout.DrawerListener getDrawerListener() {
        return mDrawerListener;
    }

    public void setDrawerListener(DrawerLayout.DrawerListener drawerListener) {
        mDrawerListener = drawerListener;
    }

    public RadioGroup.OnCheckedChangeListener getChangeListener() {
        return mChangeListener;
    }

    public void setChangeListener(RadioGroup.OnCheckedChangeListener changeListener) {
        mChangeListener = changeListener;
    }

    @Bindable
    public int getRadioButtonId() {
        return mRadioButtonId;
    }

    public void setRadioButtonId(int radioButtonId) {
        mRadioButtonId = radioButtonId;
        notifyPropertyChanged(BR.radioButtonId);
    }

    @Bindable
    public DepartmentAdapter getDepartmentAdapter() {
        return mDepartmentAdapter;
    }

    public void setDepartmentAdapter(DepartmentAdapter departmentAdapter) {
        mDepartmentAdapter = departmentAdapter;
        notifyPropertyChanged(BR.departmentAdapter);
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    @Bindable
    public User getCustomer() {
        return mCustomer;
    }

    public void setCustomer(User customer) {
        mCustomer = customer;
        notifyPropertyChanged(BR.customer);
    }

    @Bindable
    public String getCustomerNameError() {
        return mCustomerNameError;
    }

    public void setCustomerNameError(String customerNameError) {
        mCustomerNameError = customerNameError;
        notifyPropertyChanged(BR.customerNameError);
    }

    @Bindable
    public String getTitleFragment() {
        return mTitleFragment;
    }

    public void setTitleFragment(String titleFragment) {
        mTitleFragment = titleFragment;
        notifyPropertyChanged(BR.titleFragment);
    }

    @Bindable
    public boolean isWaiting() {
        return mIsWaiting;
    }

    public void setWaiting(boolean waiting) {
        mIsWaiting = waiting;
        notifyPropertyChanged(BR.waiting);
    }

    @Bindable
    public boolean isCompleted() {
        return mIsCompleted;
    }

    public void setCompleted(boolean completed) {
        mIsCompleted = completed;
        notifyPropertyChanged(BR.completed);
    }

    @Bindable
    public boolean isCanceled() {
        return mIsCanceled;
    }

    public void setCanceled(boolean canceled) {
        mIsCanceled = canceled;
        notifyPropertyChanged(BR.canceled);
    }

    public Calendar getCalendar() {
        return mCalendar;
    }

    public void setCalendar(Calendar calendar) {
        mCalendar = calendar;
    }

    public int getStartDate() {
        return mStartDate;
    }

    public void setStartDate(int startDate) {
        mStartDate = startDate;
    }

    public int getEndDate() {
        return mEndDate;
    }

    public void setEndDate(int endDate) {
        mEndDate = endDate;
    }

    public int getTypeSelectDate() {
        return mTypeSelectDate;
    }

    public void setTypeSelectDate(int typeSelectDate) {
        mTypeSelectDate = typeSelectDate;
    }

    @Bindable
    public int getStatusColor() {
        return mStatusColor;
    }

    public void setStatusColor(int statusColor) {
        mStatusColor = statusColor;
        notifyPropertyChanged(BR.statusColor);
    }

    @Override
    public void onSelectedSalonPosition(int pos, Salon salon) {
        if (salon == null) {
            return;
        }
        mDepartmentAdapter.selectedPosition(pos);
        setDepartmentId(salon.getId());
    }

    public String getTypeFilter() {
        return mTypeFilter;
    }

    public void setTypeFilter(String typeFilter) {
        mTypeFilter = typeFilter;
    }

    public String getStatusId() {
        return mStatusId;
    }

    public void setStatusId(String statusId) {
        mStatusId = statusId;
    }

    public int getDepartmentId() {
        return mDepartmentId;
    }

    public void setDepartmentId(int departmentId) {
        mDepartmentId = departmentId;
    }

    public int getCustomerId() {
        return mCustomerId;
    }

    public void setCustomerId(int customerId) {
        mCustomerId = customerId;
    }

    @Bindable
    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
        notifyPropertyChanged(BR.status);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        setRadioButtonId(mRadioButtonId);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (mDatePickerDialog != null) mDatePickerDialog.dismiss();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        switch (mTypeSelectDate) {
            case TAB_START_DATE:
                mStartDate = (int) (mCalendar.getTimeInMillis() / 1000);
                mTypeSelectDate = TAB_END_DATE;
                mDatePickerDialog = DatePickerDialog.newInstance(this, year,
                    monthOfYear, dayOfMonth);
                mDatePickerDialog.setOnCancelListener(this);
                mSpaceTime = Utils.convertDate(mCalendar.getTime());
                showDatePickerDialog();
                return;
            case TAB_SELECT_DATE:
                mEndDate = OUT_OF_INDEX;
                setTitleFragment(Utils.convertDate(mCalendar.getTime()));
                mStartDate = (int) (mCalendar.getTimeInMillis() / 1000);
                setRadioButtonId(R.id.filter_select_date);
                break;
            case TAB_END_DATE:
                mEndDate = (int) (mCalendar.getTimeInMillis() / 1000);
                mSpaceTime = mSpaceTime.concat(" - " + Utils.convertDate(mCalendar.getTime()));
                setTitleFragment(mSpaceTime);
                setRadioButtonId(R.id.filter_space_time);
                mPresenter.filterBills(mTypeFilter, mStartDate, mEndDate, mStatusId, mDepartmentId,
                    mCustomerId);
            default:
                break;
        }
    }
}
