package symphome.de.locationtracker;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    IntentFilter locationFilter;
    BroadcastReceiver locationReceiver;


    // UI Elements
    TextView tvLocation;
    ListView lvLocations;
    Button btStartTracker;

    List<String> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    99
            );
        }

        //set UI Elements
        tvLocation = findViewById(R.id.tvLocation);
        lvLocations = findViewById(R.id.lvLocations);
        btStartTracker = findViewById(R.id.btStartTracker);

        // set up Location BC
        locationFilter = new IntentFilter();
        locationFilter.addAction(LocationService.ACTION_LOCATION_BC);

        locationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double la = intent.getDoubleExtra("lat", 1);
                double lo = intent.getDoubleExtra("long", 1);

                tvLocation.setText("Latitude: " + la + " | Longitude: " + lo);
                locations.add("Latitude: " + la + " | Longitude: " + lo);
                lvLocations.setAdapter(new ArrayAdapter<>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        locations
                ));
            }
        };
        registerReceiver(locationReceiver, locationFilter);

        // Button

        btStartTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Service start", Toast.LENGTH_LONG).show();
                startService(new Intent(MainActivity.this, LocationService.class));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 99 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(locationReceiver, locationFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(locationReceiver);
    }
}
