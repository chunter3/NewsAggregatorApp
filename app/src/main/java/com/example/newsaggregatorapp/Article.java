package com.example.newsaggregatorapp;

import android.os.Build;

import androidx.annotation.RequiresApi;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Article {

    private final String author;
    private final String title;
    private final String description;
    private final String url;
    private final String imageURL;
    private String publishDate;

    // Article obj holds info regarding a particular news article from a news source
    public Article(String author, String title, String description, String url, String imageURL, String publishDate) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.imageURL = imageURL;
        this.publishDate = publishDate;
    }

    public String getAuthor() {
        if (author == null || author.isEmpty() || author.equals("null")) {
            return "";
        }
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getImageURL() {
        return imageURL;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getPublishDate() {
        if (publishDate == null || publishDate.isEmpty()) {
            return "";
        }
        return String.valueOf(ZonedDateTime.parse(publishDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME).format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm").withLocale(Locale.US)));
    }
}