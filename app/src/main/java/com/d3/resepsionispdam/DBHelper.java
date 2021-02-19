package com.d3.resepsionispdam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String database_name = "db_logbook";
    //BUKUTAMU
    public static final String table_name1 = "tabel_bukutamu";
    public static final String table_name2 = "tabel_master_pegawai";
    public static final String table_login = "tabel_login";

    //LOGBOOK
    public static final String table_namesatu = "tabel_umum";
    public static final String table_namedua = "tabel_master_kegiatan";
    public static final String table_nametiga = "tabel_master_lokasi";

    //BUKUTAMU
    public static final String row_id = "_id";
    public static final String row_tanggal = "Tanggal";
    public static final String row_nomor = "Nomor";
    public static final String row_nama = "Nama";
    public static final String row_keterangan = "Keterangan";
    public static final String row_namapegawai = "Pegawai";
    public static final String row_gambar = "Gambar";
    public static final String row_master_pegawai = "Master_Pegawai";

    //LOGBOOK
    public static final String row_idlogbook = "_id";
    public static final String row_tanggallogbook = "Tanggal";
    public static final String row_waktu = "Waktu";
    public static final String row_kegiatan = "Kegiatan";
    public static final String row_keteranganlogbook = "Keterangan";
    public static final String row_lokasi = "Lokasi";
    public static final String row_gambarlogbook = "Gambar";
    public static final String row_master_kegiatan = "Master_Kegiatan";
    public static final String row_master_lokasi = "Master_Lokasi";

    public static final String row_idlogin = "_idlogin";
    public static final String row_username = "Username";
    public static final String row_password =   "Password";

    private SQLiteDatabase db;

    public DBHelper(Context context){
        super(context, database_name, null, 3);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE " + table_name1 + " ( " + row_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + row_tanggal + " TEXT, " + row_nomor + " TEXT, " + row_nama + " TEXT, " + row_keterangan + " TEXT, "
                + row_namapegawai + " TEXT, " + row_gambar + " TEXT)" ;
        db.execSQL(query1);

        //tabel login
        String querylogin = "CREATE TABLE " + table_login + "(" + row_idlogin + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + row_username + " TEXT," + row_password + " TEXT)";
        db.execSQL(querylogin);

        //spinnerpegawai
        String query2 = "CREATE TABLE " + table_name2 + " ( " + row_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + row_master_pegawai + " TEXT) " ;
        db.execSQL(query2);

        //tabel logbook
        String querysatu = "CREATE TABLE " + table_namesatu + " ( " + row_idlogbook + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + row_tanggallogbook + " TEXT, " + row_waktu + " TEXT, " + row_kegiatan + " TEXT, " + row_keteranganlogbook + " TEXT, "
                + row_lokasi + " TEXT, " + row_gambarlogbook + " TEXT)" ;
        db.execSQL(querysatu);

        String querydua = "CREATE TABLE " + table_namedua + " ( " + row_idlogbook + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + row_master_kegiatan + " TEXT) " ;
        db.execSQL(querydua);

        String querytiga = "CREATE TABLE " + table_nametiga + " ( " + row_idlogbook + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + row_master_lokasi + " TEXT) " ;
        db.execSQL(querytiga);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name1);
        db.execSQL("DROP TABLE IF EXISTS " + table_name2);
        db.execSQL("DROP TABLE IF EXISTS " + table_nametiga);
        db.execSQL("DROP TABLE IF EXISTS " + table_namesatu);
        db.execSQL("DROP TABLE IF EXISTS " + table_namedua);
        db.execSQL("DROP TABLE IF EXISTS " + table_login);
        onCreate(db);
    }

    public Cursor allData(){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_name1,  null);
        return cur;
    }
    public Cursor allDataa(){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_namesatu,  null);
        return cur;
    }

    //bukutamuspinner
    public List<String> getPegawai(){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_name2, null);
        List<String> lists = new ArrayList<>();
        if (cur.moveToFirst()){
            do{
                lists.add(cur.getString(1));
            }while (cur.moveToNext());
        }
        return lists;
    }

    public List<String> getEvent(){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_namedua, null);
        List<String> lists = new ArrayList<>();
        if (cur.moveToFirst()){
            do{
                lists.add(cur.getString(1));
            }while (cur.moveToNext());
        }
        return lists;
    }
    public List<String> getLocation(){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_nametiga, null);
        List<String> lists = new ArrayList<>();
        if (cur.moveToFirst()){
            do{
                lists.add(cur.getString(1));
            }while (cur.moveToNext());
        }
        return lists;
    }

    public Cursor oneData(Long id){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_name1 + " WHERE " + row_id + "=" + id, null);
        return cur;
    }
    public Cursor oneDataa(Long id){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_namesatu + " WHERE " + row_idlogbook + "=" + id, null);
        return cur;
    }

    public void insertData(ContentValues values){
        db.insert(table_name1, null, values);
    }

    public void insertDataa(ContentValues values){
        db.insert(table_login, null, values);
    }

    public void insertMasterPegawai(ContentValues values){
        db.insert(table_name2, null, values);
    }

    //logbook
    public void insertDatalogbook(ContentValues values){
        db.insert(table_namesatu, null, values);
    }

    public void insertMasterK(ContentValues values){
        db.insert(table_namedua, null, values);
    }

    public void insertMasterL(ContentValues values){
        db.insert(table_nametiga, null, values);
    }

    public boolean checkUser(String username, String password){
        String[] columns = {row_idlogin};
        SQLiteDatabase db = getReadableDatabase();
        String selection = row_username + "=?" + " and " + row_password + "=?";
        String[] selectionArgs = {username,password};
        Cursor cursor = db.query(table_login, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count>0)
            return true;
        else
            return false;
    }

    public void updateData(ContentValues values, long id){
        db.update(table_name1, values, row_id + '=' + id, null);
    }

    public void deleteData(long id){
        db.delete(table_name1, row_id + "=" + id, null);
    }

    //logbook
    public void updateDatalogbook(ContentValues values, long id){
        db.update(table_namesatu, values, row_idlogbook + '=' + id, null);
    }

    public void deleteDatalogbook(long id){
        db.delete(table_namesatu, row_idlogbook + "=" + id, null);
    }

}
