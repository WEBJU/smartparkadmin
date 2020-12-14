package com.example.smartparkadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.io.Console;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import models.Geofences;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private List<String> lastSearches;
//    Context context=this;
    private MaterialSearchBar searchBar;
    Geocoder geocoder;
    List<Address> coordinates;
    private GoogleMap mMap;
//    private DatabaseReference databaseReference;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private Button btnSave,btnDelete,btnCancel;
    private TextInputEditText geofence;
    FirebaseAuth auth;
    final Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());
        auth=FirebaseAuth.getInstance();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("parking").child("geofences");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng nairobi=new LatLng(-1.2713984,36.8345088);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nairobi, 10));
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String gname=snapshot.getKey();
                        double latitude=dataSnapshot.child("latitude").getValue(Double.class);
                        double longitude=dataSnapshot.child("longitude").getValue(Double.class);
                        LatLng parking=new LatLng(latitude,longitude);
                        mMap.addMarker(new MarkerOptions().position(parking).title(gname+" "+"parking spot"));

                    }
               }else {
                   Toast.makeText(context, "onChildAdded:No data", Toast.LENGTH_SHORT).show();
               }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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
                startActivity(new Intent(MapsActivity.this,AddGeofenceName.class));
                finish();
                return true;
            case R.id.delete_geofence:
                startActivity(new Intent(MapsActivity.this,ViewGeofences.class));
                finish();
//                Toast.makeText(context, "Coding thiss..", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(MapsActivity.this,LoginActivity.class));
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}