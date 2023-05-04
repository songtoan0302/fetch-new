package com.vnpt.tnvn.send_email.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class EmbeddedIdUserCategory implements Serializable {
    @Column(name = "USER_ID")
    public Long userId;
    @Column(name = "CATEGORY_ID")
    public Long categoryId;
}
