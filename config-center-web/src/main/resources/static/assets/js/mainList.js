(function ($) {


    getSession();

    var appId = -1;
    var envId = -1;
    var version = "#";

    //
    // 获取Env信息
    //
    $.ajax({
        type: "GET",
        url: "/env/list"
    }).done(
        function (data) {
            if (data.code == 100) {
                var html = "";
                var result = data.data;
                $.each(result, function (index, item) {
                    html += '<li><a rel=' + index + ' href="#">'
                        + item.displayName + '</a></li>';
                });
                $("#envChoice").html(html);
            }
        });
    $("#envChoice").on('click', 'li a', function () {
        envId = $(this).attr('rel');
        $("#envChoice li").removeClass("active");
        $(this).parent().addClass("active");
        version = "#";
        fetchMainList();
    });

    fetchMainList();

    //
    // 渲染主列表
    //
    function fetchMainList() {

        // 参数不正确，清空列表
        if (envId == -1) {
            $("#mainlist_error").text("请选择" + getTips()).show();
            $("#accountBody").html("");
            $("#mainlist").hide();
            return;
        }

        $("#mainlist_error").hide();
        var parameter = ""

        url = "/config/list";
        if (envId == null && version == null) {

        } else {
            url += "?";
            if (envId != -1) {
                url += "envId=" + envId + "&";
            }
            if (version != "#") {
                url += "version=" + version + "&";
            }
        }

        $.ajax({
            type: "GET",
            url: url
        }).done(function (data) {
            if (data.code == 100) {
                $("#config_upload").val(envId);
                var html = "";
                var result = data.data;
                $.each(result, function (index, item) {
                    html += renderItem(envId, item, index);
                });
                if (html != "") {
                    $("#mainlist").show();
                    $("#accountBody").html(html);
                } else {
                    $("#accountBody").html("");
                }

            } else {
                $("#accountBody").html("");
                $("#mainlist").hide();
            }

            bindDetailEvent(result);
        });
        var mainTpl = $("#tbodyTpl").html();

        // 渲染主列表
        function renderItem(envId, item, i) {

            var modify_link = '<a target="_blank" href="modifyItem.html?envId=' +envId +'&key='+ item.key
                + '"><i title="修改" class="icon-edit"></i></a>';

            var del_link = '<a id="itemDel_' + item.key
                + '" href="/config/item/delete?envId=' +envId +'&key='+ item.key
                + '" style="cursor: pointer; cursor: hand; " ><i title="删除" class="icon-remove"></i></a>';

            return Util.string.format(mainTpl, '', item.key, item.value, modify_link, del_link);
        }
    }

    //
    // 渲染 配置 value
    //
    function fetchConfigValue(configId, object) {
        //
        // 获取APP信息
        //
        $.ajax({
            type: "GET",
            url: "/web/config/" + configId
        }).done(
            function (data) {
                if (data.code == 100) {
                    var result = data.result;

                    var e = object;
                    e.popover({
                        content: "<pre>" + Util.input.escapeHtml(result.value) + "</pre>",
                        html: true
                    }).popover('show');
                }
            });
    }

    // 详细列表绑定事件
    function bindDetailEvent(result) {
        if (result == null) {
            return;
        }
        $.each(result, function (index, item) {
            var id = item.configId;

            // 绑定删除事件
            $("#itemDel" + id).on("click", function (e) {
                deleteDetailTable(id, item.key);
            });

            $(".valuefetch" + id).on('click', function () {
                var e = $(this);
                e.unbind('click');
                fetchConfigValue(id, e);
            });
        });

    }

    // 删除
    function deleteDetailTable(id, name) {

        var ret = confirm("你确定要删除吗 " + name + "?");
        if (ret == false) {
            return false;
        }

        $.ajax({
            type: "DELETE",
            url: "/web/config/" + id
        }).done(function (data) {
            if (data.code == 100) {
                fetchMainList();
            }
        });
    }

    //
    function getTips() {
        if (appId == -1) {
            return "APP";
        }
        if (envId == -1) {
            return "环境";
        }
        return "参数";
    }

})(jQuery);
