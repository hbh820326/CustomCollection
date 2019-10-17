package com.doone.CustomCollection;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.doone.CustomCollection.R;

public class FragmentSwich extends Fragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gonge_view, container, false);
        SwichView swichView=view.findViewById(R.id.swich);
        swichView.setOnOpenListener(new SwichView.OnOpenListener() {
            @Override
            public void isOnOrOff(boolean isOn) {
                Toast.makeText(getActivity(),isOn?"打开":"关闭",Toast.LENGTH_SHORT).show();
            }
        });
        SwichView swichView2=view.findViewById(R.id.swich2);
        swichView2.setOnOpenListener(new SwichView.OnOpenListener() {
            @Override
            public void isOnOrOff(boolean isOn) {
                Toast.makeText(getActivity(),isOn?"打开":"关闭",Toast.LENGTH_SHORT).show();
            }
        });
        SwichView swichView3=view.findViewById(R.id.swich3);
        swichView3.setOnOpenListener(new SwichView.OnOpenListener() {
            @Override
            public void isOnOrOff(boolean isOn) {
                Toast.makeText(getActivity(),isOn?"打开":"关闭",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
