package com.mosstech.StarRadio.Adapters;

import android.view.View;

public interface RecyclerViewOnItemClickListener {
    void onItemClick(View view,int position);
    void onItemFavoriteClick(View favoriteView, int position);
}
