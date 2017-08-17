package com.framgia.fsalon.data.source;

import com.framgia.fsalon.data.model.BillRequest;
import com.framgia.fsalon.data.model.BillResponse;
import com.framgia.fsalon.data.model.ListBillRespond;
import com.framgia.fsalon.data.source.remote.BillRemoteDataSource;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by MyPC on 03/08/2017.
 */
public class BillRepository implements BillDataSource {
    private BillRemoteDataSource mRemoteDataSource;

    public BillRepository(BillRemoteDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public Observable<BillResponse> createBill(BillRequest billRequest) {
        return mRemoteDataSource.createBill(billRequest);
    }

    @Override
    public Observable<List<ListBillRespond>> filterBills(String type, int startDate, int endDate,
                                                         String status, int departmentId,
                                                         int customerId) {
        return mRemoteDataSource
            .filterBills(type, startDate, endDate, status, departmentId, customerId);
    }

    @Override
    public Observable<BillResponse> getBillById(int id) {
        return mRemoteDataSource.getBillById(id);
    }
}
