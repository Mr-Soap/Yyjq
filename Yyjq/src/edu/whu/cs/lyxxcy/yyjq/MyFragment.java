package edu.whu.cs.lyxxcy.yyjq;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyFragment extends Fragment {
//    public int resource;
//
//    public MyFragment(int resource) {
//        this.resource = resource;
//    }

    public int resource;

    public MyFragment(int resource) {
        this.resource = resource;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(resource, container, false);
        return view;
    }
}
