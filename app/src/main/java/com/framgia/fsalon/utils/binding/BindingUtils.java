package com.framgia.fsalon.utils.binding;

import android.app.Activity;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.fsalon.FSalonApplication;
import com.framgia.fsalon.R;
import com.framgia.fsalon.data.model.Salon;
import com.framgia.fsalon.data.model.Service;
import com.framgia.fsalon.data.model.Stylist;
import com.framgia.fsalon.screen.booking.BookingViewModel;
import com.framgia.fsalon.utils.LayoutManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import static com.framgia.fsalon.utils.Constant.NO_SCROLL;

/**
 * Created by MyPC on 20/07/2017.
 */
public class BindingUtils {
    @BindingAdapter("errorText")
    public static void setErrorText(TextInputLayout layout, String text) {
        layout.setError(text);
    }

    @BindingAdapter(value = {"recyclerAdapter", "layoutManager", "scrollListenner"},
        requireAll = false)
    public static void setAdapterForRecyclerView(RecyclerView recyclerView,
                                                 RecyclerView.Adapter adapter,
                                                 RecyclerView.LayoutManager layoutManager,
                                                 RecyclerView.OnScrollListener listener) {
        recyclerView.setAdapter(adapter);
        if (layoutManager != null) {
            recyclerView.setLayoutManager(layoutManager);
        }
        if (listener != null) {
            recyclerView.addOnScrollListener(listener);
        }
    }

    @BindingAdapter("recyclerViewPosition")
    public static void setRecyclerViewPosition(RecyclerView recyclerView,
                                               int position) {
        if (position != NO_SCROLL) {
            recyclerView.getLayoutManager().scrollToPosition(position);
        }
    }

    @BindingAdapter("deviderItem")
    public static void setDeviderItem(RecyclerView recyclerView, int devider) {
        recyclerView.addItemDecoration(new DeviderItemDecoration(devider));
    }

    @BindingAdapter({"searchableSpinnerAdapter"})
    public static void setSearchableSpinnerAdapter(SearchableSpinner view, ArrayAdapter adapter) {
        view.setAdapter(adapter);
    }

    @BindingAdapter({"resourceId"})
    public static void setResourceId(LinearLayout layout, int resId) {
        layout.setBackgroundResource(resId);
    }

    @InverseBindingAdapter(attribute = "stylistId", event = "stylistIdAttrChanged")
    public static int captureStylistId(SearchableSpinner view) {
        Object selectedItem = view.getSelectedItem();
        return ((Stylist) selectedItem).getId();
    }

    @BindingAdapter(value = {"viewModel", "stylistId", "stylistIdAttrChanged"}, requireAll = false)
    public static void setCategoryId(SearchableSpinner view, final BookingViewModel viewModel,
                                     int value, final InverseBindingListener bindingListener) {
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bindingListener.onChange();
                viewModel.getData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        view.setOnItemSelectedListener(listener);
    }

    @BindingAdapter("bind:viewPagerAdapter")
    public static void setViewPagerAdapter(ViewPager viewPager, PagerAdapter pagerAdapter) {
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
    }

    @BindingAdapter("bind:listener")
    public static void setBottomNavigationViewListener(BottomNavigationView view,
                                                       BottomNavigationView.OnNavigationItemSelectedListener listener) {
        view.setOnNavigationItemSelectedListener(listener);
    }

    @BindingAdapter("bind:listener")
    public static void setSwipeRefreshLayoutListener(SwipeRefreshLayout view, SwipeRefreshLayout
        .OnRefreshListener listener) {
        view.setOnRefreshListener(listener);
    }

    @BindingAdapter("bind:finish")
    public static void setRefresh(SwipeRefreshLayout view, boolean isFinish) {
        if (isFinish) {
            view.setRefreshing(false);
        }
    }

    @BindingAdapter("bind:tabSelect")
    public static void setSelectedTab(ViewPager viewPager, int position) {
        viewPager.setCurrentItem(position);
    }

    @BindingAdapter({"colorId"})
    public static void setcolorId(TextView view, int colorId) {
        view.setTextColor(view.getResources().getColor(colorId));
    }

