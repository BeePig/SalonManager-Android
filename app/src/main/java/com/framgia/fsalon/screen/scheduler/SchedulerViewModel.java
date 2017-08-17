package com.framgia.fsalon.screen.scheduler;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioGroup;

import com.framgia.fsalon.BR;
import com.framgia.fsalon.FSalonApplication;
import com.framgia.fsalon.R;
import com.framgia.fsalon.data.model.ManageBookingResponse;
import com.framgia.fsalon.data.model.Salon;
import com.framgia.fsalon.screen.scheduler.detail.BookingDetailActivity;
import com.framgia.fsalon.utils.OnDepartmentItemClick;
import com.framgia.fsalon.utils.Utils;
import com.framgia.fsalon.utils.navigator.Navigator;
import com.framgia.fsalon.wiget.StickyHeaderLayoutManager;
import com.framgia.fsalon.wiget.TopSheetBehavior;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.framgia.fsalon.data.model.BookingOder.STATUS_CANCELED;
import static com.framgia.fsalon.data.model.BookingOder.STATUS_FINISHED;
import static com.framgia.fsalon.data.model.BookingOder.STATUS_IN_LATE;
import static com.framgia.fsalon.data.model.BookingOder.STATUS_WATTING;
import static com.framgia.fsalon.data.source.remote.ManageBookingRemoteDataSource.FILTER_DAY;
import static com.framgia.fsalon.data.source.remote.ManageBookingRemoteDataSource.FILTER_SPACE;
import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_END_DATE;
import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_SELECT_DATE;
import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_SPACE_TIME;
import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_START_DATE;
import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_TODAY;
import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_TOMORROW;
import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_YESTERDAY;
import static com.framgia.fsalon.utils.Constant.ApiParram.OUT_OF_INDEX;
import static com.framgia.fsalon.utils.Constant.FIRST_ITEM;
import static com.framgia.fsalon.utils.Constant.NO_SCROLL;

/**
 * Exposes the data to be used in the Scheduler screen.
 */
