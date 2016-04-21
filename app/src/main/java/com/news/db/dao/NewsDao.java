package com.news.db.dao;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.news.app.Application;
import com.news.db.SQLiteHelper;
import com.news.model.db.NewEntity;
import com.news.util.base.ArrayUtils;
import com.news.util.base.ListUtils;
import com.news.util.base.LogUtils;

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

    private Dao<NewEntity,String> newsDao;

    public NewsDao() {
         helper = OpenHelperManager.getHelper(Application.getInstance(), SQLiteHelper.class);
        try {
            newsDao = DaoManager.createDao(helper.getConnectionSource(), NewEntity.class);
        } catch (SQLException e) {
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
    public void saveAll(List<NewEntity> newlists){
        if (!ListUtils.isEmpty(newlists)){
            helper.getReadableDatabase().beginTransaction();
            try {
            for (int i=0;i<newlists.size();i++){
                    newsDao.createIfNotExists(newlists.get(i));
            }
                helper.getReadableDatabase().setTransactionSuccessful();
            } catch (SQLException e) {
                LogUtils.i("批量保存异常！");
                e.printStackTrace();
            }finally {
                helper.getReadableDatabase().endTransaction();
            }
        }

    }
    
    
    /**
     * @desc:根据条件查询新闻数据
     * @author：Administrator on 2016/4/20 17:21
     */
    public List<NewEntity> findAllNews(){
        List<NewEntity> lists=new ArrayList<>();
        QueryBuilder<NewEntity,String> bulider=newsDao.queryBuilder();
        bulider.orderBy("pubDate",false);
        try {
            return newsDao.query(bulider.prepare());
        } catch (SQLException e) {
            LogUtils.i("查找异常！");
            e.printStackTrace();
        }
        return  lists;
    }

    /**
     * @desc:根据频道ID查询新闻数据 支持分页
     * @author：Administrator on 2016/4/21 16:42
     */
    public List<NewEntity> findNewsByChannelId(String channelId,int page){
        int pageSize=20;
        page=page-1;
        long offset=page*pageSize;
        long limit=offset+pageSize;

        List<NewEntity> lists=new ArrayList<>();
        QueryBuilder<NewEntity,String> queryBuilder=newsDao.queryBuilder();
        try {
            queryBuilder.where().eq("channelId",channelId);
            queryBuilder.offset(offset);
            queryBuilder.limit(limit);
            return  newsDao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  lists;
    }

}
