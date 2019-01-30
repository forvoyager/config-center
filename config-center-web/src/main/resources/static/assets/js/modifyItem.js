(function ($) {

    getSession();

    var envId = Util.param.getParamByName("envId");
    var key = Util.param.getParamByName("key");

    fetchItem();

    //
    // 获取配置项
    //
    function fetchItem() {

        //
        // 获取此配置项的数据
        //
        $.ajax({
            type: "GET",
            url: "/config/item?envId=" + envId + "&key=" + key
        }).done(
            function (data) {
                if (data.code == 100) {
                    var result = data.data;
                    $("#version").text(result.version);
                    $("#key").text(result.key);
                    $("#value").val(result.value);
                }
            });
    }

    // 提交
    $("#submit").on("click", function (e) {
        $("#error").addClass("hide");
        var key = $("#key").text();
        var value = $("#value").val();
        var version = $("#version").text();
        // 验证
        if (!value) {
            $("#error").removeClass("hide");
            $("#error").html("表单不能为空或填写格式错误！");
            return;
        }
        $.ajax({
            type: "PUT",
            url: "/config/item/submit",
            data: {
                "envId": envId,
                "key": key,
                "value": value,
                "version": version
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

})(jQuery);
