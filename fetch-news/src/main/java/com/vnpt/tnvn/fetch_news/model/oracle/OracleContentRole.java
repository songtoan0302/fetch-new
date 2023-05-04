package com.vnpt.tnvn.fetch_news.model.oracle;

import javax.persistence.*;

@Entity(name = "CONTENT_UNIT")
public class OracleContentRole {
    @EmbeddedId
    public EmbeddedIdContentRole embeddedId;
}