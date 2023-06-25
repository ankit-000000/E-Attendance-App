package com.example.e_attendece_application.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.e_attendece_application.Add_Student_Fragment;
import com.example.e_attendece_application.Add_Teacher_Fragment;

public class ViewPagerRegistrationAdapter extends FragmentPagerAdapter {
    public ViewPagerRegistrationAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return new Add_Student_Fragment();
        }
        else {
            return new Add_Teacher_Fragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0){
            return "Add Student";
        }else {
            return "Add Teacher";
        }
    }
}
