var appId = -1;
var envId = -1;
var version = "";
getSession();

// 提交
$("#item_submit").on("click", function (e) {
    $("#error").addClass("hide");
    var displayName = $("#displayName").val();
    var url = $("#url").val();
    var description = $("#description").val();

    // 验证
    if (!displayName || !url) {
        $("#error").removeClass("hide");
        $("#error").html("表单不能为空或填写格式错误！");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/env/add",
        data: {
            "displayName": displayName,
            "url": url,
            "description": description
        }
    }).done(function (data) {
        $("#error").removeClass("hide");
        if (data.code == 100) {
            $("#error").html(data.message);
        } else {
            Util.input.whiteError($("#error"), data);
        }
    });
});
