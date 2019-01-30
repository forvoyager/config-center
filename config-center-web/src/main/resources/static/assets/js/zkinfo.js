(function ($) {

    getSession();

    fetchConfigUsage();

    //
    // 渲染主列表
    //
    function fetchConfigUsage() {

        url = "/usage/list";

        $.ajax({
            type: "GET",
            url: url
        }).done(function (data) {
            if (data.code == 100) {
                var html = data.result.hostInfo;
                $("#hostInfo").html(html);
            }
        });

    }

})(jQuery);
