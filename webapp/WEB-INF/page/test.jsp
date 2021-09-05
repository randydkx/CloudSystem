<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>

<head>
    <title>this is a single test</title>
    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/css/bootstrap.min.css">
    <script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/popper.js/1.15.0/umd/popper.min.js"></script>
    <script src="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>

    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/echarts@5/dist/echarts.min.js"></script>
</head>
<%--测试是否可以访问静态资源--%>
<%--<body style="background: url(<%=basePath%>/static/img/2030474.jpg) no-repeat;background-size: cover;background-attachment: fixed">--%>
<body>
    <table class="table table-bordered table-striped" id="tablelist">
        <thead>
        <tr>
            <th>reqID</th>
            <th>requestFromAddr</th>
            <th>requestToAddr</th>
            <th>requestTime</th>
            <th>responseTime</th>
            <th>status</th>
            <th>bodySendLength</th>
        </tr>
        </thead>
        <tbody>

        </tbody>
    </table>

    <div style="height: 400px" class="" id="lineChart"></div>
</body>
<script type="text/javascript">
    function query(){
        $.ajax({
            type:"GET",
            dataType:"json",
            url: '<%=basePath%>/test/fun2.do',
            async:true,
            contentType: 'application/json;charset=utf-8',
            success:function(data){
                var obj=eval(data);
                var tbody=$('<tbody></tbody>');
                $(obj).each(function (index){
                    var val=obj[index];
                    var tr=$('<tr></tr>');
                    tr.append(
                        '<td>'+ val.reqID + '</td>' +
                        '<td>'+ val.requestFromAddr + '</td>' +
                        '<td>'+ val.requestToAddr + '</td>' +
                        '<td>'+ val.requestTime + '</td>'+
                        '<td>'+ val.response.responseTime + '</td>'+
                        '<td>'+ val.response.status + '</td>'+
                        '<td>'+ val.bodySendLength + '</td>'
                    );
                    tbody.append(tr);
                });
                $('#tablelist tbody').replaceWith(tbody);
            }

        });
    }



    function updateLineQuery(){
        var x = [];
        var y = [];

        $.ajax({
            type: 'post',
            async: true,
            url: '<%=basePath%>/test/fun3.do',
            dataType: 'json',
            success: function (result) {
                var echartsA = echarts.init(document.getElementById('lineChart'));

                if (result) {
                    // console.log(result)
                    for(var i=0;i<result.length;i++){
                        x.push(result[i]);
                        y.push(result[i]);
                    }
                    echartsA.setOption({
                        xAxis: {
                            type: 'category',
                            data: x
                        },
                        yAxis: {
                            type: 'value'
                        },
                        series: [{
                            data: y,
                            type: 'line'
                        }]
                    });
                }
            },
        });
    }


    $(function(){
        setInterval(query,3000);
        setInterval(updateLineQuery,3000);
    })

</script>
</html>
