package com.artissue.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {

    List<ReviewDTO> getExhibitionReview(@Param("no") int no);

    int writeReview(ReviewDTO reviewDTO);
}
