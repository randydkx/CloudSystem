<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
    %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <title>调度平台</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/css/all.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/css/overview.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/css/style.css">
    <script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>
</head>
<body>
<!--导航栏-->
<nav class="shell">
    <h1 style="margin-top: 20px;margin-left: 10px;font-size: 25px;font-weight: 300">Kubernetes</h1>
    <h1 style="margin-top: 20px;margin-left: 6px;font-size: 25px">资源调度平台</h1>
    <ul class="buttons">
        <li class="li">
            <a href="<%=basePath%>/to/index.do" class="nav-text">概况</a>
        </li>
        <li class="li">
            <c:if test="${not empty sessionScope.nodeIndex}">
                <a href="<%=basePath%>/to/nodes.do?nodeIndex=${sessionScope.nodeIndex}" class="nav-text">Nodes</a>
            </c:if>
            <c:if test="${empty sessionScope.nodeIndex}">
                <a href="<%=basePath%>/to/nodes.do?nodeIndex=0" class="nav-text">Nodes</a>
            </c:if>
        </li>
        <li class="li">
            <a href="<%=basePath%>/to/load.do" class="nav-text">Pods</a>
        </li>
        <li class="li">
            <a href="<%=basePath%>/to/deployment.do" class="nav-text">load</a>
        </li>
        <li class="li">
            <a href="<%=basePath%>/to/container" class="nav-text">Containers</a>
        </li>
    </ul>
</nav>

<div style="margin-left: 17%;width: 75%;">
    <div>
        <div class="deploy-title2">
            集群Container的详细信息
        </div>

            <div style="height: 20px"></div>
            <div style="width: 90%;margin-left: 5%">
                <table class="table table-bordered table-advance table-hover" id="containerTable">
                    <thead style="border: black">
                    <tr>
                        <th>name</th>
                        <th>image</th>
                    </tr>
                    </thead>
                    <tbody>
                        <td>加载中...</td>
                        <td>加载中...</td>
                    </tbody>
                </table>
            </div>
    </div>
</div>
<div style="height: 5%"></div>

</body>
<script>

    function Ajax_get_Container_Table(){
        $.ajax({
            type:"GET",
            dataType:"json",
            url: '<%=basePath%>/global/getContainerTable.do',
            async:true,
            contentType: 'application/json;charset=utf-8',
            success:function(data){
                var containerList = eval(data);
                var tbody=$('<tbody></tbody>');
                $(containerList).each(function (index){
                    var val=containerList[index];
                    var tr=$('<tr></tr>');

                    tr.append(
                        '<td>'+ val.name + '</td>' +
                        '<td>'+ val.image + '</td>'
                    );
                    tbody.append(tr);
                });
                $('#containerTable tbody').replaceWith(tbody);
            }
        });
    }

    $(function(){
        setInterval(Ajax_get_Container_Table,10000);
    })

</script>
</html>