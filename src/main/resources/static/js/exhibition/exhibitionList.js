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
                    let start_date = new Date(exhibition.exhibition_start_date);
                    let end_date = new Date(exhibition.exhibition_end_date);

                    let exhibition_start_date = start_date.getFullYear() +
                        '-' + ( (start_date.getMonth()+1) <= 9 ? "0" + (start_date.getMonth()+1) : (start_date.getMonth()+1) )+
                        '-' + ( (start_date.getDate()) <= 9 ? "0" + (start_date.getDate()) : (start_date.getDate()) );

                    let exhibition_end_date = end_date.getFullYear() +
                        '-' + ( (end_date.getMonth()+1) <= 9 ? "0" + (end_date.getMonth()+1) : (end_date.getMonth()+1) )+
                        '-' + ( (end_date.getDate()) <= 9 ? "0" + (end_date.getDate()) : (end_date.getDate()) );

                    var row = "" +
                        "<div class='col-12 col-md-6 col-lg-4'>" +
                            "<div class='single-event-area mb-30'>" +
                                "<div class='event-thumbnail'>" +
                                    "<a href='/exhi/content?no="+exhibition.exhibition_key+"'><img src='"+ exhibition.exhibition_thumnail +"' alt=''></a>" +
                                "</div>" +
                                "<div class='event-text'>" +
                                    "<a class='exhi-title' href='/exhi/content?no="+exhibition.exhibition_key+"'><h4>"+ exhibition.exhibition_title +"</h4></a>" +
                                    "<div class='event-meta-data'><a style='color: transparent;'></a>" +
                                        "<a href='/exhi/content?no="+exhibition.exhibition_key+"' class='event-place'>" + exhibition.exhibition_place + "</a><br>" +
                                        "<a href='/exhi/content?no="+exhibition.exhibition_key+"' class='event-date'>" + exhibition_start_date + " ~ " + exhibition_end_date +"</a>" +
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

    $('#search-keyword').keyup(function (e) {
        keyword = $('#search-keyword').val();

        // 기존 목록 초기화
        $('#exhibition-list').empty();
        offset = 0;

        getExhibitionList(keyword);
    });

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