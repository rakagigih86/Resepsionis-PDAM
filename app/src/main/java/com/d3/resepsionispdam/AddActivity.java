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
import android.app.TimePickerDialog;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    DBHelper helper;
    EditText TxTanggal, TxtWaktu;
    Spinner spinnerKegiatan, spinnerLokasi, spinnerKeterangan;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    ImageView imageView;
    Uri uri;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_idlogbook, 0);

        TxTanggal = (EditText)findViewById(R.id.txTanggal);
        TxtWaktu = (EditText)findViewById(R.id.txWaktu);
        spinnerKeterangan = (Spinner)findViewById(R.id.spinnerKeterangan);
        imageView = (ImageView)findViewById(R.id.image_bukti);

        spinnerKegiatan = (Spinner)findViewById(R.id.spinnerKegiatan);
        spinnerLokasi = (Spinner)findViewById(R.id.spinnerLokasi);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        //waktu
        TxtWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrenTime = Calendar.getInstance();
                int hour = mcurrenTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrenTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        TxtWaktu.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        DBHelper dbHelper = new DBHelper(this);
        List<String> eventLabels = dbHelper.getEvent();
        final ArrayAdapter<String> adapterLabels = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                eventLabels);
        adapterLabels.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKegiatan.setAdapter(adapterLabels);

        List<String> locationsLabel = dbHelper.getLocation();
        final ArrayAdapter<String> adapterLocations = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                locationsLabel
        );
        adapterLocations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLokasi.setAdapter(adapterLocations);

        TxTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CropImage.startPickImageActivity(AddActivity.this);
//            }
//        });
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
        getMenuInflater().inflate(R.menu.add_menulogbook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_addd:
                String tanggal = TxTanggal.getText().toString().trim();
                String waktu = TxtWaktu.getText().toString().trim();
                String kegiatan = spinnerKegiatan.getSelectedItem().toString().trim();
                String keterangan = spinnerKeterangan.getSelectedItem().toString().trim();
                String lokasi = spinnerLokasi.getSelectedItem().toString().trim();

                ContentValues values = new ContentValues();
                values.put(DBHelper.row_tanggallogbook, tanggal);
                values.put(DBHelper.row_waktu, waktu);
                values.put(DBHelper.row_kegiatan, kegiatan);
                values.put(DBHelper.row_keteranganlogbook, keterangan);
                values.put(DBHelper.row_lokasi, lokasi);
                values.put(DBHelper.row_gambarlogbook, String.valueOf(uri));

                if (tanggal.equals("") || waktu.equals("") || kegiatan.equals("") || keterangan.equals("") || lokasi.equals("")){
                    Toast.makeText(AddActivity.this, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }else{
                    helper.insertDatalogbook(values);
                    Toast.makeText(AddActivity.this, "Data tersimpan", Toast.LENGTH_SHORT).show();
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