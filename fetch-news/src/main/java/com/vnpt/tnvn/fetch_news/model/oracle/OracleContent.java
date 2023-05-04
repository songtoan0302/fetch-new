package com.vnpt.tnvn.fetch_news.model.oracle;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "TNVN_CONTENT")
public class OracleContent {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_ID")
    @SequenceGenerator(name = "S_ID", sequenceName = "S_CONTENT", allocationSize = 1)
    public long id;
    @Column(name = "TITLE")
    public String title;
    @Column(name = "DESCRIPTION")
    public String description;
    @Column(name = "INFO")
    public String info;
    @Column(name = "IMAGE")
    public String image;
    @Column(name = "AUTHOR")
    public String author;
    @Column(name = "HASH_TAG")
    public String hashtag;
    @Column(name = "STATUS")
    public int status;
    @Column(name = "SOURCE")
    public String source;
    @Column(name = "CREATE_TIME")
    public Date createTime;
    @Column(name = "PRIORITY")
    public int priority;
    @Column(name = "ON_MAIN_PAGE")
    public int onMainPage;
    @Column(name = "SYNCH_STATUS")
    public int syncStatus;
    @Column(name = "VIEW_COUNT")
    public int viewCount;
    @Column(name = "WEB_VIEW_LINK")
    public String webViewLink;
    @Column(name = "ORIGINAL_ID")
    public String originalId;
    @Column(name = "UPDATE_TIME")
    public Date updateTime;
    @Column(name = "USER_ID_CREATE")
    public String userIdCreate;
    @Column(name = "USER_ID_APPROVE")
    public String userIdApprove;
    @Column(name = "ISALL")
    public int isAll;
}