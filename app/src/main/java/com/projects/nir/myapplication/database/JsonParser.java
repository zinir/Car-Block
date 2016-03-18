package com.projects.nir.myapplication.database;

import com.projects.nir.myapplication.Entities.BlockedRelation;
import com.projects.nir.myapplication.Entities.User;
import com.projects.nir.myapplication.Entities.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by zilkha on 08/03/2016.
 */
public class JsonParser {

    public static boolean VerifyServerResultOk(String json)
    {
        JSONObject webResult;
        try
        {
            int webInfo = json.indexOf("\n<!-- Hosting24 Analytics Code -->\n");
            json = json.substring(0, webInfo);
            webResult = new JSONObject(json);
            if (webResult.has("status"))
                return  (webResult.getString("status").toUpperCase().equals("OK"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static User ParseUser(String json) {
        String jsonData;
        JSONObject webResult;
        User temp = null;
        if (json != null) {
            if (VerifyServerResultOk(json)) {
                try {
                    int webInfo = json.indexOf("\n<!-- Hosting24 Analytics Code -->\n");
                    json = json.substring(0, webInfo);
                    webResult = new JSONObject(json);

                    if (webResult.has("imageUrl"))
                        temp = new User(Integer.parseInt(webResult.getString("_userId")), webResult.getString("name"), webResult.getString("firstName"), webResult.getString("password"), webResult.getString("imageUrl"));
                    else
                        temp = new User(Integer.parseInt(webResult.getString("_userId")), webResult.getString("name"), webResult.getString("firstName"), webResult.getString("password"), "");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        return temp;
    }

    public static boolean ParseUserNameExists(String json) {
        String jsonData;
        JSONObject webResult;
        User temp = null;
        if (json != null) {
            if (VerifyServerResultOk(json)) {
                try {
                    int webInfo = json.indexOf("\n<!-- Hosting24 Analytics Code -->\n");
                    json = json.substring(0, webInfo);
                    webResult = new JSONObject(json);
                    if (webResult.has("nameExists"))
                        return  (webResult.getString("nameExists").toUpperCase().equals("YES"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        return false;
    }

    public static ArrayList<UserProfile> ParseUserProfile(String json) {
        String jsonData;
        JSONObject webResult;
        ArrayList<UserProfile> temp = new ArrayList<>();
        if (json != null) {
            if (VerifyServerResultOk(json)) {
                try {
                    int webInfo = json.indexOf("\n<!-- Hosting24 Analytics Code -->\n");
                    json = json.substring(0, webInfo);
                    webResult = new JSONObject(json);

                    JSONArray msg = (JSONArray) webResult.get("profile");
                    for (int i=0;i<msg.length();i++)
                    {
                        JSONObject profile = msg.getJSONObject(i);
                        temp.add(new UserProfile(profile.getInt("_profileId"),profile.getInt("_userId"),profile.getString("value"),profile.getInt("type")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        return temp;
    }

    public static int ParseSearchCarResult(String json,ArrayList<UserProfile> data) {
        String jsonData;
        JSONObject webResult;
        int searchId = -1;
        if (json != null) {
            if (VerifyServerResultOk(json)) {
                try {
                    int webInfo = json.indexOf("\n<!-- Hosting24 Analytics Code -->\n");
                    json = json.substring(0, webInfo);
                    webResult = new JSONObject(json);

                    searchId = webResult.getInt("searchId");

                    JSONArray msg = (JSONArray) webResult.get("profile");
                    for (int i=0;i<msg.length();i++)
                    {
                        JSONObject profile = msg.getJSONObject(i);
                        data.add(new UserProfile(profile.getInt("_profileId"),profile.getInt("_userId"),profile.getString("value"),profile.getInt("type"),profile.getInt("Blocked")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        return searchId;
    }

    public static int ParseInsertResult(String json) {
        String jsonData;
        JSONObject webResult;
        User temp = null;
        if (json != null) {
            if (VerifyServerResultOk(json)) {
                try {
                    int webInfo = json.indexOf("\n<!-- Hosting24 Analytics Code -->\n");
                    json = json.substring(0, webInfo);
                    webResult = new JSONObject(json);
                    if (webResult.has("result"))
                        return Integer.parseInt(webResult.getString("result"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }
    public static ArrayList<String> ParsePhoneNumbers(String json) {
        String jsonData;
        JSONObject webResult;
        ArrayList<String> temp = new ArrayList<>();
        if (json != null) {
            if (VerifyServerResultOk(json)) {
                try {
                    int webInfo = json.indexOf("\n<!-- Hosting24 Analytics Code -->\n");
                    json = json.substring(0, webInfo);
                    webResult = new JSONObject(json);

                    JSONArray msg = (JSONArray) webResult.get("phoneList");
                    for (int i=0;i<msg.length();i++)
                    {
                        JSONObject profile = msg.getJSONObject(i);
                        temp.add(profile.getString("phone"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        return temp;
    }

    public static ArrayList<BlockedRelation> ParseBlockedRelations(String json) {
        String jsonData;
        JSONObject webResult;
        ArrayList<BlockedRelation> temp = new ArrayList<>();
        if (json != null) {
            if (VerifyServerResultOk(json)) {
                try {
                    int webInfo = json.indexOf("\n<!-- Hosting24 Analytics Code -->\n");
                    json = json.substring(0, webInfo);
                    webResult = new JSONObject(json);

                    JSONArray msg = (JSONArray) webResult.get("profile");
                    for (int i=0;i<msg.length();i++)
                    {
                        JSONObject profile = msg.getJSONObject(i);
                        temp.add(new BlockedRelation(profile.getInt("_relationId"),profile.getInt("_blockingUserId"),profile.getInt("_blockedUserId"),profile.getInt("_blockedProfileId"),profile.getString("firstName")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return temp;
    }

    public static ArrayList<BlockedRelation> ParseBlockedRelations1(String json) {
        String jsonData;
        JSONObject webResult;
        ArrayList<BlockedRelation> temp = new ArrayList<>();
        if (json != null) {
            if (VerifyServerResultOk(json)) {
                try {
                    int webInfo = json.indexOf("\n<!-- Hosting24 Analytics Code -->\n");
                    json = json.substring(0, webInfo);
                    webResult = new JSONObject(json);

                    JSONArray msg = (JSONArray) webResult.get("profile");
                    for (int i=0;i<msg.length();i++)
                    {
                        JSONObject profile = msg.getJSONObject(i);
                        temp.add(new BlockedRelation(profile.getInt("_relationId"),profile.getInt("_blockingUserId"),profile.getInt("_blockedUserId"),profile.getInt("_blockedProfileId")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return temp;
    }
}
