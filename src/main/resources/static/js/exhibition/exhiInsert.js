import {
    ClassicEditor,
    AccessibilityHelp,
    Autoformat,
    AutoImage,
    Autosave,
    Bold,
    CloudServices,
    Code,
    Essentials,
    GeneralHtmlSupport,
    HtmlComment,
    HtmlEmbed,
    ImageBlock,
    ImageCaption,
    ImageInline,
    ImageInsertViaUrl,
    ImageResize,
    ImageStyle,
    ImageTextAlternative,
    ImageToolbar,
    ImageUpload,
    Italic,
    Paragraph,
    SelectAll,
    ShowBlocks,
    SourceEditing,
    TextTransformation,
    Undo
} from 'ckeditor5';

const editorConfig = {
    toolbar: {
        items: [
            'undo',
            'redo',
            '|',
            'sourceEditing',
            'showBlocks',
            'selectAll',
            '|',
            'bold',
            'italic',
            'code',
            '|',
            'htmlEmbed',
            '|',
            'accessibilityHelp'
        ],
        shouldNotGroupWhenFull: false
    },
    plugins: [
        AccessibilityHelp,
        Autoformat,
        AutoImage,
        Autosave,
        Bold,
        CloudServices,
        Code,
        Essentials,
        GeneralHtmlSupport,
        HtmlComment,
        HtmlEmbed,
        ImageBlock,
        ImageCaption,
        ImageInline,
        ImageInsertViaUrl,
        ImageResize,
        ImageStyle,
        ImageTextAlternative,
        ImageToolbar,
        ImageUpload,
        Italic,
        Paragraph,
        SelectAll,
        ShowBlocks,
        SourceEditing,
        TextTransformation,
        Undo
    ],
    htmlSupport: {
        allow: [
            {
                name: /^.*$/,
                styles: true,
                attributes: true,
                classes: true
            }
        ]
    },
    image: {
        toolbar: [
            'toggleImageCaption',
            'imageTextAlternative',
            '|',
            'imageStyle:inline',
            'imageStyle:wrapText',
            'imageStyle:breakText',
            '|',
            'resizeImage'
        ]
    },
    placeholder: '내용을 입력해주세요!'
};

$(document).ready(function (){
    ClassicEditor
        .create(document.querySelector('#exhibition_notice'), editorConfig)
        .then(editor => {
            $('style').append('.ck-content { height: 300px; }');

            editor.model.document.on('change:data', () => {
                $('#exhibition_notice').val(editor.getData());
            }, {priority: 'high'});
        })
        .catch(error => {
            console.error(error);
        });

    ClassicEditor
        .create(document.querySelector('#exhibition_detail_info'), editorConfig)
        .then(editor => {
            $('style').append('.ck-content { height: 300px; }');

            editor.model.document.on('change:data', () => {
                $('#exhibition_detail_info').val(editor.getData());
            }, {priority: 'high'});
        })
        .catch(error => {
            console.error(error);
        });

    ClassicEditor
        .create(document.querySelector('#exhibition_seller_info'), editorConfig)
        .then(editor => {
            $('style').append('.ck-content { height: 300px; }');

            editor.model.document.on('change:data', () => {
                $('#exhibition_seller_info').val(editor.getData());
            }, {priority: 'high'});
        })
        .catch(error => {
            console.error(error);
        });

    ClassicEditor
        .create(document.querySelector('#exhibition_product_info'), editorConfig)
        .then(editor => {
            $('style').append('.ck-content { height: 300px; }');

            editor.model.document.on('change:data', () => {
                $('#exhibition_product_info').val(editor.getData());
            }, {priority: 'high'});
        })
        .catch(error => {
            console.error(error);
        });

    $('#addPrice').on('click', function (){
        var PriceDiv = $('.price-form');

        // 티켓 옵션과 가격, 버튼들이 들어갈 새 div 생성
        var newPriceEntry = $('<div>').attr('class', 'price-entry');

        // 티켓 옵션 생성
        var ticketNameInput = $('<input>')
            .attr('type', 'text')
            .attr('name', 'ticket_name[]')
            .attr('class', 'ticket-option')
            .attr('placeholder', '티켓 이름');

        // 티켓 가격 생성
        var ticketPriceInput = $('<input>')
            .attr('type', 'number')
            .attr('name', 'ticket_price[]')
            .attr('class', 'ticket-price')
            .attr('placeholder', '가격')
            .attr('min', '0');

        // 가격 삭제 버튼 생성
        var deletePrice = $('<input>')
            .attr('type', 'button')
            .attr('class', 'price-btn')
            .attr('id', 'delete_price')
            .attr('value', '가격 삭제')
            .on('click', function() {
                newPriceEntry.remove(); //
            });

        // div 안에 요소들 넣기
        newPriceEntry.append(ticketNameInput);
        newPriceEntry.append(ticketPriceInput);
        newPriceEntry.append(deletePrice);

        // 해당 div를 가격 div 안에 넣기
        PriceDiv.append(newPriceEntry);
    });

});


