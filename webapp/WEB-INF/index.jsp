<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
    %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <title>homepage</title>
</head>
<body>
<%--初始化重定向--%>
</body>
<script>
    window.location.replace("<%=basePath%>/to/index.do")
</script>
</html>
