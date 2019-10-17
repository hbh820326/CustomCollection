package com.doone.CustomCollection;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.doone.CustomCollection.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class FragmentPhotoView extends Fragment {

    RequestManager glide;
    DialogView dialogView;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        glide = Glide.with(context);
        dialogView=new DialogView(getActivity());


    }

    ArrayList<Map<String, Object>> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tablayout, container, false);

        data.add(new HashMap<String, Object>() {{
            put("title", "网络GIF图片");
            put("value", "http://3fantizi.com/article/pic/TP8975_01.gif");//网络GIF图片
        }});
        data.add(new HashMap<String, Object>() {{
            put("title", "网络图片");
            put("value", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569500056044&di=e31ab8af319f0ea59b88714ecf46321e&imgtype=0&src=http%3A%2F%2Fimg.smzy.com%2Fimges%2F2017%2F0930%2F20170930035031651.jpg");//本地GIF图片
        }});
        data.add(new HashMap<String, Object>() {{
            put("title", "网络GIF图片");
            put("value", "http://img2.shangxueba.com/img/fevte/20140813/9/C55847DC31292AAF4379DF85522DF3B3.gif");//网络GIF图片
        }});
        data.add(new HashMap<String, Object>() {{
            put("title", "网络GIF图片");
            put("value", "http://img.zcool.cn/community/03895f156a58ade32f875520f2bb24f.gif");//网络GIF图片
        }});

        data.add(new HashMap<String, Object>() {{
            put("title", "本地图片");
            put("value", R.drawable.pic1);//本地资源图片
        }});
        data.add(new HashMap<String, Object>() {{
            put("title", "本地GIF图片");
            put("value", R.drawable.timg);//本地GIF图片
        }});

        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new PagerAdapter() {
            ArrayList<PictureView> array = new ArrayList();

            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return data.get(position).get("title") + "";
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            public void destroyItem(ViewGroup container, int position, Object object) {
                PictureView pictureView = (PictureView) object;
                container.removeView(pictureView);
                pictureView.reset();//回收资源，bitma占内存巨大，一定要回收
                array.add(pictureView);
            }

            public Object instantiateItem(ViewGroup container, int position) {
                final PictureView pictureView;
                if (array.size() > 0) {
                    pictureView = array.remove(array.size() - 1);
                } else {
                    pictureView = new PictureView(container.getContext());
                }

                Object value=data.get(position).get("value");
                if(value instanceof String){
                    dialogView.start("加载中。。。");
                    ZCApplication.RequestNetwork((String)value,null,new Callback() {//用okhttp进行下载图片
                        @Override
                        public void onFailure(Call call, IOException e) {
                            dialogView.stop();
                            Log.e("Z", "请求失败：", e);
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            dialogView.stop();
                            pictureView.setData(response.body().bytes());

                        }
                    });
                }else pictureView.setData(getResources().openRawResource((int)value));


             //   用glide加载网络图片或本地图片         这里有图片缓存
//                dialogView.start("加载中。。。");
//                glide.load(data.get(position).get("value")).override(618, 1000).into(new CustomTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//                        pictureView.setData(resource);//这里可以传 Bitmap,Drawable,GIFDrawable
//                        dialogView.stop();
//                    }
//                    @Override
//                    public void onLoadCleared( Drawable placeholder) {
//                        dialogView.stop();
//                    }
//                });

                container.addView(pictureView);
                return pictureView;
            }
        });
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        return view;
    }
}

