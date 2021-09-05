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
    <script src="<%=basePath%>/static/js/echarts.min.js"></script>
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
            <a href="<%=basePath%>/to/nodes.do" class="nav-text">Nodes</a>
        </li>
        <li class="li">
            <a href="<%=basePath%>/to/load.do" class="nav-text">Pods</a>
        </li>
        <li class="li">
            <a href="<%=basePath%>/to/deployment.do" class="nav-text">负载</a>
        </li>
    </ul>
</nav>
<!--选择-->
<!--<div class="pod-select" >-->
<!--    <div class="pod-title">Pods选择</div>-->
<!--    <div id="pod-select">-->

<!--    </div>-->

<!--</div>-->
<!--使用率-->
<!--<div class="deploy-usage">-->
<!--    &lt;!&ndash;    CPU使用率&ndash;&gt;-->
<!--    <div class="pod-cpu-usage">-->
<!--        <div class="pod-cpu-title">-->
<!--            CPU Usage-->
<!--        </div>-->
<!--        &lt;!&ndash;        图表&ndash;&gt;-->
<!--        <div id="pod-cpu-use">-->

<!--        </div>-->

<!--    </div>-->
<!--    &lt;!&ndash;    内存使用率&ndash;&gt;-->
<!--    <div class="pod-memory-usage">-->
<!--        <div class="pod-memory-title">-->
<!--            Memory Usage-->
<!--        </div>-->
<!--        &lt;!&ndash;        图表&ndash;&gt;-->
<!--        <div id="pod-memory-use">-->

<!--        </div>-->

<!--    </div>-->
<!--</div>-->
<!--请求-->
<div style="height:5%;"></div>
<div class="responseTime">
    <div class="responseTime-title">Response Time</div>
    <div id="responseTime"></div>
</div>

<div style="margin-left: 20%;width: 75%;">
    <div>
        <div class="deploy-title2">
            负载详细信息
        </div>
        <div style="height: 20px"></div>
        <div style="width: 90%;margin-left: 5%">
            <table class="table table-bordered table-advance table-hover">
                <thead style="border: black">
                <tr>
                    <th>Request ID</th>
                    <th>From Addr</th>
                    <th>To Addr</th>
                    <th>Request Time</th>
                    <th>Status</th>
                    <th>Length</th>
                    <th>查看</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>b2ebadb2e3b2b23b21</td>
                    <td>192.168.1.1</td>
                    <td>10.122.1.26:8080</td>
                    <td>08/Aug/2021:07:17:46+000</td>
                    <td>200</td>
                    <td>2</td>
                    <td><a href="<%=basePath%>/to/deployment.do" class="btn btn-xs">View</a></td>
                </tr>
<%--                <tr>--%>
<%--                    <td>b2ebadb2e3b2b23b21</td>--%>
<%--                    <td>192.168.1.1</td>--%>
<%--                    <td>10.122.1.26:8080</td>--%>
<%--                    <td>08/Aug/2021:07:17:46+000</td>--%>
<%--                    <td>200</td>--%>
<%--                    <td>2</td>--%>
<%--                    <td><a href="deployment.jsp" class="btn btn-xs">View</a></td>--%>
<%--                </tr>--%>
<%--                <tr>--%>
<%--                    <td>b2ebadb2e3b2b23b21</td>--%>
<%--                    <td>192.168.1.1</td>--%>
<%--                    <td>10.122.1.26:8080</td>--%>
<%--                    <td>08/Aug/2021:07:17:46+000</td>--%>
<%--                    <td>200</td>--%>
<%--                    <td>2</td>--%>
<%--                    <td><a href="deployment.jsp" class="btn btn-xs">View</a></td>--%>
<%--                </tr>--%>
<%--                <tr>--%>
<%--                    <td>b2ebadb2e3b2b23b21</td>--%>
<%--                    <td>192.168.1.1</td>--%>
<%--                    <td>10.122.1.26:8080</td>--%>
<%--                    <td>08/Aug/2021:07:17:46+000</td>--%>
<%--                    <td>200</td>--%>
<%--                    <td>2</td>--%>
<%--                    <td><a href="deployment.jsp" class="btn btn-xs">View</a></td>--%>
<%--                </tr>--%>
<%--                <tr>--%>
<%--                    <td>b2ebadb2e3b2b23b21</td>--%>
<%--                    <td>192.168.1.1</td>--%>
<%--                    <td>10.122.1.26:8080</td>--%>
<%--                    <td>08/Aug/2021:07:17:46+000</td>--%>
<%--                    <td>200</td>--%>
<%--                    <td>2</td>--%>
<%--                    <td><a href="load.jsp" class="btn btn-xs">View</a></td>--%>
<%--                </tr>--%>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div style="height: 5%"></div>

</body>
<script>
    window.onload = function () {
        charts1()

        function charts1() {
            var mCharts = echarts.init(document.getElementById('responseTime'), 'macarons');
            var xDataArr = ['7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09',
                '7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09',
                '7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11']
            var yDataArr = [0.7, 0.83, 0.62, 0.4, 0.7, 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62, 0.7, 0.83, 0.62, 0.4, 0.7, 0.65,
                0.45, 0.86, 0.42, 0.74, 0.75, 0.62, 0.7, 0.83, 0.62, 0.4, 0.7, 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62, 0.7, 0.83, 0.62, 0.4, 0.7, 0.65,
                0.45, 0.86, 0.42, 0.74, 0.75, 0.62]
            var option = {
                xAxis: {
                    type: 'category',
                    data: xDataArr,
                    boundaryGap: false
                },
                yAxis: {
                    type: 'value',
                    //scale: true
                },
                grid: {
                    left: '6%',
                    top: '5%',
                    bottom: '8%',
                    right: '2%',
                    // containLabel: true
                },
                tooltip: {
                    trigger: 'axis'
                },
                series: [
                    {
                        symbol: "none",
                        name: '占比',
                        data: yDataArr,
                        type: 'line',
                        smooth: true,
                        lineStyle: {
                            color: '#20b2aa',
                            // color:'white',
                            type: 'solid'
                        },
                        areaStyle: {
                            // color: 	'#ffdead'
                            color: {
                                type: 'linear',
                                x: 0,
                                y: 0,
                                x2: 0,
                                y2: 1,
                                colorStops: [{
                                    offset: 0, color: '#66cdaa'// 0% 处的颜色
                                }, {
                                    offset: 1, color: '#20b2aa' // 100% 处的颜色
                                }],
                                global: false // 缺省为 false
                            }
                        }

                    }
                ]
            }
            mCharts.setOption(option)
        }

    }

</script>
</html>