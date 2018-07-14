package edu10g.android.quiz.testseries.models;

/**
 * Created by vikram on 12/7/18.
 */

public class Coupon {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public int getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(int couponAmount) {
        this.couponAmount = couponAmount;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    private String couponCode;

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public String getCouponSubTitle() {
        return couponSubTitle;
    }

    public void setCouponSubTitle(String couponSubTitle) {
        this.couponSubTitle = couponSubTitle;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getMinimum_cart_value() {
        return minimum_cart_value;
    }

    public void setMinimum_cart_value(int minimum_cart_value) {
        this.minimum_cart_value = minimum_cart_value;
    }

    public String getAmount_type() {
        return amount_type;
    }

    public void setAmount_type(String amount_type) {
        this.amount_type = amount_type;
    }

    public String getCoupon_description() {
        return coupon_description;
    }

    public void setCoupon_description(String coupon_description) {
        this.coupon_description = coupon_description;
    }

    private String couponTitle;
    private String couponSubTitle;
    private String start_date;
    private String end_date;
    private String couponType;

    public String getCouponCategory() {
        return couponCategory;
    }

    public void setCouponCategory(String couponCategory) {
        this.couponCategory = couponCategory;
    }

    private String couponCategory;
    private int couponAmount;
    private int minimum_cart_value;
    private String amount_type;
    private String coupon_description;
    private boolean isRead;
}
