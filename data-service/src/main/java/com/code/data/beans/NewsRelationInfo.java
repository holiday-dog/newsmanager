package com.code.data.beans;

import lombok.Data;

@Data
public class NewsRelationInfo {
    private Long id;

    private Long newsId;

    private String newsRelationTitle;

    private String newsRelationUrl;
}
