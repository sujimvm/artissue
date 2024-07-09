// 화면 시작
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

    $('.contentTitle h3').each(function(){
        $(this).html($(this).html().replace(/class="contentTitle"/g,'class=""'));
    });
});