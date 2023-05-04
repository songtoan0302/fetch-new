package com.vnpt.tnvn.send_email.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "CATEGORY")
public class OracleCategory {
    @Id
    @Column(name = "ID")
    public Long id;
    @Column(name = "CATNAME")
    public String name;
    public Long ord;
}