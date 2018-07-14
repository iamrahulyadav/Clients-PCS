package edu10g.android.quiz.testseries.models;

import java.util.ArrayList;

/**
 * Created by Vikram on 1/8/2018.
 */

public class Categary{
    private String cat_id;
    private String category_name;

    public ArrayList<TestCategory> getListing() {
        return listing;
    }

    public void setListing(ArrayList<TestCategory> listing) {
        this.listing = listing;
    }

    private ArrayList<TestCategory> listing ;

    public Categary()
    {

    }
    public Categary(String cat_id,String category_name,ArrayList listing) {
        this.cat_id=cat_id;
        this.category_name=category_name;
        this.listing=listing;
    }

    public String getCat_id()
    {
        return cat_id;
    }
    public void setCat_id(String cat_id)
    {
        this.cat_id=cat_id;
    }

    public String getCategory_name()
    {
        return category_name;
    }
    public void setCategory_name(String category_name)
    {
        this.category_name=category_name;
    }

}