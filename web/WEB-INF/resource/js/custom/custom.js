var custom = {
    //绑定事件
    bindEvent: function () {
        var localObj = window.location;
        var contextPath = localObj.pathname.split("/")[1];
        this.basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    },

    doRequest: function () {
        $("#request_btn").attr("onclick", "return false;");
        $("#request_btn").attr("style", "opacity: 0.2");
        var url = $("#url").val();
        var type = $("#type").val();
        var elements = $('input[id^=param_]');
        var param = {};
        for (var i = 0; i < elements.length; i++) {
            var current = elements.eq(i);
            var name = $(current).attr("data-name");
            var value = $(current).val();
            param[name] = value;
        }
        $.post(this.basePath + url, param, function (e) {
            $("#response").html(JSON.stringify(e, null, 4));
            $("#request_btn").attr("onclick", "custom.doRequest();");
            $("#request_btn").attr("style", "");
        }, type);
    },

    //初始化绑定事件
    init: function () {
        custom.bindEvent();
    }
};

//初始化事件
$(function () {
        custom.init();
    }
);