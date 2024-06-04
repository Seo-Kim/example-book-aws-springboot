const index = {
    init: function() {
        const _this = this;
        $( "#tbody tr" ).on( "click", function() {
            if( ! $( this ).data( "id" ) )
                return false;

            window.location.href = "/posts/update/"+$( this ).data( "id" );
        } );
    },
};

index.init();