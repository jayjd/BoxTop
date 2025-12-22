package com.jayjd.boxtop.cards;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.jayjd.boxtop.R;
import com.jayjd.boxtop.cards.adapter.AppUsageRankAdapter;
import com.jayjd.boxtop.dao.AllAppsInfoDao;
import com.jayjd.boxtop.database.AppDataBase;
import com.jayjd.boxtop.entity.AppInfo;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import java.util.List;


public class CardSystem extends BaseCardFragment {

    AppUsageRankAdapter adapter;
    AllAppsInfoDao allAppsInfoDao;
    private TvRecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_card_system, container, false);
        recyclerView = root.findViewById(R.id.rv_app_rank);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(null);

        AppDataBase instance = AppDataBase.getInstance(appContext);
        allAppsInfoDao = instance.getAllAppsInfoDao();
        adapter = new AppUsageRankAdapter();
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
        Log.d("CardSystem", "onFragmentVisible() called");
        new Thread(() -> {
            List<AppInfo> appInfoByOpenAppCountDesc = allAppsInfoDao.getAppInfoByOpenAppCountDesc();
            requireActivity().runOnUiThread(() -> adapter.submitList(appInfoByOpenAppCountDesc));
        }).start();
    }

    @Override
    protected void onFragmentInvisible() {
        super.onFragmentInvisible();
        Log.d("CardSystem", "onFragmentInvisible() called");
    }
}