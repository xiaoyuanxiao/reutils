package com.burning.reutils;

import java.io.File;

import rx.Subscription;

/**
 * Created by burning on 2017/11/6.
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

public class DownloadUtils {
    public static Subscription download(String url, File file, final DownloadProgressListener downloadInterceptor) {
        DownLoadAPI downLoadAPI = DownLoadAPI.newInstans();
        final long length = file.length();//上次下载位置
        // 这里直接是读取 文件的长度 如果不是之前下载的文件 会导致直接写入
        // 从而变成不是要下载的文件 请做好自身处理
        DownloadProgressListener downloadInterceptor1 = new DownloadProgressListener() {
            @Override
            public void update(long read, long count, boolean done) {
                if (downloadInterceptor != null)
                    downloadInterceptor.update(read + length, count + length, done);
            }

            @Override
            public void onFail(Throwable e) {
                if (downloadInterceptor != null)
                    downloadInterceptor.onFail(e);
            }
        };
        return downLoadAPI.download(url, file, downloadInterceptor1);
    }

    public static Subscription downloadOnthis(String url, File file, final DownloadProgressListener downloadInterceptor) {
        DownLoadAPI downLoadAPI = DownLoadAPI.newInstans();
        final long length = file.length();//上次下载位置
        DownloadProgressListener downloadInterceptor1 = new DownloadProgressListener() {
            @Override
            public void update(long read, long count, boolean done) {
                if (downloadInterceptor != null)
                    downloadInterceptor.update(read + length, count + length, done);
            }

            @Override
            public void onFail(Throwable e) {
                if (downloadInterceptor != null)
                    downloadInterceptor.onFail(e);
            }
        };
        return downLoadAPI.downloadOnthis(url, file, downloadInterceptor1);
    }
}
