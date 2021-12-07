function csrf_init(){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(function() {
        $(document).ajaxSend(function(e, xhr, options) {
             xhr.setRequestHeader(header,token);
        });
    });
}

function init(){
    csrf_init();
}

init();