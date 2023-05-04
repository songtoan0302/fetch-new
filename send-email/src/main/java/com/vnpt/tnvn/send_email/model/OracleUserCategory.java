package com.vnpt.tnvn.send_email.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name = "USERS_CATEGORY")
public class OracleUserCategory {
    @EmbeddedId
    public EmbeddedIdUserCategory embedded;
}
