package com.vnpt.tnvn.save_log.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "TNVN_EVENT")
public class OracleLogEvent {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    @Column(name = "ACTION")
    public String action;
    @Column(name = "API")
    public String api;
    @Column(name = "CREATE_TIME")
    public Timestamp createTime;
    @Column(name = "DELTATIME")
    public Long deltaTime;
    @Column(name = "REQUEST")
    public String request;
    @Column(name = "RESPONSE")
    public String response;
    @Column(name = "TIME")
    public Timestamp startTime;
    @Column(name = "USERNAME")
    public String username;
}
