package com.news.model.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2016/4/19.
 */
@DatabaseTable(tableName = "tbl_channels")
public class ChannelEntity {

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField(columnName = "channelId",canBeNull = false,unique =true)
    public String channelId;
    @DatabaseField(columnName = "name",canBeNull =false)
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
