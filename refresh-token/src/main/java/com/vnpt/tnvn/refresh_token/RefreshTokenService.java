package com.vnpt.tnvn.refresh_token;

import com.google.gson.GsonBuilder;
import com.vnpt.tnvn.refresh_token.common.Constants;
import com.vnpt.tnvn.refresh_token.model.RefreshTokenResponse;
import com.vnpt.tnvn.refresh_token.model.Token;
import com.vnpt.tnvn.refresh_token.network.ApiService;
import com.vnpt.tnvn.refresh_token.network.ClientService;
import okhttp3.OkHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService extends ClientService {

    @Autowired
    private RefreshTokenConfiguration configuration;
    @Autowired
    @Qualifier(value = Constants.Bean.REFRESH_TOKEN_API_SERVICE)
    private ApiService apiService;
    @Autowired
    private TokenRepository tokenRepository;

    private boolean isRefreshing;

    public void refresh() {
        Logger.getLogger(getClass()).info("isRefreshing=" + isRefreshing);
        if (isRefreshing) return;
        List<Token> tokens = tokenRepository.findTokensByRefreshTokenNotNull();
        if (tokens.isEmpty()) return;
        isRefreshing = true;
        int[] count = new int[1];
        for (Token token : tokens) {
            Map<String, String> params = new HashMap<>();
            params.put("grant_type", "refresh_token");
            params.put("refresh_token", token.refreshToken);
            params.put("client_id", configuration.getClientId());
            params.put("client_secret", configuration.getClientSecret());
            doPostFormUrlEncoded(
                    apiService,
                    configuration.getBaseUrl() + configuration.getRefreshApi(),
                    params,
                    RefreshTokenResponse.class,
                    data -> {
                        if (data != null) {
                            token.idToken = data.idToken;
                            token.accessToken = data.accessToken;
                            token.refreshToken = data.refreshToken;
                            tokenRepository.save(token);
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                Logger.getLogger(getClass()).error("refresh: " + e.getMessage());
                            }
                        }
                        isRefreshing = !(++count[0] == tokens.size());
                    }
            );
        }
    }

    @Bean(name = Constants.Bean.REFRESH_TOKEN_API_SERVICE)
    public ApiService initUpdateTokenApiService() {
        return new Retrofit.Builder().baseUrl(configuration.getBaseUrl())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build()
                .create(ApiService.class);
    }
}
