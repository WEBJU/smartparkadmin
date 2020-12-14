package com.example.smartparkadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

import models.Geofences;

public class AddGeofenceName extends AppCompatActivity {
    //    private ProgressBar progressBar;
    TextInputEditText geo_name;
    private ProgressBar progressBar;
    Geocoder geocoder;
    Button btnSave,btnBack;
    FirebaseAuth auth;
    List<Address> coordinates;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_geofence_name);
        geo_name = (TextInputEditText) findViewById(R.id.geofence_name);
        btnSave = (Button) findViewById(R.id.addGeofence);
        btnBack=findViewById(R.id.btn_back);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        geocoder = new Geocoder(AddGeofenceName.this, Locale.getDefault());
//        coordinates=geocoder.
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("parking");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addGeofence();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddGeofenceName.this,MapsActivity.class));
                finish();
            }
        });
    }

    private void addGeofence() {
        String geofence = geo_name.getText().toString().trim();
        if (TextUtils.isEmpty(geofence)) {
            Toast.makeText(AddGeofenceName.this, "Geofence name cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
                progressBar.setVisibility(View.VISIBLE);
                try {
                    coordinates = geocoder.getFromLocationName(geofence, 1);
                    if (coordinates != null) {
                    if (coordinates.size() > 0) {
                    final double lat = coordinates.get(0).getLatitude();
                    final double lon = coordinates.get(0).getLongitude();
                    int radius = 100;

                    Geofences geofences = new Geofences(lat, lon, radius);
                    databaseReference.child("geofences").child(geofence).push().setValue(geofences).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AddGeofenceName.this, "New geofence added successfully!!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AddGeofenceName.this, "Could not add geofence", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddGeofenceName.this, "Error while adding geofence", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            return;
                        }
                    });
                        } else {
                            Toast.makeText(this, "Address cannot be converted", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else {
                        Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_geofence:
                startActivity(new Intent(AddGeofenceName.this,AddGeofenceName.class));
                finish();
                return true;
            case R.id.delete_geofence:
                startActivity(new Intent(AddGeofenceName.this,ViewGeofences.class));
                finish();
//                Toast.makeText(context, "Coding thiss..", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(AddGeofenceName.this,LoginActivity.class));
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}