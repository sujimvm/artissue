package com.artissue.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReservationMapper {
    List<PriceDTO> getPriceList(int exhibition_key);
    int addReservation(ReservationDTO dto);

    List<ReservationDTO> getReserveList(String reservation_id);
    int updateReservePay(String reservation_id);

    ReservationDTO checkReservation(int member_key, int exhibition_key);

    void insertQrCode(String reservation_id, String qrCode);

    List<MemberDTO> getMyExhibitionsReservationList(int reservation_id,int exhibition_key);

    int updateReservationBV(String reservation_id);
}
