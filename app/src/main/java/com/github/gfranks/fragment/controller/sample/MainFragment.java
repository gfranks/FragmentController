package com.github.gfranks.fragment.controller.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.github.gfranks.fragment.controller.FragmentController;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends FragmentController {

    private List<String> mTags;

    @NonNull
    @Override
    public List<String> getTagsForFragments() {
        if (mTags == null) {
            mTags = new ArrayList<>();
            mTags.add(FirstFragment.TAG);
            mTags.add(SecondFragment.TAG);
        }
        return mTags;
    }

    @Override
    public Bundle getInitialArgumentsForFragment(String tag) {
        return new Bundle();
    }

    @NonNull
    @Override
    public Fragment createFragmentFromTag(String tag, Bundle args) {
        Fragment fragment;
        if (tag.equals(FirstFragment.TAG)) {
            fragment = new FirstFragment();
        } else {
            fragment = new SecondFragment();
        }

        fragment.setArguments(args);

        return fragment;
    }
}
