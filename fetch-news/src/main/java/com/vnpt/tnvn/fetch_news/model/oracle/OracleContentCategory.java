package com.vnpt.tnvn.fetch_news.model.oracle;

import javax.persistence.*;

@Entity(name = "CONTENT_CATEGORY")
public class OracleContentCategory {
    @Id
    @Column(name = "CONTENT_ID")
    public Long contentId;
    @Column(name = "CATEGORY_ID")
    public Long categoryId;
    @Column(name = "ORD")
    public Long ord;
}