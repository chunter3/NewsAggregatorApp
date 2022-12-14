package com.example.newsaggregatorapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleHolder extends RecyclerView.ViewHolder {

    TextView articleTitle;
    TextView articleDate;
    TextView articleAuthor;
    TextView articleText;
    TextView articleCount;
    ImageView articleImage;

    public ArticleHolder(@NonNull View itemView) {
        super(itemView);
        articleTitle = itemView.findViewById(R.id.articleTitle);
        articleDate = itemView.findViewById(R.id.articleDate);
        articleAuthor = itemView.findViewById(R.id.articleAuthor);
        articleText = itemView.findViewById(R.id.articleText);
        articleCount = itemView.findViewById(R.id.articleCount);
        articleImage = itemView.findViewById(R.id.articleImage);
    }
}
