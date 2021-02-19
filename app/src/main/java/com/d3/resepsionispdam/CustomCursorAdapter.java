package com.d3.resepsionispdam;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

public class CustomCursorAdapter extends CursorAdapter {
    private LayoutInflater layoutInflater;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CustomCursorAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup){
        View v = layoutInflater.inflate(R.layout.row_dataa, viewGroup, false);
        MyHolder holder = new MyHolder();
        holder.ListID = (TextView)v.findViewById(R.id.list_id);
        holder.ListNama = (TextView)v.findViewById(R.id.list_nama);
        holder.ListTanggal = (TextView)v.findViewById(R.id.list_tanggal);
        holder.ListWaktu = (TextView)v.findViewById(R.id.list_waktu);
        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MyHolder holder = (MyHolder)view.getTag();

        holder.ListID.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_idlogbook)));
        holder.ListNama.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_kegiatan)));
        holder.ListTanggal.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_tanggallogbook)));
        holder.ListWaktu.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_waktu)));
    }

    class MyHolder{
        TextView ListID;
        TextView ListNama;
        TextView ListTanggal;
        TextView ListWaktu;
    }


}