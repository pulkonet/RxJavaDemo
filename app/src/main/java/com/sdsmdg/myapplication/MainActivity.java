package com.sdsmdg.myapplication;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    Button btnSearch;
    EditText editText;
    TextView tvName, tvBio, tvfollow;
    ImageView profileImage;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSearch = (Button) findViewById(R.id.btn);
        editText = (EditText) findViewById(R.id.et);
        tvName = (TextView) findViewById(R.id.tv);
        tvBio = (TextView) findViewById(R.id.tvBio);
        tvfollow = (TextView) findViewById(R.id.tvFollowers);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("https://api.github.com/")
                        .build();
                final GithubAPI githubAPI = retrofit.create(GithubAPI.class);
                Subscriber<UserModel> subscriber = new Subscriber<UserModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserModel userModel) {
                        tvName.setText(userModel.getName());
                        tvBio.setText(userModel.getBio());
                        tvfollow.setText("Followers: " + userModel.getFollowers() + " Following: " + userModel.getFollowing());
                        Picasso.with(getApplicationContext()).load(userModel.getAvatarUrl()).into(profileImage);
                    }
                };

                Observable<UserModel> githubUser = githubAPI.getInfo(editText.getText().toString());
                githubUser.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);

            }
        });
    }
}