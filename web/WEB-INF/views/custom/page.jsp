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
    <title>${json.name}</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>resource/css/style.css" media="all">
    <script type="text/javascript" src="<%=path%>/resource/js/jQuery/jquery-3.3.1.js"></script>
    <script type="text/javascript" src="<%=path%>/resource/js/custom/custom.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
        });
    </script>
</head>
<body>
<input id="url" type="hidden" value="${json.url}">
<input id="type" type="hidden" value="${json.type}">
<div style="margin:10px 30px 10px 30px;">
    <h1 style="color: black;font-weight: bold;">${json.name}</h1>
    <br/>
    <br/>
    <br/>
    <span style="color: black;font-weight: bold; font-size: 16px;">参数</span>
    <br/>
    <br/>
    <table id="table" width="700px" border="0" cellspacing="0" cellpadding="0">
        <c:forEach var="p" items="${json.params}">
            <tr>
                <td style="width: 250px; text-align: right; height: 40px;">
                    <span style="margin-right: 10px;">${p.description}:</span>
                </td>
                <td style="width: 540px; height: 40px;">
                    <input id="param_${p.name}" data-name="${p.name}" type="text" class="text-input text-input-name" value=""/>
                </td>
            </tr>
        </c:forEach>
    </table>
    <div style="width: 100%">
        <a id="request_btn" class="execute_btn" onclick="custom.doRequest();">请 求</a>
    </div>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <span style="color: black;font-weight: bold; font-size: 16px;">响应</span>
    <br/>
    <br/>
    <pre id="response"></pre>
</div>
</body>
</html>
