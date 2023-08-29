<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <link rel="Shortcut Icon" href="<%=path%>/resource/image/logo_small.png">
    <title>自定义功能页面列表</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>resource/css/style.css" media="all">
</head>
<body>
<div style="margin:10px 30px 10px 30px;">
    <h1 style="color: black;font-weight: bold;">自定义功能页面列表</h1>
    <table id="table" width="700px" border="0" cellspacing="0" cellpadding="0">
        <c:forEach var="p" items="${resp}">
            <tr>
                <td>
                    <a class="btn" href="<%=basePath%>${p.url}">${p.name}</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
