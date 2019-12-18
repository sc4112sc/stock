package com.example.ch06startactforresult;


import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Path;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



public class ControlPanelActivity extends AppCompatActivity {
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    private String account,playerName,playerChar;
    public static int playerCash;
    public static String stocksList,currentDate;
    public static float currentSellPrice;


    private boolean isMute,isShowChar,isShowBackGround;
    public static DBHelper dbHelper;
    //  private AlertDialog.Builder builder;
    private static final int REQUEST_CODE_LOGIN = 1;
    private static final int REQUEST_CODE_CREATE_ROLE = 2;
    public static Cursor cursor;

    private TextView tvDays;
    private Button btnLogIn,btnGameStart,btnCreateRole;
    private TextView tvPlayerName;
    private ImageView imgIcon;

    private LinearLayout myRoom;
    private ImageView roomRole;
    private String tempName;
    private int tempRole;
    private int[] roles;
    private int[] animRoles;
    private int passingDays;


    private SystemVoice systemVoice;
    private BgmClass bgmClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

        bgmClass=new BgmClass(this);
        systemVoice=new SystemVoice(this);

        dbHelper = new DBHelper(this);

        preferences = getSharedPreferences("localSetting",MODE_PRIVATE);

        roomRole = findViewById(R.id.roomRole);
        myRoom = findViewById(R.id.myRoom);

        tvDays = findViewById(R.id.tvDays);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnGameStart = findViewById(R.id.btnGameStart);
        btnGameStart.setOnClickListener(gameStartListener);
        btnCreateRole = findViewById(R.id.btnCreateRole);
        btnCreateRole.setOnClickListener(createRoleListener);
        tvPlayerName= findViewById(R.id.tvPlayerName);
        imgIcon = findViewById(R.id.imgIcon);
        imgIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLogIn()){
                    selectPlayer();}
                else{
                    Toast.makeText(ControlPanelActivity.this, getResources().getString(R.string.plzLogIn),Toast.LENGTH_SHORT).show();
                }
            }
        });
