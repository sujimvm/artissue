package com.artissue.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ReservationMapper {
    List<PriceDTO> getPriceList(int exhibition_key);
    int addReservation(ReservationDTO dto);

    List<ReservationDTO> getReserveList(String reservation_id);
    void updateReservePay(String reservation_id);
    void deleteReserveFail(HashMap<String,String> map);

    ReservationDTO checkReservation(int member_key, int exhibition_key);

    void insertQrCode(String reservation_id, String qrCode);

    List<MemberDTO> getMyExhibitionsReservationList(int reservation_id,int exhibition_key);

    int updateReservationBV(String reservation_id);

    void upPaymentKey(@Param("reservation_id") String reservationId, @Param("paymentKey") String paymentKey);
}
