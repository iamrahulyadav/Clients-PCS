package edu10g.android.quiz.statepcstest.models;

import java.util.ArrayList;

/**
 * Created by Vikram on 1/6/2018.
 */

public class Category_data {
    private boolean statuscode;
    private String message;

    public class Categary{
        public int cat_id;
        public String category_name;
        ArrayList<TestCategory> listing=new ArrayList<TestCategory>();

    }
}

