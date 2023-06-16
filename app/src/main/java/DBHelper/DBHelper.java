package DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
public class DBHelper extends SQLiteOpenHelper
{
    public DBHelper(@Nullable Context context)
    {
        super(context, "ContactBook", null, 1);
        Log.d("TTT", "DBHelper: Database Created");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String query="create table Contact (id integer primary key autoincrement,name text,number text)";
        sqLiteDatabase.execSQL(query);
        Log.d("TTT", "DBHelper: Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void saveContact(String name, String number)
    {
        String query="insert into Contact(name,number) values('"+name+"','"+number+"')";
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        sqLiteDatabase.execSQL(query);
    }
    public void updateContact(int id, String name, String number)
    {
        String query = "update Contact set name = '"+name+"',number = '"+number+"' where id = '"+id+"'";
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        sqLiteDatabase.execSQL(query);

    }
    public void deleteContact(int id)
    {
        String query = "delete from Contact where id = '"+id+"'";
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(query);
    }
    public Cursor displayContact()
    {
        String query="select * from Contact";
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }
}