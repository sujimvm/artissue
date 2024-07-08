$(document).ready(function (){
    let offset = 0;
    let limit = 18;

    // 공고리스트 출력, 조회
    function getExhibitionList(){
        $.ajax({
            url : "/ajax/exhibition", // URL 경로 확인
            type : "get",
            dataType : "json",
            data : {
                "offset" : offset,
                "limit" : limit
            },
            success: function(list){
                const exhibitions = list;

                exhibitions.forEach(function(exhibition) {
                    var row = "<div class='col-12 col-md-6 col-lg-4'>" +
                        "<div class='single-event-area mb-30'>" +
                        "<div class='event-thumbnail'>" +
                        "<img src='"+ exhibition.exhibition_thumnail +"' alt=''>" +
                        "</div>" +
                        "<div class='event-text'>" +
                        "<h4>"+ exhibition.exhibition_title +"</h4>" +
                        "<div class='event-meta-data'>" +
                        "<a href='/exhi/content?no="+exhibition.exhibition_key+"' class='event-place'>" + exhibition.exhibition_place + "</a>" +
                        "<a href='/exhi/content?no="+exhibition.exhibition_key+"' class='event-date'>" + exhibition.exhibition_start_date + " ~ " + exhibition.exhibition_end_date +"</a>" +
                        "</div>" +
                        "<a href='/exhi/content?no="+exhibition.exhibition_key+"' class='btn see-more-btn'>자세히 보기</a>" +
                        "</div>" +
                        "</div>" +
                        "</div>";
                    $('#exhibition-list').append(row);
                });

                offset += limit;

            },
            error: function (error) {
                console.error('Error loading exhibitions:', error);
            }
        });
    }

    // 초기 전시회 목록 로드
    getExhibitionList();

    // 'Load More' 버튼 클릭 이벤트
    $('#load-more').click(function (event) {
        event.preventDefault();
        getExhibitionList();
    });
});