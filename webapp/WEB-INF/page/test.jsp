<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<head>
    <title>this is a single test</title>
</head>
<%--测试是否可以访问静态资源--%>
<body style="background: url(<%=basePath%>/static/img/2030474.jpg) no-repeat;background-size: cover;background-attachment: fixed">

    <form action="<%=basePath%>/test/fun.do" method="post">
        <button type="submit">登录</button>
    </form>
</body>
</html>
