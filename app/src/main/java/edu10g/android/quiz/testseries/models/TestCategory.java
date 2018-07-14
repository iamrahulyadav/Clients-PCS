package edu10g.android.quiz.testseries.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vikram on 1/8/2018.
 */

public class TestCategory implements Parcelable{
    private String qpid;

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    private int cat_id;
    private String brand_title;
    private String brand_id;
    private String total_no_of_quiz;
    private String package_name;
    private String is_image;

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public double getNew_price() {
        return new_price;
    }

    public void setNew_price(double new_price) {
        this.new_price = new_price;
    }

    public int getIsFavourites() {
        return isFavourites;
    }

    public void setIsFavourites(int isFavourites) {
        this.isFavourites = isFavourites;
    }

    private String seller;
    private double price;
    private double new_price;
    private int price_offer;
    private String mod_url;
    private String subject;
    private String product_listing_type;
    private int quantity;
    private int languageId;

    public int isFavourites() {
        return isFavourites;
    }

    public void setFavourites(int favourites) {
        isFavourites = favourites;
    }

    private int isFavourites;


    public void setQpid(String qpid) {
        this.qpid = qpid;
    }

    public String getProduct_listing_type() {
        return product_listing_type;
    }

    public void setProduct_listing_type(String product_listing_type) {
        this.product_listing_type = product_listing_type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public TestCategory()
    {

    }
    public TestCategory(String qpid, String brand_title, String brand_id, String total_no_of_quiz, String package_name, String is_image, int price, int price_offer, String mod_url)
    {
        this.qpid=qpid;
        this.brand_title=brand_title;
        this.brand_id=brand_id;
        this.total_no_of_quiz=total_no_of_quiz;
        this.package_name=package_name;
        this.is_image=is_image;
        this.price=price;
        this.price_offer=price_offer;
        this.mod_url=mod_url;
    }
    public String getQpid()
    {
        return qpid;
    }

    public void setqpid(String qpid)
    {
        this.qpid=qpid;
    }

    public String getBrand_title()
    {
        return brand_title;
    }

    public void setBrand_title(String brand_title)
    {
        this.brand_title=brand_title;
    }

    public String getBrand_id()
    {
        return brand_id;
    }

    public void setBrand_id(String brand_id)
    {
        this.brand_id=brand_id;
    }

    public String getTotal_no_of_quiz()
    {
        return total_no_of_quiz;
    }

    public void setTotal_no_of_quiz(String total_no_of_quiz)
    {
        this.total_no_of_quiz=total_no_of_quiz;
    }
    public String getPackage_name()
    {
        return package_name;
    }

    public void setPackage_name(String package_name)
    {
        this.package_name=package_name;
    }

    public String getIs_image()
    {
        return is_image;
    }

    public void setIs_image(String is_image)
    {
        this.is_image=is_image;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price=price;
    }

    public int getPrice_offer()
    {
        return price_offer;
    }

    public void setPrice_offer(int price_offer)
    {
        this.price_offer=price_offer;
    }

    public String getMod_url()
    {
        return mod_url;
    }

    public void setMod_url(String mod_url)
    {
        this.mod_url=mod_url;
    }


    public TestCategory(Parcel parcel){
        this.qpid = parcel.readString();
        this.isFavourites = parcel.readInt();
        this.cat_id = parcel.readInt();
        this.brand_title = parcel.readString();
        this.brand_id = parcel.readString();
        this.total_no_of_quiz = parcel.readString();
        this.package_name = parcel.readString();
        this.is_image = parcel.readString();
        this.seller = parcel.readString();
        this.price = parcel.readDouble();
        this.new_price = parcel.readDouble();
        this.price_offer = parcel.readInt();
        this.mod_url = parcel.readString();
        this.subject = parcel.readString();
        this.product_listing_type = parcel.readString();
        this.quantity = parcel.readInt();
        this.languageId = parcel.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TestCategory createFromParcel(Parcel in) {
            return new TestCategory(in);
        }

        public TestCategory[] newArray(int size) {
            return new TestCategory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(qpid);
        parcel.writeInt(isFavourites);
        parcel.writeInt(cat_id);
        parcel.writeString(brand_title);
        parcel.writeString(brand_id);
        parcel.writeString(total_no_of_quiz);
        parcel.writeString(package_name);
        parcel.writeString(is_image);
        parcel.writeString(seller);
        parcel.writeDouble(price);
        parcel.writeDouble(new_price);
        parcel.writeInt(price_offer);
        parcel.writeString(mod_url);
        parcel.writeString(subject);
        parcel.writeString(product_listing_type);
        parcel.writeInt(quantity);
        parcel.writeInt(languageId);
    }




}
