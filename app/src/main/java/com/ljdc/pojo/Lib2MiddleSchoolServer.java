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
@DatabaseTable(tableName = "lib2_middle_school_server")
public class Lib2MiddleSchoolServer {
    @DatabaseField(columnName = "lib2_id",generatedId = true)
    public int lib2Id;
    
    @ForeignCollectionField(eager = true)
    public ForeignCollection<LearnLib2Server> learnLib2Server;
    
    @DatabaseField(foreign = true,columnName = "word_id")
    public WordLibServer wordLibServer;
}
