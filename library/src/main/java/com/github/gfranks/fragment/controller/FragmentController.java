package com.github.gfranks.fragment.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class FragmentController extends Fragment {

    public static final String BROADCAST_TOGGLE = "broadcast_toggle";
    private static final String LAST_SELECTED_FRAGMENT = "last_selected_fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getTagsForFragments().isEmpty()) {
            throw new IllegalStateException("You must specify at least 1 tag to initialize the fragment controller");
        }
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_controller, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            String tag = savedInstanceState.getString(LAST_SELECTED_FRAGMENT, getTagForFragment(getNextFragmentIndex()));
            showFragment(getTagForFragment(getTagsForFragments().indexOf(tag)), savedInstanceState);
        } else {
            String tag = getTagForFragment(getNextFragmentIndex());
            showFragment(tag, getInitialArgumentsForFragment(tag));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver, new IntentFilter(BROADCAST_TOGGLE));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LAST_SELECTED_FRAGMENT, getChildFragmentManager().findFragmentById(getContainerId()).getTag());
        getChildFragmentManager().findFragmentById(getContainerId()).onSaveInstanceState(outState);
    }

    protected final int getContainerId() {
        return R.id.fragment_controller;
    }

    private void showFragment(String tag, Bundle args) {
        Fragment fragment = getChildFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = createFragmentFromTag(tag, args);
        } else {
            if (fragment.getArguments() != null) {
                fragment.getArguments().putAll(args);
            } else {
                fragment.setArguments(args);
            }

            if (getChildFragmentManager().findFragmentById(getContainerId()) != null &&
                    getChildFragmentManager().findFragmentById(getContainerId()).getTag().equals(tag)) {
                return;
            }
        }
        getChildFragmentManager().beginTransaction()
                .replace(getContainerId(), fragment, tag)
                .commit();
    }

    private String getTagForFragment(int index) {
        return getTagsForFragments().get(index);
    }

    private boolean canToggle() {
        return getTagsForFragments().size() > 1;
    }

    private int getNextFragmentIndex() {
        int index = 0;
        if (!canToggle() || getChildFragmentManager().findFragmentById(getContainerId()) == null) {
            return index;
        }

        String tag = getChildFragmentManager().findFragmentById(getContainerId()).getTag();
        index = getTagsForFragments().indexOf(tag);
        if (index == getTagsForFragments().size() - 1) {
            return 0;
        } else {
            return ++index;
        }
    }

    /**
     * Gets the list of tags to use when toggling between fragments
     *
     * @return List of tags
     */
    public abstract @NonNull List<String> getTagsForFragments();

    /**
     * Creates the Fragment, if needed when toggling
     *
     * @param tag String representing the Tag of the current Fragment
     * @param args Bundle of initial arguments
     * @return Next Fragment to be displayed
     */
    public abstract @NonNull Fragment createFragmentFromTag(String tag, Bundle args);

    /**
     *
     * Gets the Bundle arguments for the initial creation of the fragment
     *
     * @param tag String representing the Tag of the current Fragment
     * @return Bundle with initial fragment arguments
     */
    public abstract Bundle getInitialArgumentsForFragment(String tag);

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCAST_TOGGLE)) {
                if (!canToggle()) {
                    return;
                }

                showFragment(getTagForFragment(getNextFragmentIndex()), intent.getExtras());
            }
        }
    };
}
