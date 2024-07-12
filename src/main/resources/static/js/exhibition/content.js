// 화면 시작
var exhibitionKey = $("#exhibition_key").val();
var memberKey = /*[[${session.mDTO.member_key}]]*/ null;
console.log(exhibitionKey)
console.log(memberKey)

$(document).ready(function() {

    changeHeart();

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

        if(reviewScore == null){
            alert("별점을 선택해 주세요!")
            return
        }

        if(confirm("리뷰를 등록하시겠습니까?")){
            $.ajax({
                url : '/ajax/writeReview',
                type : 'POST',
                data : {
                    title : reviewTitle,
                    cont : reviewContent,
                    score : reviewScore,
                    exhibitionKey : exhibitionKey
                },
                success : function (result){
                    if(result == 1){
                        alert("리뷰를 등록하였습니다!")
                        location.reload();
                    }else if(result < 0){
                        alert("예매를 하지 않은 전시회는 리뷰를 작성할수 없습니다.")
                    }else {
                        alert("이미 리뷰를 쓰셨습니다.")
                    }
                },error: function(xhr, status, error) {
                    console.error(xhr);
                }
            })
        }
    })

    $('#oneMusic-chk').click(function(){
        debugger;
        $.ajax({
            url : '/ajax/zzimCheck',
            type: 'get',
            data: {
                exhibition_key : exhibitionKey
            },
            success : function (result){
                debugger;
                if(result < 0){
                    if(confirm("찜 리스트에서 삭제하시겠습니까?")){
                        $.ajax({
                            url: '/ajax/zzimDelete',
                            type: 'post',
                            async: false,
                            data: {
                                exhibition_key : exhibitionKey
                            },
                            success : function (result){
                                alert("찜 리스트에서 삭제 되었습니다.")
                                changeHeart();
                                location.reload();
                            }
                        })
                    }
                }else{
                    if(confirm("찜 리스트에 추가하시겠습니까?")){
                        $.ajax({
                            url: '/ajax/zzimAdd',
                            type : 'post',
                            async: false,
                            data: {
                                exhibition_key : exhibitionKey
                            },
                            success : function (result){
                                alert("찜 리스트에 추가되었습니다.")
                                changeHeart();
                                location.reload();
                            }
                        })
                    }
                }
            },error: function(xhr, status, error) {
                console.error(xhr);
            }
        })

    });

    $('#rewriteReview').on('click', function() {
        $('#reWriteReview-form').toggle(); // display 상태를 토글합니다.
    });

    $('#re-review-btn').on('click', rewriteReview);

});

function changeHeart() {
    var heart = $('#heart');

    $.ajax({
        url : '/ajax/zzimCheck',
        type: 'get',
        data: {
            exhibition_key : exhibitionKey
        },
        success : function (result){
            if(result < 0){
                heart.attr('src', '/img/interest_icon3.png');
            }else{
                heart.attr('src', '/img/interest_icon1.png'); // 클릭시 전환할 이미지
            }
        }
    })

}

function rewriteReview(e){
    e.preventDefault();

    var reviewTitle = $('#re-review-title').val();
    var reviewContent = $('#re-review-cont').val();
    var reviewScore = $('input[name="re-rating"]:checked').val();

    if(reviewScore == null){
        alert("별점을 선택해 주세요!")
        return
    }

    if(confirm("리뷰를 수정하시겠습니까?")){
        $.ajax({
            url : '/ajax/reWriteReview',
            type : 'POST',
            data : {
                review_title : reviewTitle,
                review_cont : reviewContent,
                review_score : reviewScore,
                exhibition_key : exhibitionKey
            },
            success : function (result){
                if(result == 1){
                    alert("리뷰를 수정하였습니다!")
                    location.reload();
                }else {
                    alert("리뷰 수정에 실패하였습니다.")
                }
            },error: function(xhr, status, error) {
                console.error(xhr);
            }
        });
    }
}
