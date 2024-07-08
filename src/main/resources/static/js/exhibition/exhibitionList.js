$(document).ready(function (){
    let offset = 0;
    let limit = 18;
    let keyword = "";
    let sellCodes = [];
    let locCodes = [];

    // 공고리스트 출력, 조회
    function getExhibitionList(){
        $.ajax({
            url : "/ajax/exhibition", // URL 경로 확인
            type : "get",
            dataType : "json",
            data : {
                "offset" : offset,
                "limit" : limit,
                "keyword" : keyword,
                "sellCodes" : sellCodes,
                "locCodes" : locCodes
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

    function keywordSearch() {
        keyword = $('#search-keyword').val();

        // 기존 목록 초기화
        $('#exhibition-list').empty();
        offset = 0;

        getExhibitionList(keyword);
    }
    function filterList(){

        $('input[name="sellCode"]:checked').each(function() {
            sellCodes.push($(this).val());
        });

        $('input[name="locCode"]:checked').each(function() {
            locCodes.push($(this).val());
        });

        // 기존 목록 초기화
        $('#exhibition-list').empty();
        offset = 0;

        getExhibitionList(sellCodes, locCodes);
    }

    // 필터 초기화 함수
    function filterReset() {
        $('#search-keyword').val('');
        $('input[name="sellCode"]').prop('checked', false);
        $('input[name="locCode"]').prop('checked', false);

        // 기존 목록 초기화
        $('#exhibition-list').empty();
        offset = 0;
        keyword = "";
        sellCodes = [];
        locCodes = [];

        getExhibitionList(keyword, sellCodes, locCodes);
    }

    // 검색 버튼 클릭 이벤트
    $('input[value="검색"]').click(function() {
        keywordSearch();
    });

    $('input[value="조건검색"]').click(function (){
        filterList();
    })

    // 초기화 버튼 클릭 이벤트
    $('input[value="초기화"]').click(function() {
        filterReset();
    });
});