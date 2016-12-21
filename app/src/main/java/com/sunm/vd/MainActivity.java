package com.sunm.vd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sunm.widght.htextview.HTextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button set_btn;
    private HTextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        set_btn = (Button) findViewById(R.id.set_btn);
        text1 = (HTextView) findViewById(R.id.text1);

        set_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_btn:
                text1.clearText();
                text1.animateText("there is no feature");
                Toast.makeText(this, "there is my all", Toast.LENGTH_SHORT).show();
                text1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(intent);
                    }
                },1000);
                break;
        }
    }
}
