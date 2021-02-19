package com.d3.resepsionispdam;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityBukuTamu extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    DBHelper helper;
    LayoutInflater inflater;
    View dialogView;
    TextView Tv_tanggal, Tv_nomor, Tv_nama, Tv_keterangan, Tv_pegawai;
    ImageView Tv_gambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_buku_tamu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivityBukuTamu.this, AddActivityBukuTamu.class));
            }
        });

        helper = new DBHelper(this);
        listView = (ListView)findViewById(R.id.list_data);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.masterPegawai) {
            Intent intent = new Intent(this, MasterPegawaiActivity.class);
            this.startActivity(intent);
        }
        if (id == R.id.about) {
            Intent intent = new Intent(this, AboutUsActivityBukuTamu.class);
            this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListView(){
        Cursor cursor = helper.allData();
        CustomCursorAdapterBukuTamu customCursorAdapter = new CustomCursorAdapterBukuTamu(this, cursor, 1);
        listView.setAdapter(customCursorAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long x) {
        TextView getId = (TextView)view.findViewById(R.id.list_id);
        final long id = Long.parseLong(getId.getText().toString());
        final Cursor cur = helper.oneData(id);
        cur.moveToFirst();

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityBukuTamu.this);
        builder.setTitle("Pilih Opsi");

        String[] options = {"Lihat Data Pengunjung","Edit Data Pengunjung", "Hapus Data Pengunjung"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        final AlertDialog.Builder viewData = new AlertDialog.Builder(MainActivityBukuTamu.this);
                        inflater = getLayoutInflater();
                        dialogView = inflater.inflate(R.layout.view_data, null);
                        viewData.setView(dialogView);
                        viewData.setTitle("Lihat Data Pengunjung");

                        Tv_tanggal = (TextView)dialogView.findViewById(R.id.tv_Tanggal);
                        Tv_nomor = (TextView)dialogView.findViewById(R.id.tv_nomor);
                        Tv_nama = (TextView)dialogView.findViewById(R.id.tv_nama);
                        Tv_keterangan = (TextView)dialogView.findViewById(R.id.tv_keterangan);
                        Tv_pegawai = (TextView)dialogView.findViewById(R.id.tv_pegawai);
                        Tv_gambar = (ImageView)dialogView.findViewById(R.id.tv_Gambar);

                        Tv_tanggal.setText("Tanggal: " + cur.getString(cur.getColumnIndex(DBHelper.row_tanggal)));
                        Tv_nomor.setText("Nomor: " + cur.getString(cur.getColumnIndex(DBHelper.row_id)));
                        Tv_nama.setText("Nama: " + cur.getString(cur.getColumnIndex(DBHelper.row_nama)));
                        Tv_keterangan.setText("Keterangan: " + cur.getString(cur.getColumnIndex(DBHelper.row_keterangan)));
                        Tv_pegawai.setText("Kepada: " + cur.getString(cur.getColumnIndex(DBHelper.row_namapegawai)));

                        Tv_gambar.setImageURI(Uri.parse(cur.getString(cur.getColumnIndex(DBHelper.row_gambar))));

                        viewData.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        viewData.show();
                }
                switch (which){
                    case 1:
                        Intent iddata = new Intent(MainActivityBukuTamu.this, EditActivityBukuTamu.class);
                        iddata.putExtra(DBHelper.row_id, id);
                        startActivity(iddata);
                }
                switch (which){
                    case 2:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivityBukuTamu.this);
                        builder1.setMessage("Data ini akan dihapus");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                helper.deleteData(id);
                                Toast.makeText(MainActivityBukuTamu.this, "Data terhapus", Toast.LENGTH_SHORT).show();
                                setListView();
                            }
                        });
                        builder1.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder1.create();
                        alertDialog.show();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListView();
    }

}