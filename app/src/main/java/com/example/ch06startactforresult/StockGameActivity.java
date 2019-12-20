package com.example.ch06startactforresult;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Bundle;




import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.math.BigDecimal;


public class StockGameActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private SystemVoice systemVoice;
    private BgmClass bgmClass;

    private ImageView imgIcon;
    private TextView tvPlayerName,tvCash,tvDate,tvDays ,tvProfit;
    public static Cursor cursor;
    public static DBHelper dbHelper;
    private FrameLayout flContent;
    public static FragmentHome fragmentHome;
    private FragmentSell fragmentSell;
    private FragmentBuy fragmentBuy;
    private FragmentManager fragmentManager;
   public static String currentDate;
    private static float currentBuyPrice;
    public static float currentSellPrice;
    public static int currentCash;
    private static int passingDays;
    private static int trasactionCount;
    private static float stockP;
    private static float pri;

    private Cursor cursor2;
    private static int fragIndex;
    private static String playerName;
    private static String account;
    private static String stockID;
    public static String stocksList;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private int currentMenuItem;
    public static FragmentTransaction transaction;
    private AlertDialog.Builder builder;

    private static int[] roles = {R.drawable.h1, R.drawable.h2,R.drawable.h3,R.drawable.h4,R.drawable.h5
            ,R.drawable.h6,R.drawable.h7,R.drawable.h8,R.drawable.h9,R.drawable.h10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_game);


        bgmClass=new BgmClass(this);
        systemVoice=new SystemVoice(this);

        account = getIntent().getStringExtra(DBHelper.COLUMN_ACCOUNT);
        playerName = getIntent().getStringExtra(DBHelper.COLUMN_PLAYER);


        dbHelper = new DBHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        flContent = findViewById(R.id.flContent);
        View header = navigationView.getHeaderView(0);
        imgIcon = header.findViewById(R.id.imgIcon);
        tvPlayerName = header.findViewById(R.id.tvPlayerName);
        tvDate = header.findViewById(R.id.tvDate);
        tvCash = header.findViewById(R.id.tvCash);
        tvDays = header.findViewById(R.id.tvDays);
        tvProfit = header.findViewById(R.id.tvProfit);



       // View ii = navigationView

        fragmentHome = new FragmentHome();
        fragmentSell = new FragmentSell();
        fragmentBuy = new FragmentBuy();
        builder = new AlertDialog.Builder(this);



       // imgTest.setImageResource(R.drawable.icon);
     //   imgTest.setOnTouchListener(new MulitPointTouchListener());

