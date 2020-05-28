package com.mosstech.StarRadio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mosstech.StarRadio.Models.IChannel;
import com.squareup.picasso.Picasso;

public class PlayerFullScreenActivity extends BaseMusicServiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_full_screen);
    }

    @Override
    protected void initViews() {
        if(mMusicService != null)
        {
            TextView chnName = findViewById(R.id.textView_fullScreen_channelName);
            TextView listName = findViewById(R.id.textView_fullScreen_listName);
            ImageView iv_Play = findViewById(R.id.buttonPlay);
            ProgressBar progressBar = findViewById(R.id.progressBar_mp_preparing);


            if(mMusicService.isPreparing() || mMusicService.isPlaying()) {
                iv_Play.setImageResource(R.drawable.button_stop);
            }
            else {
                iv_Play.setImageResource(R.drawable.button_play);
            }

            chnName.setText(mMusicService.getChannelName());
            listName.setText(mMusicService.getPlayListName());

            progressBar.setVisibility(mMusicService.isPreparing()?View.VISIBLE:View.INVISIBLE);

            final IChannel channel = mMusicService.getChannel();
            if(channel != null) {
                TextView language = findViewById(R.id.textView_fullScreen_language);
                TextView country = findViewById(R.id.textView_fullScreen_country);
                TextView webSite = findViewById(R.id.textView_fullScreen_website);
                ImageView logo = findViewById(R.id.imageView_fullScreen_logo);
                language.setText(channel.getLanguage());
                country.setText(channel.getCountry());
                webSite.setText(channel.getHomepage());
                webSite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(channel.getHomepage())));
                    }
                });

                //prepare logo
                Picasso.get()
                        .load(channel.getFavicon())
                        .placeholder(R.drawable.default_info)
                        .error(R.drawable.default_info)
                        .fit().centerCrop()
                        .into(logo);
            }
        }
    }

    public void handleCloseFullScrClick(View v)
    {
        finish();
    }
}
