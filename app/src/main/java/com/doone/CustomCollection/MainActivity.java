package com.doone.CustomCollection;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.doone.CustomCollection.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 主页面
 *
 * @author 林建鹏 QQ83606260
 * @date 2019年9月18日
 */
public class MainActivity extends AppCompatActivity {

    FragmentManager fm = getFragmentManager();
    FragmentTransaction tx ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ZcView zc = findViewById(R.id.zc);
        zc.setOnSelectListener(new ZcView.SelectListener() {
            @Override
            public void selectOn(int index) {
                switch (index){
                    case 0:
                        tx = fm.beginTransaction();
                        tx.replace(R.id.fragment_container, new FragmentGonGe(), "0");
                        tx.commit();
                        break;
                    case 1:
                        tx = fm.beginTransaction();
                        tx.replace(R.id.fragment_container, new FragmentAddress(), "1");
                        tx.commit();
                        break;
                    case 2:
                        tx = fm.beginTransaction();
                        tx.replace(R.id.fragment_container, new FragmentPhotoView(), "2");
                        tx.commit();
                        break;
                    case 3:
                        tx = fm.beginTransaction();
                        tx.replace(R.id.fragment_container, new FragmentSwich(), "3");

                        tx.commit();
                        break;
                }
            }
        });
        zc.setData(new ArrayList() {{
            add(new HashMap() {{
                put("icon0", R.mipmap.bottom_app0);
                put("icon1", R.mipmap.bottom_app1);
                put("name", "应用");
                put("select", false);
                put("cont", 0);
            }});
            add(new HashMap() {{
                put("icon0", R.mipmap.bottom_contacts0);
                put("icon1", R.mipmap.bottom_contacts1);
                put("name", "通讯录");
                put("select", false);
                put("cont", 100);
            }});
            add(new HashMap() {{
                put("icon0", R.mipmap.bottom_msg0);
                put("icon1", R.mipmap.bottom_msg1);
                put("name", "消息");
                put("select", false);
                put("cont", 8);

            }});
            add(new HashMap() {{
                put("icon0", R.mipmap.bottom_me0);
                put("icon1", R.mipmap.bottom_me1);
                put("name", "我");
                put("select", false);
                put("cont", 25);
            }});
        }});
    }

}