package com.vnpt.tnvn.refresh_token.model;

import com.google.gson.annotations.SerializedName;

public class RefreshTokenResponse {
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("refresh_token")
    public String refreshToken;
    @SerializedName("scope")
    public String scope;
    @SerializedName("id_token")
    public String idToken;
    @SerializedName("token_type")
    public String tokenType;
    @SerializedName("expires_in")
    public String timeExpire;
}