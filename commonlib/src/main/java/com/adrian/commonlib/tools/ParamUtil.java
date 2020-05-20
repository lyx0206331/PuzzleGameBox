package com.adrian.commonlib.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.adrian.commonlib.application.BaseApplication;

/**
 * Created by ranqing on 2017/1/2.
 */

public class ParamUtil {

    private static ParamUtil instance;

    public static final String BOARD_COLOR = "boardColor";
    public static final String BG_RES_ID = "bgResId";
    public static final String SINGLE_PLAYER = "singlePlayer";
    public static final String OPENED_PIECE = "openedPieceSound";
    public static final String OPENED_BG_MUSIC = "openedBgMusic";

    public static ParamUtil getInstance() {
        if (instance == null) {
            instance = new ParamUtil();
        }
        return instance;
    }

    private SharedPreferences getPref(Context context) {
        SharedPreferences pref = context.getSharedPreferences("param", Context.MODE_PRIVATE);
        return pref;
    }

    /**
     * 设置背景资源ID
     *
     * @param resId
     */
    public void setBgResId(Context context, int resId) {
        SharedPreferences pref = getPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(BG_RES_ID, resId);
        editor.commit();
    }

    /**
     * 获取背景资源ID
     * @return
     */
    public int getBgResId(Context context,int resId) {
        SharedPreferences pref = getPref(context);
        return pref.getInt(BG_RES_ID, resId);
    }

    /**
     * 设置棋盘颜色
     * @param color
     */
    public void setBoardColor(Context context, int color) {
        SharedPreferences pref = getPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(BOARD_COLOR, color);
        editor.commit();
    }

    /**
     * 获取棋盘颜色
     * @return
     */
    public int getBoardColor(Context context) {
        SharedPreferences pref = getPref(context);
        return pref.getInt(BOARD_COLOR, 0x88000000);
    }

    /**
     * 设置是否单人模式
     * @param isSingle
     */
    public void setSinglePlayer(Context context, boolean isSingle) {
        SharedPreferences pref = getPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(SINGLE_PLAYER, isSingle);
        editor.commit();
    }

    /**
     * 获取是否单人模式
     * @return
     */
    public boolean isSinglePlayer(Context context) {
        SharedPreferences pref = getPref(context);
        return pref.getBoolean(SINGLE_PLAYER, true);
    }

    /**
     * 设置是否打开落子声音
     *
     * @param has
     */
    public void setOpenedPieceSound(Context context, boolean has) {
        SharedPreferences pref = getPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(OPENED_PIECE, has);
        editor.commit();
    }

    /**
     * 判断是否已打开落子声音
     *
     * @return
     */
    public boolean openedPieceSound(Context context) {
        SharedPreferences pref = getPref(context);
        return pref.getBoolean(OPENED_PIECE, true);
    }

    /**
     * 设置是否打开背景音乐
     *
     * @param opened
     */
    public void setOpenBgMusic(Context context, boolean opened) {
        SharedPreferences pref = getPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(OPENED_BG_MUSIC, opened);
        editor.commit();
    }

    /**
     * 判断是否已打开背景音乐
     *
     * @return
     */
    public boolean openedBgMusic(Context context) {
        SharedPreferences pref = getPref(context);
        return pref.getBoolean(OPENED_BG_MUSIC, true);
    }
}
