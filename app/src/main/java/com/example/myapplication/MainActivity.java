package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText editTextButton;
    private int appWidgetId;
    private Boolean income = false;
    private Boolean expenses = false;
    private Database db;
    private ArrayList<Money> expensesList;
    private Button btnAdd,btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextButton = findViewById(R.id.edt_money);


        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        db = new Database(MainActivity.this);
        income = false;
        expenses = false;

        Intent intent = getIntent();
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        Toast.makeText(this, appWidgetId+"1", Toast.LENGTH_SHORT).show();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }


    }

    public void onClickAdd(View v){
        if(editTextButton.getText().toString().equals("")){
            Toast.makeText(this, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.appwidget);
        views.setOnClickPendingIntent(R.id.btnAdd, pendingIntent);
        Database db = new Database(MainActivity.this);

        Intent serviceIntent = new Intent(this, CollectionWidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.putExtra("expenses", editTextButton.getText().toString());
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.tvTotal);

        db.addExpenses(editTextButton.getText().toString().trim());



        // Cập nhật widget để hiển thị dữ liệu mới
        views.setRemoteAdapter(R.id.lv, serviceIntent);
        serviceIntent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        appWidgetManager.updateAppWidget(appWidgetId, views);
        Toast.makeText(this, appWidgetId+" 2", Toast.LENGTH_SHORT).show();

        // Thiết lập kết quả và kết thúc hoạt động
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
    public void onClickDelete(View v){
        //Xóa dữ liệu trên RecyclerView
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        db.deleteAllData();
        // Gửi broadcast thông báo rằng dữ liệu đã thay đổi
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv);
        expensesList.clear();
    }
}