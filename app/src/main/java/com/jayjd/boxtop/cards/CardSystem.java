package com.jayjd.boxtop.cards;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jayjd.boxtop.R;


public class CardSystem extends BaseCardFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_system, container, false);
    }

    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
        Log.d("CardSystem", "onFragmentVisible() called");
    }

    @Override
    protected void onFragmentInvisible() {
        super.onFragmentInvisible();
        Log.d("CardSystem", "onFragmentInvisible() called");
    }
}