/*
        //站時先設定登入中
        editor = preferences.edit();
        editor.putString("account", "111111@gmail.com");
        editor.putString("playerName", "John");
        editor.commit();*/
        account = preferences.getString("account","");
        playerName = preferences.getString("playerName","");




        roles = new int[]{R.drawable.h1, R.drawable.h2,R.drawable.h3,R.drawable.h4,R.drawable.h5
                ,R.drawable.h6,R.drawable.h7,R.drawable.h8,R.drawable.h9,R.drawable.h10};
        animRoles = new int[]{R.drawable.c1_list, R.drawable.c2_list,R.drawable.c3_list,R.drawable.c4_list,R.drawable.c5_list
                ,R.drawable.c6_list,R.drawable.c7_list,R.drawable.c8_list,R.drawable.c9_list,R.drawable.c10_list};

    //    tempName = getIntent().getStringExtra("name");
     //   tempRole = getIntent().getIntExtra("role",0);

       // Log.v("scott",tempName+""+tempRole);


    }





    private boolean isLogIn(){
        if (!account.equals("")){
            return true;   }
        return false;
    }

    private void loadingLocalUserData(){
        if(isLogIn()){
            roleAnim();
            btnLogIn.setText("Log Out");
            btnLogIn.setOnClickListener(logOutListener);
            playerName = preferences.getString("playerName","");
   //         if(playerName.equals("")){
     //           tvDays.setText("DAY ?");
       //     }
         //   else{
                setScreenData();
           // }
            //tvDays.setText("DAY 0");
          //  tvPlayerName.setText(tempName);
          //  imgIcon.setImageResource(roles[tempRole]);
        }/*
        else{
            btnLogIn.setText("Log In");
            btnLogIn.setOnClickListener(logInListener);
            tvDays.setText("DAY ?");

            tvPlayerName.setText("未登入");
            imgIcon.setImageResource(R.drawable.point);

        }*/

    }

    private void setScreenData(){
        cursor=null;
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
                    passingDays= cursor.getInt(4);
                    int cash = cursor.getInt(3);
                    tvDays.setText("DAY "+passingDays);
                    tvPlayerName.setText(playerName);
                    imgIcon.setImageResource(roles[iconID]);
                    roomRole.setBackgroundResource(animRoles[iconID]);
                    playerCash = cash;
                    changeRoom();
                    roleAnim();
                    stocksList = cursor.getString(6);
                    currentDate = cursor.getString(8);
                    currentSellPrice = 0.0f;
                    cursor = null;
                    cursor = dbHelper.queryTodayStockDetail(currentDate);
                    if(cursor.getCount()>0){
                        cursor.moveToFirst();
                        currentSellPrice=cursor.getFloat(4);
                    }
                    return;
                }
                cursor.moveToNext();
            }
        }

    }





    OnClickListener logInListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ControlPanelActivity.this,MainActivity.class);
            startActivityForResult(intent,REQUEST_CODE_LOGIN);
            systemVoice.ButtonTouchVoice();
        }
    };

    OnClickListener logOutListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(ControlPanelActivity.this,MainActivity.class);
            startActivity(intent);
            systemVoice.ButtonTouchVoice();

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
/*
        if(requestCode==REQUEST_CODE_LOGIN){
            switch (resultCode){
                case RESULT_OK:
                    account = data.getExtras().getString("account");
                    editor = preferences.edit();
                    editor.putString("account",account);
                    editor.commit();
                    loadingLocalUserData();
                    break;
                default:
                    break;
            }
        }*/
        if(requestCode==REQUEST_CODE_CREATE_ROLE){
            if(resultCode==RESULT_OK){
                Toast.makeText(ControlPanelActivity.this,
                        getResources().getString(R.string.NEW_PLAYER) +" "+
                                data.getExtras().getString("playerName") ,Toast.LENGTH_SHORT).show();
                editor = preferences.edit();
                editor.putString("playerName",data.getExtras().getString("playerName"));
                editor.commit();
                loadingLocalUserData();
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        loadingLocalUserData();
        bgmClass.BGM_controlpanelStart();
    }


    private void gotoStockGame(){
        Intent intent = new Intent(ControlPanelActivity.this,StockGameActivity.class);
        intent.putExtra("account",account);
        intent.putExtra("playerName",playerName);
        startActivity(intent);
        systemVoice.ButtonTouchVoice();
    }

    OnClickListener gameStartListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            playerName = preferences.getString("playerName","");
            if(isLogIn()){
                if(!playerName.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ControlPanelActivity.this, R.style.MyAlertDialogTheme)
                            .setTitle(getResources().getString(R.string.GAME_START))
                            .setMessage(playerName+" "+"DAY " +passingDays)
                            .setPositiveButton(getResources().getString(R.string.POSITIVE), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    gotoStockGame();
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.NEGATIVE), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setCancelable(false);
                    builder.show();

                }
                else{
                    Toast.makeText(
                            ControlPanelActivity.this, getResources().getString(R.string.plzChange),Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(
                        ControlPanelActivity.this, getResources().getString(R.string.plzLogIn),Toast.LENGTH_SHORT).show();
            }
            systemVoice.ButtonTouchVoice();
        }
    };

    OnClickListener createRoleListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isLogIn()){
                Intent intent = new Intent(ControlPanelActivity.this,CreareRoleActivity.class);
                intent.putExtra("account",account);
               // startActivityForResult(intent,REQUEST_CODE_CREATE_ROLE);
                startActivity(intent);
            }
            else {
                Toast.makeText(ControlPanelActivity.this,
                        getResources().getString(R.string.plzLogIn),Toast.LENGTH_SHORT).show();
            }
            systemVoice.ButtonTouchVoice();
        }
    };


    private void selectPlayer(){
        cursor = null;
        cursor =dbHelper.queryNonClearPlayerData(account,0);
        final String[] oldRoles;
        if(cursor.getCount()>0){
            oldRoles = new String[cursor.getCount()];
            String playerName;
            cursor.moveToFirst();
            for(int i=0;i<oldRoles.length;i++){
                playerName = cursor.getString(1);
                Log.v("aaa","playerName"+playerName);
                oldRoles[i] = playerName;
                cursor.moveToNext();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(ControlPanelActivity.this, R.style.MyAlertDialogTheme)
                    .setTitle("請選擇角色:")
                    .setItems(oldRoles, new DialogInterface.OnClickListener(){
                        @Override

                        //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
                        public void onClick(DialogInterface dialog, int which) {
                            // Toast.makeText(ControlPanelActivity.this, "你選的是" + oldRoles[which], Toast.LENGTH_SHORT).show();
                            String select = oldRoles[which];
                            editor = preferences.edit();
                            editor.putString("playerName",select);
                            editor.commit();
                            loadingLocalUserData();
                        }
                    });
            if(oldRoles.length>0)
                builder.show();
        }
        else{
            Toast.makeText(ControlPanelActivity.this,
                    getResources().getString(R.string.plzCreate),Toast.LENGTH_SHORT).show();
        }
    }


    public void gotohistroy(View view) {
        Intent intent = new Intent(this, HistroyActivity.class);

        startActivity(intent);
        systemVoice.ButtonTouchVoice();
    }

    public void gotoOrder(View view) {
//        Intent intent = new Intent(this,OrderActivity.class);
//        startActivity(intent);
    }


    public void changeRoom(){

        Log.v("aaa",playerCash+"!!!!!!!!!!!!!!!!!!!!!!!!!");
        if(playerCash<=18000){
            myRoom.setBackground(ContextCompat.getDrawable(this, R.drawable.background01));
        }else if(playerCash<=20000){
            myRoom.setBackground(ContextCompat.getDrawable(this, R.drawable.r04));
        }else {
            myRoom.setBackground(ContextCompat.getDrawable(this, R.drawable.r01));
        }
    }

    private void roleAnim(){
        DisplayMetrics dm = new DisplayMetrics();
        //取得視窗屬性
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //視窗的寬度
        int screenWidth = dm.widthPixels;

        //視窗高度
        int screenHeight = dm.heightPixels;

        Path path = new Path();
        path.moveTo(0,0);

        path.lineTo((screenWidth-300)/4,(-screenHeight+1200));
        path.lineTo((screenWidth-300)/3,(-screenHeight+1150));
        path.lineTo((screenWidth-300)/2,(-screenHeight+1100));
        path.lineTo((screenWidth-300)/1,(-screenHeight+1150));
        path.lineTo((screenWidth-300)/2,(-screenHeight+1200));
        path.lineTo((screenWidth-300)/3,(-screenHeight+1150));
        path.lineTo((screenWidth-300)/4,(-screenHeight+1200));
        path.lineTo(0,0);


        ObjectAnimator animation = ObjectAnimator.ofFloat(roomRole,"translationX","translationY",path);
        animation.setRepeatCount(-1);
        animation.setDuration(20000);
        animation.start();


        AnimationDrawable background = (AnimationDrawable)roomRole.getBackground();
        // 開始播放
        background.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bgmClass.BGM_controlpanelStart();
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


    public void gotoME(View view) {
        Intent intent = new Intent(this, ME.class);
        intent.putExtra("name",playerName);
        intent.putExtra("account",account);
        startActivity(intent);
        systemVoice.ButtonTouchVoice();
    }
}
