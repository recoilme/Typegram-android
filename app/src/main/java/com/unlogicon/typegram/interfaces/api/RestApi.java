package com.unlogicon.typegram.interfaces.api;

import com.unlogicon.typegram.models.Article;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Nikita Korovkin 09.10.2018.
 */
public interface RestApi {

    /**
     * Получаем список статей, первые 20
     * @param p - id плоследней статьи для загрузки следующей партии
     * @return
     */
    @GET("mid")
    Observable<List<Article>> getArticles(
            @Query("p") int p
    );

    /**
     * Получить статью по id
     * @param user - имя юзера
     * @param id - id статьи
     * @return
     */
    @GET("@{user}/{id}")
    Observable<Article> getArticle(
            @Path("user") String user,
            @Path("id") int id
    );

}
