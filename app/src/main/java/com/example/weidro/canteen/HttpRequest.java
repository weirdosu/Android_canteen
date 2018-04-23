package com.example.weidro.canteen;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.List;


/**
 * Created by Weidro on 2018/4/11.
 */
public  final  class HttpRequest {

    private static String LOGIN_URL = "http://101.132.186.165:8080/CM_Web/servlet/return_android";
    public static Handler handler;
    public static void goPost(final List<NameValuePair> ListData, boolean checknet)
    {
        // TODO Auto-generated method stub
        final Message message =new Message();
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(LOGIN_URL);
                    if (ListData != null) {

                        HttpEntity entity = new UrlEncodedFormEntity(ListData, HTTP.UTF_8);

                        httpPost.setEntity(entity);
                    }

                    httpClient.getParams().setParameter(
                            CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);

                    httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                            3000);

                    HttpResponse httpResp = httpClient.execute(httpPost);
                    if (httpResp.getStatusLine().getStatusCode() == 200) {

                        String result = EntityUtils.toString(httpResp.getEntity(), HTTP.UTF_8);
                        message.obj=result;
                        Log.i("result",result);
                        handler.sendMessage(message);
                    } else {
                        message.obj= "0";
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    message.obj="2";
                    handler.sendMessage(message);
                    Log.e("Request","Exception"+e.toString());
                }
                // ProgreDialogAndSendData.pairList.clear();
            }
        });
        //check network
        if (checknet==true)
        {
            thread.start();
        }else if(checknet==false)
        {
            message.obj="0";
            handler.sendMessage(message);
        }
    }

}
