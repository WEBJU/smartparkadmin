package com.example.smartparkadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapters.GeofenceAdapters;
import models.GeoName;
import models.Geofences;

public class ViewGeofences extends AppCompatActivity {
    private List<Geofences>geofence_data;
//    ArrayList<String> geonames;
    Geocoder geocoder;
    FirebaseAuth auth;
    Button btnDelete;
    TextView geofence_name;
    List<Address> coordinates;
    private RecyclerView recyclerView;
//    FirebaseRecyclerAdapter<Geofences, ViewHolder> geofenceAdapters;
    private GeofenceAdapters geofenceAdapter;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_geofences);
        recyclerView=findViewById(R.id.recycler1);
        auth = FirebaseAuth.getInstance();
        //to display the recyler view linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewGeofences.this));

        geofence_data=new ArrayList<>();
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference=database.getReference("parking").child("geofences");
//      databaseReference.child("geofences");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                progressDialog.setTitle("Loading geofences...");
//                progressDialog.show();
//                if (snapshot.getChildrenCount() > 0){
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            String name=dataSnapshot.getKey();
                            Log.wtf("Key", dataSnapshot.getKey());
//                            double latitude = dataSnapshot.child("latitude").getValue(Double.class);
//                            double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                            Geofences geo= dataSnapshot.getValue(Geofences.class);
                            geo.setKey(dataSnapshot.getKey());
                            geofence_data.add(geo);
                        }
                        geofenceAdapter = new GeofenceAdapters(geofence_data);
                        recyclerView.setAdapter(geofenceAdapter);
//                        progressDialog.dismiss();
                    }else{
                        Toast.makeText(ViewGeofences.this, "Nothing like that!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
//            }else{
//                    Toast.makeText(ViewGeofences.this, "Check!!", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(ViewGeofences.this, "Error"+ error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
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
                startActivity(new Intent(ViewGeofences.this,AddGeofenceName.class));
                finish();
                return true;
            case R.id.delete_geofence:
                startActivity(new Intent(ViewGeofences.this,ViewGeofences.class));
                finish();
//                Toast.makeText(context, "Coding thiss..", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(ViewGeofences.this,LoginActivity.class));
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}