package com.artissue.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExhibitionMapper {

    List<ExhibitionDTO> getExhibitionsList(@Param("offset") int offset, @Param("limit") int limit);

    ExhibitionDTO getExhibitionCont(@Param("no") int no);

}
