package com.artissue.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExhibitionMapper {

    List<ExhibitionDTO> getExhibitionsList(@Param("offset") int offset, @Param("limit") int limit, @Param("keyword") String keyword, @Param("sellCodes") List<String> sellCodes, @Param("locCodes") List<String> locCodes);

    ExhibitionDTO getExhibitionCont(@Param("no") int no);

    List<PriceDTO> getExhibitionPrice(@Param("no") int no);

    ZzimDTO checkZzim(int member_key, int exhibition_key);

    void insertZzim(int member_key, int exhibition_key);

    void deleteZzim(int member_key, int exhibition_key);

    /* (기업마이페이지) 내 전시회 */
    List<ExhibitionDTO> getMyExhibitionsList(int member_key);

}
