package com.mosstech.StarRadio.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class RecyclerViewAdapter<T,H extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<H> {
    protected List<T> mDataList;
    protected Context mContext;

    protected RecyclerViewOnItemClickListener mListener;

    private List<T> mDataListBackup;
    private boolean mBackedUp = false;
    private int mLastQueryStringSize = 0;
    public RecyclerViewAdapter(@NonNull Context mContext, @NonNull List<T> mDataList,
                               @NonNull RecyclerViewOnItemClickListener mListener) {
        this.mDataList = mDataList;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @Override
    public int getItemCount() {
        if(mDataList !=null)
            return mDataList.size();
        else
            return 0;
    }

    protected boolean isBackedUp(){
        return mBackedUp;
    }

    protected List<T> getRealDataList(){
        return mDataList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getDataList()
    {
        if(mBackedUp)
            return mDataListBackup;

        return mDataList;
    }


    public void doFilter(String filterQuery)
    {
        if(!mBackedUp)
        {
            mDataListBackup = new ArrayList<>(mDataList);
            mBackedUp = true;
        }

        if(filterQuery.length() < mLastQueryStringSize)
            mDataList = new ArrayList<>(mDataListBackup);

        mLastQueryStringSize = filterQuery.length();

        Iterator<T> iter = mDataList.iterator();
        while( iter.hasNext() ) {
            T object = iter.next();
            if( !object.toString().toLowerCase().contains(filterQuery.toLowerCase()) ) {
                iter.remove();
            }
        }

        notifyDataSetChanged();
    }

    public void endFilter()
    {
        if(mDataListBackup != null) {
            mDataList = new ArrayList<>(mDataListBackup);
            mDataListBackup = null;
            notifyDataSetChanged();
        }
        mBackedUp = false;
    }
}
