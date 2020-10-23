package com.example.buscandohogar.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.buscandohogar.classes.User;

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    private static final String USER_TABLE = "usuarios";
    private static final String ANIMAL_TABLE = "animal";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String LASTNAME_COLUMN = "lastname";
    private static final String PASS_COLUMN = "password";
    private static final String ADDRESS_COLUMN = "address";
    private static final String EMAIL_COLUMN = "email";
    private static final String PHONE_COLUMN = "phone";
    private static final String CITY_COLUMN = "city";
    private static final String TAG = "DBManager";

    public DBManager(Context context){
        helper = DBHelper.getInstance(context);
        db = helper.getWritableDatabase();
    }

    public User checkUserExist(String userEmail, String pass){
        User user = new User();

        String USER_SELECT = String.format("SELECT * FROM %s WHERE %s = '"+ userEmail +"' AND %s = '"+ pass +"'",
                USER_TABLE,
                EMAIL_COLUMN,
                PASS_COLUMN);

        Cursor cursor = db.rawQuery(USER_SELECT, null);
        try{
            if( cursor.moveToFirst()) {
                user.setId(cursor.getInt(cursor.getColumnIndex(ID_COLUMN)));
                user.setName(cursor.getString(cursor.getColumnIndex(NAME_COLUMN)));
                user.setLastname(cursor.getString(cursor.getColumnIndex(LASTNAME_COLUMN)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL_COLUMN)));
                user.setAddress(cursor.getString(cursor.getColumnIndex(ADDRESS_COLUMN)));
                user.setPhone(cursor.getInt(cursor.getColumnIndex(PHONE_COLUMN)));
                user.setCity(cursor.getString(cursor.getColumnIndex(CITY_COLUMN)));
            }
        } catch (Exception e){
            Log.d(TAG, "checkUserExist: "+ e.getMessage());
        } finally {
            if( cursor != null && !cursor.isClosed() ){
                cursor.close();
            }
        }
        return user;
    }


}
