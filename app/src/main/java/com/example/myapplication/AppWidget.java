package com.example.myapplication;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {


    private int totalValue;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, totalValue);

            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget);
            RemoteViews viewsText = new RemoteViews(context.getPackageName(), R.id.tvTotal);
            
            viewsText.setTextViewText(R.id.tvTotal, "sssssssssss");

            views.setOnClickPendingIntent(R.id.btnAdd, pendingIntent);
            String rs = totalValue + "k";

            Toast.makeText(context,appWidgetId+ "0", Toast.LENGTH_SHORT).show();

            // Tạo Intent để kết nối với CollectionWidgetService, liên kết listView với CollectionWidgetService
            Intent intentService = new Intent(context, CollectionWidgetService.class);
            intentService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            // Đặt URI cho Intent để đảm bảo tính duy nhất
            intentService.setData(Uri.parse(intentService.toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(appWidgetId, R.id.lv, intentService);

            updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId, totalValue);
            appWidgetManager.updateAppWidget(appWidgetIds, views);
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            totalValue = intent.getIntExtra("totalValue", 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget);
            views.setTextViewText(R.id.tvTotal, "sssssssssss");
            // Call the updateAppWidget method to update the widget with the new totalValue
            updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId, totalValue);

        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int totalValue) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget);
        views.setTextViewText(R.id.tvTotal, totalValue+"k");
        Log.d("vaooo1",totalValue+"");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }



    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}