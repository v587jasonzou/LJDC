package com.ljdc.pojo;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Created with IntelliJ IDEA
 * User:邹旭
 * Date:2017/2/22 0022
 * Time:下午 7:57
 * Desc:略
 */
@DatabaseTable(tableName = "user_server")
public class UserServer {
    @DatabaseField(columnName = "user_id", generatedId = true)
    public int userId;

    @DatabaseField(columnName = "phone")
    public String phone;

    @DatabaseField(columnName = "password")
    public String password;

    @DatabaseField(columnName = "email")
    public String email;

    @DatabaseField(columnName = "nickname")
    public String nickname;

    @DatabaseField(columnName = "head_image_url")
    public String headImageUrl;

    @ForeignCollectionField(eager = true)
    public ForeignCollection<LearnLib1Server> learnLib1Servers;

    @ForeignCollectionField(eager = true)
    public ForeignCollection<LearnLib2Server> learnLib2Servers;

    @ForeignCollectionField(eager = true)
    public ForeignCollection<WordDevelopmentServer> wordDevelopmentServers;

}
