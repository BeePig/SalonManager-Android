package com.framgia.fsalon.screen.listbill;

import android.support.v4.widget.DrawerLayout;

import com.framgia.fsalon.BasePresenter;
import com.framgia.fsalon.BaseViewModel;
import com.framgia.fsalon.data.model.ListBillRespond;
import com.framgia.fsalon.data.model.Salon;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface ListBillContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onCreateBillClick();
        void onFilterSuccessfully(List<ListBillRespond> bills);
        void onHideProgressBar();
        void onShowProgressBar();
        void onFilterClick(DrawerLayout layout);
        void onInputCustomerNameError();
        void onHideCustomerNameError();
        void onSpaceTimeClick();
        void onSelectDateClick();
        void onGetSalonsSuccess(List<Salon> salons);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void filterBills(String type, int startDate, int endDate, String status,
                         int departmentId, int customerId);
        void getAllSalons();
    }
}
