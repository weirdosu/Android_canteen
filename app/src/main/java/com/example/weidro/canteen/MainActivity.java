package com.example.weidro.canteen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weidro.canteen.widget.CustomDatePicker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG="MainActivity";
    private Button button;
    private EditText edit1;
    ProgressDialog proDialog;
//    private MyHandler myhandler = new MyHandler(this);

    private RelativeLayout selectDate, selectTime;
    private TextView currentDate, currentTime;
    private CustomDatePicker customDatePicker1, customDatePicker2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectTime = (RelativeLayout) findViewById(R.id.selectTime);
        selectTime.setOnClickListener(this);
        selectDate = (RelativeLayout) findViewById(R.id.selectDate);
        selectDate.setOnClickListener(this);
        currentDate = (TextView) findViewById(R.id.currentDate);
        currentTime = (TextView) findViewById(R.id.currentTime);

        initDatePicker();
        Log.d(TAG, "time is "+currentDate.getText());
        InitView();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectDate:
                // 日期格式为yyyy-MM-dd
                customDatePicker1.show(currentDate.getText().toString());
                break;

            case R.id.selectTime:
                // 日期格式为yyyy-MM-dd HH:mm
                customDatePicker2.show(currentTime.getText().toString());
                break;
        }
    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
//        currentDate.setText(now.split(" ")[0]);
        currentDate.setText(now);
        currentTime.setText(now);

        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                currentDate.setText(time);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(true); // 显示时和分
        customDatePicker1.setIsLoop(true); // 允许循环滚动

        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                currentTime.setText(time);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(true); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动
    }

    void InitView(){
        button = (Button) findViewById(R.id.button_ok);
        edit1 = (EditText) findViewById(R.id.edit_Name);
        // Toast.makeText(MainActivity.this, "str111", Toast.LENGTH_SHORT).show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edit1.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "饭卡号不能为空", Toast.LENGTH_SHORT).show();
                }
                else{
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    NameValuePair pair1 = new BasicNameValuePair("student_ID", edit1.getText().toString());
                    NameValuePair pair2 = new BasicNameValuePair("begin_time", currentDate.getText().toString()+":00");
                    NameValuePair pair3 = new BasicNameValuePair("end_time", currentTime.getText().toString()+":00");
                    params.add(pair1);
                    params.add(pair2);
                    params.add(pair3);
                    HttpRequest.goPost(params,true);

                    proDialog = ProgressDialog.show(MainActivity.this, "正在提交", "请稍后...");
                }
            }
        });

        HttpRequest.handler= new Handler() {
            public void handleMessage(Message msg) {
                if(msg.obj.equals("0")){
                    Toast.makeText(MainActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                    if(proDialog!=null){
                        proDialog.dismiss();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                    if(proDialog!=null){
                        proDialog.dismiss();
                    }
                    Intent intent =new Intent(MainActivity.this, Message_set.class);
                    intent.putExtra("JSON", msg.obj.toString());
                    startActivity(intent);
                }
                super.handleMessage(msg);
            }
        };
    }

}
