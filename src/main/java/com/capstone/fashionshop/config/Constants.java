package com.capstone.fashionshop.config;

public class Constants {
    public static final String ENABLE = "enable";
    public static final String DISABLE = "disable";
    //ROLE
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_STAFF = "ROLE_STAFF";
    //USER STATE
    public static final String USER_STATE_ACTIVATED = "activated";
    public static final String USER_STATE_DEACTIVATED = "deactivated";
    public static final String USER_STATE_UNVERIFIED = "unverified";
    //ORDER STATE
    public static final String ORDER_STATE_ENABLE = "enable";
    public static final String ORDER_STATE_CANCEL = "cancel";
    public static final String ORDER_STATE_PROCESS = "process";
    public static final String ORDER_STATE_DONE = "done";
    public static final String ORDER_STATE_DELIVERY = "delivery";
    public static final String ORDER_STATE_DELIVERED = "delivered";
    public static final String ORDER_STATE_PREPARE = "prepare";
    public static final String ORDER_STATE_PENDING = "pending";
    //PAYMENT TYPE
    public static final String PAYMENT_PAYPAL = "paypal";
    public static final String PAYMENT_VNPAY = "vnpay";
    public static final String PAYMENT_COD = "cod";
    public static final int PAYMENT_TIMEOUT = 10 * 60 * 1000;
    //API GHN
    public static final String GHN_URL = "https://dev-online-gateway.ghn.vn/shiip/public-api/";
    //RECOMMEND TYPE
    public static final String REVIEW_GOOD_TYPE = "review";
    public static final String VIEW_TYPE = "view";
    public static final String CART_TYPE = "cart";

}
