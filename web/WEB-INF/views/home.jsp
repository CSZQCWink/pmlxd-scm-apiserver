<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <link rel="Shortcut Icon" href="<%=basePath%>resource/image/logo_small.png">
    <title>欢迎进入接口平台系统</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>resource/css/style.css" media="all">
</head>

<body>
<div style="margin: 50px auto; width: 600px;">
    <a class="btn" href="<%=basePath%>View/JobView/index?page=1">任务管理</a>
    <a class="btn" href="<%=basePath%>Services/">接口列表</a>
    <a class="btn" href="<%=basePath%>View/custom/list">自定义功能页面</a>
    <a class="btn" href="<%=basePath%>View/LogFile/index?page=1">日志下载</a>
</div>
</body>
</html>
