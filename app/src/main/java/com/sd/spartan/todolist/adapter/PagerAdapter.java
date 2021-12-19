package com.sd.spartan.todolist.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sd.spartan.todolist.fragment.FirstFragment;
import com.sd.spartan.todolist.fragment.SecondFragment;
import com.sd.spartan.todolist.fragment.ThirdFragment;

public class PagerAdapter extends FragmentStateAdapter {
    private int mNumOfTabs;

    public PagerAdapter(FragmentActivity fa, int mNumOfTabs) {
        super(fa);
        this.mNumOfTabs = mNumOfTabs ;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FirstFragment();
            case 1:
                return new SecondFragment();
            case 2:
                return new ThirdFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return mNumOfTabs;
    }
}
