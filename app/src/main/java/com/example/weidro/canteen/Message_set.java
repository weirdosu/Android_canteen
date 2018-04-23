package com.example.weidro.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by Weidro on 2018/4/11.
 */

public class Message_set extends AppCompatActivity {
    private String id;
    private String name;
    private String money;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_message);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String s = bundle.getString("JSON");

        parseJSONWithJSONObject(s);

        TextView student_ID = (TextView)findViewById(R.id.student_id);
        TextView student_name = (TextView)findViewById(R.id.student_name);
        TextView sum_money = (TextView)findViewById(R.id.sum_money);

        student_ID.setText(id);
        student_name.setText(name);
        sum_money.setText(money+"元");
    }

    private void parseJSONWithJSONObject(String jsonData) {
        try {

            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                id = jsonObject.getString("student_ID");
                name = jsonObject.getString("student_name");
                money = jsonObject.getString("sum_money");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
