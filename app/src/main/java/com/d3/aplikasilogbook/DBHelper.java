package com.d3.aplikasilogbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String database_name = "db_logbook";
    public static final String table_name1 = "tabel_umum";
    public static final String table_name2 = "tabel_master_kegiatan";
    public static final String table_name3 = "tabel_master_lokasi";

    public static final String row_id = "_id";
    public static final String row_tanggal = "Tanggal";
    public static final String row_kegiatan = "Kegiatan";
    public static final String row_keterangan = "Keterangan";
    public static final String row_lokasi = "Lokasi";
    public static final String row_gambar = "Gambar";
    public static final String row_master_kegiatan = "Master_Kegiatan";
    public static final String row_master_lokasi = "Master_Lokasi";

    private SQLiteDatabase db;

    public DBHelper(Context context){
        super(context, database_name, null, 3);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE " + table_name1 + " ( " + row_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + row_tanggal + " TEXT, " + row_kegiatan + " TEXT, " + row_keterangan + " TEXT, "
                + row_lokasi + " TEXT, " + row_gambar + " TEXT)" ;
        db.execSQL(query1);

        String query2 = "CREATE TABLE " + table_name2 + " ( " + row_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + row_master_kegiatan + " TEXT) " ;
        db.execSQL(query2);

        String query3 = "CREATE TABLE " + table_name3 + " ( " + row_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + row_master_lokasi + " TEXT) " ;
        db.execSQL(query3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name1);
        db.execSQL("DROP TABLE IF EXISTS " + table_name2);
        db.execSQL("DROP TABLE IF EXISTS " + table_name3);
        onCreate(db);
    }

    public Cursor allData(){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_name1, null);
        return cur;
    }

    public Cursor oneData(Long id){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_name1 + " WHERE " + row_id + "=" + id, null);
        return cur;
    }

    public void insertData(ContentValues values){
        db.insert(table_name1, null, values);
    }

    public void insertMasterK(ContentValues values){
        db.insert(table_name2, null, values);
    }

    public void insertMasterL(ContentValues values){
        db.insert(table_name3, null, values);
    }

    public void updateData(ContentValues values, long id){
        db.update(table_name1, values, row_id + '=' + id, null);
    }

    public void deleteData(long id){
        db.delete(table_name1, row_id + "=" + id, null);
    }

}
