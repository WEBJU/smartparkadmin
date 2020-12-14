package models;

public class GeoName {
    private String geofence_name;

    public GeoName() {
    }

    public GeoName(String geofence_name) {
        this.geofence_name = geofence_name;
    }

    public String getGeofence_name() {
        return geofence_name;
    }

    public void setGeofence_name(String geofence_name) {
        this.geofence_name = geofence_name;
    }
}
