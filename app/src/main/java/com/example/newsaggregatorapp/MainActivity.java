package com.example.newsaggregatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.newsaggregatorapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefresher;
    private Menu menu;
    private int[] menuColors;
    private DrawerLayout sourceDrawerLayout;
    private ListView sourceDrawerList;
    private ActionBarDrawerToggle sourceDrawerToggle;
    private ActivityMainBinding mainBinding;
    private ArrayList<CharSequence> newsSourceNames = new ArrayList<>();
    private final ArrayList<Source> newsSourceList = new ArrayList<>();
    private final String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Successfully created");

        menuColors = new int[]{Color.GREEN, ContextCompat.getColor(this, R.color.orange), ContextCompat.getColor(this, R.color.honey_yellow), Color.RED, ContextCompat.getColor(this, R.color.violet), Color.BLUE, Color.MAGENTA};

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        swipeRefresher = mainBinding.HomeScreenSwipeRefresh;
        swipeRefresher.setOnRefreshListener(() -> {
            if (hasNetworkConnection()) {
                Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
            else {
                doDownload();
            }
            swipeRefresher.setRefreshing(false);
        });

        sourceDrawerLayout = mainBinding.SourcesDrawer;
        sourceDrawerList = mainBinding.leftDrawer;
        sourceDrawerList.setOnItemClickListener((parent, view, position, id) -> selectSource(position));
        sourceDrawerToggle = new ActionBarDrawerToggle(this, sourceDrawerLayout, R.string.drawer_open, R.string.drawer_close);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (hasNetworkConnection()) {  // verifying if there's an internet connection
            this.setTitle("News Gateway");
            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        this.setTitle("News Gateway");
        doDownload();
        super.onCreate(savedInstanceState);
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo == null || !networkInfo.isConnectedOrConnecting());
    }

    private void doDownload() {
        NewsAggVolley.downloadNewsSources(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        sourceDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        sourceDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (sourceDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: sourceDrawerToggle " + item);
            return true;
        }
        createDrawer(this.newsSourceList, String.valueOf(item.getTitle()));
        return super.onOptionsItemSelected(item);
    }

    public void createMenu(LinkedHashSet<String> ntList) {
        menu.clear();
        ArrayList<String> newsTopicList = new ArrayList<>(ntList);
        Collections.sort(newsTopicList); // sorts news topics in alphabetical order
        menu.add(0, 0,0, "All");
        for (int i = 0; i < newsTopicList.size(); i++) {
            SpannableString topic = new SpannableString(newsTopicList.get(i));
            topic.setSpan(new ForegroundColorSpan(menuColors[i]), 0, topic.length(), 0);
            menu.add(0, i + 1, i + 1, topic);
        }
    }

    public void createDrawer(List<Source> newsSourceList, String topic) {

        if (this.newsSourceList.size() == 0) {
            this.newsSourceList.addAll(newsSourceList);  // all news sources from volley are added here to ArrayList
        }
        newsSourceNames.clear();
        if (topic.equals("All")) {
            SpannableString category;
            for (int i = 0; i < this.newsSourceList.size(); i++) {
                switch (newsSourceList.get(i).getSourceCategory()) {
                    case "general":
                        category = new SpannableString(newsSourceList.get(i).getSourceName());
                        category.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.honey_yellow)), 0, category.length(), 0);
                        newsSourceNames.add(category);
                        break;
                    case "sports":
                        category = new SpannableString(newsSourceList.get(i).getSourceName());
                        category.setSpan(new ForegroundColorSpan(Color.BLUE), 0, category.length(), 0);
                        newsSourceNames.add(category);
                        break;
                    case "business":
                        category = new SpannableString(newsSourceList.get(i).getSourceName());
                        category.setSpan(new ForegroundColorSpan(Color.GREEN), 0, category.length(), 0);
                        newsSourceNames.add(category);
                        break;
                    case "entertainment":
                        category = new SpannableString(newsSourceList.get(i).getSourceName());
                        category.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.orange)), 0, category.length(), 0);
                        newsSourceNames.add(category);
                        break;
                    case "science":
                        category = new SpannableString(newsSourceList.get(i).getSourceName());
                        category.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.violet)), 0, category.length(), 0);
                        newsSourceNames.add(category);
                        break;
                    case "health":
                        category = new SpannableString(newsSourceList.get(i).getSourceName());
                        category.setSpan(new ForegroundColorSpan(Color.RED), 0, category.length(), 0);
                        newsSourceNames.add(category);
                        break;
                    case "technology":
                        category = new SpannableString(newsSourceList.get(i).getSourceName());
                        category.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, category.length(), 0);
                        newsSourceNames.add(category);
                        break;
                }
            }
        } else {
            SpannableString category;
            switch (topic) {
                case "general":
                    for (int i = 0; i < this.newsSourceList.size(); i++) {
                        if (newsSourceList.get(i).getSourceCategory().equals("general")) {
                            category = new SpannableString(newsSourceList.get(i).getSourceName());
                            category.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.honey_yellow)), 0, category.length(), 0);
                            newsSourceNames.add(category);
                        }
                    }
                    break;
                case "sports":
                    for (int i = 0; i < this.newsSourceList.size(); i++) {
                        if (newsSourceList.get(i).getSourceCategory().equals("sports")) {
                            category = new SpannableString(newsSourceList.get(i).getSourceName());
                            category.setSpan(new ForegroundColorSpan(Color.BLUE), 0, category.length(), 0);
                            newsSourceNames.add(category);
                        }
                    }
                    break;
                case "business":
                    for (int i = 0; i < this.newsSourceList.size(); i++) {
                        if (newsSourceList.get(i).getSourceCategory().equals("business")) {
                            category = new SpannableString(newsSourceList.get(i).getSourceName());
                            category.setSpan(new ForegroundColorSpan(Color.GREEN), 0, category.length(), 0);
                            newsSourceNames.add(category);
                        }
                    }
                    break;
                case "entertainment":
                    for (int i = 0; i < this.newsSourceList.size(); i++) {
                        if (newsSourceList.get(i).getSourceCategory().equals("entertainment")) {
                            category = new SpannableString(newsSourceList.get(i).getSourceName());
                            category.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.orange)), 0, category.length(), 0);
                            newsSourceNames.add(category);
                        }
                    }
                    break;
                case "science":
                    for (int i = 0; i < this.newsSourceList.size(); i++) {
                        if (newsSourceList.get(i).getSourceCategory().equals("science")) {
                            category = new SpannableString(newsSourceList.get(i).getSourceName());
                            category.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.violet)), 0, category.length(), 0);
                            newsSourceNames.add(category);
                        }
                    }
                    break;
                case "health":
                    for (int i = 0; i < this.newsSourceList.size(); i++) {
                        if (newsSourceList.get(i).getSourceCategory().equals("health")) {
                            category = new SpannableString(newsSourceList.get(i).getSourceName());
                            category.setSpan(new ForegroundColorSpan(Color.RED), 0, category.length(), 0);
                            newsSourceNames.add(category);
                        }
                    }
                    break;
                case "technology":
                    for (int i = 0; i < this.newsSourceList.size(); i++) {
                        if (newsSourceList.get(i).getSourceCategory().equals("technology")) {
                            category = new SpannableString(newsSourceList.get(i).getSourceName());
                            category.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, category.length(), 0);
                            newsSourceNames.add(category);
                        }
                    }
                    break;
            }
        }
        sourceDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, newsSourceNames));
        this.setTitle("News Gateway " + "(" + this.newsSourceNames.size() + ")");
    }

    private void selectSource(int position) {
        String sourceID = "";
        String chosenSource = String.valueOf(newsSourceNames.get(position));
        for (int i = 0; i < newsSourceList.size(); i++) {
            if (chosenSource.equals(newsSourceList.get(i).getSourceName())) {
                sourceID = newsSourceList.get(i).getSourceID();
                break;
            }
        }
        Intent articleAct = new Intent(this, ArticlesActivity.class);
        articleAct.putExtra("SOURCE_ID", sourceID);
        articleAct.putExtra("CHOSEN_SOURCE", chosenSource);
        articleAct.putExtra("SOURCE_NAMES", newsSourceNames);
        articleAct.putExtra("SOURCES", newsSourceList);
        startActivity(articleAct);
        sourceDrawerLayout.closeDrawer(sourceDrawerList); // closes the drawer automatically
    }

    public void downloadFailed() {
        newsSourceList.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Start application");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Resume application");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Application paused");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Application stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Application terminated");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: Application restarted");
    }
}