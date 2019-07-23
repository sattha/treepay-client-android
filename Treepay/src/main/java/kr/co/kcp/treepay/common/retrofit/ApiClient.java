package kr.co.kcp.treepay.common.retrofit;

import android.os.Build;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import kr.co.kcp.treepay.common.BuildConfig;

/**
 * Created by jhlee on 2017. 6. 8..
 */

public class ApiClient
{

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {

            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
            okHttpBuilder.connectTimeout(60, TimeUnit.SECONDS);
            okHttpBuilder.readTimeout(60, TimeUnit.SECONDS);

            // Android Lollipop 이하 버전에서 서버 통신 시 hanshake 오류가 발생하여 TLS1.0 으로 지정
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_0)
                        .allEnabledCipherSuites()
                        .build();

                okHttpBuilder.connectionSpecs(Collections.singletonList(spec));
            }

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = okHttpBuilder.addInterceptor(interceptor).build();

            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BuildConfig.TREEPAY_SERVER_HOST_DIRECT).build();
        }
        return retrofit;
    }
}
