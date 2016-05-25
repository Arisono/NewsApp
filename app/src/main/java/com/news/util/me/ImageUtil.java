package com.news.util.me;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Arisono on 2016/5/25.
 * 处理图片工具类
 */
public class ImageUtil {


    /**
     *在部分Android手机（如MT788、Note2）上，
     * 使用Camera拍照以后，得到的照片会被自动旋转（90°、180°、270°）
     * ，这个情况很不符合预期。仔细分析了一下，
     * 因为照片属性中是存储了旋转信息的，
     * 所以要解决这个问题，可以在onActivityResult方法中，
     * 获取到照片数据后，读取它的旋转信息，如果不是0，
     * 说明这个照片已经被旋转过了，那么再使用android.graphics.Matrix将照片旋转回去即可。*/

    /**
     * @author Administrator
     * @功能:兼容性三星等手机旋转问题+等比例缩放
     */
    public static Bitmap roateBitmapAndScale(Bitmap bitmap, int degree, int newWidth ,int newHeight ) {
        if (degree == 0) {
            return bitmap;
        }
        // 获得图片的宽高
        int width = bitmap.getWidth();
       // int height = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        //float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree); //解决旋转问题
        matrix.postScale(scaleWidth, scaleWidth);//等比例缩放
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bmp;
    }


    /**
     * 将图片按照某个角度进行旋转
     * @param bm  需要旋转的图片
     * @param degree  旋转角度 getBitmapDegree()
     * @return 旋转后的图片  无等比例缩放
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 读取图片的旋转的角度
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }



  /**
   * @功能:计算图片的缩放值
   * @param:
   * @author:Arisono
   */
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }





    /**
     * 压缩已存在的图片对象，并返回压缩后的图片(压缩质量，压缩尺寸函数)
     * @param bitmap ：图片对象
     * @param quality:1-100;100表示不质量压缩
     * @param reqsW：压缩宽度
     * @param reqsH：压缩高度
     * @return
     */
    public final static Bitmap compressBitmap(Bitmap bitmap, int quality,int reqsW, int reqsH) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);//压缩质量
            byte[] bts = baos.toByteArray();
            Bitmap res = compressBitmapWithByte(bts, reqsW, reqsH);//压缩尺寸
            baos.close();
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return bitmap;
        }
    }


    /**
     * 压缩指定byte[]图片，并得到压缩后的图像
     * @param bts
     * @param reqsW
     * @param reqsH
     * @return
     */
    public final static Bitmap compressBitmapWithByte(byte[] bts, int reqsW, int reqsH) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bts, 0, bts.length, options);
        options.inSampleSize =   calculateInSampleSize(options, reqsW, reqsH);//计算尺寸比例
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bts, 0, bts.length, options);
    }

    /**
     * @功能:压缩指定filePath图片，并得到压缩后的图像
     * @author:Arisono
     * @param:filePath
     * @return: Bitmap
     */
    public static Bitmap compressBitmapWithfilePath(String filePath,int reqsW, int reqsH) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize =   calculateInSampleSize(options, reqsW, reqsH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
}
