package com.vnpt.tnvn.fetch_news.model.oracle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "TNVN_UNIT")
public class OracleRole {
    @Id
    @Column(name = "ID")
    public Long id;
    @Column(name = "UNIT_NAME")
    public String name;
}