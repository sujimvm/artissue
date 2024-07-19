$(document).ready(function (){
    ClassicEditor
        .create(document.querySelector('#exhibition_notice'), {
            removePlugins: ['Heading'],
            language: "ko",
            blockElements: 'div'
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
            language: "ko",
            blockElements: 'div'
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
            language: "ko",
            blockElements: 'div'
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
            language: "ko",
            blockElements: 'div'
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

    $('#addPrice').on('click', function (){
        var PriceDiv = $('.price-form');

        // Create a new div to hold the ticket name, price inputs and delete button
        var newPriceEntry = $('<div>').attr('class', 'price-entry');

        // Create the ticket name input
        var ticketNameInput = $('<input>')
            .attr('type', 'text')
            .attr('name', 'ticket_name[]')
            .attr('class', 'ticket-option')
            .attr('placeholder', '티켓 이름');

        // Create the ticket price input
        var ticketPriceInput = $('<input>')
            .attr('type', 'number')
            .attr('name', 'ticket_price[]')
            .attr('class', 'ticket-price')
            .attr('placeholder', '가격')
            .attr('min', '0');

        // Create the delete button
        var deletePrice = $('<input>')
            .attr('type', 'button')
            .attr('class', 'price-btn')
            .attr('id', 'delete_price')
            .attr('value', '가격 삭제')
            .on('click', function() {
                newPriceEntry.remove(); // Remove the entire price entry div
            });

        // Append the inputs and delete button to the new div
        newPriceEntry.append(ticketNameInput);
        newPriceEntry.append(ticketPriceInput);
        newPriceEntry.append(deletePrice);

        // Append the new div to the price-form
        PriceDiv.append(newPriceEntry);
    });

});


