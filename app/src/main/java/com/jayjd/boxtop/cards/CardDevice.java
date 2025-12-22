package com.jayjd.boxtop.cards;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.jayjd.boxtop.R;
import com.jayjd.boxtop.cards.adapter.DeviceInfoAdapter;
import com.jayjd.boxtop.entity.DeviceInfo;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7GridLayoutManager;

import java.util.ArrayList;
import java.util.List;


public class CardDevice extends BaseCardFragment {

    private TvRecyclerView rvDevices;
    private DeviceInfoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View root = inflater.inflate(R.layout.fragment_card_device, container, false);

        initRecyclerView(root);
        initData();

        return root;
    }

    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
        Log.d("CardDevice", "onFragmentVisible() called");
    }

    @Override
    protected void onFragmentInvisible() {
        super.onFragmentInvisible();
        Log.d("CardDevice", "onFragmentInvisible() called");
    }

    private void initRecyclerView(View root) {
        rvDevices = root.findViewById(R.id.rv_devices);

        // 2 列网格（TV 专用）
        V7GridLayoutManager layoutManager =
                new V7GridLayoutManager(requireContext(), 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        rvDevices.setLayoutManager(layoutManager);
        rvDevices.setFocusable(false);
        rvDevices.setItemAnimator(null); // 信息卡建议关闭动画
        rvDevices.setHasFixedSize(true);
    }

    private void initData() {
        List<DeviceInfo> list = new ArrayList<>();
        list.add(new DeviceInfo(R.drawable.ic_wifi_24dp, "网络", "已连接"));
        list.add(new DeviceInfo(R.drawable.ic_security_key_24dp, "存储", "已挂载"));
        list.add(new DeviceInfo(R.drawable.ic_bluetooth_connected_24dp, "蓝牙", "已连接"));
        list.add(new DeviceInfo(R.drawable.ic_developer_board_24dp, "CPU", "已启动"));

        adapter = new DeviceInfoAdapter();
        rvDevices.setAdapter(adapter);
        adapter.setItems(list);
    }
}
