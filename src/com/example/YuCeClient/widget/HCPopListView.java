package com.example.YuCeClient.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.*;
import android.widget.*;
import com.example.YuCeClient.R;
import com.example.YuCeClient.util.DisplayUtil;

import java.util.List;

/**
 * Created by sreay on 15-6-8.
 */
public class HCPopListView extends FrameLayout {
    private TextView title;
    private ListView listView;
    private TextView cancel;

    private FrameLayout rootView;
    private MyAdapter myAdapter;

    private Activity activity;
    private HCPopListViewListener listViewListener;
    private List<String> items;

    private int itemHeight = 0;

    public interface HCPopListViewListener {
        public void onItemClicked(int index, String content);

        public void onCancelClicked();
    }

    public HCPopListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, null, null, null, null);
    }

    public HCPopListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null, null, null, null);
    }

    public HCPopListView(Context context) {
        super(context);
        init(context, null, null, null, null);
    }

    public HCPopListView(Context context, String titleStr, String cancelStr, List<String> items, HCPopListViewListener listViewListener) {
        super(context);
        init(context, titleStr, cancelStr, items, listViewListener);
    }


    private void init(Context context, String titleStr, String cancelStr, List<String> items, HCPopListViewListener listViewListener) {
        this.activity = (Activity) context;
        itemHeight = DisplayUtil.dipToPixels(activity, 40);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.hc_pop_listview, this, true);
        title = (TextView) findViewById(R.id.title);
        listView = (ListView) findViewById(R.id.listView);
        cancel = (TextView) findViewById(R.id.cancel);

        if (items != null) {
            this.items = items;
        }

        if (!TextUtils.isEmpty(titleStr)) {
            title.setText(titleStr);
        }

        if (!TextUtils.isEmpty(cancelStr)) {
            cancel.setText(cancelStr);
        }

        rootView = getRootView(activity);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(params);
        setId(R.id.hc_pop_listview);
        this.listViewListener = listViewListener;

        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (HCPopListView.this.listViewListener != null) {
                    HCPopListView.this.listViewListener.onItemClicked(i + 1, HCPopListView.this.items.get(i));
                    dismiss();
                }
            }
        });

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HCPopListView.this.listViewListener != null) {
                    HCPopListView.this.listViewListener.onCancelClicked();
                    dismiss();
                }
            }
        });
    }

    private void notifyDataChanged(List<String> items) {
        this.items = items;
        if (myAdapter != null) {
            myAdapter.notifyDataSetChanged();
        }
    }

    public static boolean onBackPressed(Activity activity) {
        HCPopListView dlg = getDlgView(activity);
        if (null != dlg && dlg.isShowing()) {
            dlg.dismiss();
            return true;
        }
        return false;
    }

    public static boolean hasDlg(Activity activity) {
        HCPopListView dlg = getDlgView(activity);
        return dlg != null;
    }

    public static boolean isShowing(Activity activity) {
        HCPopListView dlg = getDlgView(activity);
        if (null != dlg && dlg.isShowing()) {
            return true;
        }
        return false;
    }

    @SuppressLint("WrongViewCast")
    public static HCPopListView getDlgView(Activity activity) {
        return (HCPopListView) getRootView(activity).findViewById(R.id.hc_pop_listview);
    }

    private static FrameLayout getRootView(Activity activity) {
        return (FrameLayout) activity.findViewById(R.id.rootView);
    }

    public boolean isShowing() {
        return getVisibility() == View.VISIBLE;
    }

    public void show() {
        if (getParent() != null) {
            return;
        }
        rootView.addView(this);
        setVisibility(View.VISIBLE);
    }

    public void dismiss() {
        if (getParent() == null) {
            return;
        }
        setVisibility(View.GONE);
        rootView.removeView(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (items == null) {
                return 0;
            } else {
                return items.size();
            }
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            TextView itemView = null;
            if (view == null) {
                itemView = new TextView(activity);
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
                itemView.setLayoutParams(params);
                itemView.setTextSize(getResources().getDimensionPixelSize(R.dimen.hc_pop_listview_item_text_size));
                itemView.setGravity(Gravity.CENTER);
                itemView.setTextColor(Color.parseColor("#000000"));
            } else {
                itemView = (TextView) view;
            }
            itemView.setText(items.get(position));
            return itemView;
        }
    }
}
