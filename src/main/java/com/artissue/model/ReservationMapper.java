package com.artissue.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReservationMapper {
    List<PriceDTO> getPriceList(int exhibition_key);
    int addReservation(ReservationDTO dto);

    List<ReservationDTO> getReserveList(String reservation_id);
}
