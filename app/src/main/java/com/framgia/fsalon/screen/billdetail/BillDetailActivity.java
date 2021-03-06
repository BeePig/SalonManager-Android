package com.framgia.fsalon.screen.billdetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.framgia.fsalon.R;
import com.framgia.fsalon.data.source.BillRepository;
import com.framgia.fsalon.data.source.api.FSalonServiceClient;
import com.framgia.fsalon.data.source.remote.BillRemoteDataSource;
import com.framgia.fsalon.databinding.ActivityBillDetailBinding;

import static com.framgia.fsalon.utils.Constant.OUT_OF_INDEX;

/**
 * Billdetail Screen.
 */
public class BillDetailActivity extends AppCompatActivity {
    private BillDetailContract.ViewModel mViewModel;
    private static final String BUNDLE_BILL_ID = "bundle_bill_id";

    public static Intent getInstance(Context context, int billId) {
        Intent intent = new Intent(context, BillDetailActivity.class);
        Bundle args = new Bundle();
        args.putInt(BUNDLE_BILL_ID, billId);
        intent.putExtras(args);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int billId = OUT_OF_INDEX;
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            billId = bundle.getInt(BUNDLE_BILL_ID);
        }
        mViewModel = new BillDetailViewModel(this);
        BillDetailContract.Presenter presenter =
            new BillDetailPresenter(mViewModel, billId, new BillRepository(new
                BillRemoteDataSource(FSalonServiceClient.getInstance())));
        mViewModel.setPresenter(presenter);
        ActivityBillDetailBinding binding =
            DataBindingUtil.setContentView(this, R.layout.activity_bill_detail);
        binding.setViewModel((BillDetailViewModel) mViewModel);
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_booking_detail);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.color_white));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    protected void onStop() {
        mViewModel.onStop();
        super.onStop();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
