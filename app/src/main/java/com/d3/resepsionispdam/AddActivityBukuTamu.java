
package com.d3.resepsionispdam;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddActivityBukuTamu extends AppCompatActivity {

    DBHelper helper;
    TextView TxNomor;
    EditText TxTanggal, TxNama, TxKeterangan;
    Spinner  SpinnerPegawai ;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    CircularImageView imageView;
    Uri uri;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_buku_tamu);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 1);

        TxTanggal = (EditText)findViewById(R.id.txTanggal);
        TxNomor = (TextView)findViewById(R.id.txNomor);
        TxKeterangan = (EditText) findViewById(R.id.txKeterangan);
        imageView = (CircularImageView)findViewById(R.id.image_bukti);

        TxNama = (EditText) findViewById(R.id.txNama);
        SpinnerPegawai = (Spinner)findViewById(R.id.spinnerPegawai);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);


        DBHelper dbHelper = new DBHelper(this);
        List<String> locationsLabel = dbHelper.getPegawai();
        final ArrayAdapter<String> adapterLocations = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                locationsLabel
        );
        adapterLocations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerPegawai.setAdapter(adapterLocations);

        TxTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(AddActivityBukuTamu.this);
            }
       });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                TxTanggal.setText(dateFormatter.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_add:
                String tanggal = TxTanggal.getText().toString().trim();
                String nama = TxNama.getText().toString().trim();
                String keterangan = TxKeterangan.getText().toString().trim();
                String pegawai = SpinnerPegawai.getSelectedItem().toString().trim();

                ContentValues values = new ContentValues();
                values.put(DBHelper.row_tanggal, tanggal);
                values.put(DBHelper.row_nama, nama);
                values.put(DBHelper.row_keterangan, keterangan);
                values.put(DBHelper.row_namapegawai, pegawai);
                values.put(DBHelper.row_gambar, String.valueOf(uri));

                if (tanggal.equals("") ||  nama.equals("") || keterangan.equals("") || pegawai.equals("")){
                    Toast.makeText(AddActivityBukuTamu.this, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }else{
                    helper.insertData(values);
                    Toast.makeText(AddActivityBukuTamu.this, "Data tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingSuperCall")
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
                && resultCode == Activity.RESULT_OK){
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            if(CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)){
                uri = imageuri;
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}
                        , 0);
            } else {
                startCrop(imageuri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                imageView.setImageURI(result.getUri());
                uri = result.getUri();
            }
        }
    }

    private void startCrop(Uri imageuri){
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);
        uri = imageuri;
    }
}