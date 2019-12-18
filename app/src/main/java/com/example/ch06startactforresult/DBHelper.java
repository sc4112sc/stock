package com.example.ch06startactforresult;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StockGame.db";

    private static final int DATABASE_VERSION = 2019120101;
    public static final String DATA_TABLE_PLAYER = "Player_Data";
    public static final String DATA_TABLE_STOCK = "Stock_Data";
    public static final String DATA_TABLE_RECORD = "Record_Data";

    //TABLE Player_Data
    public static final String COLUMN_ACCOUNT ="account";   //0
    public static final String COLUMN_PLAYER ="playerName"; //1
    public static final String COLUMN_ICON ="icon"; //2
    public static final String COLUMN_CASH ="playerCash";   //3
    public static final String COLUMN_DAYS ="passingDays";  //4
    public static final String COLUMN_STOCK_ID ="stockID";  //5
    public static final String COLUMN_STOCKS_LIST ="stocksList";    //6
    public static final String COLUMN_STAGE_CLEAR ="stageClear";    //7
    public static final String COLUMN_CURRENT_DATE = "currentDate";    //8
    public static final String COLUMN_IS_IN_LOOP = "isInLoop"; //9
    public static final String COLUMN_TRANSACTION_COUNT = "transactionCount"; //10

    //TABLE Stock_Data
    public static final String COLUMN_DATE = "date";//0
    public static final String COLUMN_OPEN = "open";//1
    public static final String COLUMN_HIGH = "high";//2
    public static final String COLUMN_LOW = "low";//3
    public static final String COLUMN_CLOSE = "close";//4
    //5 stockID

    //TABLE Record_Data
    public static final String COLUMN_RECORD_ID = "_id"; //0
    //1:account
    //2:playerName
    public static final String COLUMN_RECORD_TYPE = "type"; //3
    public static final String COLUMN_RECORD_DATE = "date";    //4
    public static final String COLUMN_RECORD_STOCKQ = "stockQ";    //5 StockQuantity
    public static final String COLUMN_RECORD_STOCKP = "stockP";    //6 StockPrice
    public static final String COLUMN_RECORD_STOCKID = "stockID";    //7 StockID
    public static final String COLUMN_RECORD_LOOP = "transactionCount"; //8 transactionLoop


    private String sql;
    private static SQLiteDatabase db;
    private Cursor cursor;
    private Context context;
/*
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS Player_Data (account varchar(50) NOT NULL,PlayerName varchar(20) PRIMARY KEY NOT NULL,CharName varchar(20),PlayerCash int,PassingDays int,StockAmount int,StockName varchar(20),PosessStocks TEXT,StageClear bit)");
        loadingTestData(db);
        //currentData varchar(20)
    }
*/
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context =context;
        db = getWritableDatabase();
        //resetDatabase();
        mCreateDataTable(db);
        //loadingTestData(db);
        loadingStockTable(db);
    }
    public void resetDatabase(){

         db.execSQL("Drop TABLE IF EXISTS " + DATA_TABLE_PLAYER);
         db.execSQL("Drop TABLE IF EXISTS " + DATA_TABLE_STOCK);
         db.execSQL("Drop TABLE IF EXISTS " + DATA_TABLE_RECORD);
    }

    public Cursor queryTransactionRecord(String account,String playerName){
        cursor = null;

        cursor = db.rawQuery("SELECT * FROM "+DATA_TABLE_RECORD+" WHERE "+COLUMN_ACCOUNT+"=? and "+COLUMN_PLAYER+"=?",new String[]{account,playerName});

        return cursor;
    }

    private void mCreateDataTable(SQLiteDatabase db){
        db.beginTransaction();
        try{


            //db.execSQL("CREATE TABLE IF NOT EXISTS Player_Data (account varchar(50) NOT NULL,PlayerName varchar(20) PRIMARY KEY NOT NULL,CharName varchar(20),PlayerCash int,PassingDays int,StockAmount int,StockName varchar(20),PosessStocks TEXT,StageClear bit)");
            db.execSQL("CREATE TABLE IF NOT EXISTS "+DATA_TABLE_PLAYER+" ("+COLUMN_ACCOUNT+" varchar(50) NOT NULL,"+
                    COLUMN_PLAYER+" varchar(20) PRIMARY KEY NOT NULL,"+COLUMN_ICON+" int,"+COLUMN_CASH+" int,"+COLUMN_DAYS+" int,"+
                    COLUMN_STOCK_ID+" varchar(20),"+COLUMN_STOCKS_LIST+" TEXT,"+COLUMN_STAGE_CLEAR+" bit,"+COLUMN_CURRENT_DATE+" varchar(20),"+
                    COLUMN_IS_IN_LOOP+" bit,"+ COLUMN_TRANSACTION_COUNT +" int);");
            db.execSQL("CREATE TABLE IF NOT EXISTS "+DATA_TABLE_STOCK+" ("+COLUMN_DATE+" varchar(20) PRIMARY KEY NOT NULL,"+COLUMN_OPEN+" float,"+COLUMN_HIGH+" float,"+
                    COLUMN_LOW+" float,"+COLUMN_CLOSE+" float,"+COLUMN_STOCK_ID+" varchar(20));");

            db.execSQL("CREATE TABLE IF NOT EXISTS "+DATA_TABLE_RECORD +" ("+COLUMN_RECORD_ID+" INTEGER PRIMARY KEY,"+COLUMN_ACCOUNT+" varchar(50) ,"+COLUMN_PLAYER+" varchar(20) ,"+
                    COLUMN_RECORD_TYPE+" varchar(20),"+COLUMN_RECORD_DATE+" varchar(20),"+ COLUMN_RECORD_STOCKQ+" int,"+COLUMN_RECORD_STOCKP+" float,"+COLUMN_RECORD_STOCKID+" varchar(20),"+
                    COLUMN_RECORD_LOOP +" int);");

            db.setTransactionSuccessful();
        }
        catch (Exception e){e.printStackTrace();}
        finally {
            db.endTransaction();
        }

    }



    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL("CREATE TABLE IF NOT EXISTS Player_Data (account varchar(50) NOT NULL,PlayerName varchar(20) PRIMARY KEY NOT NULL,CharName varchar(20),PlayerCash int,PassingDays int,StockAmount int,StockName varchar(20),PosessStocks TEXT,StageClear bit)");
        //loadingTestData(db);
