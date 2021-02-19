package com.d3.resepsionispdam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class MasterPegawaiActivity extends AppCompatActivity {

    DBHelper helper;
    EditText TxMasterPegawai;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_pegawai);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        TxMasterPegawai = (EditText)findViewById(R.id.txMasterPegawai);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.master_pegawai_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_master_pegawai:
                String master_pegawai = TxMasterPegawai.getText().toString().trim();

                ContentValues values = new ContentValues();
                values.put(DBHelper.row_master_pegawai, master_pegawai);

                if (master_pegawai.equals("")){
                    Toast.makeText(MasterPegawaiActivity.this, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }else{
                    helper.insertMasterPegawai(values);
                    Toast.makeText(MasterPegawaiActivity.this, "Data tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}