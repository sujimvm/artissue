$(document).ready(function() {
    ClassicEditor
        .create(document.querySelector('#exhibition_notice'), {
            removePlugins: ['Heading'],
            language: "ko"
        })
        .then(editor => {
            let objEditor = editor;

            editor.model.document.on('change:data', () => {
                $('#exhibition_notice').val(editor.getData());
            }, {priority: 'high'});
        })
        .catch(error => {
            console.error(error);
        });

    ClassicEditor
        .create(document.querySelector('#exhibition_detail_info'), {
            removePlugins: ['Heading'],
            language: "ko"
        })
        .then(editor => {
            let objEditor = editor;

            editor.model.document.on('change:data', () => {
                $('#exhibition_detail_info').val(editor.getData());
            }, {priority: 'high'});
        })
        .catch(error => {
            console.error(error);
        });

    ClassicEditor
        .create(document.querySelector('#exhibition_seller_info'), {
            removePlugins: ['Heading'],
            language: "ko"
        })
        .then(editor => {
            let objEditor = editor;

            editor.model.document.on('change:data', () => {
                $('#exhibition_seller_info').val(editor.getData());
            }, {priority: 'high'});
        })
        .catch(error => {
            console.error(error);
        });

    ClassicEditor
        .create(document.querySelector('#exhibition_product_info'), {
            removePlugins: ['Heading'],
            language: "ko"
        })
        .then(editor => {
            let objEditor = editor;

            editor.model.document.on('change:data', () => {
                $('#exhibition_product_info').val(editor.getData());
            }, {priority: 'high'});
        })
        .catch(error => {
            console.error(error);
        });

    $('#addPrice').on('click', function() {
        var PriceDiv = $('.price-form');

        // 새로운 가격 항목을 위한 div 생성
        var newPriceEntry = $('<div>').attr('class', 'price-entry');

        var priceKeyInput = $('<input>')
            .attr('type', 'hidden')
            .attr('name', 'price_key[]')
            .val('0')

        // 티켓 이름 입력란 생성
        var ticketNameInput = $('<input>')
            .attr('type', 'text')
            .attr('name', 'ticket_name[]')
            .attr('class', 'ticket-option')
            .attr('placeholder', '티켓 이름');

        // 티켓 가격 입력란 생성
        var ticketPriceInput = $('<input>')
            .attr('type', 'number')
            .attr('name', 'ticket_price[]')
            .attr('class', 'ticket-price')
            .attr('placeholder', '가격')
            .attr('min', '0');

        // 삭제 버튼 생성
        var deletePrice = $('<input>')
            .attr('type', 'button')
            .attr('class', 'price-btn')
            .attr('id', 'delete_price')
            .attr('value', '가격 삭제')
            .on('click', function() {
                var deleteKeyInput = $('<input>')

                    .attr('type', 'hidden')
                    .attr('name', 'delete_price_key[]')
                    .val(priceKeyInput.val());

                newPriceEntry.append(deleteKeyInput);
                newPriceEntry.hide();
            });

        // 새로운 div에 입력란과 삭제 버튼 추가
        newPriceEntry.append(priceKeyInput);
        newPriceEntry.append(ticketNameInput);
        newPriceEntry.append(ticketPriceInput);
        newPriceEntry.append(deletePrice);

        // 새로운 div를 price-form에 추가
        PriceDiv.append(newPriceEntry);
    });

    $('.price-form').on('click', '#delete_price', function() {
        var priceEntry = $(this).closest('.price-entry');
        var priceKeyInput = priceEntry.find('input[name="price_key[]"]');
        var deleteKeyInput = $('<input>')
            .attr('type', 'hidden')
            .attr('name', 'delete_price_key[]')
            .val(priceKeyInput.val()); // 삭제할 항목의 price_key 값 설정

        priceEntry.append(deleteKeyInput);
        priceEntry.hide(); // 항목을 화면에서 숨김
    });
});