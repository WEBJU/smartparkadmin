package adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartparkadmin.R;
import com.example.smartparkadmin.ViewGeofences;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.w3c.dom.Text;

import java.util.List;

import models.GeoName;
import models.Geofences;

public class GeofenceAdapters extends RecyclerView.Adapter<GeofenceAdapters.ViewHolder> {

    private List<Geofences>geofence_data;

    public GeofenceAdapters(@NonNull List<Geofences>geofence_data) {
        this.geofence_data=geofence_data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_geofence,parent,false);
        

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeofenceAdapters.ViewHolder holder, int position) {
        Geofences geo=geofence_data.get(position);
//        double lat=ge
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ViewGeofences viewGeofences=new ViewGeofences();
//                viewGeofences.showToast(,"We have done it");
//                Log.d("")
            }
        });
        holder.gname.setText(geo.getKey());
    }

    @Override
    public int getItemCount() {
        return geofence_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView gname,geofence_name;
        private Button btnDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            btnDelete=itemView.findViewById(R.id.delete_btn);
            geofence_name=itemView.findViewById(R.id.txtGeofenceName);
            gname =itemView.findViewById(R.id.txtGeofenceName);
        }
    }
}
