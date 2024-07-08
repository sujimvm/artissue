// 화면 시작
$(document).ready(function() {

    $(document).on('click', '#reserveBt', function() {
        sendReserve();
    });
});

function sendReserve() {
    var reserveFrom = $("#reserveFrom").serialize();

    $.ajax({
        url: '/reserve/sendReserve',
        type: 'post',
        dataType: 'json',
        data: reserveFrom,
        success: function() {
            location.reload();
        },error: function(xhr, status, error) {
            console.error(xhr);
        }
    });
}