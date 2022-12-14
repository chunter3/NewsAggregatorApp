package com.example.newsaggregatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.newsaggregatorapp.databinding.ActivityArticlesBinding;


import java.util.ArrayList;
import java.util.List;

public class ArticlesActivity extends AppCompatActivity {

    private ActivityArticlesBinding articleBinding;
    private ViewPager2 articleViewPager;
    private DrawerLayout sourceDrawerLayout;
    private ListView sourceDrawerList;
    private ActionBarDrawerToggle sourceDrawerToggle;
    private ArticleAdapter articleAdapter;
    private String sourceID = "";
    private List<String> sourceNames = new ArrayList<>();
    private List<Source> sourcesList = new ArrayList<>();
    private final List<Article> articleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        articleBinding = ActivityArticlesBinding.inflate(getLayoutInflater());
        setContentView(articleBinding.getRoot());

        articleViewPager = articleBinding.articleViewPager;

        articleAdapter = new ArticleAdapter(articleList, this);
        articleViewPager.setAdapter(articleAdapter);
        articleViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        sourceDrawerLayout = articleBinding.SourcesDrawer;
        sourceDrawerList = articleBinding.leftDrawer;
        sourceDrawerList.setOnItemClickListener((parent, view, position, id) -> selectSource(position));
        sourceDrawerToggle = new ActionBarDrawerToggle(this, sourceDrawerLayout, R.string.drawer_open, R.string.drawer_close);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        Intent articleInfo = getIntent();

        if (articleInfo.hasExtra("SOURCE_ID")) {
            sourceID = articleInfo.getStringExtra("SOURCE_ID");
        }
        String chosenSource = "News Gateway";
        if (articleInfo.hasExtra("CHOSEN_SOURCE")) {
            chosenSource = articleInfo.getStringExtra("CHOSEN_SOURCE");
        }
        if (articleInfo.hasExtra("SOURCE_NAMES")) {
            sourceNames.addAll(articleInfo.getStringArrayListExtra("SOURCE_NAMES"));
        }
        if (articleInfo.hasExtra("SOURCES")) {
            sourcesList.addAll(articleInfo.getParcelableArrayListExtra("SOURCES"));
        }

        this.setTitle(chosenSource);
        sourceDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, sourceNames));
        doDownload(sourceID);

        super.onCreate(savedInstanceState);
    }

    private void doDownload(String sourceID) {
        NewsAggVolley.downloadNewsArticles(this, sourceID);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        sourceDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        sourceDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (sourceDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateActivityScreen(ArrayList<Article> newsArticleList) {
        if (newsArticleList == null) {
            Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
            return;
        }
        articleList.clear();
        articleAdapter.notifyDataSetChanged();
        articleList.addAll(newsArticleList);
        articleAdapter.notifyItemRangeInserted(0, articleList.size());
        articleViewPager.setCurrentItem(0, false);
    }

    private void selectSource(int position) {
        String sourceID = "";
        String chosenSource = String.valueOf(sourceNames.get(position));
        for (int i = 0; i < sourcesList.size(); i++) {
            if (chosenSource.equals(sourcesList.get(i).getSourceName())) {
                sourceID = sourcesList.get(i).getSourceID();
                break;
            }
        }
        this.setTitle(chosenSource);
        doDownload(sourceID);
        sourceDrawerLayout.closeDrawer(sourceDrawerList);
    }

    public void downloadFailed() {
        articleList.clear();
        articleAdapter.notifyItemRangeChanged(0, articleList.size());

    }
}