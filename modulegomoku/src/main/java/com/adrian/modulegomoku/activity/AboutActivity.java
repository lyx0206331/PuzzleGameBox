
package com.adrian.modulegomoku.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.adrian.commonlib.baseComponent.BaseActivity;
import com.adrian.commonlib.tools.CommUtil;
import com.adrian.modulegomoku.R;


public class AboutActivity extends BaseActivity {

    private TextView mVersionTV;
    private TextView mAppNameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.modulegomoku_activity_about);
        initToolbar();
        mVersionTV = (TextView) findViewById(R.id.tv_version);
        mAppNameTV = (TextView) findViewById(R.id.tv_name);

        mAppNameTV.setText(CommUtil.INSTANCE.getAppName(this));
        mVersionTV.setText(getString(R.string.modulegomoku_version) + CommUtil.INSTANCE.getVersionName(this));
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
        mToolBarTextView.setText(R.string.modulegomoku_about);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.modulegomoku_menu_transparent, menu);
        return true;
    }
}
