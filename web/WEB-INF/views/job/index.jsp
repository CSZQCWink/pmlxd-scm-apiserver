<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<link rel="Shortcut Icon" href="<%=path%>/resource/image/logo_small.png">
<title>任务管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="<%=path%>/resource/css/style.css" media="all">

<script type="text/javascript" src="<%=path%>/resource/js/jQuery/jquery-3.3.1.js"></script>
<script type="text/javascript" src="<%=path%>/resource/js/job/job.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
	});
</script>
</head>

<body>
	<input id="status" type="hidden" value="">
	<input id="isAdd" type="hidden" value="0">
	<input id="scheduleGroup" type="hidden" value="${scheduleGroup}">
	<!-- Main -->
	<div id="main">
		<div class="cl">&nbsp;</div>
		<!-- Content -->
		<div id="content">
			<!-- Box -->
			<div class="box">
				<!-- Box Head -->
				<div class="box-head">
					<h2 class="left">任务列表</h2>
					<div><a id="refresh" class="ico refresh" href="<%=path%>/View/JobView/index?page=1">刷新</a></div>
					<div id="pauseAllDiv"><a id="pauseAll" class="ico pauseAll" onclick="job.pauseAllJob();">暂停所有</a></div>
					<div id="resumeAllDiv" style="display: none"><a id="resumeAll" class="ico startAll" onclick="job.resumeAllJob();">恢复所有</a></div>
					<div><a id="add" class="ico add" onclick="job.addLine();">新增</a></div>
				</div>
				<!-- End Box Head -->

				<!-- Table -->
				<div class="table">
					<table id="table" width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th class="al" width="13px"><input type="checkbox" class="checkbox" /></th>
							<th class="al" width="200px">任务名</th>
							<th class="al" width="370px">任务描述</th>
							<th class="al" width="190px">表达式</th>
							<th class="ac" width="100px">所属任务组</th>
							<th class="ac" width="60px">当前状态</th>
							<th class="ac" width="300px">操作</th>
						</tr>
						<c:forEach var="job" items="${jobs}" varStatus="status">
							<tr id="job_${job.id}">
								<td><input id="job_box_${job.id}" type="checkbox" class="checkbox" value="${job.id}" /></td>
								<td><input id="jobName_${job.id}" type="text" class="text-input text-input-name" value="${job.jobName}" disabled="disabled" /></td>
								<td><input id="description_${job.id}" type="text" class="text-input text-input-desc" value="${job.description}" /></td>
								<td>
									<input id="cronExpression_${job.id}" type="text" class="text-input text-input-cro" value="${job.cronExpression}" />
									<a id="job_edit_${job.id}" class="ico nextTime" title="下一次执行时间" onclick="job.getNextTime(${job.id});"></a>
								</td>
								<td><input id="groupName_${job.id}" type="text" class="text-input text-input-group" value="${job.groupName}" disabled="disabled" /></td>
								<td class="ac">
									<c:if test="${job.runStatus == 'NORMAL'}">
										<img id="jod_status_${job.id}" title="正常" src="<%=path%>/resource/css/images/sungeon_ok.png">
									</c:if>
									<c:if test="${job.runStatus == 'COMPLETE'}">
										<img id="jod_status_${job.id}" title="完成" src="<%=path%>/resource/css/images/sungeon_ok.png">
									</c:if>
									<c:if test="${job.runStatus == 'PAUSED'}">
										<img id="jod_status_${job.id}" title="暂停" src="<%=path%>/resource/css/images/sungeon_paused.png">
									</c:if>
									<c:if test="${job.runStatus == 'NONE'}">
										<img id="jod_status_${job.id}" title="无" src="<%=path%>/resource/css/images/sungeon_stop.png">
									</c:if>
									<c:if test="${job.runStatus == 'ERROR'}">
										<img id="jod_status_${job.id}" title="错误" src="<%=path%>/resource/css/images/sungeon_error.png">
									</c:if>
									<c:if test="${job.runStatus == 'BLOCKED'}">
										<img id="jod_status_${job.id}" title="阻塞" src="<%=path%>/resource/css/images/sungeon_stop.png">
									</c:if>
								</td>
								<td>
									<div id="job_opera_${job.id}">
										<c:if test="${job.runStatus == 'NORMAL'}">
											<a id="status_opera_${job.id}" data-id="${job.id}" class="ico pause" onclick="job.pauseJob(${job.id});">暂停</a>
										</c:if>
										<c:if test="${job.runStatus != 'NORMAL'}">
											<a id="status_opera_${job.id}" data-id="${job.id}" class="ico start" onclick="job.resumeJob(${job.id});">恢复</a>
										</c:if>
										<%--
										<c:if test="${job.runStatus == 'BLOCKED'}">											
											<a id="status_opera_${job.id}" data-id="${job.id}" class="ico interrupt" onclick="job.interruptJob(${job.id});">中断</a>
										</c:if>
										--%>
										<a id="job_trigger_${job.id}" class="ico start" onclick="job.triggerJob(${job.id});">立即执行</a>
										<a id="job_edit_${job.id}" class="ico edit" onclick="job.saveJob(${job.id});">修改</a>
										<a id="job_delete_${job.id}" class="ico delete" onclick="job.deleteJob(${job.id});">删除</a>
										<%--<a href="<%=path%>/Servlet/Job/delete?jobId=${job.id}" class="ico delete">删除</a>--%>
									</div>
								</td>
							</tr>
						</c:forEach>
					</table>

					<!-- Pagging -->
					<div class="pagging">
						<div class="left">
							<span>显示</span> <span>${countDesc}</span>
						</div>
						<div class="next-left"><span id="statusDesc">${statusDesc}</span></div>
						<div class="right">
							<a class="page" href="<%=path%>/View/JobView/index?page=${lastPage}">上一页</a>
							<c:if test="${page <= pageSize && pageSize <= 4}">
								<c:forEach var="i" begin="1" end="${pageSize}">
									<c:if test="${i == page}">
										<a class="page-now" href="<%=path%>/View/JobView/index?page=<c:out value="${i}" />"><c:out value="${i}" /></a>
									</c:if>
									<c:if test="${i != page}">
										<a class="page" href="<%=path%>/View/JobView/index?page=<c:out value="${i}" />"><c:out value="${i}" /></a>
									</c:if>
								</c:forEach>
							</c:if>
							<c:if test="${page <= pageSize && pageSize > 4}">
								<c:if test="${page <= 3}">
									<c:forEach var="i" begin="1" end="3">
										<c:if test="${i == page}">
											<a class="page-now" href="<%=path%>/View/JobView/index?page=<c:out value="${i}" />"><c:out value="${i}" /></a>
										</c:if>
										<c:if test="${i != page}">
											<a class="page" href="<%=path%>/View/JobView/index?page=<c:out value="${i}" />"><c:out value="${i}" /></a>
										</c:if>
										<c:if test="${i == 3}">
											<c:if test="${page == 3}">
												<a class="page" href="<%=path%>/View/JobView/index?page=4"><c:out value="4" /></a>
											</c:if>
											<span>...</span>
											<a class="page" href="<%=path%>/View/JobView/index?page=${pageSize}"><c:out value="${pageSize}" /></a>
										</c:if>
									</c:forEach>
								</c:if>
								<c:if test="${page >= 4 && page <= (pageSize - 2)}">
									<a class="page" href="<%=path%>/View/JobView/index?page=1"><c:out value="1" /></a>
									<span>...</span>
									<c:forEach var="i" begin="${page - 1}" end="${page + 1}">
										<c:if test="${i == page}">
											<a class="page-now" href="<%=path%>/View/JobView/index?page=<c:out value="${i}" />"><c:out value="${i}" /></a>
										</c:if>
										<c:if test="${i != page}">
											<a class="page" href="<%=path%>/View/JobView/index?page=<c:out value="${i}" />"><c:out value="${i}" /></a>
										</c:if>
										<c:if test="${i == (page + 1)}">
											<c:if test="${page < (pageSize - 2)}">
												<span>...</span>
											</c:if>
											<a class="page" href="<%=path%>/View/JobView/index?page=${pageSize}"><c:out value="${pageSize}" /></a>
										</c:if>
									</c:forEach>
								</c:if>
								<c:if test="${page > (pageSize - 2)}">
									<a class="page" href="<%=path%>/View/JobView/index?page=1"><c:out value="1" /></a>
									<span>...</span>
									<c:forEach var="i" begin="${page - 2}" end="${pageSize}">
										<c:if test="${i == page}">
											<a class="page-now" href="<%=path%>/View/JobView/index?page=<c:out value="${i}" />"><c:out value="${i}" /></a>
										</c:if>
										<c:if test="${i != page}">
											<a class="page" href="<%=path%>/View/JobView/index?page=<c:out value="${i}" />"><c:out value="${i}" /></a>
										</c:if>
									</c:forEach>
								</c:if>
							</c:if>
							<a class="page" href="<%=path%>/View/JobView/index?page=${nextPage}">下一页</a>
						</div>
					</div>
					<!-- End Pagging -->
				</div>
				<!-- Table -->
			</div>
			<!-- End Box -->
		</div>
		<!-- End Content -->
	</div>

</body>
</html>