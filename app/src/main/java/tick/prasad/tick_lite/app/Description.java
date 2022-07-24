package tick.prasad.tick_lite.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tick.prasad.tick_lite.R;
import tick.prasad.tick_lite.common.config;

public class Description extends AppCompatActivity {

    private ListView lvPlacesVisiting;
    private Recycle_list_view recycle_list_view;
    private List<MoreDetailsGS> list;
    private RequestQueue requestQueue;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;



    @Override
    protected void onPostResume() {
        super.onPostResume();

        alertDialog.cancel();
        if (isNetworkAvailable()) {
            getJSON();

        } else {
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        networkDialog();

        lvPlacesVisiting = (ListView)findViewById(R.id.lvPlaces);
        list = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        if (isNetworkAvailable()) {
            getJSON();
        } else {
            alertDialog.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void prepareDescription(JSONArray jsonArray){

        list.clear();

        try{

            for(int index = 0; index<jsonArray.length(); index++){

                JSONObject jsonObject = jsonArray.getJSONObject(index);

                String x1 = jsonObject.getString("place");
                String x2 = jsonObject.getString("price");

                list.add(new MoreDetailsGS(x1, x2));

            }

            recycle_list_view = new Recycle_list_view(this, list);
            lvPlacesVisiting.setAdapter(recycle_list_view);

        }catch (Exception ex){
            ex.toString();
        }
    }

    private void getJSON(){

        Bundle bundle  = getIntent().getExtras();

        String END_POINT = config.URL+"/"+(bundle.getString("NAME").toString().replace(" ", "_"));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                END_POINT,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            prepareDescription(response.getJSONArray("data"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private boolean isNetworkAvailable() {

        if(isAirplaneModeOn(this) || !isConnected()){
            return false;
        }else {

            if(isConnected()){
                return true;
            }else{
                return false;
            }

        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;

            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        } else {
            return false;
        }

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;

    }

    private void networkDialog() {
        alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setTitle("No Internet Connection");

        alertDialogBuilder
                .setMessage("No internet connection available on this device, Enable internet connection to continue")
                .setCancelable(false)
                .setPositiveButton("Data", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));

                    }
                })
                .setNegativeButton("WiFi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });


        alertDialog = alertDialogBuilder.create();

    }
}
