// 初始入口
(function () {
    window.VISITOR = {};
})();

//
// 头部显示初始化
//
function headShowInit() {
    if (VISITOR.userId) {
        $(".login-no").hide();
        $(".login-yes").show();
        $("#username").show();
        $("#username").html(VISITOR.name);
    } else {
        $(".login-no").show();
        $(".login-yes").hide();
        $("#username").hide();
    }
}

//
// 登录其它的控制
//
function loginActions() {
    if (VISITOR.userId) {
        $("#brand_url").attr("href", "/main.html");
    } else {
        $("#brand_url").attr("href", "/");
    }
}

//
// 获取Session信息
//
function getSession() {
    $.ajax({
        type: "GET",
        url: "/user/account/session",
        timeout: 3000 // 3s timeout
    }).done(function (data) {
        if (data.code == 100) {
            window.VISITOR = data.data;
            headShowInit();
        } else {
            window.location.href = "/login.html";
        }
    }).fail(function (xmlHttpRequest, textStatus) {
        window.location.href = "/login.html";
    });
}

// 获取是否登录并且进行跳转
function getSession2Redirect() {
    $.ajax({
        type: "GET",
        url: "/user/account/session"
    }).done(function (data) {
        if (data.code == 100) {
            window.location.href = "/main.html";
        } else {
        }
    });
    loginActions();
}