/*
        db.execSQL("CREATE TABLE IF NOT EXISTS "+
                " Player_Data (account varchar(50) NOT NULL,"+
                "PlayerName varchar(20) PRIMARY KEY NOT NULL,"+
                "CharName varchar(20),PlayerCash int,PassingDays int,"
                +"StockAmount int,StockName varchar(20),PosessStocks TEXT,StageClear bit)");*/

        // sql = "CREATE TABLE IF NOT EXISTS Player_Data (account varchar(50) NOT NULL,PlayerName varchar(20) PRIMARY KEY NOT NULL,CharName varchar(20),PlayerCash int,PassingDays int,StockAmount int,StockName varchar(20),PosessStocks TEXT,StageClear bit)";
   //     sql="DROP TABLE IF EXISTS " +DATA_TABLE_PLAYER+";";
   //     db.execSQL(sql);
       // sql="CREATE TABLE IF NOT EXISTS Player_Data (account varchar(50) NOT NULL,PlayerName varchar(20) PRIMARY KEY NOT NULL,CharName varchar(20),PlayerCash int,PassingDays int,StockAmount int,StockName varchar(20),PosessStocks TEXT,StageClear bit)";
     //   db.execSQL("CREATE TABLE IF NOT EXISTS Player_Data (account varchar(50) NOT NULL,PlayerName varchar(20) PRIMARY KEY NOT NULL,CharName varchar(20),PlayerCash int,PassingDays int,StockAmount int,StockName varchar(20),PosessStocks TEXT,StageClear bit)");
        //UserName      雲端資料庫帳號登入check,先預設為testPlayer
        //PlayerName    提供一個edit text view 給玩家修改
        //CharName      角色頭象名稱,預計有X個頭像給玩家選擇
        //PlayerCash    一開始設定為200000
        //PassingDays  一開始設定為0
        //StockAmount  手中多少股票,先不設定,系統會自動設為null
        //StockName  手中股票的名稱,進入遊戲後決定,先不設定,系統會自動設為null
    }
    public void loadingStockTable(SQLiteDatabase db){
      //  db = getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM "+DATA_TABLE_STOCK,null);
      //  Log.v("aaa","dbhelper cursor"+cursor.getCount());
        if (cursor.getCount()<1){
            String stockID = "QCOM";
            db.beginTransaction();
            try{
                InputStreamReader is = new InputStreamReader(context.getAssets().open("dataset.csv"));
                BufferedReader br = new BufferedReader(is);
                br.readLine();
                String line,date,open,high,low,close;
                String[] rawList;
                String temp ="INSERT INTO "+DATA_TABLE_STOCK+" ("+COLUMN_DATE+","+COLUMN_OPEN+","+COLUMN_HIGH+","+COLUMN_LOW+","+COLUMN_CLOSE+","+COLUMN_STOCK_ID+
                        ") values(";
                while ((line=br.readLine()) != null){
                    rawList = line.split(",");
                    date = "'"+rawList[0]+"'";
                    open = rawList[1];
                    high = rawList[2];
                    low = rawList[3];
                    close = rawList[4];
                    sql = temp+date+","+open+","+high+","+low+","+close+",'"+stockID+"');";
                    db.execSQL(sql);
                    Log.v("aaa",sql);
                }
                db.setTransactionSuccessful();
            }
            catch (Exception e){e.printStackTrace();}
            finally {
                db.endTransaction();
            }
        }
    }

    private void loadingTestData(SQLiteDatabase db){
        cursor = null;
        cursor = db.rawQuery("SELECT * FROM "+DATA_TABLE_PLAYER,null);
        if(cursor==null || cursor.getCount()<1)
        {
            db.beginTransaction();
            try{
                sql = "INSERT INTO "+DATA_TABLE_PLAYER+" ("+COLUMN_ACCOUNT+","+COLUMN_PLAYER+","+COLUMN_ICON+","+
                        COLUMN_CASH+","+COLUMN_DAYS+","+COLUMN_STOCK_ID+","+COLUMN_STOCKS_LIST+","+COLUMN_STAGE_CLEAR+","+
                        COLUMN_CURRENT_DATE+","+COLUMN_IS_IN_LOOP+","+COLUMN_TRANSACTION_COUNT+
                        ") values('111111@gmail.com','John',2,300000,0,'QCOM','',0,'2019/05/01',0,0)";
                db.execSQL(sql);
/*                sql = "INSERT INTO "+DATA_TABLE_PLAYER+" ("+COLUMN_ACCOUNT+","+COLUMN_PLAYER+","+COLUMN_ICON+","+
                        COLUMN_CASH+","+COLUMN_DAYS+","+COLUMN_STOCK_ID+","+COLUMN_STAGE_CLEAR+","+COLUMN_STOCKS_LIST+","+COLUMN_CURRENT_DATE+
                        ") values('111111@gmail.com','Henry',1,300000,10,'QCOM',0,'2019/10/01:300,24.53;2019/11/01:200,26.30;2019/12/01:100,28.50','2019/05/01')";
                db.execSQL(sql);
                sql = "INSERT INTO "+DATA_TABLE_PLAYER+" ("+COLUMN_ACCOUNT+","+COLUMN_PLAYER+","+COLUMN_ICON+","+
                        COLUMN_CASH+","+COLUMN_DAYS+","+COLUMN_STOCK_ID+","+COLUMN_STAGE_CLEAR+","+COLUMN_STOCKS_LIST+","+COLUMN_CURRENT_DATE+
                        ") values('111111@gmail.com','Tina',2,300000,0,'QCOM',0,'2019/10/01:300,24.53;2019/11/01:200,26.30;2019/12/01:100,28.50','2019/05/01')";
                db.execSQL(sql);*/
                db.setTransactionSuccessful();
            }
            catch (Exception e){e.printStackTrace();}
            finally {
                db.endTransaction();
            }

        }
    }
    public void updateClearStage(String account,String playerName){
        ContentValues values = new ContentValues();
        values.put(COLUMN_STAGE_CLEAR,1);
        String whereClause= DBHelper.COLUMN_ACCOUNT+"= ? AND "+ DBHelper.COLUMN_PLAYER+"= ? ";
        String[] whereArgs=new String[]{account,playerName};
        db.update(DBHelper.DATA_TABLE_PLAYER,values,whereClause,whereArgs);
    }

    public void updateDays(String account,String playerName,String currentDate,int passingDays){
        ContentValues values = new ContentValues();
        values.put(COLUMN_DAYS,passingDays);
        values.put(COLUMN_CURRENT_DATE,currentDate);
        String whereClause= DBHelper.COLUMN_ACCOUNT+"= ? AND "+ DBHelper.COLUMN_PLAYER+"= ? ";
        String[] whereArgs=new String[]{account,playerName};
        db.update(DBHelper.DATA_TABLE_PLAYER,values,whereClause,whereArgs);
    }

    public void updateBuyOutCome(String account,String playerName,String stockList,int totalCash,int transactionCount,boolean isInLoop){
        ContentValues values = new ContentValues();
        values.put(COLUMN_CASH,totalCash);
        values.put(COLUMN_STOCKS_LIST,stockList);
        values.put(COLUMN_TRANSACTION_COUNT,transactionCount);
        values.put(COLUMN_IS_IN_LOOP,isInLoop);
        String whereClause= DBHelper.COLUMN_ACCOUNT+"= ? AND "+ DBHelper.COLUMN_PLAYER+"= ? ";
        String[] whereArgs=new String[]{account,playerName};
        db.update(DBHelper.DATA_TABLE_PLAYER,values,whereClause,whereArgs);
    }

    public void updateSellOutcome(String account,String playerName,String stocksList,int cashTotal){
        ContentValues values = new ContentValues();
        values.put(COLUMN_STOCKS_LIST,stocksList);
        values.put(COLUMN_CASH,cashTotal);
        //String whereClause=DBHelper.COLUMN_ACCOUNT+"= '"+account+"' AND "+DBHelper.COLUMN_PLAYER+"= '"+playerName+"'";
       // db.update(DBHelper.DATA_TABLE_PLAYER,values,whereClause,null);

        String whereClause= DBHelper.COLUMN_ACCOUNT+"= ? AND "+ DBHelper.COLUMN_PLAYER+"= ?";
        String[] whereArgs=new String[]{account,playerName};
        db.update(DBHelper.DATA_TABLE_PLAYER,values,whereClause,whereArgs);
      //  db.execSQL("update Player_Data set PosessStocks=?,PlayerCash=60000 where account=? and playerName=?;",new String[]{stocksList,account,playerName});
       // db.execSQL("update Player_Data set PosessStocks='ccc',PlayerCash=? where account='111111@gmail.com' and playerName='Tina';",new Object[]{cashTotal});
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public Cursor queryPlayerData(String account,String playerName)
    {
        cursor=null;
        cursor = db.rawQuery("SELECT * FROM "+DATA_TABLE_PLAYER+" WHERE "+COLUMN_ACCOUNT+"=? and "+COLUMN_PLAYER+"=?",new String[]{account,playerName});

        return cursor;
    }

    public Cursor queryNonClearPlayerData(String account,int stageClear)
    {
        cursor=null;
        cursor = db.rawQuery("SELECT * FROM "+DATA_TABLE_PLAYER,null);
        if(cursor.getCount()>0) {
            cursor = db.rawQuery("SELECT * FROM " + DATA_TABLE_PLAYER + " WHERE " + COLUMN_ACCOUNT + "=? and " + COLUMN_STAGE_CLEAR + "=?", new String[]{account, Integer.toString(stageClear)});
        }
        else { cursor = null;}
        return cursor;
    }
    public Cursor queryAllPlayerData(String account)
    {
        cursor=null;
        cursor = db.rawQuery("SELECT * FROM "+DATA_TABLE_PLAYER,null);
        if(cursor.getCount()>0) {
            cursor = db.rawQuery("SELECT * FROM " + DATA_TABLE_PLAYER + " WHERE " + COLUMN_ACCOUNT + "=?", new String[]{account});
        }
        else{
            cursor=null;
        }
        return cursor;
    }
    public Cursor queryAllStockData()
    {
        cursor=null;
        cursor = db.rawQuery("SELECT * FROM "+DATA_TABLE_STOCK,null);
        return cursor;
    }
    public Cursor queryTodayStockDetail(String today){
        cursor = null;
        cursor = db.rawQuery("SELECT * FROM "+DATA_TABLE_STOCK+" WHERE "+COLUMN_DATE+"=?",new String[]{today});
        return cursor;
    }


    public Cursor queryNextDate(String today){
        cursor = null;
        cursor = db.rawQuery("SELECT * FROM "+DATA_TABLE_STOCK,null);
        String target="";
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            if(cursor.getString(0).equals(today)){
                cursor.moveToNext();
                target = cursor.getString(0);
                break;
            }
            cursor.moveToNext();
        }
        cursor = null;
        if(!target.equals(""))
            cursor = queryTodayStockDetail(target);
        return cursor;
    }

    public void insertStockRecord(ContentValues values){
        db.insert(DATA_TABLE_RECORD,null,values);
    }

    public void addPlayer(ContentValues values){

        db.insert(DATA_TABLE_PLAYER,null,values);
       // db.insert()

      //  sql = "INSERT INTO "+DATA_TABLE_PLAYER+" ("+COLUMN_ACCOUNT+","+COLUMN_PLAYER+","+COLUMN_ICON+","+
   //             COLUMN_CASH+","+COLUMN_DAYS+","+COLUMN_STOCK_ID+","+COLUMN_STAGE_CLEAR+","+COLUMN_STOCKS_LIST+","+COLUMN_CURRENT_DATE+
   //             ") values('111111@gmail.com','aade',0,300000,60,'QCDM',1,'2019/10/01:300,24.53;2019/11/01:200,26.30;2019/12/01:100,28.50','2019/05/01')";
   //     db.execSQL(sql);
    }


    /*

    Cursor用法
    userName = "testPlayer";
    DBHelper dbHelper = new DBHelper(this);
    Cursor cursor=null;
    cursor = dbHelper.queryPlayerData(userName);
    if(cursor!=null){
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String account = cursor.getString(0);
            String PlayerName = cursor.getString(1);
            String CharName = cursor.getString(2);
            int PlayerCash = cursor.getInt(3);
            int PassingDays= cursor.getInt(4);
            String stocksList = cursor.getString(6);

            cursor.moveToNext();
        }

    }
    cursor.close();


     */


}
