$(document).ready(function (){
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

    $('#addPrice').on('click', function (){
        var newPriceDiv = $('<div class="ticket-price"></div>');

        // Create the ticket name input
        var ticketNameInput = $('<input>')
            .attr('type', 'text')
            .attr('name', 'ticket_name[]')
            .attr('placeholder', '티켓 이름');

        // Create the ticket price input
        var ticketPriceInput = $('<input>')
            .attr('type', 'number')
            .attr('name', 'ticket_price[]')
            .attr('placeholder', '가격')
            .attr('min', '0');

        // Append the inputs to the new div
        newPriceDiv.append(ticketNameInput);
        newPriceDiv.append(ticketPriceInput);

        // Append the new div to the price_form div
        $('#price_form').append(newPriceDiv);
    })
});