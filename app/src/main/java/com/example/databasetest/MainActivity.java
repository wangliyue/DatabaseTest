package com.example.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MyDatabaseHelper myDatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDatabaseHelper = new MyDatabaseHelper(this,"BookStore.db",null,3);
        Button createBtn = (Button)findViewById(R.id.create_database);
        createBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myDatabaseHelper.getReadableDatabase();
            }
        });

        Button addBtn = (Button)findViewById(R.id.add_data);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                ContentValues value = new ContentValues();
                value.put("author","wangliyue");
                value.put("price",15.35);
                value.put("pages",600);
                value.put("name","Java Book");
                db.insert("Book",null,value);
                value.clear();
//                value.put("category_name","IT Book");
//                value.put("category_code",1);
//                db.insert("Category",null,value);

                //sql插入
               // db.execSQL("insert into Book (author,price,pages,name) values (?,?,?,?)",new String[]{"wangliyue","15.35","600","Java Book"});
            }
        });

        Button updateBtn = (Button)findViewById(R.id.update_data);
        updateBtn.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                ContentValues value = new ContentValues();
                value.put("price",10.00);
                db.update("Book",value,"name=?",new String[]{"Android Book"});

                //sql更新
                //db.execSQL("update Book set price = ? where name = ?",new String[]{"10.00","Android Book"});
            }
        });

        Button deleteBtn = (Button)findViewById(R.id.delete_data);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                db.delete("Book","pages > ?",new String[]{"500"});

                //sql删除
                //db.execSQL("delete from Book where pages > ?",new String[]{"500"});
            }
        });
        
        Button queryBtn = (Button)findViewById(R.id.query_data);
        queryBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                Cursor cursor = db.query("Book",null,null,null,null,null,null);
                if(cursor.moveToFirst()){
                    do{
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d(TAG, "name: "+name);
                        Log.d(TAG, "author: "+author);
                        Log.d(TAG, "pages: "+pages);
                        Log.d(TAG, "price: "+price);
                    }while (cursor.moveToNext());
                }
                cursor.close();

                //sql查询
                //db.rawQuery("select * from Book",null);
            }
        });
    }
}
