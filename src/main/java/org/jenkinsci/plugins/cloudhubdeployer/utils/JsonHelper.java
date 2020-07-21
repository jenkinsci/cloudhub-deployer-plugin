package org.jenkinsci.plugins.cloudhubdeployer.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jenkinsci.plugins.cloudhubdeployer.exception.CloudHubRequestException;


public final class JsonHelper {

    public static boolean checkIfKeyExists(String response, String key) {
        return new Gson().fromJson(response, JsonObject.class).has(key);
    }

    public static String parseAccessToken(String loginResponse){
        return new Gson().fromJson(loginResponse, JsonObject.class).get(Constants.JSON_KEY_ACCESS_TOKEN).getAsString();
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

    public static JsonArray getenvList(String response) throws CloudHubRequestException {

        if(!checkIfKeyExists(response,"data"))
            throw new CloudHubRequestException("No environment exists with given name or id");

        return new Gson().fromJson(response, JsonObject.class).get("data").getAsJsonArray();
    }

    public static String verifyOrGetEnvId(String response, String envIdOrName) throws CloudHubRequestException {
        JsonArray data = getenvList(response);

        for (JsonElement jsonElement : data) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String id = jsonObject.get("id").getAsString().toLowerCase();
            String name = jsonObject.get("name").getAsString().toLowerCase();

            if(id.equals(envIdOrName.toLowerCase()) || name.equals(envIdOrName.toLowerCase())){
                return id;
            }

        }

        return null;
    }
}
