const index = {
    init: function() {
        const _this = this;
        $( "#tbody tr" ).on( "click", function() {
            window.location.href = "/posts/update/"+$( this ).data( "id" );
        } );
    },
};

index.init();