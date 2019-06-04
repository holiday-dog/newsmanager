package com.code.common.bean;

import lombok.Data;

import java.util.List;

@Data
public class PageBean<T> {
    private Long totalPage;

    private Integer currentPage;

    private List<T> infoList;
}
