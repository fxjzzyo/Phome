package com.example.fxjzzyo.phome.beanDao;


import android.net.http.AndroidHttpClient;
import android.net.http.HttpResponseCache;

import com.alibaba.fastjson.JSON;
import com.android.internal.http.multipart.MultipartEntity;
import com.example.fxjzzyo.phome.javabean.Comment;
import com.example.fxjzzyo.phome.javabean.Phone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */
public class UserDao {

    /**
     * 注册
     * @param path
     * @param userName
     * @param userPassword
     * @return
     */
    public String register(String path,String userName,String userPassword){
        return doGet(path,userName,userPassword);
    }

    /**
     * 登录
     * @param path
     * @param userName
     * @param userPassword
     * @return
     */
    public String userLogin(String path,String userName,String userPassword){
        return doGet(path,userName,userPassword);
    }

    /**
     * 以get方式请求数据
     * @param path
     * @param userName
     * @param userPassword
     * @return
     */
    public String doGet(String path,String userName,String userPassword){
    String getUrl = path + "?user_name=" + userName + "&user_password=" + userPassword;//拼接地址
        String response="failed";
        HttpGet httpGet = new HttpGet(getUrl);
    HttpClient hc = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(hc.getParams(),5000);//设置超时
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
     * 向服务器提交评论
     * @param path
     * @param comment
     * @return
     */
    public String submitCommentToServer(String path,Comment comment){
        String commentJson = JSON.toJSONString(comment);
        HttpPost httpPost = new HttpPost(path);
        try {

            httpPost.setEntity(new StringEntity(commentJson, "UTF-8"));

        HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                HttpEntity entity = httpResponse.getEntity();
                InputStream is = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuffer sb = new StringBuffer();
                String readLine=null;

                while ((readLine=br.readLine())!=null)
                {
                    sb.append(readLine);
                }
                is.close();

                return sb.toString();

            }else {
                return "failed";
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "failed";

    }

    /**
     * 上传图片到服务器
     * @param path
     * @param imagePath
     * @return
     */
    public String submitImageToServer(String path,String userId,String imagePath){

        String getUrl = path + "?userId=" + userId;//拼接地址
        String response="failed";
        HttpGet httpGet = new HttpGet(getUrl);
        HttpClient hc = new DefaultHttpClient();
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
        String result = "failed";
        if(response.equals("success"))
        {
            File file = new File(imagePath);
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(path);
                FileEntity entity = new FileEntity(file,"binary/octet-stream");
                httpPost.setEntity(entity);
                entity.setContentEncoding("binary/octet-stream");
                HttpResponse httpResponse = client.execute(httpPost);
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                {
                    InputStream in = httpResponse.getEntity().getContent();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    StringBuffer stringBuffer = new StringBuffer();
                    while ((len = in.read(buffer)) != -1)
                    {
                        stringBuffer.append(new String(buffer, 0, len,
                                "utf-8"));
                    }
                    result =  stringBuffer.toString().trim();
                    System.out.println("stringBuffer = "
                            + stringBuffer.toString().trim());
                    file.delete();//删除本地文件
                    return result;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }







public String getUserLoves(String path,String userId){
    String getUrl = path + "?userId=" + userId;//拼接地址
    String response = null;
    HttpGet httpGet = new HttpGet(getUrl);
    HttpClient hc = new DefaultHttpClient();
    HttpConnectionParams.setConnectionTimeout(hc.getParams(),5000);//设置超时
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
     * 判断/设置该用户是否已经喜欢该手机
     * @param path
     * @param userId
     * @param phoneId
     * @param type love:喜欢该手机，unlove:不喜欢了，check:检测是否喜欢
     * @return
     */
    public String isLove(String path,String userId,String phoneId,String type){
        String getUrl = path + "?userId=" + userId+"&phoneId="+phoneId+"&type="+type;//拼接地址
        String response = "failed";
        HttpGet httpGet = new HttpGet(getUrl);
        HttpClient hc = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(hc.getParams(),5000);//设置超时
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
     * 更改用户名或密码
     * @param path
     * @param userId
     * @param value
     * @param type password：改密码，userName:改用户名
     * @return
     */
    public String motifyUser(String path,String userId,String value,String type){

        String getUrl = path + "?userId=" + userId+"&value="+value+"&type="+type;//拼接地址
        String response = "failed";
        HttpGet httpGet = new HttpGet(getUrl);
        HttpClient hc = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(hc.getParams(),5000);//设置超时
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
