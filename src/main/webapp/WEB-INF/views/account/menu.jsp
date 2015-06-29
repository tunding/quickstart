<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<title>主菜单</title>
</head>
<body>
	<table class="table table-striped table-bordered">
		<tr>
			<th>controller</th>
			<th>访问方法</th>
		</tr>
		<tr>
			<td>currentUser</td>
			<td><a href="${ ctx }/profile">查询个人信息</a></td>
		</tr>
		<tr>
			<td>runner</td>
			<td>
				<table>
					<tr>
						<td><a href="${ ctx }/runner/near/test">test</a></td>
					</tr>
					<tr>
						<td><a href="${ ctx }/runner/near/list">附近的人</a></td>
					</tr>
					<tr>
						<td>上报runner的gps</a></td>
						<td>
							<form action="${ctx}/gps/info/putgeohash" method="post">
							  <p>纬度: <input type="text" name="lat" /></p>
							  <p>经度: <input type="text" name="lon" /></p>
							  <input class="btn btn-primary"  type="submit" value="保存" />
							</form>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>activity</td>
			<td>
				<table>
					<tr>
						<td>附近的活动</td>
						<td>
							<form action="${ ctx }/activity/near/list" method="post">
							  <p>纬度: <input type="text" name="latitude" /></p>
							  <p>经度: <input type="text" name="longitude" /></p>
							  <input class="btn btn-primary"  type="submit" value="附近的活动" />
							</form>
						</td>
					</tr>
					<tr>
						<td>上报活动信息</td>
						<td>
						<form action="${ ctx }/activity/info/saveactivity" method="post">
						  <p>纬度: <input type="text" name="latitude" /></p>
						  <p>经度: <input type="text" name="longitude" /></p>
						  <p>地址: <input type="text" name="address" /></p>
						  <p>距离: <input type="text" name="kilometer" /></p>
						  <p>开始时间: <input type="text" name="time" /></p>
						  <p>详情: <input type="text" name="info" /></p>
						  <input class="btn btn-primary"  type="submit" value="上报活动" />
						</form>
						</td>
					</tr>
					<tr>
						<td><a href="${ ctx }/activity/info/getactivity">查询发布活动</a></td>
					</tr>
					<tr>
						<td><a href="${ ctx }/activity/info/gethistoryactivity">查询发布历史活动</a></td>
					</tr>
					<tr>
						<td><a href="${ ctx }/activity/info/gethistoryactivity">删除发布的活动</a></td>
						<td>
							<form action="${ ctx }/activity/info/deleteactivity" method="post">
							  <p>活动actuuid: <input type="text" name="actuuid" /></p>
							  <input class="btn btn-primary"  type="submit" value="删除活动" />
							</form>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		
		<tr>
			<td>currentUser</td>
			<td><a href="${ ctx }/ralationship/friendship/listfriend">通讯录</a></td>
		</tr>
	</table>
</body>
</html>