package com.jeevan.shrofile.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jeevan.shrofile.R;

public class MainActivity extends BaseActivity {
    private RecyclerView rvVideoList;
    private SwipeRefreshLayout swlVideoList;
    private FloatingActionButton fabCreateVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        fabCreateVideo = (FloatingActionButton) findViewById(R.id.fab_crate_video);
        swlVideoList = (SwipeRefreshLayout) findViewById(R.id.swl_video_list);
        rvVideoList = (RecyclerView) findViewById(R.id.rv_video_list);
        rvVideoList.setLayoutManager(new LinearLayoutManager(MainActivity.this));


    }

    @Override
    protected void initListener() {
        swlVideoList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

    }

    @Override
    protected void bindDataWithUi() {

    }



  /*  mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0 && mFloatingActionButton.getVisibility() == View.VISIBLE) {
                mFloatingActionButton.hide();
            } else if (dy < 0 && mFloatingActionButton.getVisibility() != View.VISIBLE) {
                mFloatingActionButton.show();
            }
        }
    });*/
}
