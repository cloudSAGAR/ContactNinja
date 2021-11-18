package com.intricare.test.retrofit;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
