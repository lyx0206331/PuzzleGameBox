package com.adrian.modulegomoku.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.adrian.commonlib.baseComponent.BaseActivity;
import com.adrian.commonlib.tools.ParamUtil;
import com.adrian.modulegomoku.R;
import com.adrian.modulegomoku.views.RippleView;

public class ModeActivity extends BaseActivity {

    private RadioGroup mModeRG;
    private RippleView mConfirmBtn;

    private boolean isSingle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initVariables() {
        isSingle = ParamUtil.getInstance().isSinglePlayer(getApplicationContext());
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.modulegomoku_activity_mode);
        initToolbar();
        mModeRG = (RadioGroup) findViewById(R.id.rg_mode);
        mConfirmBtn = (RippleView) findViewById(R.id.btn_confirm);

        mModeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_single) {
                    isSingle = true;
                } else if (checkedId == R.id.rb_double) {
                    isSingle = false;
                }
            }
        });
        mConfirmBtn.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                ParamUtil.getInstance().setSinglePlayer(getApplicationContext(), isSingle);
                setResult(MainActivity.RES_MODE);
                finish();
            }
        });
        if (isSingle) {
            mModeRG.check(R.id.rb_single);
        } else {
            mModeRG.check(R.id.rb_double);
        }
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
        mToolBarTextView.setText(R.string.modulegomoku_mode);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.modulegomoku_menu_transparent, menu);
        return true;
    }
}
