package com.news.model.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2016/4/19.
 */
@DatabaseTable(tableName = "tbl_imageUrls")
public class ImageUrls {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "url")
    private String url;
    @DatabaseField(columnName = "height")
    private int height;
    @DatabaseField(columnName = "width")
    private int width;
    @DatabaseField(columnName = "newsId")
    private int newsId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
