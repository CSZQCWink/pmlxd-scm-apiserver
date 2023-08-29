<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<html>
<head>
    <link rel="Shortcut Icon" href="<%=basePath%>resource/image/logo_small.png">
    <title>欢迎进入接口平台</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>resource/css/style.css" media="all">
</head>
<body>
<div style="margin: 100px auto; width: 450px; text-align: center; font-size: 30px; color: red; font-weight: bold;">
    欢迎进入接口平台
</div>
</body>
</html>
