package edu10g.android.quiz.statepcstest.helpers;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Lenovo on 30-10-2015.
 */
public interface CallBackInterface {

    public void onJsonObjectSuccess(JSONObject object);

    public void onJsonArrarSuccess(JSONArray array);

    public void onFailure(String str);
}
