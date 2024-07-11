// 화면 시작
var exhibitionKey = $("#exhibition_key").val();
var memberKey = /*[[${session.mDTO.member_key}]]*/ null;
console.log(exhibitionKey)
console.log(memberKey)

$(document).ready(function() {
    $(document).on('click', '#reserveOpenBt', function() {
        var url = "/reserve/open?No="+$("#exhibition_key").val();
        var name = "reserve";
        var option = "width = 1000, height = 500, top = 100, left = 200, location = no"
        window.open(url, name, option);
    });

    $(document).on('click', '.nav-menu', function () {
        // 모든 탭의 'is-active' 클래스 제거
        $('.nav-menu').removeClass('is-active');
        // 클릭된 탭에 'is-active' 클래스 추가
        $(this).addClass('is-active');

        // 모든 콘텐츠 숨기기
        $('.content').hide();
        // 클릭된 탭에 해당하는 콘텐츠 표시
        const contentId = $(this).attr('id').replace('-tab', '');
        $('#' + contentId).show();
    });

    $("#review-btn").on('click',function (e){
        e.preventDefault();

        var reviewTitle = $('#review-title').val();
        var reviewContent = $('#review-cont').val();
        var reviewScore = $('input[name="rating"]:checked').val();

        $.ajax({
            url : '/ajax/writeReview',
            type : 'POST',
            data : {
                title : reviewTitle,
                cont : reviewContent,
                score : reviewScore,
                exhibitionKey : exhibitionKey
            },
            success : function (review){

            }
        })
    })

    $('#oneMusic-chk').click(function(){
        changeHeart();

    });

});

function changeHeart() {
    var heart = $('#heart');
    var currentSrc = heart.attr('src');

    if (currentSrc.includes('interest_icon1.png')) {
        heart.attr('src', '/img/interest_icon3.png'); // 클릭시 전환할 이미지
    } else {
        heart.attr('src', '/img/interest_icon1.png'); // 다시 기본 이미지로 전환
    }
}
