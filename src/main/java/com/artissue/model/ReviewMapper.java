package com.artissue.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {

    List<ReviewDTO> getExhibitionReview(@Param("no") int no);

    ReviewDTO checkReview(int member_key, int exhibition_key);

    int writeReview(ReviewDTO reviewDTO);

    int updateReview(int review_key, String review_title, String review_cont, int review_score);
}
