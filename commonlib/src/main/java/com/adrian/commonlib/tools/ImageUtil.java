package com.adrian.commonlib.tools;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.adrian.commonlib.application.BaseApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ranqing on 2017/1/7.
 */

public class ImageUtil {

    private static int getResizedDimension(int maxPrimary, int maxSecondary,
                                           int actualPrimary, int actualSecondary) {
        // If no dominant value at all, just return the actual.
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }

        // If primary is unspecified, scale primary to match secondary's scaling
        // ratio.
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    private static int findBestSampleSize(int actualWidth, int actualHeight,
                                          int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
    }

    public static Bitmap getImageFromAssetsFile(String fileName, int mMaxWidth,
                                                int mMaxHeight) {
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        Bitmap bitmap = null;
        Bitmap.Config preferredConfig = Bitmap.Config.RGB_565;
        AssetManager am = BaseApplication.Companion.getInstance().getResources().getAssets();
        try {
            if (mMaxWidth == 0 && mMaxHeight == 0) {
                decodeOptions.inPreferredConfig = preferredConfig;
                InputStream is = am.open(fileName);
                bitmap = BitmapFactory.decodeStream(is, null, decodeOptions);
                is.close();
            } else {
                // If we have to resize this image, first get the natural
                // bounds.
                decodeOptions.inJustDecodeBounds = true;
                decodeOptions.inPreferredConfig = preferredConfig;
                InputStream is = am.open(fileName);
                BitmapFactory.decodeStream(is, null, decodeOptions);
                is.close();
                int actualWidth = decodeOptions.outWidth;
                int actualHeight = decodeOptions.outHeight;

                // Then compute the dimensions we would ideally like to decode
                // to.
                int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight,
                        actualWidth, actualHeight);
                int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth,
                        actualHeight, actualWidth);

                // Decode to the nearest power of two scaling factor.
                decodeOptions.inJustDecodeBounds = false;
                // TODO(ficus): Do we need this or is it okay since API 8
                // doesn't
                // support it?
                // decodeOptions.inPreferQualityOverSpeed =
                // PREFER_QUALITY_OVER_SPEED;
                decodeOptions.inSampleSize = findBestSampleSize(actualWidth,
                        actualHeight, desiredWidth, desiredHeight);
                decodeOptions.inPreferredConfig = preferredConfig;
                InputStream is2 = am.open(fileName);
                Bitmap tempBitmap = BitmapFactory.decodeStream(is2, null,
                        decodeOptions);
                is2.close();
                // If necessary, scale down to the maximal acceptable size.
                if (tempBitmap != null
                        && (tempBitmap.getWidth() > desiredWidth || tempBitmap
                        .getHeight() > desiredHeight)) {
                    bitmap = Bitmap.createScaledBitmap(tempBitmap,
                            desiredWidth, desiredHeight, true);
                    tempBitmap.recycle();
                } else {
                    bitmap = tempBitmap;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap getImageFromData(byte[] data, int mMaxWidth,
                                          int mMaxHeight) {
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        Bitmap bitmap = null;
        if (mMaxWidth == 0 && mMaxHeight == 0) {
            decodeOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
                    decodeOptions);
        } else {
            // If we have to resize this image, first get the natural bounds.
            decodeOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
            int actualWidth = decodeOptions.outWidth;
            int actualHeight = decodeOptions.outHeight;

            // Then compute the dimensions we would ideally like to decode to.
            int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight,
                    actualWidth, actualHeight);
            int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth,
                    actualHeight, actualWidth);

            // Decode to the nearest power of two scaling factor.
            decodeOptions.inJustDecodeBounds = false;
            // TODO(ficus): Do we need this or is it okay since API 8 doesn't
            // support it?
            // decodeOptions.inPreferQualityOverSpeed =
            // PREFER_QUALITY_OVER_SPEED;
            decodeOptions.inSampleSize = findBestSampleSize(actualWidth,
                    actualHeight, desiredWidth, desiredHeight);
            Bitmap tempBitmap = BitmapFactory.decodeByteArray(data, 0,
                    data.length, decodeOptions);

            // If necessary, scale down to the maximal acceptable size.
            if (tempBitmap != null
                    && (tempBitmap.getWidth() > desiredWidth || tempBitmap
                    .getHeight() > desiredHeight)) {
                bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth,
                        desiredHeight, true);
                tempBitmap.recycle();
            } else {
                bitmap = tempBitmap;
            }
        }
        return bitmap;
    }

    public static Bitmap getImageFromBitmap(Bitmap srcBitmap, int mMaxWidth,
                                            int mMaxHeight) {
        Bitmap bitmap = null;
        if (mMaxWidth == 0 && mMaxHeight == 0) {
            bitmap = srcBitmap;
        } else {
            int actualWidth = srcBitmap.getWidth();
            int actualHeight = srcBitmap.getHeight();

            // Then compute the dimensions we would ideally like to decode to.
            int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight,
                    actualWidth, actualHeight);
            int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth,
                    actualHeight, actualWidth);
            bitmap = Bitmap.createScaledBitmap(srcBitmap, desiredWidth,
                    desiredHeight, true);
        }
        return bitmap;
    }

    public static Bitmap getImageFromUri(Uri uri, int mMaxWidth,
                                         int mMaxHeight) {
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        Bitmap bitmap = null;
        Bitmap.Config preferredConfig = Bitmap.Config.RGB_565;
        try {
            if (mMaxWidth == 0 && mMaxHeight == 0) {
                decodeOptions.inPreferredConfig = preferredConfig;
                InputStream is = BaseApplication.Companion.getInstance().getContentResolver()
                        .openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(is, null, decodeOptions);
                is.close();
            } else {
                // If we have to resize this image, first get the natural
                // bounds.
                decodeOptions.inJustDecodeBounds = true;
                decodeOptions.inPreferredConfig = preferredConfig;
                InputStream is = BaseApplication.Companion.getInstance().getContentResolver()
                        .openInputStream(uri);
                BitmapFactory.decodeStream(is, null, decodeOptions);
                is.close();
                int actualWidth = decodeOptions.outWidth;
                int actualHeight = decodeOptions.outHeight;

                // Then compute the dimensions we would ideally like to decode
                // to.
                int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight,
                        actualWidth, actualHeight);
                int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth,
                        actualHeight, actualWidth);

                // Decode to the nearest power of two scaling factor.
                decodeOptions.inJustDecodeBounds = false;
                // TODO(ficus): Do we need this or is it okay since API 8
                // doesn't
                // support it?
                // decodeOptions.inPreferQualityOverSpeed =
                // PREFER_QUALITY_OVER_SPEED;
                decodeOptions.inSampleSize = findBestSampleSize(actualWidth,
                        actualHeight, desiredWidth, desiredHeight);
                decodeOptions.inPreferredConfig = preferredConfig;
                InputStream is2 = BaseApplication.Companion.getInstance().getContentResolver()
                        .openInputStream(uri);
                Bitmap tempBitmap = BitmapFactory.decodeStream(is2, null,
                        decodeOptions);
                is2.close();
                // If necessary, scale down to the maximal acceptable size.
                if (tempBitmap != null
                        && (tempBitmap.getWidth() > desiredWidth || tempBitmap
                        .getHeight() > desiredHeight)) {
                    bitmap = Bitmap.createScaledBitmap(tempBitmap,
                            desiredWidth, desiredHeight, true);
                    tempBitmap.recycle();
                } else {
                    bitmap = tempBitmap;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap getImageFromResource(int resId, int mMaxWidth,
                                              int mMaxHeight) {
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        Bitmap bitmap = null;
        Bitmap.Config preferredConfig = Bitmap.Config.RGB_565;
        try {
            if (mMaxWidth == 0 && mMaxHeight == 0) {
                decodeOptions.inPreferredConfig = preferredConfig;
                InputStream is = BaseApplication.Companion.getInstance().getResources().openRawResource(
                        resId);
                bitmap = BitmapFactory.decodeStream(is, null, decodeOptions);
                is.close();
            } else {
                // If we have to resize this image, first get the natural
                // bounds.
                decodeOptions.inJustDecodeBounds = true;
                decodeOptions.inPreferredConfig = preferredConfig;
                InputStream is = BaseApplication.Companion.getInstance().getResources().openRawResource(
                        resId);
                BitmapFactory.decodeStream(is, null, decodeOptions);
                is.close();
                int actualWidth = decodeOptions.outWidth;
                int actualHeight = decodeOptions.outHeight;

                // Then compute the dimensions we would ideally like to decode
                // to.
                int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight,
                        actualWidth, actualHeight);
                int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth,
                        actualHeight, actualWidth);

                // Decode to the nearest power of two scaling factor.
                decodeOptions.inJustDecodeBounds = false;
                // TODO(ficus): Do we need this or is it okay since API 8
                // doesn't
                // support it?
                // decodeOptions.inPreferQualityOverSpeed =
                // PREFER_QUALITY_OVER_SPEED;
                decodeOptions.inSampleSize = findBestSampleSize(actualWidth,
                        actualHeight, desiredWidth, desiredHeight);
                decodeOptions.inPreferredConfig = preferredConfig;
                InputStream is2 = BaseApplication.Companion.getInstance().getResources()
                        .openRawResource(resId);
                Bitmap tempBitmap = BitmapFactory.decodeStream(is2, null,
                        decodeOptions);
                is2.close();
                // If necessary, scale down to the maximal acceptable size.
                if (tempBitmap != null
                        && (tempBitmap.getWidth() > desiredWidth || tempBitmap
                        .getHeight() > desiredHeight)) {
                    bitmap = Bitmap.createScaledBitmap(tempBitmap,
                            desiredWidth, desiredHeight, true);
                    tempBitmap.recycle();
                } else {
                    bitmap = tempBitmap;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap getImageFromFile(File file, int mMaxWidth,
                                          int mMaxHeight) {
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        Bitmap bitmap = null;
        Bitmap.Config preferredConfig = Bitmap.Config.RGB_565;
        try {
            if (mMaxWidth == 0 && mMaxHeight == 0) {
                bitmap = BitmapFactory.decodeFile(file.getPath());
            } else {
                // If we have to resize this image, first get the natural
                // bounds.
                decodeOptions.inJustDecodeBounds = true;
                decodeOptions.inPreferredConfig = preferredConfig;
                bitmap = BitmapFactory
                        .decodeFile(file.getPath(), decodeOptions);
                int actualWidth = decodeOptions.outWidth;
                int actualHeight = decodeOptions.outHeight;

                // Then compute the dimensions we would ideally like to decode
                // to.
                int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight,
                        actualWidth, actualHeight);
                int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth,
                        actualHeight, actualWidth);

                // Decode to the nearest power of two scaling factor.
                decodeOptions.inJustDecodeBounds = false;
                // TODO(ficus): Do we need this or is it okay since API 8
                // doesn't
                // support it?
                // decodeOptions.inPreferQualityOverSpeed =
                // PREFER_QUALITY_OVER_SPEED;
                decodeOptions.inSampleSize = findBestSampleSize(actualWidth,
                        actualHeight, desiredWidth, desiredHeight);
                decodeOptions.inPreferredConfig = preferredConfig;
                Bitmap tempBitmap = BitmapFactory.decodeFile(file.getPath(),
                        decodeOptions);
                // If necessary, scale down to the maximal acceptable size.
                if (tempBitmap != null
                        && (tempBitmap.getWidth() > desiredWidth || tempBitmap
                        .getHeight() > desiredHeight)) {
                    bitmap = Bitmap.createScaledBitmap(tempBitmap,
                            desiredWidth, desiredHeight, true);
                    tempBitmap.recycle();
                } else {
                    bitmap = tempBitmap;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
