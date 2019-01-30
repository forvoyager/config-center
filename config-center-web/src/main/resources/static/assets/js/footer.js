(function ($) {

    // 登出
    $("#signout").on("click", function () {
        $.ajax({
            type: "GET",
            url: "/user/account/logout"
        }).done(function (data) {
            if (data.code == 100) {
                VISITOR = {};
                getSession();
            }
        });
    });

})(jQuery);