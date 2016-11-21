package com.gersion.stepview;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.gersion.stepview.view.StepView;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, TextWatcher {

    private StepView mSvStep;
    private SeekBar mSeekBar;
    private EditText mEtMax;
    private Handler mHandler = new Handler();

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
        mSvStep.setProgress(2000);
//        mSvStep.setColor(Color.parseColor("#00ffff"));
        mSvStep.setBigTextSize(150);
        mSvStep.setDotSize(12);
        mSvStep.setSmallTextSize(40);
        mSvStep.setLineDistance(40);
        mSvStep.setStrokeWidth(2);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mEtMax = (EditText) findViewById(R.id.et_max);
    }

    private void initEvent() {
        mSeekBar.setOnSeekBarChangeListener(this);
        mEtMax.addTextChangedListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mSvStep.stopAnimator(5500);

                    }
                });
            }
        }).start();
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
