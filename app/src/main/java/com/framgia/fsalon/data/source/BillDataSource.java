package com.framgia.fsalon.data.source;

import com.framgia.fsalon.data.model.BillRequest;
import com.framgia.fsalon.data.model.BillResponse;
import com.framgia.fsalon.data.model.ListBillRespond;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by MyPC on 03/08/2017.
 */
public interface BillDataSource {
    Observable<BillResponse> createBill(BillRequest billRequest);
    Observable<List<ListBillRespond>> filterBills(String type, int startDate, int endDate,
                                                  String status, int departmentId, int customerId);
    Observable<BillResponse> getBillById(int id);
}
