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
@DatabaseTable(tableName = "lib1")
public class Lib1EnglishGrand4CoreServer {
    @DatabaseField(columnName = "lib1Id",generatedId = true)
    public int lib1Id;

    @ForeignCollectionField(eager = true)
    public ForeignCollection<LearnLib1Server> learnLib1Server;
    
    @DatabaseField(foreign = true,columnName = "wordId")
    public WordLibServer wordLibServer;

}
