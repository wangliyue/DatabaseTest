package com.example.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {

    private static UriMatcher uriMatcher;
    private MyDatabaseHelper myDatabaseHelper;

    private static String authority = "com.example.www.databasetest.provider";

    private static final int BOOK_DIR = 0;
    private static final int BOOK_ITEM = 1;
    private static final int CATEGORY_DIR = 2;
    private static final int CATEGORY_ITEM = 3;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(authority,"Book",BOOK_DIR);
        uriMatcher.addURI(authority,"Book/#",BOOK_ITEM);
        uriMatcher.addURI(authority,"Category",CATEGORY_DIR);
        uriMatcher.addURI(authority,"Category/#",CATEGORY_ITEM);
    }
    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        myDatabaseHelper = new MyDatabaseHelper(getContext(),"BookStore.db",null,3);
        return true;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                long newBookId = db.insert("Book",null,values);
                uriReturn = Uri.parse("content://"+authority+"/Book/"+newBookId);
                break;
            case BOOK_ITEM: break;
            case CATEGORY_DIR:
                long newCategoryId = db.insert("Category",null,values);
                uriReturn = Uri.parse("content://"+authority+"/Category/"+newCategoryId);
                break;
            case CATEGORY_ITEM: break;
                default:break;
        }
        return uriReturn;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        int rows = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                rows = db.delete("Book",selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                rows = db.delete("Book","id = ?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                rows = db.delete("Category",selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                rows = db.delete("Category","id = ?",new String[]{categoryId});
                break;
            default:break;
        }
        return rows;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                cursor = db.query("Book",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("Book",projection,"id = ?",new String[]{bookId},null,null,sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = db.query("Category",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("Category",projection,"id = ?",new String[]{categoryId},null,null,sortOrder);
                break;
            default:
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = myDatabaseHelper.getReadableDatabase();
        int rows = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                rows = db.update("Book",values,selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                rows = db.update("Book",values,"id = ?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                rows = db.update("Category",values,selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                rows = db.update("Category",values,"id = ?",new String[]{categoryId});
                break;
            default:
        }
        return rows;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.www.databasetest.provider.Book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.www.databasetest.provider.Book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.www.databasetest.provider.Category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.www.databasetest.provider.Category";
        }
        return null;
    }

}
