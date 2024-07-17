// 화면 시작
$(document).ready(function() {
    $(document).on('click', '#reserveBt', function() {
        if($("#totalPriceCk").val() > 0){
            $("#reserveFrom").submit();
        }else{
            alert("예매권을 선택해주세요.");
        }
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
        $("#totalPrice").text(totalPrice+"원");
        $("#totalPriceCk").val(totalPrice);
    });
});