    @BindingAdapter(value = {"bind:imageUrl", "bind:error"}, requireAll = false)
    public static void loadImage(ImageView view, String imageUrl, Drawable error) {
        if (error == null) {
            Glide.with(view.getContext())
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_no_image)
                .into(view);
        } else {
            Glide.with(view.getContext()).load(imageUrl).centerCrop().placeholder(error).into(view);
        }
    }

    @BindingAdapter("lineChartData")
    public static void setNewDataChart(LineChart lineChart, LineData lineData) {
        lineChart.setData(lineData);
    }

    @InverseBindingAdapter(attribute = "bind:stylist", event = "stylistAttrChanged")
    public static Stylist captureStylist(SearchableSpinner view) {
        Object selectedItem = view.getSelectedItem();
        return (Stylist) selectedItem;
    }

    @BindingAdapter(value = {"bind:stylist", "stylistAttrChanged"}, requireAll = false)
    public static void setStylist(SearchableSpinner view, Stylist value,
                                  final InverseBindingListener bindingListener) {
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bindingListener.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        view.setOnItemSelectedListener(listener);
    }

    @InverseBindingAdapter(attribute = "bind:service", event = "serviceAttrChanged")
    public static Service captureService(SearchableSpinner view) {
        Object selectedItem = view.getSelectedItem();
        return (Service) selectedItem;
    }

    @BindingAdapter(value = {"bind:service", "serviceAttrChanged"}, requireAll = false)
    public static void setService(SearchableSpinner view, final Service value,
                                  final InverseBindingListener bindingListener) {
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bindingListener.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        view.setOnItemSelectedListener(listener);
    }

    @InverseBindingAdapter(attribute = "bind:salon", event = "salonAttrChanged")
    public static Salon captureSalon(SearchableSpinner view) {
        Object selectedItem = view.getSelectedItem();
        return (Salon) selectedItem;
    }

    @BindingAdapter(value = {"bind:salon", "salonAttrChanged"}, requireAll = false)
    public static void setSalon(SearchableSpinner view, Salon salon,
                                final InverseBindingListener bindingListener) {
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bindingListener.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        view.setOnItemSelectedListener(listener);
    }

    @BindingAdapter(value = {"layoutManager", "adapter"}, requireAll = false)
    public static void setRecyclerView(RecyclerView view,
                                       LayoutManager.LayoutManagerFactory factory,
                                       RecyclerView.Adapter adapter) {
        if (adapter == null) {
            return;
        }
        view.setAdapter(adapter);
        view.setLayoutManager(factory.create(view));
    }

    @BindingAdapter({"bind:array", "listener"})
    public static void setupSpinner(Spinner spinner, final String[] stringArray, Spinner
        .OnItemSelectedListener listener) {
        ArrayAdapter<String> adapter =
            new ArrayAdapter<>(FSalonApplication.getInstant(), android.R.layout
                .simple_spinner_item, stringArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (listener != null) spinner.setOnItemSelectedListener(listener);
    }

    @BindingAdapter(value = {"lockMode", "drawerListener"}, requireAll = false)
    public static void setLockMode(DrawerLayout layout, int lockMode,
                                   DrawerLayout.DrawerListener drawerListener) {
        layout.setDrawerLockMode(lockMode);
        if (drawerListener != null) {
            layout.addDrawerListener(drawerListener);
        }
    }

    @BindingAdapter("changeListener")
    public static void setChangeListener(RadioGroup view,
                                         RadioGroup.OnCheckedChangeListener listener) {
        if (listener == null) {
            return;
        }
        view.setOnCheckedChangeListener(listener);
    }

    @BindingAdapter("check")
    public static void checkRadioButton(RadioGroup view, int radioButtonId) {
        view.check(radioButtonId);
    }

    @BindingAdapter("imageResource")
    public static void setResourceId(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter("bind:selection")
    public static void setResourceId(Spinner spinner, int position) {
        spinner.setSelection(position);
    }

    @BindingAdapter("activity")
    public static void setUpDrawerListener(final DrawerLayout drawlayout, final Activity activity) {
        ActionBarDrawerToggle actionBarDrawerToggle =
            new ActionBarDrawerToggle(activity, drawlayout, R.string.msg_open_drawer,
                R.string.msg_close_drawer);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_filter_30dp);
        actionBarDrawerToggle.syncState();
    }

    @BindingAdapter("textColor")
    public static void setTextColor(TextView text, int textColor) {
        text.setTextColor(text.getResources().getColor(textColor));
    }
}
