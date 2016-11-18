package com.gersion.stepview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gersion.stepview.view.StepView;

public class MainActivity extends AppCompatActivity {

    private StepView mSvStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mSvStep = (StepView) findViewById(R.id.sv_step);
        mSvStep.setMaxProgress(6000);
        mSvStep.setProgress(13232);
    }
}
