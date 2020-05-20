package com.adrian.modulegomoku.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.adrian.commonlib.baseComponent.BaseActivity;
import com.adrian.commonlib.tools.ParamUtil;
import com.adrian.modulegomoku.R;
import com.adrian.modulegomoku.views.MultipleRadioGroup;
import com.adrian.modulegomoku.views.RippleView;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

public class ThemeActivity extends BaseActivity implements ColorPicker.OnColorChangedListener {

    private RippleView mConfirmBtn;
    private MultipleRadioGroup mBgImgMRG;

    private ColorPicker picker;
    private SVBar svBar;
    private OpacityBar opacityBar;
    private SaturationBar saturationBar;
    private ValueBar valueBar;

    private int boardColor;
    private int bgResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initVariables() {
        boardColor = ParamUtil.getInstance().getBoardColor(getApplicationContext());
        bgResId = ParamUtil.getInstance().getBgResId(getApplicationContext(), R.drawable.bg_4);
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.modulegomoku_activity_theme);
        initToolbar();
        mConfirmBtn = (RippleView) findViewById(R.id.btn_confirm);
        mConfirmBtn.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                ParamUtil.getInstance().setBoardColor(getApplicationContext(), boardColor);
                ParamUtil.getInstance().setBgResId(getApplicationContext(), bgResId);

                Intent intent = new Intent();
                intent.putExtra(ParamUtil.BG_RES_ID, bgResId);
                intent.putExtra(ParamUtil.BOARD_COLOR, boardColor);
                setResult(MainActivity.RES_THEME, intent);
                finish();
            }
        });
        mBgImgMRG = (MultipleRadioGroup) findViewById(R.id.mrg_bg);
        mBgImgMRG.setOnCheckedChangeListener(new MultipleRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MultipleRadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_bg_0) {
                    bgResId = R.drawable.bg_0;
                } else if (checkedId == R.id.rb_bg_1) {
                    bgResId = R.drawable.bg_1;
                } else if (checkedId == R.id.rb_bg_2) {
                    bgResId = R.drawable.bg_2;
                } else if (checkedId == R.id.rb_bg_3) {
                    bgResId = R.drawable.bg_3;
                } else if (checkedId == R.id.rb_bg_4) {
                    bgResId = R.drawable.bg_4;
                } else if (checkedId == R.id.rb_bg_5) {
                    bgResId = R.drawable.bg_5;
                } else {
                    bgResId = R.drawable.bg_4;
                }
            }
        });

        picker = (ColorPicker) findViewById(R.id.picker);
        svBar = (SVBar) findViewById(R.id.svbar);
        opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
        saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
        valueBar = (ValueBar) findViewById(R.id.valuebar);

        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);

        picker.setShowOldCenterColor(true);
        picker.setOldCenterColor(boardColor);
        picker.setOnColorChangedListener(this);
        picker.setColor(boardColor);
        picker.changeOpacityBarColor(boardColor);
        picker.changeSaturationBarColor(boardColor);
        picker.changeValueBarColor(boardColor);

        opacityBar.setOnOpacityChangedListener(new OpacityBar.OnOpacityChangedListener() {
            @Override
            public void onOpacityChanged(int opacity) {

            }
        });
        valueBar.setOnValueChangedListener(new ValueBar.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {

            }
        });
        saturationBar.setOnSaturationChangedListener(new SaturationBar.OnSaturationChangedListener() {
            @Override
            public void onSaturationChanged(int saturation) {

            }
        });
    }

    @Override
    protected void loadData() {

    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mToolbar.setNavigationIcon(R.mipmap.btn_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolBarTextView.setText(R.string.modulegomoku_theme);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.modulegomoku_menu_transparent, menu);
        return true;
    }

    @Override
    public void onColorChanged(int color) {
//        Log.e("THEME_TAG", "color : " + color);
        boardColor = color;
    }
}
