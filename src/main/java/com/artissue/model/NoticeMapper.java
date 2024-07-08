package com.artissue.model;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {

    List<NoticeDTO> noticeList();

    int noticeInsert(NoticeDTO noticeDTO);

    NoticeDTO noticeCont(int notice_no);

    int noticeModify(NoticeDTO noticeDTO);

    int noticeDelete(int notice_no);
}
