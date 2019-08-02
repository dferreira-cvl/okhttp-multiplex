package com.codavel.okhttp_multiplex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_REQS = 5;
    private static final String URL = "http-multiplexing.codavel.com/img.jpg";
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";

    private static int REQS_COMPLETED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.http1_single_conn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                http1SingleConnTransfers();
            }
        });

        findViewById(R.id.http1_connpool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                http1ConnPoolTransfers();
            }
        });

        findViewById(R.id.regular).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regularTransfers();
            }
        });

        findViewById(R.id.multiplex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiplexTransfers();
            }
        });
    }

    void http1SingleConnTransfers() {
        Log.d("info", "http1SingleConnTransfers");

        REQS_COMPLETED = 0;

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);
        dispatcher.setMaxRequestsPerHost(1);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.dispatcher(dispatcher);

        final OkHttpClient client = builder.build();

        for (int i = 0; i < MAX_REQS; i++) {
            Request get = new Request.Builder()
                    .url(HTTP + URL)
                    .build();

            client.newCall(get).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        ResponseBody responseBody = response.body();
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        }

                        responseBody.string();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    REQS_COMPLETED++;
                    if (REQS_COMPLETED == MAX_REQS) {
                        client.connectionPool().evictAll();
                    }
                }
            });
        }
    }

    void http1ConnPoolTransfers() {
        Log.d("info", "http1ConnPoolTransfers");

        REQS_COMPLETED = 0;
        final OkHttpClient client = new OkHttpClient();

        for (int i = 0; i < MAX_REQS; i++) {
            Request get = new Request.Builder()
                    .url(HTTP + URL)
                    .build();

            client.newCall(get).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        ResponseBody responseBody = response.body();
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        }

                        responseBody.string();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    REQS_COMPLETED++;
                    if (REQS_COMPLETED == MAX_REQS) {
                        client.connectionPool().evictAll();
                    }
                }
            });
        }
    }

    void regularTransfers() {
        Log.d("info", "regularTransfers");

        for (int i = 0; i < MAX_REQS; i++) {
            final OkHttpClient client = new OkHttpClient();

            Request get = new Request.Builder()
                    .url(HTTPS + URL)
                    .build();

            client.newCall(get).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        ResponseBody responseBody = response.body();
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        }

                        responseBody.string();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    client.connectionPool().evictAll();
                }
            });
        }
    }

    void multiplexTransfers() {
        Log.d("info", "multiplexTransfers");

        REQS_COMPLETED = 0;
        final OkHttpClient client = new OkHttpClient();

        for (int i = 0; i < MAX_REQS; i++) {
            Request get = new Request.Builder()
                    .url(HTTPS + URL)
                    .build();

            client.newCall(get).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        ResponseBody responseBody = response.body();
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        }

                        responseBody.string();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    REQS_COMPLETED++;
                    if (REQS_COMPLETED == MAX_REQS) {
                        client.connectionPool().evictAll();
                    }
                }
            });
        }
    }
}
