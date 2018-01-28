package com.example.analysisxmldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {
    private EditText mXmlNameEdt;
    private Button mAnalysisBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        mXmlNameEdt = findViewById(R.id.xml_name_edt);
        mAnalysisBtn = findViewById(R.id.analysis_btn);
        mXmlNameEdt.setFocusable(true);

        mAnalysisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(util.FILE_NAME, mXmlNameEdt.getText().toString());
                intent.setClass(getApplicationContext(), ContentActivity.class);
                startActivity(intent);
                mXmlNameEdt.setText(null);
            }
        });
    }

}
