package model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
public class Database extends SQLiteOpenHelper
{
    public Database(@Nullable Context context)
    {
        super(context, "PhoneBook", null, 1);
        Log.d("TTT", "DBHelper: Database Created");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String query="create table Contactbook (id integer primary key autoincrement,name text,number text,imagePath)";
        sqLiteDatabase.execSQL(query);
        Log.d("TTT", "DBHelper: Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void saveContact(String name, String number, String imagePath)
    {
        String query="insert into Contactbook(name,number,imagePath) values('"+name+"','"+number+"','"+imagePath+"')";
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        sqLiteDatabase.execSQL(query);
    }
    public void updateContact(int id, String name, String number, String imagePath)
    {
        String query = "update Contactbook set imagePath = '"+imagePath+"', name = '"+name+"',number = '"+number+"' where id = '"+id+"'";
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        sqLiteDatabase.execSQL(query);

    }
    public void deleteContact(int id)
    {
        String query = "delete from Contactbook where id = '"+id+"'";
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(query);
    }
    public Cursor displayContact()
    {
        String query="select * from Contactbook";
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }
}