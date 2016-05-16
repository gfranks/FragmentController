package com.github.gfranks.fragment.controller.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.gfranks.fragment.controller.FragmentController;

public class FirstFragment extends Fragment {

    public static final String TAG = "first_fragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // you can pass any extras here and they will be passed on to the next fragment
                Intent intent = new Intent(FragmentController.BROADCAST_TOGGLE);
                // intent.putExtra(<name>, <extra>);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            }
        });
    }
}
