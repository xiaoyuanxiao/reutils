package com.burning.reutils;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Interceptor;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by burning on 2017/11/2.
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * -------------------------//┏┓　　　┏┓
 * -------------------------//┏┛┻━━━┛┻┓
 * -------------------------//┃　　　　　　　┃
 * -------------------------//┃　　　━　　　┃
 * -------------------------//┃　┳┛　┗┳　┃
 * -------------------------//┃　　　　　　　┃
 * -------------------------//┃　　　┻　　　┃
 * -------------------------//┃　　　　　　　┃
 * -------------------------//┗━┓　　　┏━┛
 * -------------------------//┃　　　┃  神兽保佑
 * -------------------------//┃　　　┃  代码无BUG！
 * -------------------------//┃　　　┗━━━┓
 * -------------------------//┃　　　　　　　┣┓
 * -------------------------//┃　　　　　　　┏┛
 * -------------------------//┗┓┓┏━┳┓┏┛
 * -------------------------// ┃┫┫　┃┫┫
 * -------------------------// ┗┻┛　┗┻┛
 */

public class DownLoadAPI {
    static DownLoadAPI downLoadAPI;

    private DownLoadAPI() {
        super();
    }

    static synchronized DownLoadAPI newInstans() {
        if (downLoadAPI == null)
            downLoadAPI = new DownLoadAPI();
        return downLoadAPI;
    }

    Subscription download(final String url, final File file, final DownloadProgressListener interceptor) {

        return download(new MysubCribe<ResponseBody, DownLoadService>() {
            @Override
            public Observable getObservable(DownLoadService retrofit) {
                return retrofit.downLoad("bytes=" + file.length() + "-", url);
            }

            @Override
            public void onCompleted() {

                //   System.out.println("onCompleted============");
            }

            @Override
            public void onError(Throwable e) {
                interceptor.onFail(e);
                // System.out.println("Throwable=====Throwable====" + e.toString());
            }

            @Override
            public void onNext(ResponseBody o) {
                try {
                    writeCache(o, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, new DownloadInterceptor(interceptor));

    }

    public Subscription downloadOnthis(final String url, final File file, final DownloadProgressListener interceptor) {
        return downloadOnthis(new MysubCribe<ResponseBody, DownLoadService>() {
            @Override
            public Observable getObservable(DownLoadService retrofit) {
                return retrofit.downLoad("bytes=" + file.length() + "-", url);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                interceptor.onFail(e);
            }

            @Override
            public void onNext(ResponseBody o) {
                try {
                    writeCache(o, file);
                } catch (Exception e) {
                    System.out.println("==onNext==" + e.toString());
                }
            }
        }, new DownloadInterceptor(interceptor));

    }

    public void writeCache(ResponseBody responseBody, File file) throws IOException {
        RandomAccessFile ac = new RandomAccessFile(file, "rw");//rw  读写 r 只读
        InputStream inputStream = responseBody.byteStream();
        byte[] fileReader = new byte[4096];
        ac.seek(file.length());
        while (true) {
            int read = inputStream.read(fileReader);
            if (read == -1) {
                break;
            }
            ac.write(fileReader, 0, read);
        }
        inputStream.close();
        ac.close();
    }

    private Subscription download(MysubCribe frext, Interceptor interceptor) {
        ReHttpUtils instans = ReHttpUtils.instans();
        Retrofit retrofit = instans.creatRetrofit(instans.addInterceptor(interceptor));
        return frext.getObservable(retrofit.create(frext.getTempalteType()))
                .subscribeOn(Schedulers.io())
                .subscribe(frext);
    }

    /**
     * 运行在当前线程下
     *
     * @param frext
     * @param interceptor
     * @return
     */
    private Subscription downloadOnthis(MysubCribe frext, Interceptor interceptor) {
        ReHttpUtils instans = ReHttpUtils.instans();
        Retrofit retrofit = instans.creatRetrofit(instans.addInterceptor(interceptor));
        return frext.getObservable(retrofit.create(frext.getTempalteType()))
                .subscribe(frext);
    }
}
