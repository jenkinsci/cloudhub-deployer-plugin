package org.jenkinsci.plugins.cloudhubdeployer.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public final class JsonHelper {

    public static boolean checkIfKeyExists(String response, String key) {
        return new Gson().fromJson(response, JsonObject.class).has(key);
    }

    public static String parseAccessToken(String loginResponse){
        return new Gson().fromJson(loginResponse, JsonObject.class).get(Constants.LABEL_ACCESS_TOKEN).getAsString();
    }

    public static boolean checkIfApiExists(String response, String domainName) {

        JsonArray array = new Gson().fromJson(response,JsonArray.class);

        for (JsonElement jsonElement : array) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if(jsonObject.get(Constants.LABEL_API_DOMAIN).getAsString().equals(domainName)){
                return true;
            }
        }
        return false;
    }

    private JsonHelper() {
        // hide constructor
    }
}
