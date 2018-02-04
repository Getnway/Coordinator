package com.example.coordinator;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyBehavior";
    private static final int COLLAPSING = 120;
    private static final int TEAM_NAME_PADDING_BOTTOM_START = 30;
    private static final int TEAM_NAME_PADDING_BOTTOM_END = 45;
    private static final int MATCH_SCORE_PADDING_TOP_START = 78;
    private static final int MATCH_SCORE_PADDING_TOP_END = 138;
    private AppBarLayout mAppBarLayout;
    private ImageView civ_host_logo;
    private ImageView civ_guest_logo;
    private TextView tv_match_status;
    private LinearLayout ll_host;
    private LinearLayout ll_guest;
    private RelativeLayout rl_match_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        civ_host_logo = (ImageView) findViewById(R.id.civ_host_logo);
        civ_guest_logo = (ImageView) findViewById(R.id.civ_guest_logo);
        tv_match_status = (TextView) findViewById(R.id.tv_match_status);
        ll_host = (LinearLayout) findViewById(R.id.ll_host);
        ll_guest = (LinearLayout) findViewById(R.id.ll_guest);
        rl_match_score = (RelativeLayout) findViewById(R.id.rl_match_score);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float scale = ((float) Math.abs(verticalOffset)) / COLLAPSING;
                setScale(civ_host_logo, 1 - scale);
                setScale(civ_guest_logo, 1 - scale);
                setScale(tv_match_status, 1 - scale);
                int teamNamePaddingBottom = (int) (TEAM_NAME_PADDING_BOTTOM_START + (TEAM_NAME_PADDING_BOTTOM_END - TEAM_NAME_PADDING_BOTTOM_START) * scale);
                ll_host.setPadding(ll_host.getPaddingLeft(), ll_host.getPaddingTop(), ll_host.getPaddingRight(), teamNamePaddingBottom);
                ll_guest.setPadding(ll_guest.getPaddingLeft(), ll_guest.getPaddingTop(), ll_guest.getPaddingRight(), teamNamePaddingBottom);
                int matchScorePaddingTop = (int) (MATCH_SCORE_PADDING_TOP_START + (MATCH_SCORE_PADDING_TOP_END - MATCH_SCORE_PADDING_TOP_START) * scale);
                rl_match_score.setPadding(rl_match_score.getPaddingLeft(), matchScorePaddingTop,
                        rl_match_score.getPaddingRight(), rl_match_score.getPaddingBottom());
                Log.d(TAG, String.format("call onOffsetChanged(): verticalOffset = [%s], scale = [%s], teamNamePaddingBottom = [%s], matchScorePaddingTop = [%s]",
                        verticalOffset, scale, teamNamePaddingBottom, matchScorePaddingTop));
            }
        });
    }

    private void setScale(View view, float scale) {
        view.setScaleX(scale);
        view.setScaleY(scale);
    }
}
