package com.news.util.cache;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.StatFs;

import com.news.db.SQLiteHelper;
import com.news.util.base.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * SD卡相关的辅助类
 */
public class SDCardUtils {
    private SDCardUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }


    /**
     * @desc:数据库数据导出
     * @author：Administrator on 2016/4/28 15:28
     */
    public static String  exprotDataBase(Context ct){
       //获取raw目录下的数据文件，没有则创建
       if(isSDCardEnable()){
          File db_file= ct.getDatabasePath(SQLiteHelper.DATABASE_NAME);
          if (db_file.exists()){
              File sd_file = new File(Environment.getExternalStorageDirectory()+"/NewsApp/db/", SQLiteHelper.DATABASE_NAME);
              try {
                  LogUtils.i(db_file.getAbsolutePath());
                  LogUtils.i(sd_file.getAbsolutePath());
                  //判断目标文件所在的目录是否存在
                  if(!sd_file.getParentFile().exists()) {
                      //如果目标文件所在的目录不存在，则创建父目录
                      LogUtils.i("目标文件所在目录不存在，准备创建它！");
                      if(!sd_file.getParentFile().mkdirs()) {
                          LogUtils.i("创建目标文件所在目录失败！");
                      }
                  }
                  sd_file.createNewFile();
                  copyFile(db_file,sd_file);
                  return "数据库文件导入成功!";
              } catch (IOException e) {
                  e.printStackTrace();
                  return "导入失败!";
              }
          }else{
              return "数据库文件未创建!";
          }
       }else {
           return "SD卡不可用！";
       }
    }



    /**
     * @desc:数据库导入
     * @author：Administrator on 2016/4/29 15:56
     */
    public void impotDataBase(){

    }



    /**
     * @desc:copy 文件
     * @author：Administrator on 2016/4/29 16:29
     */
    public static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }

}