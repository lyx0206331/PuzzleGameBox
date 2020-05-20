package com.adrian.modulegomoku.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.IntRange;

import com.adrian.modulegomoku.R;
import com.adrian.modulegomoku.ai.GomokuAIKt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ranqing on 2016/12/17.
 */

public class GomokuView extends View {

    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACH_ARRAY = "instance_black_array";

    private static final int AI_TURN = 0;

    private int mPanelWidth;
    private float mLineHeight;
    private int MAX_LINE = 15;
    private int MAX_COUNT_IN_LINE = 5;

    private Paint mPaint = new Paint();
    private int boardColor = 0x88000000;

    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    private float ratioPieceOfLineHeight = 3 * 1.0f / 4;

    private boolean mIsWhite = false;    //执黑先行
    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();

    private boolean mIsGameOver;
    private boolean mIsWhiteWinner;

    private IGameOverListener listener;
    private GomokuAIKt gomokuAI;

    private SoundPool soundPool;
    private int pieceSoundResId;
    private int pieceSoundId;

    private boolean isAiOpened = false;
    private boolean isSoundOpened = true;

    private int diffcultLevel = 3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AI_TURN:
                    Point aiPoint = gomokuAI.getBestPoint(mWhiteArray, mBlackArray, diffcultLevel);
                    mWhiteArray.add(aiPoint);
                    invalidate();
                    playSound();
                    if (gomokuAI.isAiWin(aiPoint)) {
                        mIsGameOver = true;
                        if (listener != null) listener.gameOver(true);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public GomokuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint.setColor(boardColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.piece1);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.piece0);

        gomokuAI = new GomokuAIKt(MAX_LINE);
    }

    public int getPieceSoundResId() {
        return pieceSoundResId;
    }

    public void setPieceSoundResId(int pieceSoundResId) {
        this.pieceSoundResId = pieceSoundResId;
        if (soundPool == null) {
            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 5);
        }
        pieceSoundId = soundPool.load(getContext(), pieceSoundResId, 1);
    }

    public boolean isSoundOpened() {
        return isSoundOpened;
    }

    /**
     * 设置是否打开落子音效
     *
     * @param soundOpened
     */
    public void setSoundOpened(boolean soundOpened) {
        isSoundOpened = soundOpened;
    }

    /**
     * 播放落子音效
     */
    private void playSound() {
        if (isSoundOpened) {
            if (pieceSoundResId != 0) {
                soundPool.play(pieceSoundId, 1.0f, 1.0f, 0, 0, 1);
            } else {
                Log.e("GOMOKU_VIEW", "无音频文件!");
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);

        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }

        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;

        int pieceWidth = (int) (mLineHeight * ratioPieceOfLineHeight);

        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth, false);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsGameOver) return false;

        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point p = getValidPoint(x, y);

            if (mWhiteArray.contains(p) || mBlackArray.contains(p)) {
                return false;
            }

            if (isAiOpened) {
                mBlackArray.add(p);
                if (gomokuAI.isPlayerWin(p)) {
//                    invalidate();
                    mIsGameOver = true;
                    if (listener != null) listener.gameOver(false);
                } else {
                    handler.sendEmptyMessageDelayed(AI_TURN, 500);
                }
            } else {
                if (mIsWhite) {
                    mWhiteArray.add(p);
                } else {
                    mBlackArray.add(p);
                }
//                invalidate();
                mIsWhite = !mIsWhite;
            }
            invalidate();
            playSound();
        }

        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);

        drawPieces(canvas);

        if (!isAiOpened) {
            checkGameOver();
        }
    }

    private void checkGameOver() {
        boolean whiteWin = checkFiveInLine(mWhiteArray);
        boolean blackWin = checkFiveInLine(mBlackArray);

        if (whiteWin || blackWin) {
            mIsGameOver = true;
            mIsWhiteWinner = whiteWin;

            if (listener != null) {
                listener.gameOver(mIsWhiteWinner);
            }
        }
    }

    private boolean checkFiveInLine(List<Point> points) {
        for (Point p :
                points) {
            int x = p.x;
            int y = p.y;

            boolean win = checkHorizontal(x, y, points);
            if (win) return true;
            win = checkVertical(x, y, points);
            if (win) return true;
            win = checkLeftDiagonal(x, y, points);
            if (win) return true;
            win = checkRightDiagonal(x, y, points);
            if (win) return true;
        }
        return false;
    }

    /**
     * 判断x, y位置的棋子，是否横向有相连的五个一起
     *
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {   //left
            if (points.contains(new Point(x - i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {   //right
            if (points.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    private boolean checkVertical(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {   //up
            if (points.contains(new Point(x, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {   //down
            if (points.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y - i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    private boolean checkRightDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    private void drawPieces(Canvas canvas) {
        for (int i = 0, n = mWhiteArray.size(); i < n; i++) {
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece,
                    (whitePoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight,
                    (whitePoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, null);
        }

        for (int i = 0, n = mBlackArray.size(); i < n; i++) {
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (blackPoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight,
                    (blackPoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, null);
        }
    }

    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;

        for (int i = 0; i < MAX_LINE; i++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);
            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX, y, endX, y, mPaint);    //横轴
            canvas.drawLine(y, startX, y, endX, mPaint);    //纵轴
        }
    }

    public void setListener(IGameOverListener listener) {
        this.listener = listener;
    }

    public int getDiffcultLevel() {
        return diffcultLevel;
    }

    /**
     * 设置难度等级
     *
     * @param diffcultLevel 一级最高，二级次之，三级最低
     */
    public void setDiffcultLevel(@IntRange(from = 1, to = 3) int diffcultLevel) {
        this.diffcultLevel = diffcultLevel;
        start();
    }

    public void start() {
        mWhiteArray.clear();
        mBlackArray.clear();
        mIsGameOver = false;
        mIsWhiteWinner = false;
        mIsWhite = false;
        gomokuAI.restart();
        invalidate();
    }

    /**
     * 撤消
     */
    public void revoke() {
        if (mIsWhite && mBlackArray.size() > 0) {
            mBlackArray.remove(mBlackArray.size() - 1);
            invalidate();
            mIsWhite = !mIsWhite;
        } else if (!mIsWhite && mWhiteArray.size() > 0) {
            mWhiteArray.remove(mWhiteArray.size() - 1);
            invalidate();
            mIsWhite = !mIsWhite;
        }
    }

    /**
     * 开关AI模式
     *
     * @param openAI
     */
    public void setAiOpened(boolean openAI) {
        this.isAiOpened = openAI;
    }

    public boolean isAiOpened() {
        return isAiOpened;
    }

    public int getBoardColor() {
        return boardColor;
    }

    public void setBoardColor(int boardColor) {
        this.boardColor = boardColor;
        mPaint.setColor(boardColor);
//        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACH_ARRAY, mBlackArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mIsGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACH_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public interface IGameOverListener {
        void gameOver(boolean isWhiteWin);
    }
}
