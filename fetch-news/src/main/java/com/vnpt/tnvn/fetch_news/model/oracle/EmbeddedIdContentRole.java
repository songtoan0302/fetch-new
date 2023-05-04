package com.vnpt.tnvn.fetch_news.model.oracle;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class EmbeddedIdContentRole implements Serializable {
    @Column(name = "CONTENT_ID")
    public Long contentId;
    @Column(name = "UNIT_ID")
    public Long unitId;

    public EmbeddedIdContentRole() {
    }

    public EmbeddedIdContentRole(Long contentId, Long unitId) {
        this.contentId = contentId;
        this.unitId = unitId;
    }
}