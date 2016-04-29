package com.news.db.dao;

import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.news.app.Application;
import com.news.db.SQLiteHelper;
import com.news.model.db.ChannelEntity;
import com.news.model.db.ImageUrls;
import com.news.model.db.NewEntity;
import com.news.util.base.ListUtils;
import com.news.util.base.LogUtils;
import com.news.util.base.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 */
public class NewsDao {

    private OrmLiteSqliteOpenHelper helper;
    private static NewsDao instance = null;

    public static final NewsDao getInstance() {
        if (instance == null) {
            synchronized (NewsDao.class) {
                if (instance == null) {
                    instance = new NewsDao();
                }
            }
        }
        return instance;
    }

    private Dao<NewEntity, String> newsDao;
    private Dao<ImageUrls, String> imageUrlDao;
    private Dao<ChannelEntity, String> channelDao;

    public NewsDao() {
        helper = OpenHelperManager.getHelper(Application.getInstance(), SQLiteHelper.class);
        try {
            newsDao = DaoManager.createDao(helper.getConnectionSource(), NewEntity.class);
            imageUrlDao = DaoManager.createDao(helper.getConnectionSource(), ImageUrls.class);
            channelDao = DaoManager.createDao(helper.getConnectionSource(), ChannelEntity.class);
        } catch (SQLException e) {
            LogUtils.i("NewDao创建异常...");
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        OpenHelperManager.releaseHelper();
    }


    /**
     * @desc:批量保存新闻数据
     * @author：Administrator on 2016/4/20 17:17
     */
    public void saveAll(List<NewEntity> newlists) {
        //保存新闻数据,还需要保存它的图片信息
        if (!ListUtils.isEmpty(newlists)) {
//            helper.getReadableDatabase().beginTransaction();
            for (int i = 0; i < newlists.size(); i++) {
                try {
                    newsDao.createIfNotExists(newlists.get(i));
                    List<ImageUrls> imageUrlses = newlists.get(i).getImageurls();
                    if (!ListUtils.isEmpty(imageUrlses)) {
                        for (int j = 0; j < imageUrlses.size(); j++) {
                            ImageUrls model = imageUrlses.get(j);
                            model.setNewsId(newlists.get(i).getId());
                            imageUrlDao.createIfNotExists(model);
                        }
                    }
                } catch (SQLException e) {
                    LogUtils.i("批量保存异常！" + newlists.get(i).getTitle());
                    e.printStackTrace();
                }
            }
//                helper.getReadableDatabase().setTransactionSuccessful();
//                helper.getReadableDatabase().endTransaction();
        }

    }


    /**
     * @desc:根据条件查询新闻数据
     * @author：Administrator on 2016/4/20 17:21
     */
    public List<NewEntity> findAllNews() {
        List<NewEntity> lists = new ArrayList<>();
        QueryBuilder<NewEntity, String> bulider = newsDao.queryBuilder();
        bulider.orderBy("pubDate", false);
        try {
            return newsDao.query(bulider.prepare());
        } catch (SQLException e) {
            LogUtils.i("查找异常！");
            e.printStackTrace();
        }
        return lists;
    }

    /**
     * @desc:根据频道ID查询新闻数据 支持分页
     * @author：Administrator on 2016/4/21 16:42
     */
    public List<NewEntity> findNewsByChannelId(String channelId, int page) {
        int pageSize = 20;
        page = page - 1;
        long offset = page * pageSize;
        long limit = offset + pageSize-1;

        List<NewEntity> lists = new ArrayList<>();
        QueryBuilder<NewEntity, String> queryBuilder = newsDao.queryBuilder();
        try {
            queryBuilder.where().eq("channelId", channelId);
            LogUtils.i("page:" + page);
            LogUtils.i("offset:" + offset);
            LogUtils.i("limit:"+limit);
            queryBuilder.offset(offset);
            queryBuilder.limit(limit);
            return newsDao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }
        return lists;
    }


    /**
     * @desc:查询图片根据新闻id
     * @author：Administrator on 2016/4/22 15:38
     */
    public List<ImageUrls> findImagesByNewsId(int newsId) {
        List<ImageUrls> images = new ArrayList<>();
        QueryBuilder<ImageUrls, String> queryBuilder = imageUrlDao.queryBuilder();
        try {
            queryBuilder.where().eq("newsId", newsId);
            return imageUrlDao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            LogUtils.i("findImagesByNewsIdy  查询图片异常 146");
            e.printStackTrace();
        }
        return images;
    }


    /**
     * @desc:保存频道数据
     * @author：Administrator on 2016/4/22 16:14
     */
    public void saveChannel(List<ChannelEntity> channels) {
        if (!ListUtils.isEmpty(channels)) {
            helper.getWritableDatabase().beginTransaction();
            for (int i = 0; i < channels.size(); i++) {
                try {
                    channelDao.createIfNotExists(channels.get(i));
                } catch (SQLException e) {
                    LogUtils.i("保存频道异常：" + channels.get(i).getName());
                    e.printStackTrace();
                }
            }
            helper.getWritableDatabase().setTransactionSuccessful();
            helper.getWritableDatabase().endTransaction();
        }
    }


    /**
     * @desc:查询频道数据
     * @author：Administrator on 2016/4/22 16:22
     */
    public List<ChannelEntity> findAllChannel() {
        List<ChannelEntity> channels = new ArrayList<>();
        QueryBuilder<ChannelEntity, String> queryBuilder = channelDao.queryBuilder();
        try {
            return channelDao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            LogUtils.i("查询频道异常");
            e.printStackTrace();
        }
        return channels;
    }
}
