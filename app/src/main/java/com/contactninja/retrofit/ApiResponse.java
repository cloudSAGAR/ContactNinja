package com.contactninja.retrofit;

import android.annotation.SuppressLint;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@SuppressLint("UnknownNullness")

public class ApiResponse {

    @SerializedName("http_status")
    @Expose
    private Integer http_status=0;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("data")
    @Expose
    private JsonElement data;


    @SerializedName("result")
    private JsonElement Result;

  /*  @SerializedName("NEWDATA")
    public Object data;*/

    // for both sample1.json and sample2.json

   /* public static class MyJsonDeserializer implements JsonDeserializer<ApiResponse> {
        @Override
        public ApiResponse deserialize(JsonElement json, Type typeOfT,
                                       JsonDeserializationContext context) throws JsonParseException {

            ApiResponse result = new Gson().fromJson(json, ApiResponse.class);

            JsonObject jsonObject = json.getAsJsonObject();
            if (jsonObject.has("result")) {
                JsonElement ele = jsonObject.get("result");
                if (ele != null && !ele.isJsonNull()) {
                    if (ele.isJsonArray()) {
                        result.data = ele.getAsJsonArray();
                    } else if (ele.isJsonObject()) {
                        result.data = ele.getAsJsonObject();
                    } else {
                        result.data = ele.getAsString();
                    }
                }
            }
            return result;
        }
    }*/

    public Integer getHttp_status() {
        return http_status;
    }

    public void setHttp_status(Integer http_status) {
        this.http_status = http_status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    public JsonElement getResult() {
        return Result;
    }

    public void setResult(JsonElement result) {
        Result = result;
    }


}
