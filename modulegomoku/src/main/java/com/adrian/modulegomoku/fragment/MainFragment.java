package com.adrian.modulegomoku.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.adrian.commonlib.tools.CommUtil;
import com.adrian.commonlib.tools.ParamUtil;
import com.adrian.modulegomoku.R;
import com.adrian.modulegomoku.views.GomokuView;
import com.umeng.analytics.MobclickAgent;

import de.cketti.library.changelog.ChangeLog;


public class MainFragment extends Fragment implements GomokuView.IGameOverListener, View.OnClickListener {

    private ImageView mBgIV;
    private GomokuView mGomokuView;
    private AlertDialog mAlertDialog;
    private Button mRevokeBtn;
    private Button mRestartBtn;
    private RadioGroup mRgLevel;

    private Display display;
    private DisplayMetrics dm;
    private int screenW, screenH;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.modulegomoku_fragment_main, container, false);
        mBgIV = (ImageView) rootView.findViewById(R.id.iv_bg);
        mGomokuView = (GomokuView) rootView.findViewById(R.id.gomoku_view);
        mRevokeBtn = (Button) rootView.findViewById(R.id.btn_revoke);
        mRestartBtn = (Button) rootView.findViewById(R.id.btn_restart);
        mRgLevel = rootView.findViewById(R.id.rgLevel);
        mGomokuView.setPieceSoundResId(R.raw.piece);
        mGomokuView.setListener(this);
        mRevokeBtn.setOnClickListener(this);
        mRestartBtn.setOnClickListener(this);
        mRgLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbJuniorLevel) {
                    mGomokuView.setDiffcultLevel(3);
                } else if (checkedId == R.id.rbMiddleLevel) {
                    mGomokuView.setDiffcultLevel(2);
                } else if (checkedId == R.id.rbHighLevel) {
                    mGomokuView.setDiffcultLevel(1);
                }
            }
        });

        ChangeLog cl = new ChangeLog(getContext());
        if (cl.isFirstRun()) {
            cl.getLogDialog().show();
        }

        dm = CommUtil.INSTANCE.getScreenInfo(getContext());
        screenW = dm.widthPixels;
        screenH = dm.heightPixels;

        setSinglePlayer();
        setPieceSound();
        setTheme();
//        setupBannerAd();
        mRgLevel.check(R.id.rbJuniorLevel);
        return rootView;
    }

    public void setTheme() {
//        mBgIV.setImageBitmap(ImageUtil.getImageFromResource(ParamUtil.getInstance().getBgResId(), screenW, screenH));
        mBgIV.setBackgroundResource(ParamUtil.getInstance().getBgResId(getContext(), R.drawable.bg_4));
        mGomokuView.setBoardColor(ParamUtil.getInstance().getBoardColor(getContext()));
    }

    public void setPieceSound() {
        mGomokuView.setSoundOpened(ParamUtil.getInstance().openedPieceSound(getContext()));
    }

    public void setSinglePlayer() {
        boolean isSingle = ParamUtil.getInstance().isSinglePlayer(getContext());
        if (isSingle && !mGomokuView.isAiOpened()) {
            mRevokeBtn.setVisibility(View.GONE);
            mGomokuView.setAiOpened(true);
            mGomokuView.start();
        } else if (!isSingle && mGomokuView.isAiOpened()) {
            mRevokeBtn.setVisibility(View.VISIBLE);
            mGomokuView.setAiOpened(false);
            mGomokuView.start();
        }
    }

    @Override
    public void gameOver(boolean isWhiteWin) {
        int msgId = isWhiteWin ? R.string.modulegomoku_white_win : R.string.modulegomoku_black_win;
        if (mAlertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            mAlertDialog = builder.setPositiveButton(R.string.modulegomoku_quit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            }).setNegativeButton(R.string.modulegomoku_start_again, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mGomokuView.start();
                    dialog.dismiss();
                }
            }).setCancelable(true).create();
        }
        mAlertDialog.setMessage(getString(msgId));
        mAlertDialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_revoke) {
            mGomokuView.revoke();
        } else if (id == R.id.btn_restart) {
            mGomokuView.start();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
