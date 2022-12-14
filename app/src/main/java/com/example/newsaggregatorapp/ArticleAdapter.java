package com.example.newsaggregatorapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleHolder> {

    private final List<Article> articleList;
    private final ArticlesActivity articleAct;


    public ArticleAdapter(List<Article> articleLst, ArticlesActivity aa) {
        articleList = articleLst;
        articleAct = aa;
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_entry, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        Article article = articleList.get(position);

        holder.articleTitle.setText(article.getTitle());
        holder.articleDate.setText(article.getPublishDate());
        holder.articleAuthor.setText(article.getAuthor());

        holder.articleText.setText(article.getDescription());
        holder.articleText.setMovementMethod(new ScrollingMovementMethod());

        holder.articleCount.setText(String.format(Locale.US, "%d of %d", position + 1, getItemCount()));
        if (article.getImageURL().isEmpty()) {
            Glide.with(articleAct).load(R.drawable.noimage).placeholder(R.drawable.loading).error(R.drawable.brokenimage).into(holder.articleImage);
        }
        else {
            Glide.with(articleAct).load(article.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.brokenimage).into(holder.articleImage);
        }
        holder.articleImage.setOnClickListener(view -> {
            if (article.getUrl().isEmpty()) {
                return;
            }
            Uri articleUri = Uri.parse(article.getUrl());
            Intent uriArticleIntent = new Intent(Intent.ACTION_VIEW, articleUri);
            articleAct.startActivity(uriArticleIntent);
        });

        holder.articleTitle.setOnClickListener(view -> {
            if (article.getUrl().isEmpty()) {
                return;
            }
            Uri articleUri = Uri.parse(article.getUrl());
            Intent uriArticleIntent = new Intent(Intent.ACTION_VIEW, articleUri);
            articleAct.startActivity(uriArticleIntent);
        });

        holder.articleText.setOnClickListener(view -> {
            if (article.getUrl().isEmpty()) {
                return;
            }
            Uri articleUri = Uri.parse(article.getUrl());
            Intent uriArticleIntent = new Intent(Intent.ACTION_VIEW, articleUri);
            articleAct.startActivity(uriArticleIntent);
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
