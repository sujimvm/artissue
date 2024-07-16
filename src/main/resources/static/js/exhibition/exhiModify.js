$(document).ready(function() {
    ClassicEditor
        .create(document.querySelector('#exhibition_notice'), {
            removePlugins: ['Heading'],
            language: "ko"
        })
        .then(editor => {
            editor.ui.view.editable.element.classList.add('custom-editor-height-200');
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
            editor.ui.view.editable.element.classList.add('custom-editor-height-500');
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
            editor.ui.view.editable.element.classList.add('custom-editor-height-400');
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
            editor.ui.view.editable.element.classList.add('custom-editor-height-400');
            let objEditor = editor;

            editor.model.document.on('change:data', () => {
                $('#exhibition_product_info').val(editor.getData());
            }, {priority: 'high'});
        })
        .catch(error => {
            console.error(error);
        });

    $('#addPrice').on('click', function() {
        var newPriceDiv = $('<div class="ticket-price"></div>');

        var priceKeyInput = $('<input>')
            .attr('type', 'hidden')
            .attr('name', 'price_key[]')
            .val('0'); // 새로운 가격 추가 시 기본값 0

        var ticketNameInput = $('<input>')
            .attr('name', 'ticket_name[]')
            .attr('placeholder', '티켓 이름');

        var ticketPriceInput = $('<input>')
            .attr('type', 'number')
            .attr('name', 'ticket_price[]')
            .attr('placeholder', '가격')
            .attr('min', '0');

        newPriceDiv.append(priceKeyInput);
        newPriceDiv.append(ticketNameInput);
        newPriceDiv.append(ticketPriceInput);

        $('#new_price_form').append(newPriceDiv);
    });
});