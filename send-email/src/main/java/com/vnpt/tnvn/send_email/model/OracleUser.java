package com.vnpt.tnvn.send_email.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "USERS")
public class OracleUser {
    @Id
    @Column(name = "ID")
    public Long id;
    @Column(name = "USERNAME")
    public String username;
    @Column(name = "FULLNAME")
    public String fullname;
    @Column(name = "EMAIL")
    public String email;
}
