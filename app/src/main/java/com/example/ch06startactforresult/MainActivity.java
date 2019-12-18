package com.example.ch06startactforresult;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    //
    private Cursor cursor;
    private DBHelper dbHelper;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    public Button create;
    public Button login;

    private EditText accountEdit;
    private EditText passwordEdit;
    private TextInputLayout accoutLayout;
    private TextInputLayout passwordLayout;


    private FirebaseAuth mAuth;

    private String myAccount;
    private String myPassword;
    //帳號所擁有的腳色個數
    private int rolesCount;

    private KenBurnsView kbv;
    private SystemVoice systemVoice;
    private BgmClass bgmClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bgmClass=new BgmClass(this);
        systemVoice=new SystemVoice(this);
        //
        dbHelper = new DBHelper(this);
        preferences = getSharedPreferences("localSetting",MODE_PRIVATE);

        rolesCount = 0;


        create = findViewById(R.id.button);
        login = findViewById(R.id.button2);

        accountEdit = (EditText) findViewById(R.id.et_username);
        passwordEdit = (EditText) findViewById(R.id.et_password);
        accoutLayout = (TextInputLayout) findViewById(R.id.username);
        passwordLayout = (TextInputLayout) findViewById(R.id.password);
        passwordLayout.setErrorEnabled(true);
        accoutLayout.setErrorEnabled(true);


        myAccount = preferences.getString("account","");
        myPassword = preferences.getString("password","");
        accountEdit.setText(myAccount);
        passwordEdit.setText(myPassword);

        ((TextView)findViewById(R.id.block)).setAlpha(0.8f);

        // Initialize Firebase Auth

        mAuth = FirebaseAuth.getInstance();




            create.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, CreateActivity.class);
                    startActivity(intent);
                    systemVoice.ButtonTouchVoice();
                }
            });

            login.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check();
                    systemVoice.ButtonTouchVoice();
                }
            });


           kbv = (KenBurnsView) findViewById(R.id.image);
           kbv.resume();

    }



    void check(){


            String account = accountEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();

            if (account.length()>=6&&password.length()>=6){
                mAuth.signInWithEmailAndPassword(account, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Toast.makeText(MainActivity.this, "Authentication success!.",Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });
            } else {

                Toast.makeText(MainActivity.this, "帳號或密碼長度不足",
                        Toast.LENGTH_SHORT).show();
            }





    }

    //Change UI according to user data.
    public void  updateUI(FirebaseUser account){
        if(account != null){
            loginDio();
            myAccount = account.getEmail();
        }else {
            Toast.makeText(this,"登入失敗!", Toast.LENGTH_LONG).show();
        }
    }

    public void loginDio(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogTheme);
        dialog.setTitle("登入成功");
        dialog.setMessage("前往選擇角色");
        dialog.setNegativeButton("登出",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "已登出", Toast.LENGTH_SHORT).show();
            }

        });
        dialog.setPositiveButton("確定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                editor = preferences.edit();
                editor.putString("account", accountEdit.getText().toString().trim());
                editor.putString("playerName", passwordEdit.getText().toString().trim());
                editor.commit();
                pickRoleDio();
            }

        });
        dialog.setCancelable(false);
        dialog.show();
    }
    public void pickRoleDio(){
        cursor = null;
        cursor =dbHelper.queryNonClearPlayerData(myAccount,0);
        final String[] oldRoles;
        if(cursor!=null && cursor.getCount()>0) {
            oldRoles = new String[cursor.getCount()];
            String playerName;
            cursor.moveToFirst();
            for (int i = 0; i < oldRoles.length; i++) {
                playerName = cursor.getString(1);
                //Log.v("aaa", "playerName" + playerName);
                oldRoles[i] = playerName;
                cursor.moveToNext();
            }

            AlertDialog.Builder dialog_list = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogTheme);
            dialog_list.setTitle("請選擇角色:");
            dialog_list.setItems(oldRoles, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // TODO Auto-generated method stub
                    String select = oldRoles[which];
                    editor = preferences.edit();
                    editor.putString("account",accountEdit.getText().toString());
                    editor.putString("playerName",select);
                    editor.commit();

                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, ControlPanelActivity.class);
                    startActivity(intent);


                }


            });
            dialog_list.setCancelable(false);
            dialog_list.show();

        } else {
            noRoleDio();
        }
        //


    }

    private void noRoleDio(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogTheme);
        dialog.setTitle("尚未擁有角色");
        dialog.setMessage("前往創建角色");
        dialog.setNegativeButton("登出",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "已登出", Toast.LENGTH_SHORT).show();
            }

        });
        dialog.setPositiveButton("確定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("account",myAccount);
                intent.setClass(MainActivity.this, CreareRoleActivity.class);
                startActivity(intent);

            }

        });
        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        bgmClass.BGM_mainStart();
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

    @Override
    protected void onResume() {
        super.onResume();
        bgmClass.BGM_mainStart();
    }

}
