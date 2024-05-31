const post_save = {
    init: function() {
        const _this = this;
        $( "#btn-save" ).on( "click", function() {
            _this.save();
        } );
    },
    save: function() {
        const data = {
            title: $( "#title" ).val(),
            author: $( "#author" ).val(),
            content: $( "#content" ).val(),
        };

        $.ajax( {
            type: "POST",
            url: "/api/v1/posts",
            dataType: "json",
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify( data )
        } ).done( function() {
            alert( "글 등록 성공" );
            window.location.href = "/";
        } ).fail( function( error ) {
            alert( JSON.stringify( error ) );
        } );
    },
};

post_save.init();