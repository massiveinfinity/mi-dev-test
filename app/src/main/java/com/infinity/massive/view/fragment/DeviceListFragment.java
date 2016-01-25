package com.infinity.massive.view.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.infinity.massive.ApplicationMassiveInfinity;
import com.infinity.massive.model.Devices;
import com.infinity.massive.utils.DataBaseClient;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Ilanthirayan on 25/1/16.
 */
public class DeviceListFragment extends Fragment{

    private static final String TAG = DeviceListFragment.class.getSimpleName();

    private ProductGridViewAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyListText;
    private Activity mActivity;

    private RecyclerView mRecyclerView;

    private GridLayoutManager mGridLayoutManager ;
    private List<Devices> productsList;

    final AtomicInteger loadingPageNumber = new AtomicInteger(0);

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 1; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int resultSize = 20;//initial value 20 for the default


    public static DeviceListFragment getInstance(Activity mActivity){
        DeviceListFragment fragment = new DeviceListFragment();
        fragment.mActivity = mActivity;
        return fragment;
    }

    final BroadcastReceiver downloadFinishedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            displayEmptyOrList();
        }
    };

    @Override
    public final void onDestroy(){
        super.onDestroy();
        ApplicationMassiveInfinity.getLbm().unregisterReceiver(downloadFinishedReceiver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ProductDownloadService.start(false, null);
    }

    protected final void displayEmptyOrList() {
        if (mActivity == null)
            return;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final int downloading = ProductDownloadService.getRemainingBlockRenderProductLayoutDownloads();

                if (downloading > 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    emptyListText.setVisibility(View.GONE);
                    Log.d(TAG, "@@displayEmptyOrList showing progress bar");
                    return;
                }

                if (!DataBaseClient.getInstance().hasAnyProducts()) {
                    progressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    emptyListText.setVisibility(View.VISIBLE);
                    Log.d(TAG, "@@displayEmptyOrList showing empty product list message");
                    return;
                }

                //NORMAL DISPLAY
                progressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                emptyListText.setVisibility(View.GONE);
                initializeAdapter();
                Log.d(TAG, "@@displayEmptyOrList showing the product list");

            }
        });
    }

    private void initializeAdapter(){
        //initialise here otherwise list will be null
        productsList = DataBaseClient.getInstance().getProducts();
        adapter = new ProductGridViewAdapter(getActivity(), productsList);
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_product, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressProductLayout);
        emptyListText = (TextView) view.findViewById(R.id.product_empty_list_msg);
        ApplicationMassiveInfinity.getLbm().registerReceiver(downloadFinishedReceiver,
                new IntentFilter(ProductDownloadService.ACTION_BLOCK_RENDER_PRODUCT_DOWNLOAD_FINISHED));

        //CARD VIEW
        mRecyclerView = (RecyclerView) view.findViewById(R.id.product_recycle_list);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mGridLayoutManager.getItemCount();
                firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal + 1) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    loadingPageNumber.incrementAndGet();
                    onLoadMore();
                    loading = true;
                }
            }
        });

        displayEmptyOrList();

        return view;
    }

    public void onLoadMore() {
        if (!Utils.isOnline())
            return;

        if (resultSize < 20) {
            return;
        }

        //add null , so the adapter will check view_type and show progress bar at bottom
        productsList.add(null);
        mGridLayoutManager.setSpanCount(1);
        adapter.notifyItemInserted(productsList.size() - 1);

        ProductController mProductController = new ProductController();
        Call<ProductsParentResponse> response = mProductController.setProductsService(loadingPageNumber.get(), 20);
        response.enqueue(new Callback<ProductsParentResponse>() {
            @Override
            public void onResponse(Response<ProductsParentResponse> response, Retrofit retrofit) {
                Log.d(TAG, "Response Raw :: " + response.raw());
                if (response.body().getStatus().getCode() == 0) {
                    //SUCCESSFULLY PRODUCTS RECEIVE

                    //remove the progress item
                    productsList.remove(productsList.size() - 1);
                    mGridLayoutManager.setSpanCount(2);
                    adapter.notifyItemRemoved(productsList.size());

                    //SAVE THE PRODUCTS IN TO THE DATABASE
                    DataBaseClient.getInstance().insertProducts(response.body(), false);

                    for (Products products : response.body().getProducts()) {
                        productsList.add(products);
                        adapter.notifyItemInserted(productsList.size());
                    }

                } else {
                    //ERROR MESSAGE DIALOG
                    Toast.makeText(ApplicationMassiveInfinity.getContext(),
                            response.body().getStatus().getMsg(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Throwable e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                //remove the progress item
                productsList.remove(productsList.size() - 1);
                mGridLayoutManager.setSpanCount(2);
                adapter.notifyItemRemoved(productsList.size());
            }
        });

    }

    public void setScrollPosition(final int mScrollPosition){
        mGridLayoutManager.scrollToPosition(mScrollPosition);
    }
}
