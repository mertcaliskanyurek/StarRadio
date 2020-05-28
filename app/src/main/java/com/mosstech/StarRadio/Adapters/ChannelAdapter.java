package com.mosstech.StarRadio.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mosstech.StarRadio.Data.PrefenceManager;
import com.mosstech.StarRadio.Models.IChannel;
import com.mosstech.StarRadio.R;

import java.util.List;

/**
 * Created by Mert on 30.08.2016.
 */
public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder> {

    private List<IChannel> mChannelList;
    //for db helper
    private Context mContext;
    private int mCurrPlayingPosition=-1;

    private RecyclerViewOnItemClickListener mListener;

    public ChannelAdapter(@NonNull Context context, @NonNull List<IChannel> channels,@NonNull RecyclerViewOnItemClickListener listener){
        mChannelList = channels;
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public ChannelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.channel_layout,viewGroup, false);

        return new ChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelViewHolder channelViewHolder, int position) {
        final IChannel chn = mChannelList.get(position);

        if(chn != null)
        {
            channelViewHolder.tvChnName.setText(mChannelList.get(position).getName());
            //init favorite iv
            channelViewHolder.ivFavorite.setImageResource(chn.getIsFavorite()?
                    R.drawable.button_favori_on_list:R.drawable.button_favori_off_list);

            if(mCurrPlayingPosition == position)
                channelViewHolder.ivPlaying.setVisibility(View.VISIBLE);
            else
                channelViewHolder.ivPlaying.setVisibility(View.INVISIBLE);

            channelViewHolder.ivLogo.setImageResource(R.drawable.default_simge);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(mChannelList !=null)
            return mChannelList.size();
        else
            return 0;
    }

    public void updateViewAtPosition(int position)
    {
        //remove playing icon on channel
        notifyItemChanged(mCurrPlayingPosition);
        mCurrPlayingPosition = position;
        //add playing icon on clicked channel
        notifyItemChanged(position);
    }

    public List<IChannel> getChannelList() {
        return mChannelList;
    }

    final class ChannelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvChnName;
        ImageView ivFavorite, ivLogo, ivPlaying;

        public ChannelViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvChnName = (TextView) itemView.findViewById(R.id.kanalIsmıTextView);
            ivLogo = (ImageView) itemView.findViewById(R.id.kanalSimgesiImageView);
            ivPlaying = (ImageView) itemView.findViewById(R.id.musicPlaying);

            ivFavorite = (ImageView) itemView.findViewById(R.id.imageView3);
            ivFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemFavoriteClick(v,getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            //add playing icon on clicked channel
            mListener.onItemClick(v, getAdapterPosition());
        }
    }

}

