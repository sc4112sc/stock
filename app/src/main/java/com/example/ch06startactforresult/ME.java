package com.example.ch06startactforresult;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import static com.example.ch06startactforresult.ControlPanelActivity.*;

public class ME extends AppCompatActivity {
    //
    private int[] roles;
  //  private Cursor cursor;
  //  private DBHelper dbHelper;

    private ViewPager viewPager;
    private Fragment[] fs = new Fragment[4];
    private String[] titles ={"Page1","Page2"};
    private ActionBar actionBar;

    private ImageView iconView;
    private TextView textView;
    private TextView cashView;
    private TextView allMoneyView;

    private String account;
    private String playerName;

    private SystemVoice systemVoice;
    private BgmClass bgmClass;


    private Cursor cursor2;

    private int allMoney;
    private int stockQuantity;
    private float stockPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        bgmClass=new BgmClass(this);
        systemVoice=new SystemVoice(this);

     //   dbHelper = new DBHelper(this);
        account = preferences.getString("account","");
        playerName = preferences.getString("playerName","");

        roles = new int[]{R.drawable.h1, R.drawable.h2,R.drawable.h3,R.drawable.h4,R.drawable.h5
                ,R.drawable.h6,R.drawable.h7,R.drawable.h8,R.drawable.h9,R.drawable.h10};
        textView = findViewById(R.id.nameText);
        iconView = findViewById(R.id.headIcon);
        cashView = findViewById(R.id.myCash);
        allMoneyView = findViewById(R.id.allMoney);


        viewPager = findViewById(R.id.viewPager);
        fs[0] = new P0(); fs[1] = new P1(); fs[2] = new P2();
        fs[3] = new P3();

        viewPager.setAdapter(new MyPagerAdpter(getSupportFragmentManager()));
        //initActionBar();
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
        @Override
            public void onPageSelected(int position){
                super.onPageSelected(position);
            Log.v("brad","Page=" + position);

            if (position == 0){
                viewPager.setCurrentItem(1);
            }else if(position == 3){
                viewPager.setCurrentItem(2);
            }else{
        //        actionBar.setSelectedNavigationItem(position-1);
            }
        }
        });
        viewPager.setCurrentItem(1);

        setScreenData();


        cursor = null;
        cursor = dbHelper.queryTransactionRecord(account,playerName);
        Log.v("aaa",""+cursor.getCount());
        Log.v("aaa","aaa");
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            for(int i=0;i<cursor.getCount();i++){
                Log.v("aaa",cursor.getString(3)+","+cursor.getString(4)+","+cursor.getString(5)+
                        ","+cursor.getString(6)+","+cursor.getString(7)+","+cursor.getString(8));
                cursor.moveToNext();
            }
        }

    }

    private void initActionBar(){
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        MyTabListener myTabListener = new MyTabListener();
        actionBar.addTab(
                actionBar.newTab().setText("Page1")
                .   setTabListener(myTabListener));
        actionBar.addTab(
                actionBar.newTab().setText("Page2")
                    .setTabListener(myTabListener));
    }


    private class MyTabListener implements ActionBar.TabListener{


        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            viewPager.setCurrentItem(tab.getPosition()+1);
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    }

    public void gotoP1(View view) {

        viewPager.setCurrentItem(1);
    }

    public void gotoP2(View view) {

        viewPager.setCurrentItem(2);
    }

    private class MyPagerAdpter extends FragmentStatePagerAdapter {

        public MyPagerAdpter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fs[position];
        }

        @Override
        public int getCount() {
            return fs.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            if(position !=0  && position !=3){
                title = titles[position-1];
            }
            return title;
        }
    }


    private void setScreenData(){
        fetchStockQuantity();
        cursor = null;
        cursor = dbHelper.queryNonClearPlayerData(account,0);
        if(cursor!=null){
            cursor.moveToFirst();
            String target="";
            while (!cursor.isAfterLast()){
                target = cursor.getString(1);
                if(playerName.equals(target))
                {
                    String playerName = cursor.getString(1);
                    int iconID = cursor.getInt(2);
                    int cash = cursor.getInt(3);

                    textView.setText(playerName);
                    iconView.setBackgroundResource(roles[iconID]);
                    cashView.setText("目前現金 : "+cash);


                    allMoney = cash+Math.round(stockQuantity*stockPrice);
                    allMoneyView.setText("目前總資產 : "+allMoney);

                    return;
                }
                cursor.moveToNext();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        bgmClass.BGM_meStart();
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

    private void fetchStockQuantity(){

        cursor2 = null;
        cursor2 = dbHelper.queryTransactionRecord(account,playerName);
        Log.v("aaa","!!!"+cursor2.getCount());

        if(cursor2.getCount()>0){
            cursor2.moveToFirst();
            for(int i=0;i<cursor2.getCount();i++){

                stockQuantity = cursor2.getInt(5);
                stockPrice = cursor2.getFloat(6);

            }
        }
    }

}
