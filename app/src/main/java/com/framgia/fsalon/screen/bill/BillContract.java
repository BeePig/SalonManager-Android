package com.framgia.fsalon.screen.bill;

import com.framgia.fsalon.BasePresenter;
import com.framgia.fsalon.BaseViewModel;
import com.framgia.fsalon.data.model.BillRequest;
import com.framgia.fsalon.data.model.BillResponse;
import com.framgia.fsalon.data.model.BookingOder;
import com.framgia.fsalon.data.model.Salon;
import com.framgia.fsalon.data.model.Service;
import com.framgia.fsalon.data.model.Stylist;
import com.framgia.fsalon.data.model.User;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface BillContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onAddBillClick();
        void showProgressbar();
        void onGetStylistSuccess(List<Stylist> stylists);
        void onGetSalonsSuccess(List<Salon> salons);
        void hideProgressbar();
        void onError(String message);
        void onGetServiceSuccess(List<Service> services);
        void onDeleteItemClick(int position);
        void onGetBillSuccess(BillResponse billResponse);
        void onCreateBillClick();
        void onGetBookingSuccess(BookingOder bookingOder);
        void onFilterPhone();
        void onInputFormError();
        void onInputCustomerNameError();
        void onInputCustomerPhoneError();
        void getCustomerSuccessfull(User user);
        void onHideCustomerPhoneError();
        void onHideCustomer();
        void onGetStatusSuccess(List<String> statusList);
        void onGetEditBillSuccess(BillResponse billResponse);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getAllServices();
        void getAllStylists(int id);
        void createBill(BillRequest billRequest);
        void getBookingByPhone(String phone);
        boolean validateFormInput(Service service, Stylist stylist, Salon salon, String pricce,
                                  String qty, String phoneCustomer, String nameCustomer);
        void getAllSalon();
        void getCustomerByPhone(String phone);
        int setSalonPosition(BillResponse billResponse, List<Salon> salons);
        void editBill(BillRequest billRequest);
        void getStatus();
        void getBill(int id);
    }
}