public class SchedulerViewModel extends BaseObservable
    implements SchedulerContract.ViewModel, DatePickerDialog.OnDateSetListener,
    DialogInterface.OnCancelListener, OnDepartmentItemClick {
    private SchedulerContract.Presenter mPresenter;
    private int mTabFilter;
    private SchedulerAdapter mAdapter;
    private Navigator mNavigator;
    private StickyHeaderLayoutManager mLayoutManager = new StickyHeaderLayoutManager();
    private boolean mIsLoadingMore = false;
    private Calendar mCalendar;
    private FragmentManager mFragmentManager;
    private int mStartDate = Utils.createTimeStamp(TAB_TODAY);
    private int mEndDate = OUT_OF_INDEX;
    private DatePickerDialog mDatePickerDialog;
    private int mTypeSelectDate;
    private String mTitleTopSheet;
    private String mFilterChoice = FILTER_DAY;
    private String mSpaceTime = "";
    private DepartmentAdapter mDepartmentAdapter;
    private Fragment mFragment;
    private boolean mIsWatting = true;
    private boolean mIsFinished = true;
    private boolean mIsCanceled = true;
    private boolean mIsInLate = true;
    private int mSalonId;
    private String mStatus = "";
    private int mRadioButtonId;
    private int mPositionNearestTime = NO_SCROLL;
    private String mSalonName;
    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(View drawerView) {
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            onFilterData();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };
    private RadioGroup.OnCheckedChangeListener mChangeListener = new RadioGroup
        .OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.filter_today:
                    mFilterChoice = FILTER_DAY;
                    mEndDate = OUT_OF_INDEX;
                    mStartDate = Utils.createTimeStamp(TAB_TODAY);
                    setTitleTopSheet(FSalonApplication.getInstant()
                        .getResources().getString(R.string.title_today));
                    setRadioButtonId(R.id.filter_today);
                    break;
                case R.id.filter_yesterday:
                    mEndDate = OUT_OF_INDEX;
                    mFilterChoice = FILTER_DAY;
                    mStartDate = Utils.createTimeStamp(TAB_YESTERDAY);
                    setTitleTopSheet(FSalonApplication.getInstant()
                        .getResources().getString(R.string.title_yesterday));
                    setRadioButtonId(R.id.filter_yesterday);
                    break;
                case R.id.filter_tomorrow:
                    mEndDate = OUT_OF_INDEX;
                    mFilterChoice = FILTER_DAY;
                    mStartDate = Utils.createTimeStamp(TAB_TOMORROW);
                    setTitleTopSheet(FSalonApplication.getInstant()
                        .getResources().getString(R.string.title_tomorrow));
                    setRadioButtonId(R.id.filter_tomorrow);
                    break;
                default:
                    break;
            }
        }
    };
    private RecyclerView.OnScrollListener mScrollListenner = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy <= 0) {
                return;
            }
            int totalItems = mLayoutManager.getItemCount();
            int visibleItems = mLayoutManager.getChildCount();
            View lastVisibleItem = mLayoutManager.getBottommostChildView();
            int lastItemsVisible =
                mLayoutManager.getViewAdapterPosition(lastVisibleItem);
            if (!mIsLoadingMore && (visibleItems + lastItemsVisible) >= totalItems) {
                // TODO: 09/08/2017  mPresenter.loadMoreData();
            }
        }
    };

    public SchedulerViewModel(SchedulerFragment fragment, FragmentManager fragmentManager) {
        mFragment = fragment;
        setTabFilter(TAB_TODAY);
        mNavigator = new Navigator(fragment);
        mFragmentManager = fragmentManager;
        setLayoutManager(mLayoutManager);
        setAdapter(new SchedulerAdapter(this, new ArrayList<ManageBookingResponse>()));
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
        mDatePickerDialog =
            DatePickerDialog.newInstance(this, mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.setOnDateSetListener(this);
        mDatePickerDialog.setOnCancelListener(this);
        setTitleTopSheet(FSalonApplication.getInstant().getResources()
            .getString(R.string.action_pick_date));
        setRadioButtonId(R.id.filter_today);
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
    public void setPresenter(SchedulerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Bindable
    public int getTabFilter() {
        return mTabFilter;
    }

    public void setTabFilter(@TabFilter int tabFilter) {
        mTabFilter = tabFilter;
        notifyPropertyChanged(BR.tabFilter);
    }

    @Override
    public void onItemFilterClick(@TabFilter int tab, View topSheet) {
        setTabFilter(tab);
        mAdapter.clear();
        switch (tab) {
            case TAB_TODAY:
                mFilterChoice = FILTER_DAY;
                mEndDate = OUT_OF_INDEX;
                mStartDate = Utils.createTimeStamp(tab);
                setTitleTopSheet(FSalonApplication.getInstant()
                    .getResources().getString(R.string.title_today));
                break;
            case TAB_TOMORROW:
                mEndDate = OUT_OF_INDEX;
                mFilterChoice = FILTER_DAY;
                mStartDate = Utils.createTimeStamp(tab);
                setTitleTopSheet(FSalonApplication.getInstant()
                    .getResources().getString(R.string.title_tomorrow));
                break;
            case TAB_YESTERDAY:
                mEndDate = OUT_OF_INDEX;
                mFilterChoice = FILTER_DAY;
                mStartDate = Utils.createTimeStamp(tab);
                setTitleTopSheet(FSalonApplication.getInstant()
                    .getResources().getString(R.string.title_yesterday));
                break;
            case TAB_SELECT_DATE:
                mEndDate = OUT_OF_INDEX;
                mFilterChoice = FILTER_DAY;
                mTypeSelectDate = TAB_SELECT_DATE;
                showDatePickerDialog();
                break;
            case TAB_SPACE_TIME:
                mFilterChoice = FILTER_SPACE;
                mTypeSelectDate = TAB_START_DATE;
                showDatePickerDialog();
                break;
            default:
                break;
        }
        TopSheetBehavior.from(topSheet).setState(TopSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onSchedulerSuccessful(List<ManageBookingResponse> sections) {
        mAdapter.updateData(sections);
        setPositionNearestTime(mAdapter.getItemWithNearestTime());
    }

    @Override
    public void onSchedulerFail() {
        mNavigator.showToast(R.string.title_scheduler_failure);
    }

    @Override
    public void showLoadMore() {
        setLoadingMore(true);
    }

    @Override
    public void hideLoadMore() {
        setLoadingMore(false);
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
    public void onGetSalonsSuccess(List<Salon> salons) {
        setDepartmentAdapter(new DepartmentAdapter(mFragment.getContext(), salons, this));
        mDepartmentAdapter.selectedPosition(FIRST_ITEM);
        mSalonId = mDepartmentAdapter.getItem(FIRST_ITEM).getId();
        setSalonName(mDepartmentAdapter.getItem(FIRST_ITEM).getName());
        onFilterData();
    }

    @Override
    public void onError(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void onSpaceTimeClick() {
        mFilterChoice = FILTER_SPACE;
        mTypeSelectDate = TAB_START_DATE;
        showDatePickerDialog();
    }

    @Override
    public void onSelectDateClick() {
        mEndDate = OUT_OF_INDEX;
        mFilterChoice = FILTER_DAY;
        mTypeSelectDate = TAB_SELECT_DATE;
        showDatePickerDialog();
    }

    public void onFilterData() {
        mStatus = "";
        if (mIsCanceled) {
            mStatus += "," + STATUS_CANCELED;
        }
        if (mIsWatting) {
            mStatus += "," + STATUS_WATTING;
        }
        if (mIsFinished) {
            mStatus += "," + STATUS_FINISHED;
        }
        if (mIsInLate) {
            mStatus += "," + STATUS_IN_LATE;
        }
        mAdapter.clear();
        mPresenter.getSchedulers(mFilterChoice, mStatus, mStartDate, mEndDate, mSalonId);
    }

    @Override
    public void onBookingItemClick(int id) {
        mNavigator.startActivity(BookingDetailActivity.getInstance(mNavigator.getContext(), id));
    }

    @Bindable
    public SchedulerAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(SchedulerAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Bindable
    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public void setLayoutManager(StickyHeaderLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
        notifyPropertyChanged(BR.layoutManager);
    }

    @Bindable
    public boolean isLoadingMore() {
        return mIsLoadingMore;
    }

    public void setLoadingMore(boolean loadingMore) {
        mIsLoadingMore = loadingMore;
        notifyPropertyChanged(BR.loadingMore);
    }

    @Bindable
    public DepartmentAdapter getDepartmentAdapter() {
        return mDepartmentAdapter;
    }

    public void setDepartmentAdapter(DepartmentAdapter departmentAdapter) {
        mDepartmentAdapter = departmentAdapter;
        notifyPropertyChanged(BR.departmentAdapter);
    }

    @Bindable
    public RecyclerView.OnScrollListener getScrollListenner() {
        return mScrollListenner;
    }

    @Bindable
    public String getTitleTopSheet() {
        return mTitleTopSheet;
    }

    public void setTitleTopSheet(String titleTopSheet) {
        mTitleTopSheet = titleTopSheet;
        notifyPropertyChanged(BR.titleTopSheet);
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
                setTitleTopSheet(Utils.convertDate(mCalendar.getTime()));
                mStartDate = (int) (mCalendar.getTimeInMillis() / 1000);
                setRadioButtonId(R.id.filter_select_date);
                break;
            case TAB_END_DATE:
                mEndDate = (int) (mCalendar.getTimeInMillis() / 1000);
                mSpaceTime = mSpaceTime.concat(" - " + Utils.convertDate(mCalendar.getTime()));
                setTitleTopSheet(mSpaceTime);
                setRadioButtonId(R.id.filter_space_time);
            default:
                break;
        }
    }

    @Bindable
    public boolean isWatting() {
        return mIsWatting;
    }

    public void setWatting(boolean watting) {
        mIsWatting = watting;
        notifyPropertyChanged(BR.watting);
    }

    @Bindable
    public boolean isFinished() {
        return mIsFinished;
    }

    public void setFinished(boolean finished) {
        mIsFinished = finished;
        notifyPropertyChanged(BR.finished);
    }

    @Bindable
    public boolean isCanceled() {
        return mIsCanceled;
    }

    public void setCanceled(boolean canceled) {
        mIsCanceled = canceled;
        notifyPropertyChanged(BR.canceled);
    }

    @Bindable
    public boolean isInLate() {
        return mIsInLate;
    }

    public void setInLate(boolean inLate) {
        mIsInLate = inLate;
        notifyPropertyChanged(BR.inLate);
    }

    @Bindable
    public RadioGroup.OnCheckedChangeListener getChangeListener() {
        return mChangeListener;
    }

    public void setChangeListener(RadioGroup.OnCheckedChangeListener changeListener) {
        mChangeListener = changeListener;
        notifyPropertyChanged(BR.changeListener);
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
    public int getPositionNearestTime() {
        return mPositionNearestTime;
    }

    public void setPositionNearestTime(int positionNearestTime) {
        mPositionNearestTime = positionNearestTime;
        notifyPropertyChanged(BR.positionNearestTime);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        setRadioButtonId(mRadioButtonId);
    }

    @Bindable
    public DrawerLayout.DrawerListener getDrawerListener() {
        return mDrawerListener;
    }

    public void setDrawerListener(DrawerLayout.DrawerListener drawerListener) {
        mDrawerListener = drawerListener;
        notifyPropertyChanged(BR.drawerListener);
    }

    @Bindable
    public String getSalonName() {
        return mSalonName;
    }

    public void setSalonName(String salonName) {
        mSalonName = salonName;
        notifyPropertyChanged(BR.salonName);
    }

    @Override
    public void onSelectedSalonPosition(int pos, Salon salon) {
        if (salon == null) {
            return;
        }
        mDepartmentAdapter.selectedPosition(pos);
        mSalonId = salon.getId();
    }

    /**
     * Anotation for filter tab follow days
     */
    @IntDef({TAB_TODAY, TAB_YESTERDAY, TAB_TOMORROW, TAB_SPACE_TIME, TAB_SELECT_DATE})
    @Target(ElementType.PARAMETER)
    public @interface TabFilter {
        int TAB_TODAY = 0;
        int TAB_YESTERDAY = 1;
        int TAB_TOMORROW = 2;
        int TAB_SPACE_TIME = 3;
        int TAB_SELECT_DATE = 4;
        int TAB_START_DATE = 5;
        int TAB_END_DATE = 6;
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
}
