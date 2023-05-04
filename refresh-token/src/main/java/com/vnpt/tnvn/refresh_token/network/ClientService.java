package com.vnpt.tnvn.refresh_token.network;

import com.google.gson.Gson;
import okhttp3.ResponseBody;
import org.apache.log4j.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;

public abstract class ClientService {

    protected interface Listener<T> {
        void onFinished(T data);
    }

    protected <T> void doGetRequest(ApiService apiService, String request, Class<T> type, Listener<T> listener) {
        apiService.doGet(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (listener != null && response.body() != null) {
                    try {
                        String responseData = response.body().string();
                        Logger.getLogger(getClass()).info("#doGetRequest#onResponse: request=\n" + request + "\nresponse=\n" + responseData);
                        listener.onFinished(new Gson().fromJson(responseData, type));
                    } catch (IOException e) {
                        listener.onFinished(null);
                        Logger.getLogger(getClass()).error("#doGetRequest#onResponse: " + e.getMessage());
                    }
                }
                if (response.errorBody() != null) {
                    Logger.getLogger(getClass()).info("#doGetRequest#errorBody: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Logger.getLogger(getClass()).error("#doGetRequest#onFailure: " + t.getMessage());
            }
        });
    }

    protected <T> void doPostFormUrlEncoded(ApiService apiService, String url, Map<String, String> params, Class<T> type, Listener<T> listener) {
        apiService.doPostFormUrlEncoded(url, params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (listener != null) {
                    if (response.body() != null) {
                        try {
                            listener.onFinished(new Gson().fromJson(response.body().string(), type));
                        } catch (IOException e) {
                            listener.onFinished(null);
                            Logger.getLogger(getClass()).error("doPostFormUrlEncoded: " + e.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}
