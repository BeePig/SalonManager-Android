package com.framgia.fsalon.screen.scheduler;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.framgia.fsalon.BasePresenter;
import com.framgia.fsalon.BaseViewModel;
import com.framgia.fsalon.data.model.ManageBookingResponse;
import com.framgia.fsalon.data.model.Salon;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface SchedulerContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onItemFilterClick(@SchedulerViewModel.TabFilter int tab, View topSheet);
        void onSchedulerSuccessful(List<ManageBookingResponse> sections);
        void onSchedulerFail();
        void showLoadMore();
        void hideLoadMore();
        void onBookingItemClick(int id);
        void onFilterClick(DrawerLayout layout);
        void onGetSalonsSuccess(List<Salon> salons);
        void onError(String message);
        void onSpaceTimeClick();
        void onSelectDateClick();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getSchedulers(String filterChoice, String status, int startDate, int endDate,
                           int departmentId);
        void loadMoreData();
        void getAllSalons();
    }
}
