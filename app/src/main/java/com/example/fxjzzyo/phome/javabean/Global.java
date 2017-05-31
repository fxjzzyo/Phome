package com.example.fxjzzyo.phome.javabean;

/**
 * Created by Administrator on 2017/4/12.
 */

public class Global {
    public static   String ip = "192.168.23.1";
    public static final String rootUrl = "http://"+ip+"/MyPhoneSite";
    public static final String registerUrl =rootUrl+ "/RegistServlet";
    public static final String loginUrl =rootUrl+ "/LoginServlet";
    public static final String getHotPhonesUrl =rootUrl+ "/GetHotPhoneServlet";
    public static final String getSpeciPhonesUrl =rootUrl+ "/GetPhonesByBrandServlet";
    public static final String getPhonesByNameUrl =rootUrl+ "/GetPhonesByNameServlet";
    public static final String phonePicUrl =rootUrl+"/GetImageServlet";

    public static final String getGeneralPhoneInfoUrl =rootUrl+"/GetGeneralPhoneInfoServlet";
    public static final String getAllParamaterInfoUrl =rootUrl+"/GetAllParamaterServlet";
    public static final String submitCommentUrl =rootUrl+"/ReceiveCommentServlet";
    public static final String getCommentUrl =rootUrl+"/GetCommentServlet";
    public static final String getUserLovesUrl =rootUrl+"/GetUserLovesServlet";
    public static final String motifyUserUrl =rootUrl+"/MotifyUserServlet";
    public static final String isLoveUrl =rootUrl+"/IsLoveServlet";
    public static final String upImageHeadUrl =rootUrl+"/ReceiveImageHeadServlet";
    //默认每次加载数据的个数
    public static final String count ="5";
    //默认每次加载热门手机的个数
    public static final String hotCount ="5";
    //判断用户是否登录的用户标识
    public static User user;
    public static boolean isNetAvailable;
    public static String orderType = "love_count";//首页热门推荐的排序方式
}
