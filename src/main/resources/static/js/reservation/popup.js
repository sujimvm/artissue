// 화면 시작
$(document).ready(function() {

    $(document).on('click', '#reserveBt', function() {
        sendReserve();
    });
    $(document).on('change', 'select[name=\'reservation_count_str\']', function() {
        var count = $(this).val().split('/')[0];
        var key = $(this).val().split('/')[1];

        $("#reservation_price_"+key).val($("#price_"+key).val() * count);

        var totalPrice = 0;
        var inputCnt = $("[name='reservation_price_str']").length;
        for(var i =0;i<inputCnt;i++){
            totalPrice += Number($("[name='reservation_price_str']").eq(i).val());
        }
        $("#totalPrice").empty();
        $("#totalPrice").text(totalPrice);
    });
});

function sendReserve() {
    var reserveFrom = $("#reserveFrom").serialize();

    $.ajax({
        url: '/reserve/sendReserve',
        type: 'post',
        data: reserveFrom,
        success: function() {
            window.close();
        },error: function(xhr, status, error) {
            console.error(xhr);
        }
    });
}