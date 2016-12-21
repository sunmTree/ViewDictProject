package com.sunm.vd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    private Button start;
    private static final String INTENT_KEY = "secondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        start = (Button) findViewById(R.id.btn_intent);
        start.setOnClickListener(this);

        String stringExtra = getIntent().getStringExtra(INTENT_KEY);
        if (stringExtra != null){
            start.setText(stringExtra);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_intent:
                Intent intent = new Intent(SecondActivity.this, SecondActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtra(INTENT_KEY,"second");
                startActivity(intent);
                break;
        }
    }
}
