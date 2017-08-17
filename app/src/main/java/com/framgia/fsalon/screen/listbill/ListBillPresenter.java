package com.framgia.fsalon.screen.listbill;

import com.framgia.fsalon.data.model.ListBillRespond;
import com.framgia.fsalon.data.model.Salon;
import com.framgia.fsalon.data.source.BillRepository;
import com.framgia.fsalon.data.source.SalonRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.framgia.fsalon.utils.Constant.OUT_OF_INDEX;

/**
 * Listens to user actions from the UI ({@link ListBillFragment}), retrieves the data and updates
 * the UI as required.
 */
public class ListBillPresenter implements ListBillContract.Presenter {
    private final ListBillContract.ViewModel mViewModel;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private BillRepository mBillRepository;
    private SalonRepository mSalonRepository;

    public ListBillPresenter(ListBillContract.ViewModel viewModel, BillRepository billRepository,
                             SalonRepository salonRepository) {
        mViewModel = viewModel;
        mBillRepository = billRepository;
        mSalonRepository = salonRepository;
        getAllSalons();
        filterBills(null, OUT_OF_INDEX, OUT_OF_INDEX, null, OUT_OF_INDEX, OUT_OF_INDEX);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mCompositeDisposable.clear();
    }

    @Override
    public void filterBills(String type, int startDate, int endDate, String status,
                            int departmentId, int customerId) {
        Disposable disposable = mBillRepository.filterBills(type, startDate, endDate, status,
            departmentId, customerId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(@NonNull Disposable disposable) throws Exception {
                    mViewModel.onShowProgressBar();
                }
            })
            .subscribeWith(new DisposableObserver<List<ListBillRespond>>() {
                @Override
                public void onNext(@NonNull List<ListBillRespond> listBillResponds) {
                    mViewModel.onFilterSuccessfully(listBillResponds);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    mViewModel.onHideProgressBar();
                }

                @Override
                public void onComplete() {
                    mViewModel.onHideProgressBar();
                }
            });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getAllSalons() {
        Disposable disposable = mSalonRepository.getAllSalons()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(@NonNull Disposable disposable) throws Exception {
                    mViewModel.onShowProgressBar();
                }
            })
            .subscribeWith(
                new DisposableObserver<List<Salon>>() {
                    @Override
                    public void onNext(@NonNull List<Salon> salons) {
                        mViewModel.onGetSalonsSuccess(salons);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mViewModel.onHideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        mViewModel.onHideProgressBar();
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
