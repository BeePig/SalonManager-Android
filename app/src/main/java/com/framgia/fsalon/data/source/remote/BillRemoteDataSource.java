package com.framgia.fsalon.data.source.remote;

import com.framgia.fsalon.data.model.BillRequest;
import com.framgia.fsalon.data.model.BillResponse;
import com.framgia.fsalon.data.model.ListBillRespond;
import com.framgia.fsalon.data.source.BillDataSource;
import com.framgia.fsalon.data.source.api.FSalonApi;
import com.framgia.fsalon.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framgia.retrofitservicecreator.api.model.Respone;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

import static com.framgia.fsalon.utils.Constant.ApiParram.BILL_ITEMS;
import static com.framgia.fsalon.utils.Constant.ApiParram.CUSTOMER_ID;
import static com.framgia.fsalon.utils.Constant.ApiParram.CUSTOMER_NAME;
import static com.framgia.fsalon.utils.Constant.ApiParram.DEPARTMENT_ID;
import static com.framgia.fsalon.utils.Constant.ApiParram.END_DATE;
import static com.framgia.fsalon.utils.Constant.ApiParram.GRAND_TOTAL;
import static com.framgia.fsalon.utils.Constant.ApiParram.ORDER_BOOKING_ID;
import static com.framgia.fsalon.utils.Constant.ApiParram.PHONE;
import static com.framgia.fsalon.utils.Constant.ApiParram.START_DATE;
import static com.framgia.fsalon.utils.Constant.ApiParram.STATUS;
import static com.framgia.fsalon.utils.Constant.ApiParram.TYPE_FILTER;

/**
 * Created by MyPC on 03/08/2017.
 */
public class BillRemoteDataSource extends BaseRemoteDataSource implements BillDataSource {
    public BillRemoteDataSource(FSalonApi fSalonApi) {
        super(fSalonApi);
    }

    @Override
    public Observable<BillResponse> createBill(BillRequest billRequest) {
        Map<String, String> parram = new HashMap<>();
        if (billRequest.getCustomerId() > 0) {
            parram.put(CUSTOMER_ID, String.valueOf(billRequest.getCustomerId()));
        }
        if (billRequest.getStatus() >= 0) {
            parram.put(STATUS, String.valueOf(billRequest.getStatus()));
        }
        if (billRequest.getOrderBookingId() > 0) {
            parram.put(ORDER_BOOKING_ID, String.valueOf(billRequest.getOrderBookingId()));
        }
        if (billRequest.getGrandTotal() > 0) {
            parram.put(GRAND_TOTAL, String.valueOf(billRequest.getGrandTotal()));
        }
        parram.put(BILL_ITEMS, billRequest.getBillItems().toString());
        parram.put(PHONE, billRequest.getPhone());
        parram.put(CUSTOMER_NAME, billRequest.getCustomerName());
        return mFSalonApi.createBill(parram).flatMap(
            new Function<Respone<BillResponse>, ObservableSource<BillResponse>>() {
                @Override
                public ObservableSource<BillResponse> apply(
                    @NonNull Respone<BillResponse> billResponseRespone)
                    throws Exception {
                    return Utils.getResponse(billResponseRespone);
                }
            });
    }

    @Override
    public Observable<List<ListBillRespond>> filterBills(String type, int startDate, int endDate,
                                                         String status, int departmentId,
                                                         int customerId) {
        Map<String, String> parrams = new HashMap<>();
        if (type != null) {
            parrams.put(TYPE_FILTER, type);
        }
        if (startDate > 0) {
            parrams.put(START_DATE, String.valueOf(startDate));
        }
        if (endDate > 0) {
            parrams.put(END_DATE, String.valueOf(endDate));
        }
        if (departmentId > 0) {
            parrams.put(DEPARTMENT_ID, String.valueOf(departmentId));
        }
        if (customerId > 0) {
            parrams.put(CUSTOMER_ID, String.valueOf(customerId));
        }
        parrams.put(STATUS, String.valueOf(status));
        return mFSalonApi.filterBills(parrams).flatMap(
            new Function<Respone<List<ListBillRespond>>,
                ObservableSource<List<ListBillRespond>>>() {
                @Override
                public ObservableSource<List<ListBillRespond>> apply(
                    @NonNull Respone<List<ListBillRespond>> listRespone)
                    throws Exception {
                    return Utils.getResponse(listRespone);
                }
            });
    }

    @Override
    public Observable<BillResponse> getBillById(int id) {
        return mFSalonApi.getBillById(id).flatMap(
            new Function<Respone<BillResponse>, ObservableSource<BillResponse>>() {
                @Override
                public ObservableSource<BillResponse> apply(
                    @NonNull Respone<BillResponse> billResponse)
                    throws Exception {
                    return Utils.getResponse(billResponse);
                }
            });
    }
}
