package com.example.ch06startactforresult;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.adapter.WheelAdapter;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.skyfishjy.library.RippleBackground;

import java.util.Random;


public class CreareRoleActivity extends AppCompatActivity {

    //
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private WheelView wheelView;
    private ImageView myCha;

    private RippleBackground rippleBackground;

    private Drawable[] drawables;
    private int count;

    private EditText nicknameEdit;
    private TextInputLayout nicknameLayout;

    private String myAccount;
    private String myNickname;
    private int myRole;

    //
    private Button btnCreateRole;
    private DBHelper dbHelper;
    private Cursor cursor;
    private String[] charNames;

    private SystemVoice systemVoice;
    private BgmClass bgmClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creare_role);

        //
        dbHelper = new DBHelper(this);
        preferences = getSharedPreferences("localSetting",MODE_PRIVATE);

        bgmClass=new BgmClass(this);
        systemVoice=new SystemVoice(this);

        //
        dbHelper = new DBHelper(this);
        btnCreateRole = findViewById(R.id.btnCreateRole);
        btnCreateRole.setOnClickListener(createRoleListener);

        charNames = new String[]{"Peter","Tina","Scott","James","Lily",
                "John","Frank","Marsa","Criss","Henry"};



        //ripple api
        rippleBackground=(RippleBackground)findViewById(R.id.content2);
        //edit api
        nicknameEdit = (EditText) findViewById(R.id.et_nickname);
        nicknameLayout = (TextInputLayout) findViewById(R.id.nickname);
        nicknameLayout.setErrorEnabled(true);


        myCha = findViewById(R.id.myCha);
        wheelView = findViewById(R.id.wheelview);
        drawables = new TextDrawable[] { new TextDrawable("Peter"), new TextDrawable("Tina"), new TextDrawable("Scott"), new TextDrawable("James")
                , new TextDrawable("Lily"), new TextDrawable("John"), new TextDrawable("Frank"), new TextDrawable("Marsa")
                , new TextDrawable("Criss"), new TextDrawable("Henry")};



        //populate the adapter, that knows how to draw each item (as you would do with a ListAdapter)
        wheelView.setAdapter(new WheelAdapter() {
            @Override
            public Drawable getDrawable(int position) {
                return drawables[position];
            }

            @Override
            public int getCount() {
                return 10;
            }
        });


        count = 0;
        AnimationDrawable background = (AnimationDrawable)myCha.getBackground();
        // 開始播放
        background.start();

        rippleBackground.startRippleAnimation();


        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
            @Override
            public void onWheelItemSelected(WheelView parent, Drawable itemDrawable, int position) {
                //the adapter position that is closest to the selection angle and it's drawable

                count = position;




                switch (count){
                    case 0:

                        myCha.setBackgroundResource(R.drawable.c1_list);
                        AnimationDrawable background1 = (AnimationDrawable)myCha.getBackground();
                        // 開始播放
                        background1.start();
                        systemVoice.ButtonTouchVoice();
                        break;

                    case 1:
                        myCha.setBackgroundResource(R.drawable.c2_list);
                        AnimationDrawable background2 = (AnimationDrawable)myCha.getBackground();
                        // 開始播放
                        background2.start();
                        systemVoice.ButtonTouchVoice();
                        break;
                    case 2:
                        myCha.setBackgroundResource(R.drawable.c3_list);

                        AnimationDrawable background3 = (AnimationDrawable)myCha.getBackground();
                        // 開始播放
                        background3.start();
                        systemVoice.ButtonTouchVoice();
                        break;

                    case 3:
                        myCha.setBackgroundResource(R.drawable.c4_list);
                        AnimationDrawable background4 = (AnimationDrawable)myCha.getBackground();
                        // 開始播放
                        background4.start();
                        systemVoice.ButtonTouchVoice();
                        break;
                    case 4:
                        myCha.setBackgroundResource(R.drawable.c5_list);
                        AnimationDrawable background5 = (AnimationDrawable)myCha.getBackground();
                        // 開始播放
                        background5.start();
                        systemVoice.ButtonTouchVoice();
                        break;

                    case 5:
                        myCha.setBackgroundResource(R.drawable.c6_list);
                        AnimationDrawable background6 = (AnimationDrawable)myCha.getBackground();
                        // 開始播放
                        background6.start();
                        systemVoice.ButtonTouchVoice();
                        break;
                    case 6:
                        myCha.setBackgroundResource(R.drawable.c7_list);
                        AnimationDrawable background7 = (AnimationDrawable)myCha.getBackground();
                        // 開始播放
                        background7.start();
                        systemVoice.ButtonTouchVoice();
                        break;

                    case 7:
                        myCha.setBackgroundResource(R.drawable.c8_list);
                        AnimationDrawable background8 = (AnimationDrawable)myCha.getBackground();
                        // 開始播放
                        background8.start();
                        systemVoice.ButtonTouchVoice();
                        break;
                    case 8:
                        myCha.setBackgroundResource(R.drawable.c9_list);
                        AnimationDrawable background9 = (AnimationDrawable)myCha.getBackground();
                        // 開始播放
                        background9.start();
                        systemVoice.ButtonTouchVoice();
                        break;

                    case 9:
                        myCha.setBackgroundResource(R.drawable.c10_list);
                        AnimationDrawable background10 = (AnimationDrawable)myCha.getBackground();
                        // 開始播放
                        background10.start();
                        systemVoice.ButtonTouchVoice();
                        break;
                }




            }
        });

        ShimmerTextView shimmerTextView = findViewById(R.id.shimmer_tv);
        Shimmer shimmer = new Shimmer();
        shimmer.start(shimmerTextView);

        myAccount = getIntent().getStringExtra("account");


    }







    public void goup(View view) {
        count = count - 1;
        if (count == -1){
            count = 9;
        }
        wheelView.setSelected(count);
    }

    public void godown(View view) {
        count = count + 1;
        if (count == 10){
            count = 0;
        }

        wheelView.setSelected(count);

    }

    public void goDio(String s1){

        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme);
        dialog.setTitle(s1);
        dialog.setMessage("請重新輸入");
        dialog.setPositiveButton("確定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub


            }

        });
        dialog.setCancelable(false);
        dialog.show();
    }



    //
    private Boolean checkRepeat(String nickName){
        Boolean isRepeat = false;
        String account = getIntent().getStringExtra("account");
        cursor = null;
        cursor = dbHelper.queryAllPlayerData(account);
        if(cursor!=null){
            cursor.moveToFirst();
            String playerName;
            while (!cursor.isAfterLast()){
                playerName =cursor.getString(1);
                if(nickName.equals(playerName)){
                    isRepeat = true;
                }
                cursor.moveToNext();
            }
        }
        return isRepeat;
    }

    View.OnClickListener createRoleListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String nickname = nicknameEdit.getText().toString().trim();
            if(!nickname.equals("")){
                if(checkRepeat(nickname)){

                    goDio(getResources().getString(R.string.REPEAT_NAME));
                }
                else {
                    String account = preferences.getString("account","");
                    String PlayerName = nickname;
                    int iconId = count;
                    int PlayerCash=20000;
                    int PassingDays=0;
                    int StageClear=0;

                    Cursor cursor = dbHelper.queryAllStockData();
                    Random random = new Random();
                    int index = random.nextInt(100);
                    cursor.moveToPosition(index+100);
                    Log.v("aaa","account"+account);
                    Log.v("aaa","PlayerName"+PlayerName);

                    ContentValues values = new ContentValues();
                    values.put(DBHelper.COLUMN_ACCOUNT,account);
                    values.put(DBHelper.COLUMN_PLAYER,PlayerName);
                    values.put(DBHelper.COLUMN_ICON,iconId);
                    values.put(DBHelper.COLUMN_CASH,PlayerCash);
                    values.put(DBHelper.COLUMN_DAYS,PassingDays);
                    values.put(DBHelper.COLUMN_STOCK_ID,"QCOM");
                    //todo  COLUMN_STOCK_ID
                    values.put(DBHelper.COLUMN_STOCKS_LIST,"");
                    values.put(DBHelper.COLUMN_STAGE_CLEAR,StageClear);
                    values.put(DBHelper.COLUMN_CURRENT_DATE,cursor.getString(0));
                    values.put(DBHelper.COLUMN_IS_IN_LOOP,0);
                    values.put(DBHelper.COLUMN_TRANSACTION_COUNT,0);
                    dbHelper.addPlayer(values);
                    cursor.isClosed();

                    editor = preferences.edit();
                    //editor.putString("account", account);
                    editor.putString("playerName", PlayerName);
                    editor.commit();

                    Intent intent = new Intent(CreareRoleActivity.this,ControlPanelActivity.class);
                    intent.putExtra("playerName",nickname);
                    startActivity(intent);
                    finish();


/*
                    myNickname = nickname;
                    myRole = count;

                    editor = preferences.edit();
                    editor.putString("account",account);
                    editor.putString("playerName",PlayerName);
                    editor.putString("icon",String.valueOf(iconId));
                    editor.putString("playerCash",String.valueOf(PlayerCash));
                    editor.putString("passingDays",String.valueOf(PassingDays));
                    editor.putString("stageClear",String.valueOf(StageClear));
                    editor.commit();




                    intent.setClass(CreareRoleActivity.this, ControlPanelActivity.class);
                    startActivity(intent);
*/


                }

            }
            else{

                goDio(getResources().getString(R.string.plzEnterName));
            }
            systemVoice.ButtonTouchVoice();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        bgmClass.BGM_creare_roleStart();
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
