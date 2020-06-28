package com.mosstech.StarRadio.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mosstech.StarRadio.Models.ICategory;
import com.mosstech.StarRadio.Models.IChannel;
import com.mosstech.StarRadio.R;

import java.util.List;

public class CategoryAdapter extends RecyclerViewAdapter<ICategory, CategoryAdapter.CategoryViewHolder> {

    public CategoryAdapter(@NonNull Context mContext,
                           @NonNull List<ICategory> mDataList,
                           @NonNull RecyclerViewOnItemClickListener mListener) {
        super(mContext, mDataList, mListener);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.category_layout,viewGroup, false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {
        ICategory cat = mDataList.get(i);

        if(cat != null)
        {
            categoryViewHolder.stationCount.setText(
                    mContext.getString(R.string.text_station_count)+": "+cat.getStationCount());
            categoryViewHolder.categoryName.setText(cat.getName());
        }
    }

    final class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView categoryName;
        TextView stationCount;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            categoryName = itemView.findViewById(R.id.textView_category_name);
            stationCount = itemView.findViewById(R.id.textView_category_station_count);
        }


        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if(isBackedUp())
            {
                ICategory curr = getRealDataList().get(pos);
                mListener.onItemClick(v, getDataList().indexOf(curr));
            }
            else
                mListener.onItemClick(v,pos);
        }
    }
}
