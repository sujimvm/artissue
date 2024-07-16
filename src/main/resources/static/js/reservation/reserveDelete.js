    function deleteReservation(element) {
    var reservationId = element.getAttribute('data-reservation-id');
    console.log('Deleting reservation with ID:', reservationId); // 로그 출력

    $.ajax({
    url: '/ajax/updateReservation',  // URL을 '/api/updateReservation'으로 수정
    type: 'POST',
    data: { reservation_id: reservationId },  // URL 인코딩된 형식으로 데이터 전송
    success: function(response) {
    if (response.success) {
    alert('삭제 성공');
    location.reload();
} else {
    alert('삭제 실패: ' + response.message);
}
},
    error: function(error) {
    console.log(error);
    alert('서버 오류가 발생했습니다.');
}
});
}
