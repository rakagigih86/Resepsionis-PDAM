package com.d3.aplikasilogbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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

public class EditActivity extends AppCompatActivity {

    DBHelper helper;
    EditText TxTanggal, TxWaktu;
    Spinner spinnerEditKegiatan, spinnerEditLokasi, spinnerEditKeterangan;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    ImageView imageView;
    Uri uri;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        TxTanggal = (EditText)findViewById(R.id.txEditTanggal);
        TxWaktu = (EditText)findViewById(R.id.txEditWaktu);
        spinnerEditKeterangan = (Spinner)findViewById(R.id.spinnerEditKeterangan);
        imageView = (ImageView)findViewById(R.id.image_bukti);

        spinnerEditKegiatan = (Spinner)findViewById(R.id.spinnerEditKegiatan);
        spinnerEditLokasi = (Spinner)findViewById(R.id.spinnerEditLokasi);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        TxTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        //waktu
        TxWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrenTime = Calendar.getInstance();
                int hour = mcurrenTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrenTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        TxWaktu.setText(hourOfDay + ":" + minute);
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
        spinnerEditKegiatan.setAdapter(adapterLabels);

        List<String> locationsLabel = dbHelper.getLocation();
        final ArrayAdapter<String> adapterLocations = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                locationsLabel
        );
        adapterLocations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEditLokasi.setAdapter(adapterLocations);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(EditActivity.this);
            }
        });
        getData();
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

    private void getData() {
        Cursor cursor = helper.oneData(id);
        if(cursor.moveToFirst()){
            String tanggal = cursor.getString(cursor.getColumnIndex(DBHelper.row_tanggal));
            String waktu = cursor.getString(cursor.getColumnIndex(DBHelper.row_waktu));
            String kegiatan = cursor.getString(cursor.getColumnIndex(DBHelper.row_kegiatan));
            String keterangan = cursor.getString(cursor.getColumnIndex(DBHelper.row_keterangan));
            String lokasi = cursor.getString(cursor.getColumnIndex(DBHelper.row_lokasi));
            String gambar = cursor.getString(cursor.getColumnIndex(DBHelper.row_gambar));

            TxTanggal.setText(tanggal);
            TxWaktu.setText(waktu);
            spinnerEditKegiatan.getOnItemClickListener();

            if (spinnerEditKegiatan.equals("Sudah selesai")){
                spinnerEditKeterangan.setSelection(0);
            } else if (spinnerEditKeterangan.equals("Belum selesai")){
                spinnerEditKeterangan.setSelection(1);
            }

            spinnerEditKegiatan.getOnItemClickListener();

            if(gambar.equals("null")){
                imageView.setImageResource(R.drawable.ic_add_photo);
            }else{
                imageView.setImageURI(Uri.parse(gambar));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_edit:
                String tanggal = TxTanggal.getText().toString().trim();
                String waktu = TxWaktu.getText().toString().trim();
                String kegiatan = spinnerEditKegiatan.getSelectedItem().toString().trim();
                String keterangan = spinnerEditKeterangan.getSelectedItem().toString().trim();
                String lokasi = spinnerEditLokasi.getSelectedItem().toString().trim();

                ContentValues values = new ContentValues();
                values.put(DBHelper.row_tanggal, tanggal);
                values.put(DBHelper.row_kegiatan, kegiatan);
                values.put(DBHelper.row_keterangan, keterangan);
                values.put(DBHelper.row_lokasi, lokasi);
                values.put(DBHelper.row_gambar, String.valueOf(uri));

                if (tanggal.equals("") || waktu.equals("") || kegiatan.equals("") || keterangan.equals("") || lokasi.equals("")) {
                    Toast.makeText(EditActivity.this, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else {
                    helper.updateData(values, id);
                    Toast.makeText(EditActivity.this, "Data tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        switch (item.getItemId()){
            case R.id.delete_edit:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setMessage("Data ini akan dihapus");
                builder.setCancelable(true);
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.deleteData(id);
                        Toast.makeText(EditActivity.this, "Data terhapus!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
            imageView.setImageURI(result.getUri());
            uri = result.getUri();
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