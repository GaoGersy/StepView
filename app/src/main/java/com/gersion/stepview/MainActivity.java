package com.gersion.stepview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;

import com.gersion.stepview.view.StepView;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, TextWatcher {

    private StepView mSvStep;
    private SeekBar mSeekBar;
    private EditText mEtMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initView() {
        mSvStep = (StepView) findViewById(R.id.sv_step);
        mSvStep.setMaxProgress(6000);
        mSvStep.setProgress(13232);
        mSvStep.setColor(Color.parseColor("#00ffff"));
        mSvStep.setBigTextSize(90);
        mSvStep.setDotSize(10);
        mSvStep.setSmallTextSize(25);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mEtMax = (EditText) findViewById(R.id.et_max);
    }

    private void initEvent() {
        mSeekBar.setOnSeekBarChangeListener(this);
        mEtMax.addTextChangedListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mSvStep.setProgress(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String max = s.toString();

        int maxProgress = 0;
        try {
            maxProgress = Integer.parseInt(max);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        mSvStep.setMaxProgress(maxProgress);
    }
}
