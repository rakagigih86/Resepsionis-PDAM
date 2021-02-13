package com.d3.aplikasilogbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class MasterLokasiActivity extends AppCompatActivity {

    DBHelper helper;
    EditText TxMasterLokasi;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_lokasi);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        TxMasterLokasi = (EditText)findViewById(R.id.txMasterLokasi);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.master_lokasi_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_master_lokasi:
                String master_lokasi = TxMasterLokasi.getText().toString().trim();

                ContentValues values = new ContentValues();
                values.put(DBHelper.row_master_lokasi, master_lokasi);

                if (master_lokasi.equals("")){
                    Toast.makeText(MasterLokasiActivity.this, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }else{
                    helper.insertMasterL(values);
                    Toast.makeText(MasterLokasiActivity.this, "Data tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}