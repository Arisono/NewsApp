package com.news.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.news.model.db.NewEntity;
import com.news.util.base.LogUtils;

import java.sql.SQLException;

/**
 * Created by Administrator on 2016/4/20.
 */
public class SQLiteHelper extends OrmLiteSqliteOpenHelper {

    public static final String DATABASE_NAME = "zhixiao.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        LogUtils.i("onCreate() 数据库第一次创建");
        versionCreate(connectionSource);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }

    private void versionCreate(ConnectionSource connSource) {
        try {
            TableUtils.createTableIfNotExists(connSource, NewEntity.class);

        } catch (SQLException e) {
            LogUtils.i("创建数据库异常！");
            e.printStackTrace();
        }
    }
}