//        TouchImageView imgTest = (TouchImageView)findViewById(R.id.imgTemp);
//        imgTest.setImageResource(R.drawable.testt);
 //       imgTest.setMaxZoom(4f);
    }


    public String getAccount(){
        return account;
    }
    public String getPlayerName(){
        return playerName;
    }

    public String getStockID(){
        return stockID;
    }

    public String getStocksList(){
        return stocksList;
    }

    public String getCurrentDate(){
        return currentDate;
    }

    public int getCurrentCash(){
        return currentCash;
    }

    public float getCurrentBuyPrice(){
        return currentBuyPrice;
    }
    public float getCurrentSellPrice(){
        return currentSellPrice;
    }

    public int getTrasactionCount(){ return trasactionCount;}

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id= menuItem.getItemId();

        if(!(menuItem==navigationView.getMenu().getItem(currentMenuItem))){
            transaction = fragmentManager.beginTransaction();
            switch (id){
                case R.id.nav_home:
                    transaction.replace(R.id.flContent,fragmentHome);
                    transaction.commit();
                    currentMenuItem=0;
                    menuItem.setChecked(true);
                    drawer.closeDrawer(GravityCompat.START);
                    systemVoice.ButtonTouchVoice();
                    break;
                case R.id.nav_sell:
                    currentMenuItem=1;
                    menuItem.setChecked(true);
                    transaction.replace(R.id.flContent,fragmentSell);
                    transaction.commit();
                    drawer.closeDrawer(GravityCompat.START);
                    systemVoice.ButtonTouchVoice();
                    break;
                case R.id.nav_buy:
                    currentMenuItem=2;
                    menuItem.setChecked(true);
                    transaction.replace(R.id.flContent,fragmentBuy);
                    transaction.commit();
                    drawer.closeDrawer(GravityCompat.START);
                    systemVoice.ButtonTouchVoice();
                    break;
                case R.id.nav_next_day:
                    showNextDayAlert();
                    systemVoice.ButtonTouchVoice();
                    break;
                case R.id.nav_send:
                    Intent intent = new Intent(this,ControlPanelActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }


        return false;
    }

    private void showNextDayAlert(){

        builder.setTitle("NEXT DAY?")
                .setMessage("結束今天?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cursor=null;
                        cursor = dbHelper.queryNextDate(currentDate);
                        if(cursor.getCount()>0){
                            cursor.moveToFirst();
                            currentDate = cursor.getString(0);
                            currentBuyPrice=cursor.getFloat(4);
                            currentSellPrice=cursor.getFloat(4);
                            passingDays+=1;
                            dbHelper.updateDays(account,playerName,currentDate,passingDays);
                            notifyDataChange();
                            loadingLocalUserData();
                            drawer.closeDrawer(GravityCompat.START);
                            fragmentHome.homeNotifyChange();

                        }
                    }
                })
                .setNegativeButton("NO",null)
                .setCancelable(false)
                .show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        loadingLocalUserData();
        bgmClass.BGM_stockgameStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.flContent,fragmentHome);
        transaction.commit();
        currentMenuItem=0;
        navigationView.getMenu().getItem(currentMenuItem).setChecked(true);
    }

    public void notifyDataChange(){
        //fragmentSell.initListView();
        //fragmentSell.fetchRemoteData();
//        fragmentBuy.initListView();
  //      fragmentBuy.fetchRemoteData();
        fragmentSell.sellNotifyChange();
        fragmentBuy.buyNotifyChange();

    }

    public void loadingLocalUserData(){
        queryRecordData(account,playerName);
        cursor=null;
        cursor = dbHelper.queryPlayerData(account,playerName);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            imgIcon.setImageResource(roles[cursor.getInt(2)]);
            tvPlayerName.setText(playerName);
            currentCash = cursor.getInt(3);
            passingDays = cursor.getInt(4);
            stockID = cursor.getString(5);
            stocksList = cursor.getString(6);
            currentDate = cursor.getString(8);
            trasactionCount = cursor.getInt(10);
            tvCash.setText(""+currentCash);
            tvDays.setText(""+passingDays);
            tvDate.setText(currentDate);
            cursor = null;
            cursor = dbHelper.queryTodayStockDetail(currentDate);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                currentBuyPrice=cursor.getFloat(4);
                currentSellPrice=cursor.getFloat(4);
            }

        }

        coculate();
        float pri2 = pri*100;
        tvProfit.setText(pri2+"%");

        if(passingDays>60){
            Intent intent = new Intent(this,GameClearActivity.class);
            cursor=null;
            cursor = dbHelper.queryPlayerData(account,playerName);
            dbHelper.updateClearStage(account,playerName);
            startActivity(intent);
            finish();
        }

    }

    private void coculate(){
        pri = 0;
        if (trasactionCount!=0) {
            float b = (currentBuyPrice - stockP) / stockP;
            pri = (float) (Math.round(b * 10000)) / 10000;
        }else {
            tvProfit.setText("0.00%");
        }
    }



    private void queryRecordData(String account,String playerName){
        cursor2 = null;
        cursor2 = dbHelper.queryTransactionRecord(account,playerName);
        Log.v("aaa","~~~"+cursor2.getCount());

        if(cursor2.getCount()>0){
            cursor2.moveToFirst();
            for(int i=0;i<cursor2.getCount();i++){
                Log.v("aaa","~~~"+cursor2.getString(3)+","+cursor2.getString(4)+","+cursor2.getString(5)+
                        ","+cursor2.getString(6)+","+cursor2.getString(7)+","+cursor2.getString(8));

                stockP = cursor2.getFloat(6);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bgmClass.BGMDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bgmClass.BGMPause();
    }


}
