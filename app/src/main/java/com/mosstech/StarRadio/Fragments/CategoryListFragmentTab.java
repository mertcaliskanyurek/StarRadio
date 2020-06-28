package com.mosstech.StarRadio.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mosstech.StarRadio.Adapters.CategoryAdapter;
import com.mosstech.StarRadio.Adapters.RecyclerViewOnItemClickListener;
import com.mosstech.StarRadio.Models.Category;
import com.mosstech.StarRadio.Models.ICategory;
import com.mosstech.StarRadio.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public abstract class CategoryListFragmentTab extends ChannelListFromWebFragmentTab
        implements RecyclerViewOnItemClickListener, Response.Listener<String>, Response.ErrorListener {

    private CategoryAdapter mCategoryAdapter;
    private String mSelectedCategoryName;

    private boolean mShowCategories = true;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list,container,false);
        setRootView(root);
        if(!mShowCategories)
            return super.onCreateView(inflater,container,savedInstanceState);

        if(mCategoryAdapter != null && mCategoryAdapter.getDataList() != null)
            onPreparedCategories(mCategoryAdapter.getDataList());
        else {
            showProgress(true);
            prepareCategories();
        }
        return root;
    }

    /**
     * Make a Volley request about which list you want. Set listeners with this keyword
     */
    protected abstract void prepareCategories();

    protected void onPreparedCategories(List<ICategory> categories)
    {
        showProgress(false);
        //init list
        if(categories != null)
        {
            final RecyclerView recyclerView = getRootView().findViewById(R.id.recyclerView_channel_list);
            mCategoryAdapter = new CategoryAdapter(getRootViewContext(), categories,this);
            recyclerView.setAdapter(mCategoryAdapter);
            final GridLayoutManager manager = new GridLayoutManager(getRootViewContext(), 3);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
        }
    }

    protected String getSelectedCategoryName()
    {
        return mSelectedCategoryName;
    }

    @Override
    public void onItemClick(View view, int position) {
        if(mShowCategories) {
            mSelectedCategoryName = mCategoryAdapter.getDataList().get(position).getName();
            String[] splitted = getListName().split("-");
            setListName(splitted[0]+" - "+mSelectedCategoryName);
            makeRequest();
            mShowCategories = false;
        }
        else
            super.onItemClick(view,position);
    }

    private void showProgress(boolean show)
    {
        final ProgressBar bar = getRootView().findViewById(R.id.progressBar_requestChannels);
        bar.setVisibility(show?View.VISIBLE:View.GONE);
    }

    @Override
    public void onResponse(String responseString) {
        if(!mShowCategories)
        {
            super.onResponse(responseString);
            return;
        }

        JSONArray response = null;
        try {
            response = new JSONArray(responseString);

            List<ICategory> categories = new ArrayList<>(response.length());
            for (int i = 0; i < response.length(); i++) {
                ICategory channel = new Category(response.getJSONObject(i));
                categories.add(channel);
            }

            onPreparedCategories(categories);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(!mShowCategories)
        {
            super.onErrorResponse(error);
            return;
        }

        Toast.makeText(getRootViewContext(),R.string.err_white_downloading_channels,Toast.LENGTH_SHORT).show();
        onPreparedCategories(null);
    }

    public boolean isShowCategories() {
        return mShowCategories;
    }

    public void onBackPressed()
    {
        mShowCategories = true;
        prepareCategories();
    }

    @Override
    public void onSearch(String searchQuery) {
        if(mShowCategories)
        {
            mCategoryAdapter.doFilter(searchQuery);
        }
        else super.onSearch(searchQuery);
    }

    @Override
    public void onSearchFinished() {
        if(mShowCategories)
        {
            mCategoryAdapter.endFilter();
        }
        else super.onSearchFinished();
    }
}
