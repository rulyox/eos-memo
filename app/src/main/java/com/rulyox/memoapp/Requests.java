package com.rulyox.memoapp;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class Requests {

    private static String url = "http://13.124.175.163/memo";

    static String getMemo() {

        try {

            OkHttpClient client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client
                    .newCall(request)
                    .execute();

            String result =  response.body().string();

            response.close();

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

            OkHttpClient client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(jsonString, MediaType.parse("application/json")))
                    .build();

            Response response = client
                    .newCall(request)
                    .execute();

            String result = response.body().string();

            response.close();

            return result;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    static String editMemo(int id, String title, String text, String date) {

        try {

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("id", id);
            jsonObject.put("title", title);
            jsonObject.put("text", text);
            jsonObject.put("date", date);

            String jsonString = jsonObject.toString();

            OkHttpClient client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .put(RequestBody.create(jsonString, MediaType.parse("application/json")))
                    .build();

            Response response = client
                    .newCall(request)
                    .execute();

            String result = response.body().string();

            response.close();

            return result;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    static String deleteMemo(int id) {

        try {

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("id", id);

            String jsonString = jsonObject.toString();

            OkHttpClient client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .delete(RequestBody.create(jsonString, MediaType.parse("application/json")))
                    .build();

            Response response = client
                    .newCall(request)
                    .execute();

            String result = response.body().string();

            response.close();

            return result;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;

    }

}
