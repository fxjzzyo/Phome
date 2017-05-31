package com.example.fxjzzyo.phome.beanDao;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2017/4/15.
 */

public class PhoneDao {
public static final int TIME_OUT = 5000;
    public String getHotPhonesFromNet(String path,String begin,String count,String type){

        String getUrl = path + "?begin=" + begin + "&count=" + count+"&type="+type;//拼接地址
        String response = null;
        HttpGet httpGet = new HttpGet(getUrl);
        HttpClient hc = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(hc.getParams(),TIME_OUT);//设置超时
        try {
            HttpResponse ht = hc.execute(httpGet); // 给客户端一个响应
            HttpEntity he = ht.getEntity(); // 内容
            InputStream is = he.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    "utf-8"));
            StringBuffer sb = new StringBuffer();

            String readLine = null;
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            is.close();
            //返回结果
            response =  sb.toString();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;


    }
    public String getPhonesByName(String path,String name){

        String getUrl = path + "?name=" + name ;//拼接地址
        String response = null;
        HttpGet httpGet = new HttpGet(getUrl);
        HttpClient hc = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(hc.getParams(),TIME_OUT);//设置超时
        try {
            HttpResponse ht = hc.execute(httpGet); // 给客户端一个响应
            HttpEntity he = ht.getEntity(); // 内容
            InputStream is = he.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    "utf-8"));
            StringBuffer sb = new StringBuffer();

            String readLine = null;
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            is.close();
            //返回结果
            response =  sb.toString();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;


    }
    /**
     * 根据手机品牌活获得特定个数的手机
     * @param path
     * @param brand
     * @param begin
     * @param count
     * @return
     */
    public String getSpeciPhonesByBrand(String path,String brand,String begin,String count){
        String getUrl = path + "?phoneBrand=" + brand+"&begin="+begin+"&count="+count;//拼接地址
        String response = null;
        HttpGet httpGet = new HttpGet(getUrl);
        HttpClient hc = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(hc.getParams(),TIME_OUT);//设置超时
        try {
            HttpResponse ht = hc.execute(httpGet); // 给客户端一个响应
            HttpEntity he = ht.getEntity(); // 内容
            InputStream is = he.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    "utf-8"));
            StringBuffer sb = new StringBuffer();

            String readLine = null;
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            is.close();
            //返回结果
            response =  sb.toString();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }

    /**
     * 根据手机id获得手机基本信息
     * @param path
     * @param phoneId
     * @return
     */
    public String getGeneralPhoneById(String path,String phoneId){
        String getUrl = path + "?phoneId=" + phoneId;//拼接地址
        String response = null;
        HttpGet httpGet = new HttpGet(getUrl);
        HttpClient hc = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(hc.getParams(),TIME_OUT);//设置超时
        try {
            HttpResponse ht = hc.execute(httpGet); // 给客户端一个响应
            HttpEntity he = ht.getEntity(); // 内容
            InputStream is = he.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    "utf-8"));
            StringBuffer sb = new StringBuffer();

            String readLine = null;
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            is.close();
            //返回结果
            response =  sb.toString();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }
    /**
     * 根据手机id获得type类手机信息
     * @param path
     * @param phoneId
     * @param type
     * @return
     */
    public String getAllParamaterById(String path,String phoneId,String type){
        String getUrl = path + "?phoneId=" + phoneId+"&paramaterType="+type;//拼接地址
        String response = null;
        HttpGet httpGet = new HttpGet(getUrl);
        HttpClient hc = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(hc.getParams(),TIME_OUT);//设置超时
        try {
            HttpResponse ht = hc.execute(httpGet); // 给客户端一个响应
            HttpEntity he = ht.getEntity(); // 内容
            InputStream is = he.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    "utf-8"));
            StringBuffer sb = new StringBuffer();

            String readLine = null;
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            is.close();
            //返回结果
            response =  sb.toString();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }
    /**
     * 根据手机id获得评论
     * @param path
     * @param phoneId
     * @return
     */
    public String getCommentById(String path,String phoneId){
        String getUrl = path + "?phoneId=" + phoneId;//拼接地址
        String response = null;
        HttpGet httpGet = new HttpGet(getUrl);
        HttpClient hc = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(hc.getParams(),TIME_OUT);//设置超时
        try {
            HttpResponse ht = hc.execute(httpGet); // 给客户端一个响应
            HttpEntity he = ht.getEntity(); // 内容
            InputStream is = he.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    "utf-8"));
            StringBuffer sb = new StringBuffer();

            String readLine = null;
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            is.close();
            //返回结果
            response =  sb.toString();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }
}
