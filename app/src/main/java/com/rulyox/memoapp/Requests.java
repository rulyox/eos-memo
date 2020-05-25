package com.rulyox.memoapp;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class Requests {

    private static String url = "http://13.124.175.163/memo";
    private static OkHttpClient client = new OkHttpClient();

    static String getMemo() {

        try {

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client
                    .newCall(request)
                    .execute();

            String result = response.body().string();

            return result;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    static String addMemo(String title, String text, String date) {

        try {

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("title", title);
            jsonObject.put("text", text);
            jsonObject.put("date", date);

            String jsonString = jsonObject.toString();

            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(jsonString, MediaType.parse("application/json")))
                    .build();

            Response response = client
                    .newCall(request)
                    .execute();

            String result = response.body().string();

            return result;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;

    }

}
