package com.d3.resepsionispdam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PilihanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilihan);
    }

    public void keaplikasilogbook(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    public void keaplikasibukutamu(View view) {
        Intent intent = new Intent(this, MainActivityBukuTamu.class);
        this.startActivity(intent);
    }
}