package com.doone.CustomCollection;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.doone.CustomCollection.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentAddress extends Fragment {

    List<Map<String, Object>> data = new ArrayList() {{
        add(new HashMap<String, Object>() {{
            put("name", "安娜");
        }});
        add(new HashMap<String, Object>() {{
            put("name", "陈阡安");
        }});
        add(new HashMap<String, Object>() {{
            put("name", "陈祖儿");
        }});
        add(new HashMap<String, Object>() {{
            put("name", "范冰冰");
        }});
        add(new HashMap<String, Object>() {{
            put("name", "郭富城");
        }});
        add(new HashMap<String, Object>() {{
            put("name", "黄碧华");
        }});
        add(new HashMap<String, Object>() {{
            put("name", "李小刚");
        }});
        add(new HashMap<String, Object>() {{
            put("name", "林心如");
        }});
        add(new HashMap<String, Object>() {{
            put("name", "刘德华");
        }});
        add(new HashMap<String, Object>() {{
            put("name", "毛宁");
        }});
        add(new HashMap<String, Object>() {{
            put("name", "容祖儿");
        }});
        add(new HashMap<String, Object>() {{
            put("name", "谢廷锋");
        }});
        add(new HashMap<String, Object>() {{
            put("name", "杨祖钦");
        }});
        add(new HashMap<String, Object>() {{
            put("name", "赵云");
        }});
        add(new HashMap<String, Object>() {{
            put("name", "张小宇");
        }});
    }};
    StickyHeaderDecoration decor;//自定义悬浮头部
    LinearLayoutManager LManager;
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Map map = (Map) v.getTag();
            Toast.makeText(getActivity(), map.get("name") + "", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addresslayout, container, false);
        RecyclerView list = view.findViewById(R.id.list);
        LManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(LManager);
        decor = new StickyHeaderDecoration(getResources().getDimension(R.dimen.dp20), getResources().getDimension(R.dimen.sp15), getResources().getDimension(R.dimen.dp15));//这个才是重点，悬浮分区
        list.addItemDecoration(decor);
        LetterIndexView indexview = view.findViewById(R.id.letter_list_view);
        indexview.setILetterIndexer(new LetterIndexView.ILetterIndexer() {
            String flag;

            @Override
            public void getPositionForSection(String section) {
                for (int i = 0; i < data.size(); i++) {
                    if (section.equals(decor.py.convert(data.get(i).get("name") + "")) && !section.equals(flag)) {
                        LManager.scrollToPositionWithOffset(i, 0);
                        flag = section;
                        break;
                    }
                }
            }
        });

        list.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = inflater.inflate(R.layout.address_item, viewGroup, false);
                view.setOnClickListener(listener);
                return new RecyclerView.ViewHolder(view) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Map map = data.get(i);

                String name = map.get("name") + "";
                TextView tv = (TextView) viewHolder.itemView;
                Bitmap bitmap = textBitmap(name, (int) getResources().getDimension(R.dimen.dp40), name.hashCode());
                tv.setText(name);
                Drawable img = new BitmapDrawable(bitmap);
                img.setBounds(0, 0, (int) getResources().getDimension(R.dimen.dp40), (int) getResources().getDimension(R.dimen.dp40));
                tv.setCompoundDrawables(img, null, null, null); //设置左按钮
                tv.setTag(map);
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        });


        return view;
    }

    public static String[] colors = {"#62cb0d", "#f64b6b", "#f6774b", "#4bc0f6", "#4b73f6", "#a04bf6", "#39e9fa", "#1ee08c", "#f1a222"};
    Rect rect = new Rect();

    /**
     * @param text     文字图片内容
     * @param diameter 圆直径
     * @return 圆形图片
     */
    public Bitmap textBitmap(String text, int diameter, int color) {
        Bitmap outBitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);//铺一层Bitmap
        Canvas canvas = new Canvas(outBitmap);//画布
        Paint paint = new Paint();//画笔
        paint.setAntiAlias(true);// 设置画笔无锯齿
        paint.setColor(Color.parseColor(colors[Math.abs(color) % 9]));//
        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, paint);//画圆
        paint.setColor(Color.WHITE);
        paint.setTextSize(getResources().getDimension(R.dimen.sp25));
        paint.getTextBounds(text, 0, 1, rect);//测量文字
        canvas.drawText(text, 0, 1, (diameter - rect.width()) / 2, (diameter - rect.height()) / 2 + rect.height()-5, paint);
        return outBitmap;
    }
}
