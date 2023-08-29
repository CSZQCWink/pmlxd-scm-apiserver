var job = {
    //绑定事件
    bindEvent: function () {
        var localObj = window.location;
        var contextPath = localObj.pathname.split("/")[1];
        this.basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    },

    // 处理提示信息
    dealStatusDesc: function (e) {
        $("#status").html(e.status);
        $("#statusDesc").html(e.statusDesc);
        if (e.status)
            $("#statusDesc").css("color", "green");

        setTimeout(function () {
            $("#statusDesc").html("");
        }, 3000);
    },
	
    // 获取下一次执行时间
    getNextTime: function (id) {
        var _this = this;
        $.post(this.basePath + "/Servlet/Job/getNextTime", {
            jobId: id
        }, function (e) {
            _this.dealStatusDesc(e);
        });
    },

    // 暂停job
    pauseJob: function (id) {
        var _this = this;
        $("#status_opera_" + id).addClass("disabled");
        $.post(this.basePath + "/Servlet/Job/pause", {
            jobId: id
        }, function (e) {
            $("#jod_status_" + id).attr("src", _this.basePath + "/resource/css/images/sungeon_paused.png");
            $("#jod_status_" + id).attr("title", "暂停");
            $("#status_opera_" + id).removeClass("pause");
            $("#status_opera_" + id).addClass("start");
            $("#status_opera_" + id).html("恢复");
            $("#status_opera_" + id).attr("onclick", "job.resumeJob(" + id + ");");
            $("#status_opera_" + id).removeClass("disabled");
            _this.dealStatusDesc(e);
        });
    },

    // 恢复job
    resumeJob: function (id) {
        var _this = this;
        $("#status_opera_" + id).addClass("disabled");
        $.post(this.basePath + "/Servlet/Job/resume", {
            jobId: id
        }, function (e) {
            $("#jod_status_" + id).attr("src", _this.basePath + "/resource/css/images/sungeon_ok.png");
            $("#jod_status_" + id).attr("title", "正常");
            $("#status_opera_" + id).removeClass("start");
            $("#status_opera_" + id).addClass("pause");
            $("#status_opera_" + id).html("暂停");
            $("#status_opera_" + id).attr("onclick", "job.pauseJob(" + id + ");");
            $("#status_opera_" + id).removeClass("disabled");
            _this.dealStatusDesc(e);
        });
    },
	
    // 中断job
    interruptJob: function (id) {
        var _this = this;
        $("#status_opera_" + id).addClass("disabled");
        $.post(this.basePath + "/Servlet/Job/interrupt", {
            jobId: id
        }, function (e) {
            $("#jod_status_" + id).attr("src", _this.basePath + "/resource/css/images/sungeon_paused.png");
            $("#jod_status_" + id).attr("title", "暂停");
            $("#status_opera_" + id).removeClass("interrupt");
            $("#status_opera_" + id).addClass("start");
            $("#status_opera_" + id).html("恢复");
            $("#status_opera_" + id).attr("onclick", "job.pauseJob(" + id + ");");
            $("#status_opera_" + id).removeClass("disabled");
            _this.dealStatusDesc(e);
        });
    },

    // 立即执行job
    triggerJob: function (id) {
        var _this = this;
        $("#job_trigger_" + id).addClass("disabled");
        $.post(this.basePath + "/Servlet/Job/trigger", {
            jobId: id
        }, function (e) {
            $("#job_trigger_" + id).removeClass("disabled");
            _this.dealStatusDesc(e);
        });
    },

    // 修改job
    saveJob: function (id) {
        var _this = this;
        $("#job_edit_" + id).addClass("disabled");
        var description = $("#description_" + id).val();
        var cronExpression = $("#cronExpression_" + id).val();

        $.post(this.basePath + "/Servlet/Job/update", {
            jobId: id,
            description: description,
            cronExpression: cronExpression
        }, function (e) {
            $("#job_edit_" + id).removeClass("disabled");
            _this.dealStatusDesc(e);
        });
    },

    // 删除job
    deleteJob: function (id) {
        var _this = this;
        $.post(this.basePath + "/Servlet/Job/delete", {
            jobId: id
        }, function (e) {
            if (e.status) {
                $("#job_" + id).remove();
                _this.dealStatusDesc(e);
            }
        });
    },

    // 添加一行输入行
    addLine: function () {
        var isAdd = $("#isAdd").val();
        if (isAdd === "1") {
            alert("存在有新增任务未保存，请先保存");
            return;
        }
        var scheduleGroup = $("#scheduleGroup").val();
        var html = "<tr id=\"job_Temp\"><td><input id=\"job_box_Temp\" class=\"checkbox\" type=\"checkbox\" value=\"\"/></td>";
        html += "<td><input id=\"jobName_Temp\" type=\"text\" class=\"text-input text-input-name\" value=\"\"></td>";
        html += "<td><input id=\"description_Temp\" type=\"text\" class=\"text-input text-input-desc\" value=\"\"></td>";
        html += "<td><input id=\"cronExpression_Temp\" type=\"text\" class=\"text-input text-input-cro\" value=\"\"></td>";
        html += "<td><input id=\"groupName_Temp\" type=\"text\" class=\"text-input text-input-group\" value=\"" + scheduleGroup + "\" disabled=\"disabled\"></td>";
        html += "<td class=\"ac\"><img id=\"jod_status_Temp\" alt=\"正常\" src=\"" + this.basePath + "/resource/css/images/sungeon_ok.png\"></td>";
        html += "<td><div id=\"job_opera_Temp\"><a id=\"job_add_Temp\" class=\"ico addLine\" onclick=\"job.addJob();\">新增并开启</a></div></td></tr>";
        $("#table").append(html);
        $("#isAdd").val("1");
    },

    // 新增job
    addJob: function () {
        var _this = this;
        var jobName = $("#jobName_Temp").val();
        var description = $("#description_Temp").val();
        var cronExpression = $("#cronExpression_Temp").val();
        var groupName = $("#groupName_Temp").val();
        $.post(this.basePath + "/Servlet/Job/add", {
            jobName: jobName,
            description: description,
            cronExpression: cronExpression,
            groupName: groupName
        }, function (e) {
            if (e.status) {
                var job = e.job;
                $("#job_Temp").attr("id", "job_" + job.id);
                $("#job_box_Temp").val(job.id);
                $("#job_box_Temp").attr("id", "job_box_" + job.id);
                $("#jobName_Temp").attr("disabled", "disabled");
                $("#jobName_Temp").val(job.jobName);
                $("#jobName_Temp").attr("id", "jobName_" + job.id);
                //$("#description_Temp").attr("disabled","disabled");
                $("#description_Temp").val(job.description);
                $("#description_Temp").attr("id", "description_" + job.id);
                //$("#cronExpression_Temp").attr("disabled","disabled");
                $("#cronExpression_Temp").val(job.cronExpression);
                $("#cronExpression_Temp").attr("id", "cronExpression_" + job.id);
                //$("#groupName_Temp").attr("disabled","disabled");
                //$("#groupName_Temp").val(job.groupName);
                $("#groupName_Temp").attr("id", "groupName_" + job.id);
                var html = "<a id=\"status_opera_" + job.id + "\" data-id=\"" + job.id + "\" class=\"ico pause\" onclick=\"job.pauseJob(" + job.id + ");\">暂停</a>\n";
                html += "<a id=\"job_trigger_" + job.id + "\" class=\"ico start\" onclick=\"job.triggerJob(" + job.id + ");\">立即执行</a>\n";
                html += "<a id=\"job_edit_" + job.id + "\" class=\"ico edit\" onclick=\"job.saveJob(" + job.id + ");\">修改</a>\n";
                html += "<a id=\"job_delete_" + job.id + "\" class=\"ico delete\" onclick=\"job.deleteJob(" + job.id + ");\">删除</a>";
                $("#job_opera_Temp").html(html);
                $("#job_opera_Temp").attr("id", "job_opera_" + job.id);
                $("#isAdd").val("0");
            }
            _this.dealStatusDesc(e);
        });
    },

    pauseAllJob: function () {
        var _this = this;
        $.post(this.basePath + "/Servlet/Job/pauseAll", {}, function (e) {
            if (e.status) {
                $('img[id^=jod_status_]').attr("title", "暂停");
                $('img[id^=jod_status_]').attr("src", _this.basePath + "/resource/css/images/sungeon_paused.png");
                var elements = $('a[id^=status_opera_]');
                $(elements).removeClass("pause");
                $(elements).addClass("start");
                $(elements).html("恢复");
                for (var i = 0; i < elements.length; i++) {
                    var current = elements.eq(i);
                    var id = $(current).attr("data-id");
                    $(current).attr("onclick", "job.resumeJob(" + id + ");");
                }
                $("#pauseAllDiv").css("display", "none");
                $("#resumeAllDiv").css("display", "block");
            }
            _this.dealStatusDesc(e);
        });
    },

    resumeAllJob: function () {
        var _this = this;
        $.post(this.basePath + "/Servlet/Job/resumeAll", {}, function (e) {
            if (e.status) {
                $('img[id^=jod_status_]').attr("title", "正常");
                $('img[id^=jod_status_]').attr("src", _this.basePath + "/resource/css/images/sungeon_ok.png");
                var elements = $('a[id^=status_opera_]');
                $(elements).removeClass("start");
                $(elements).addClass("pause");
                $(elements).html("暂停");
                for (var i = 0; i < elements.length; i++) {
                    var current = elements.eq(i);
                    var id = $(current).attr("data-id");
                    $(current).attr("onclick", "job.pauseJob(" + id + ");");
                }
                $("#pauseAllDiv").css("display", "block");
                $("#resumeAllDiv").css("display", "none");
            }
            _this.dealStatusDesc(e);
        });
    },

    //初始化绑定事件
    init: function () {
        job.bindEvent();
    }
};

//初始化事件
$(function () {
    job.init();
});