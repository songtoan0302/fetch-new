package com.vnpt.tnvn.send_email.model;

import javax.persistence.*;

@Entity(name = "QUESTIONS")
public class OracleQuestion {
    @Id
    @Column(name = "ID")
    public Long id;
    @Column(name = "QUESTION_TITLE")
    public String title;
    @Column(name = "ACCOUNT_ID")
    public Long accountId;
    @Column(name = "CATEGORY_ID")
    public Long categoryId;
    @Column(name = "ISANSWER")
    public Integer isAnswer;
    @Column(name = "FILE_PATH_1")
    public String filePath1;
    @Column(name = "FILE_PATH_2")
    public String filePath2;
    @Column(name = "FILE_PATH_3")
    public String filePath3;
    @Column(name = "QUESTION_CONTENT")
    public String content;
    @Column(name = "IS_NOTIFIED")
    public Integer isNotified;
}
