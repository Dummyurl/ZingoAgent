package app.zingo.com.zingoagent;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.com.zingoagent.Adapter.NavigationListAdapter;
import app.zingo.com.zingoagent.Model.NavBarItems;

import static android.support.v4.view.GravityCompat.START;

public class MainActivity extends AppCompatActivity {

    ListView navBarListView;
    DrawerLayout drawer;

    EditText search_editText,budget_et;
    ScrollView scrollView_app_home;
    ImageView image_city_background;
    TextView cin_date_tv,cout_date_tv,cin_time_tv,cout_time_tv,cin_day_tv,cout_day_tv;
    ImageButton location_button;
    Button search_button;
    Spinner madult,mchild;

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



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setUpNavigationDrawer();

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
