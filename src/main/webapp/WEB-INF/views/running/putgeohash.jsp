<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<title>上报gps</title>
</head>
<body>
<form action="${ctx}/gps/info/putgeohash" method="post">
  <p>纬度: <input type="text" name="lat" /></p>
  <p>经度: <input type="text" name="lon" /></p>
  <input class="btn btn-primary"  type="submit" value="保存" />
</form>
</body>
</html>