package com.example.myapplication;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;



class CollectionWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private int mAppWidgetId;
    private String expenses;
    private int total;
    private List<Money> widgetItems = new ArrayList<>();
    Database db;

    public CollectionWidgetViewFactory(Context context, Intent intent) {

        //Lấy ID của AppWidget gửi
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        mContext = context;
        expenses = intent.getStringExtra("expenses");
        db = new Database(context);
    }

    @Override
    public void onDataSetChanged() {
        widgetItems.clear();
        total = 0;
        Cursor cursor = db.readExpenses();
        Log.d("dasd2222222",cursor.getCount()+"");

        while (cursor.moveToNext()){
            this.widgetItems.add(new Money(cursor.getInt(0)));
            Log.d("ccc",cursor.getInt(0)+"");
            total += cursor.getInt(0);
            Log.d("total",total+"");
        }

        Intent updateIntent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        updateIntent.putExtra("totalValue", total);
        mContext.sendBroadcast(updateIntent);
    }

    public void onCreate() {

    }
    // Tạo và trả về RemoteViews cho mỗi item trong widget
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.expenses);
        rv.setTextViewText(R.id.tvExpenses, widgetItems.get(position).getExpenses() + "");
        // Return the RemoteViews object.
        return rv;
    }

    @Override
    public void onDestroy() {
        widgetItems.clear();
    }

    @Override
    public int getCount() {
        return widgetItems.size();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }
}
