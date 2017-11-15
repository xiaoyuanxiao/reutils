package com.burning.reutils;

import com.trello.rxlifecycle.LifecycleProvider;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Burning on 2017/5/27.
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 */


public class ReHttpUtils {
    private static String baseUrl /*= Constant.BASE_RETROFIT_URL*/;
    private static ReHttpUtils reHttpUtils;

    private ReHttpUtils() {
    }

    public synchronized static ReHttpUtils instans() {
        if (reHttpUtils == null)
            reHttpUtils = new ReHttpUtils();
        return reHttpUtils;
    }

    public static void initRetro(String baseUrl) {
        ReHttpUtils.baseUrl = baseUrl;
    }

    public Retrofit creatRetrofit(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加返回值为Oservable<T>的支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    public Retrofit creatRetrofit(String baseUrl, OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加返回值为Oservable<T>的支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }


    public OkHttpClient addInterceptor(Interceptor interceptor) {
        return getMyclient().newBuilder().addInterceptor(interceptor).build();
    }

    OkHttpClient okHttpClient;

    public OkHttpClient addNetworkInterceptor(Interceptor networkInterceptor) {
        return getMyclient().newBuilder().addNetworkInterceptor(networkInterceptor).build();
    }

    public OkHttpClient getMyclient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder.build();
    }

    public Subscription httpRequestMain(MysubCribe frext) {
        Retrofit retrofit = creatRetrofit(ReHttpUtils.baseUrl, getMyclient());
        return frext.getObservable(retrofit.create(frext.getTempalteType())).subscribe(frext);
    }

    public Subscription httpRequestLife(LifecycleProvider lifecycleProvider, MysubCribe frext) {
        Retrofit retrofit = creatRetrofit(ReHttpUtils.baseUrl, getMyclient());
        return frext.getObservable(retrofit.create(frext.getTempalteType()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleProvider.bindToLifecycle())
                .subscribe(frext);
    }

    public Subscription httpRequest(MysubCribe frext) {
        Retrofit retrofit = creatRetrofit(ReHttpUtils.baseUrl, getMyclient());
        return frext.getObservable(retrofit.create(frext.getTempalteType()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(frext);
    }

    public Subscription httpRequestMain(MysubCribe frext, Interceptor interceptor) {
        Retrofit retrofit = creatRetrofit(ReHttpUtils.baseUrl, addInterceptor(interceptor));
        return frext.getObservable(retrofit.create(frext.getTempalteType())).subscribe(frext);
    }

    public Subscription httpRequestLife(LifecycleProvider lifecycleProvider, MysubCribe frext, Interceptor interceptor) {
        Retrofit retrofit = creatRetrofit(ReHttpUtils.baseUrl, addInterceptor(interceptor));
        return frext.getObservable(retrofit.create(frext.getTempalteType()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleProvider.bindToLifecycle())
                .subscribe(frext);
    }

    public Subscription httpRequest(MysubCribe frext, Interceptor interceptor) {
        Retrofit retrofit = creatRetrofit(ReHttpUtils.baseUrl, addInterceptor(interceptor));
        return frext.getObservable(retrofit.create(frext.getTempalteType()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(frext);
    }


}
