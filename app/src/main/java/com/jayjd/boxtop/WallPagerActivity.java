package com.jayjd.boxtop;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jayjd.boxtop.adapter.WallPagerAdapter;
import com.jayjd.boxtop.entity.WallPagerEntity;
import com.jayjd.boxtop.listeners.TvOnItemListener;
import com.jayjd.boxtop.listeners.ViewAnimationShake;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7GridLayoutManager;

import java.io.File;
import java.util.List;

public class WallPagerActivity extends AppCompatActivity {

    private WallPagerAdapter wallPagerAdapter;
    private TvRecyclerView trWallList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wall_pager);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initData();
    }

    private static final String TAG = "WallPagerActivity";

    private void initData() {
        OkGo.<String>get(ApiConfig.WALLPAPER_URL).execute(new StringCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body();
                        WallPagerEntity wallPagerEntity = new Gson().fromJson(json, WallPagerEntity.class);
                        if (wallPagerEntity.getCode() == 200) {
                            List<WallPagerEntity.DataBean.ListBean> list = wallPagerEntity.getData().getList();
                            wallPagerAdapter.setItems(list);
                            wallPagerAdapter.notifyDataSetChanged();
                            trWallList.requestFocus();
                        }
                    } catch (JsonSyntaxException ignored) {
                    }
                }
                Toast.makeText(WallPagerActivity.this, "加载壁纸列表异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Toast.makeText(WallPagerActivity.this, "加载壁纸列表失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        trWallList = findViewById(R.id.tr_wall_list);
        trWallList.setLayoutManager(new V7GridLayoutManager(this, 4));
        wallPagerAdapter = new WallPagerAdapter();
        trWallList.setAdapter(wallPagerAdapter);
        trWallList.setOnItemListener(new TvOnItemListener());
        trWallList.setOnInBorderKeyEventListener(new ViewAnimationShake(trWallList, this, 0, null));

        wallPagerAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            WallPagerEntity.DataBean.ListBean item = baseQuickAdapter.getItem(i);
            String img = item.getImg();
            if (img != null && !img.isEmpty()) {
                downloadWallPager(img);
            }
        });
    }

    private void downloadWallPager(String img) {
        OkGo.<File>get(img).execute(new Callback<>() {
            @Override
            public void onStart(Request<File, ? extends Request> request) {
                Log.d(TAG, "onStart: 开启下载");
            }

            @Override
            public void onSuccess(Response<File> response) {
                Log.d(TAG, "onSuccess: 下载成功");
            }

            @Override
            public void onCacheSuccess(Response<File> response) {
                Log.d(TAG, "onCacheSuccess: 缓存成功");
            }

            @Override
            public void onError(Response<File> response) {
                Log.d(TAG, "onError: 下载失败");
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish: 下载完成");
            }

            @Override
            public void uploadProgress(Progress progress) {
                Log.d(TAG, "uploadProgress: 上传进度");            }

            @Override
            public void downloadProgress(Progress progress) {
                Log.d(TAG, "downloadProgress: 下载进度");
            }

            @Override
            public File convertResponse(okhttp3.Response response) throws Throwable {
                Log.d(TAG, "convertResponse: 转换响应");

                return null;
            }
        });
    }
}