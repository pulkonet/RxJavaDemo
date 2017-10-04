package com.sdsmdg.myapplication;

import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by pulkonet on 4/10/17.
 */

public interface GithubAPI {
    @GET("users/{username}")
    Observable<UserModel> getInfo(@Path("username") String username);
}
