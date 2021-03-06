package com.unlogicon.typegram.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuItem;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.unlogicon.typegram.Constants;
import com.unlogicon.typegram.R;
import com.unlogicon.typegram.interfaces.activities.ArticleActivityView;
import com.unlogicon.typegram.presenters.activities.ArticleActivityPresenter;

import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import ru.noties.markwon.SpannableConfiguration;
import ru.noties.markwon.il.AsyncDrawableLoader;
import ru.noties.markwon.il.NetworkSchemeHandler;
import ru.noties.markwon.view.MarkwonViewCompat;

/**
 * Nikita Korovkin 08.10.2018.
 */
public class ArticleActivity extends MvpAppCompatActivity implements ArticleActivityView {

    @InjectPresenter
    ArticleActivityPresenter presenter;

    public static final String ID_EXTRA = "id";
    public static final String USER_EXTRA = "user";

    private MarkwonViewCompat article;

    private ConstraintLayout errorLayout, loadingLayout;
    private AppCompatButton retry;

    private AppCompatTextView title, author, date;

    private AppCompatImageView avatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        article = findViewById(R.id.article);
        title = findViewById(R.id.title);
        avatar = findViewById(R.id.avatar);
        author = findViewById(R.id.author);
        date = findViewById(R.id.date);

        errorLayout = findViewById(R.id.errorLayout);
        loadingLayout = findViewById(R.id.loadingLayout);

        retry = findViewById(R.id.retry);
        retry.setOnClickListener(presenter::onClick);

        presenter.onCreate(getIntent());

    }

    @Override
    public void setErrorLayoutVisibility(int visibility) {
        errorLayout.setVisibility(visibility);
    }

    @Override
    public void setLoadingLayoutVisibility(int visibility) {
        loadingLayout.setVisibility(visibility);
    }

    @Override
    public void setTextArticle(String text) {
        SpannableConfiguration spannableConfiguration = SpannableConfiguration.builder(this)
                .asyncDrawableLoader(AsyncDrawableLoader.builder()
                        .executorService(Executors.newCachedThreadPool())
                        .addSchemeHandler(NetworkSchemeHandler.create(new OkHttpClient()))
                        .build())

                .build();
        article.setMarkdown(spannableConfiguration, text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitleText(String text) {
        title.setText(text);
    }

    @Override
    public void loadAvatar(String author) {
        Glide.with(this)
                .load(Constants.BASE_AVA_URL.replace("%s",author))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(avatar);
    }

    @Override
    public void setAuthor(String author) {
        this.author.setText(author);
    }

    @Override
    public void setDate(String date) {
        this.date.setText(date);
    }

    @Override
    public void openLinkInBrowser(Intent intent) {
        this.startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
