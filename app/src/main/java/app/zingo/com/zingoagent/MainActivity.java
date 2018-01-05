package app.zingo.com.zingoagent;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import app.zingo.com.zingoagent.Adapter.NavigationListAdapter;
import app.zingo.com.zingoagent.Model.NavBarItems;
import app.zingo.com.zingoagent.Utils.LocationHelper;

import static android.support.v4.view.GravityCompat.START;
import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback {

    ListView navBarListView;
    DrawerLayout drawer;

    EditText search_editText,budget_et;
    ScrollView scrollView_app_home;
    ImageView image_city_background;
    TextView cin_date_tv,cout_date_tv,cin_time_tv,cout_time_tv,cin_day_tv,cout_day_tv;
    ImageButton location_button;
    Button search_button;
    Spinner madult,mchild;

    DatePickerDialog datePickerDialog;
    String localty,duration;
    long cin,cout;
    SimpleDateFormat dateFormatter;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    //Location access
    private Location mLastLocation;
    double latitude;
    double longitude;

    LocationHelper locationHelper;
    private ProgressDialog progressDialog;

    private long diffDays,diffHours,diffMinutes;
    private long fd;
    private long td;
    private String ft,tt;
    private String fds,tds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Search");

        navBarListView = (ListView) findViewById(R.id.navbar_list);

        scrollView_app_home = (ScrollView)findViewById(R.id.scrollview_app_home);
        image_city_background = (ImageView)findViewById(R.id.image_city_background);

        search_editText = (EditText)findViewById(R.id.search_editText);
        budget_et = (EditText)findViewById(R.id.budget_et);

        madult = (Spinner)findViewById(R.id.adult_count);
        mchild = (Spinner)findViewById(R.id.child_count);
        cin_date_tv = (TextView)findViewById(R.id.cin_date_tv);
        cout_date_tv = (TextView)findViewById(R.id.cout_date_tv);
        cin_time_tv = (TextView)findViewById(R.id.cin_time_tv);
        cout_time_tv = (TextView)findViewById(R.id.cout_time_tv);
        cin_day_tv = (TextView)findViewById(R.id.cin_day_tv);
        cout_day_tv = (TextView)findViewById(R.id.cout_day_tv);
        location_button = (ImageButton)findViewById(R.id.location_button);
        search_button = (Button)findViewById(R.id.search_button_api);

        locationHelper=new LocationHelper(this);
        locationHelper.checkpermission();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setUpNavigationDrawer();

        //Get current date,day,time and set
        long date = System.currentTimeMillis();

        dateFormatter = new SimpleDateFormat("dd MMM");
        String dateString = dateFormatter.format(date);
        cin_date_tv.setText(dateString);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR,1);
        Date checkout = cal.getTime();

        String coutString = dateFormatter.format(checkout);
        cout_date_tv.setText(coutString);



        SimpleDateFormat sdfw = new SimpleDateFormat("EEEE");
        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm a");
        Date d = new Date();
        String dayOfTheWeek = sdfw.format(d);
        String time = sdft.format(d);
        String nextDay = sdfw.format(checkout);
        cin_day_tv.setText(dayOfTheWeek);
        cout_day_tv.setText(nextDay);
        cin_time_tv.setText(time);
        cout_time_tv.setText(time);



        //check-in date
        cin_date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(cin_date_tv);
            }
        });


        //check-out date
        cout_date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(cout_date_tv);
            }
        });



        //check-in time
        cin_time_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(cin_time_tv);

            }
        });


        //check-out time
        cout_time_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(cout_time_tv);

            }
        });


        //Google Place Auto Complete

        search_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });



        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add code access for gps
                mLastLocation=locationHelper.getLocation();

                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                    getAddress();

                } else {

                    if(search_button.isEnabled())
                        search_button.setEnabled(false);

                    showToast("Couldn't get the location. Make sure location is enabled on the device");
                }


            }
        });

        if (locationHelper.checkPlayServices()) {

            // Building the GoogleApi client
            locationHelper.buildGoogleApiClient();
        }


        //Search and send request for hotel
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();

            }
        });

    }


    public void getAddress()
    {
        Address locationAddress;

        locationAddress=locationHelper.getAddress(latitude,longitude);

        if(locationAddress!=null)
        {

            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            localty = locationAddress.getSubLocality();
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();


            String currentLocation;

            if(!isEmpty(address))
            {
                currentLocation=address;

                if (!isEmpty(address1))
                    currentLocation+="\n"+address1;


                search_editText.setText(currentLocation);
                search_editText.setVisibility(View.VISIBLE);

                if(!search_button.isEnabled())
                    search_button.setEnabled(true);
            }

        }
        else
            showToast("Something went wrong");
    }

    public void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void validateFields(){
        String fromDate = cin_date_tv.getText().toString();
        String toDate = cout_date_tv.getText().toString();
        String location = search_editText.getText().toString();
        String price = budget_et.getText().toString();
        String fromTime = cin_time_tv.getText().toString();
        String toTime = cout_time_tv.getText().toString();

        if(fromDate == null || fromDate.isEmpty())
        {
            cin_date_tv.setError("Check-in date not selected");
            cin_date_tv.requestFocus();
        }else if(toDate == null || toDate.isEmpty())
        {
            cout_date_tv.setError("Check-out date not selected");
            cout_date_tv.requestFocus();
        }else if(fromTime == null || fromTime.isEmpty())
        {
            cin_time_tv.setError("Check-in time not selected");
            cin_time_tv.requestFocus();
        }else if(toTime == null || toTime.isEmpty())
        {
            cout_time_tv.setError("Check-out time not selected");
            cout_time_tv.requestFocus();
        }else if(location == null || location.isEmpty())
        {
            search_editText.setError("Location not selected");
            search_editText.requestFocus();
        }else if(price == null || price.isEmpty())
        {
            budget_et.setError("Price date not selected");
            budget_et.requestFocus();
        }else if(Integer.parseInt(price)<200)
        {
            budget_et.setError("Price should be above 200");
            budget_et.requestFocus();
        }else{

          /*  postSearch();
            dateCal();
            Intent intent = new Intent(getApplicationContext(),HotelListActivity.class);

            //Passing Localty value for next Activty get List of Hotel
            intent.putExtra("Localty",localty);
            intent.putExtra("Duration",duration);
            System.out.println("Duration==="+duration);
            startActivity(intent);*/

            Intent intent = new Intent(getApplicationContext(),HotelList.class);
            startActivity(intent);

        }


    }

    public void setDate(final TextView textView)
    {
        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar cal = Calendar.getInstance();
                cal.set(year,month,dayOfMonth);
                Date date = cal.getTime();

                textView.setText(dateFormatter.format(date));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String dateStr = simpleDateFormat.format(date);
                System.out.println("Date Format==="+dateStr);
                String day = null;
                try {
                    day = new SimpleDateFormat("EEEE").format(simpleDateFormat.parse(dateStr));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(textView == cin_date_tv)
                {
                    if(day != null)
                    {
                        cin_day_tv.setText(day);
                    }

                    fds = sdf.format(date);


                }
                else if(textView == cout_date_tv)
                {
                    if(day != null)
                    {
                        cout_day_tv.setText(day);
                    }
                    tds = sdf.format(date);
                }
            }
        },newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    public void openTimePicker(final TextView tv){

        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                //tv.setText( selectedHour + ":" + selectedMinute);



                System.out.println("time==="+mcurrentTime);
                if (tv.equals(cin_time_tv)){
                    ft = String.format("%02d:%02d:00",hourOfDay,minute);
                    boolean isPM =(hourOfDay >= 12);
                    cin_time_tv.setText( String.format("%02d:%02d %s ", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));
                }else if (tv.equals(cout_time_tv)) {
                    tt = String.format("%02d:%02d:00",hourOfDay,minute);
                    boolean isPM =(hourOfDay >= 12);
                    cout_time_tv.setText( String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));

                }
            }
        }, hour, minute, false);//Yes 24 hour time
        if(tv.equals(cin_time_tv)){
            mTimePicker.setTitle("Check-In Time");
        }else if(tv.equals(cout_time_tv)){
            mTimePicker.setTitle("Check-Out Time");
        }

        mTimePicker.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                LatLng latLang = place.getLatLng();
                double lat  = latLang.latitude;
                double longi  = latLang.longitude;

                try {
                    Geocoder geocoder = new Geocoder(MainActivity.this);
                    List<Address> addresses = geocoder.getFromLocation(lat,longi,1);
                    localty = String.valueOf(place.getName());
                    search_editText.setText(place.getName()+","+addresses.get(0).getAdminArea()+","+addresses.get(0).getCountryName());
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpNavigationDrawer() {

        TypedArray icons = getResources().obtainTypedArray(R.array.navbar_images);
        String[] title  = getResources().getStringArray(R.array.navbar_items_title);

        ArrayList<NavBarItems> navBarItemsList = new ArrayList<>();

        for (int i=0;i<title.length;i++)
        {
            NavBarItems navbarItem = new NavBarItems(title[i],icons.getResourceId(i, -1));
            navBarItemsList.add(navbarItem);
        }

        NavigationListAdapter adapter = new NavigationListAdapter(getApplicationContext(),navBarItemsList);
        navBarListView.setAdapter(adapter);

        navBarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawer.closeDrawer(START);
                displayView(position);
            }
        });
    }

    private void displayView(int position) {
        //System.out.println("position = "+position);
        switch (position)
        {
            case 0:
                Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();

                break;
            case 1:
                Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                break;
            case 3:

                break;
            case 4:
                Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                break;
            case 7:
                Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                break;

        }
    }

}
