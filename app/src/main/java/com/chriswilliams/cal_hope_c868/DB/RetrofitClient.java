package com.chriswilliams.cal_hope_c868.DB;

import android.util.Base64;
import android.util.JsonToken;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String AUTH = "Basic " + Base64.encodeToString(("CalHope_C868:cwi1583").getBytes(), Base64.NO_WRAP);
    private static final String BASE_URL="https://wwilliams.org/chris_wgu/CalHope/public/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;
    private String autToken = null;

    private RetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder()
                        .addHeader("Authorization", AUTH)
                        .method(request.method(), request.body());
                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }


    public static synchronized RetrofitClient getInstance() {
        if(mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

//    class RetrofitDateSerializer implements JsonSerializer<LocalDate> {
//
//        @Override
//        public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
//            if (src == null) {
//                return null;
//            }
//            else {
//                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                String formattedDate = dtf.format(src);
//                return new JsonPrimitive(formattedDate);
//            }
//        }
//    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }
}