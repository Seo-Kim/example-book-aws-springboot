const common = {
    init: function() {
        const _this = this;
        $( "#btn-save" ).on( "click", function() {
            _this.save();
        } );
        $( "#btn-update" ).on( "click", function() {
            _this.update();
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
    update: function() {
        if( !postId ) {
            alert( "잘못된 접근입니다. 화면을 새로고침 해주세요" );
            return false;
        }

        const data = {
            title: $( "#title" ).val(),
            content: $( "#content" ).val(),
        };

        $.ajax( {
            type: "PUT",
            url: "/api/v1/posts/"+postId,
            dataType: "json",
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify( data )
        } ).done( function() {
            alert( "글 수정 성공 - 목록으로 돌아갑니다" );
            window.location.href = "/";
        } ).fail( function( error ) {
            alert( JSON.stringify( error ) );
        } );
    },
};

common.init();