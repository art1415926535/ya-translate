package com.art1415926535.ya_translate;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class Translator {
    private static final Translator ourInstance = new Translator();
    private static final String key = "trnsl.1.1.20170414T131406Z.4bd8303ec229922b.266e6ec4b7b55d08e21c70c7ed1a0c004cd3bca7";
    private static final String url = "https://translate.yandex.net/api/v1.5/tr.json/translate";
    private static final OkHttpClient client = new OkHttpClient();

    static Translator getInstance() {
        return ourInstance;
    }

    private Translator() {
    }

    /**
     * Translate text between two languages.
     * @param text text to translate.
     * @param fromLang from what language (name) to translate.
     * @param toLang to what language (name) to translate.
     * @return JSONObject (code, lang, text).
     */
    static JSONObject translateText(String text, String fromLang, String toLang){
        String codes;
        if ( Languages.getCodeByName(fromLang) == null){
            // Automatic language selection.
            codes = Languages.getCodeByName(toLang);
        }else{
            codes = Languages.getCodeByName(fromLang) + "-" + Languages.getCodeByName(toLang);
        }

        return post(text, codes);
    }

    static private JSONObject post(String text, String translateCodes){

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

        urlBuilder.addQueryParameter("key", key);
        urlBuilder.addQueryParameter("lang", translateCodes);

        RequestBody requestBody = new FormBody.Builder()
                .add("text", text)
                .build();

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .post(requestBody)
                .build();

        JSONObject jObject;

        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            jObject = new JSONObject(responseBody);
        } catch (IOException  | JSONException e) {
            return null;
        }
        return jObject;
    }
}
