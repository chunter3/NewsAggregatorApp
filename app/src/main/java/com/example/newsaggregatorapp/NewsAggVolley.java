package com.example.newsaggregatorapp;

import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class NewsAggVolley {

    private static RequestQueue sourceQueue;
    private static RequestQueue articleQueue;
    private static ArrayList<Source> newsSourceList = new ArrayList<>();
    private static ArrayList<Article> newsArticleList = new ArrayList<>();
    private static LinkedHashSet<String> newsTopicList = new LinkedHashSet<>(); // LinkedHashSet will ensure no duplicate news topics are in newsTopicList
    private static final String newsSourcesURL = "https://newsapi.org/v2/sources";
    private static final String newsArticlesURL = "https://newsapi.org/v2/top-headlines";
    private static final String myAPIKey = "f1c984502790416c89bd6c971b74fab8";
    private static final String TAG = "NewsAggVolley";

    public static void downloadNewsSources(MainActivity mainActivityIn) {

        sourceQueue = Volley.newRequestQueue(mainActivityIn);

        Uri.Builder buildURL = Uri.parse(newsSourcesURL).buildUpon();
        buildURL.appendQueryParameter("apiKey", myAPIKey);
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener = response -> handleResultMain(mainActivityIn, response.toString());

        Response.ErrorListener error = error1 -> handleResultMain(mainActivityIn, null);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlToUse, null, listener, error) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "News-App");
                return headers;
            }
        };
        sourceQueue.add(jsonObjectRequest);
    }

    public static void downloadNewsArticles(ArticlesActivity articlesActivityIn, String newsSource) {

        articleQueue = Volley.newRequestQueue(articlesActivityIn);

        Uri.Builder buildURL = Uri.parse(newsArticlesURL).buildUpon();
        buildURL.appendQueryParameter("sources", newsSource);
        buildURL.appendQueryParameter("apiKey", myAPIKey);
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener = response -> handleResultArticles(articlesActivityIn, response.toString());

        Response.ErrorListener error = error1 -> handleResultArticles(articlesActivityIn, null);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlToUse, null, listener, error) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "News-App");
                return headers;
            }
        };
        articleQueue.add(jsonObjectRequest);
    }

    private static void handleResultMain(MainActivity mainActivityIn, String response) {
        if (response == null) {
            mainActivityIn.runOnUiThread(mainActivityIn::downloadFailed);
            return;
        }
        final ArrayList<Source> sourceList = parseSourcesJSON(response);
        mainActivityIn.runOnUiThread(() -> mainActivityIn.createMenu(newsTopicList));
        mainActivityIn.runOnUiThread(() -> mainActivityIn.createDrawer(sourceList, "All"));
    }

    private static void handleResultArticles(ArticlesActivity articlesActivityIn, String response) {

        if (response == null) {
            articlesActivityIn.runOnUiThread(articlesActivityIn::downloadFailed);
            return;
        }
        final ArrayList<Article> articleList = parseArticleJSON(response);
        articlesActivityIn.runOnUiThread(() -> articlesActivityIn.updateActivityScreen(articleList));
    }

    private static ArrayList<Source> parseSourcesJSON(String response) {
        try {
            JSONObject jObjMain = new JSONObject(response);

            JSONArray sourcesArray = jObjMain.getJSONArray("sources");

            for (int i = 0; i < sourcesArray.length(); i++) {
                JSONObject sourceObj = sourcesArray.getJSONObject(i);

                String sourceID = sourceObj.getString("id");
                String sourceName = sourceObj.getString("name");
                String sourceCategory = sourceObj.getString("category");

                newsTopicList.add(sourceCategory);
                newsSourceList.add(new Source(sourceID, sourceName, sourceCategory));
            }
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return newsSourceList;
    }

    private static ArrayList<Article> parseArticleJSON(String response) {
        try {
            newsArticleList.clear();

            JSONObject jObjMain = new JSONObject(response);

            JSONArray articlesArray = jObjMain.getJSONArray("articles");

            for (int i = 0; i < articlesArray.length(); i++) {
                JSONObject articleObj = articlesArray.getJSONObject(i);

                String articleAuthor = articleObj.getString("author");
                String articleTitle = articleObj.getString("title");
                String  articleDescription = articleObj.getString("description");
                String articleURL = articleObj.getString("url");
                String articleImageURL = articleObj.getString("urlToImage");
                String articlePubDate = articleObj.getString("publishedAt");

                newsArticleList.add(new Article(articleAuthor, articleTitle, articleDescription, articleURL, articleImageURL, articlePubDate));
            }
        } catch (Exception e) {
            Log.d(TAG, "parseArticleJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return newsArticleList;
    }
}
