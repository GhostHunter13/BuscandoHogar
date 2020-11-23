package com.example.buscandohogar.model.entity.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    private static DBHelper instance = null;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BuscandoHogar.db";
    public static final String USERS_TABLE = "usuarios";
    public static final String ANIMAL_TABLE = "animal";

    public static DBHelper getInstance(Context context){
        if( instance == null ){
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_ANIMAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_USERS);
        db.execSQL(SQL_DELETE_ANIMAL);
        onCreate(db);
    }

    public static final String SQL_CREATE_USER_TABLE = "CREATE TABLE "+USERS_TABLE+"(id INTEGER PRIMARY KEY," +
            " name TEXT," +
            " lastname TEXT," +
            " password TEXT," +
            " email TEXT," +
            " address TEXT," +
            " phone INTEGER," +
            " city TEXT)";

    public static final String SQL_CREATE_ANIMAL_TABLE = "CREATE TABLE "+ANIMAL_TABLE+"(id INTEGER PRIMARY KEY," +
            " name TEXT," +
            " age INTEGER," +
            " breed INTEGER," +
            " description TEXT)";


    public static final String SQL_DELETE_USERS = "DROP TABLE IF EXISTS "+ USERS_TABLE;
    public static final String SQL_DELETE_ANIMAL = "DROP TABLE IF EXISTS "+ ANIMAL_TABLE;
}
