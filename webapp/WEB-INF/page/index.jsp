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
    <script src="<%=basePath%>/static/js/chalk.js"></script>
    <script src="<%=basePath%>/static/js/echarts.min.js"></script>
    <script src="<%=basePath%>/static/js/macarons.js"></script>
    <!--    <script src="js/dark.js"></script>-->
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

<!--资源概况-->
<div class="over-usage">
    <div class="title1">
        资源概况
    </div>
    <!--cpu使用情况-->
    <div class="cpu-usage">
        <div class="cpu-title">
            CPU使用率
        </div>
        <div id="cpu-charts" style="width: 100%; height: 240px;margin-top: 20px">

        </div>
    </div>
    <div class="memory-usage">
        <div class="memory-title">
            内存使用率
        </div>
        <div id="memory-charts" style="width: 100%; height: 240px;margin-top: 20px">

        </div>
    </div>
</div>
<!--节点概况-->
<div></div>

<div style="margin-left: 220px;width: 80%;">
    <div>
        <div class="title2">
            节点概况
        </div>
        <div style="height: 20px"></div>
        <div style="width: 90%;margin-left: 5%">
            <table class="table table-bordered table-advance table-hover">
                <thead style="border: black">
                <tr>
                    <th> 节点名称</th>
                    <th> 节点IP</th>
                    <th>角色</th>
                    <th>CPU占用率</th>
                    <th>内存占用率</th>
                    <th>Pods使用率</th>
                    <th>内核</th>
                    <th>状态</th>
                    <th>查看</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>master</td>
                    <td>192.168.1.1</td>
                    <td>master</td>
                    <td>12%</td>
                    <td>30%</td>
                    <td>30%</td>
                    <td>2</td>
                    <td><span class="label label-success">运行</span></td>
                    <td><a href="<%=basePath%>/to/load.do" class="btn btn-xs">View</a></td>
                </tr>
<%--                <tr>--%>
<%--                    <td>Node1</td>--%>
<%--                    <td>192.168.1.1</td>--%>
<%--                    <td>node</td>--%>
<%--                    <td>12%</td>--%>
<%--                    <td>30%</td>--%>
<%--                    <td>30%</td>--%>
<%--                    <td>2</td>--%>
<%--                    <td><span class="label label-warning">挂起</span></td>--%>
<%--                    <td><a href="nodes.jsp" class="btn btn-xs">View</a></td>--%>
<%--                </tr>--%>
<%--                <tr>--%>
<%--                    <td>Node2</td>--%>
<%--                    <td>192.168.1.1</td>--%>
<%--                    <td>node</td>--%>
<%--                    <td>12%</td>--%>
<%--                    <td>30%</td>--%>
<%--                    <td>30%</td>--%>
<%--                    <td>2</td>--%>
<%--                    <td><span class="label label-success">运行</span></td>--%>
<%--                    <td><a href="nodes.jsp" class="btn btn-xs">View</a></td>--%>
<%--                </tr>--%>
<%--                <tr>--%>
<%--                    <td>Node3</td>--%>
<%--                    <td>192.168.1.1</td>--%>
<%--                    <td>node</td>--%>
<%--                    <td>12%</td>--%>
<%--                    <td>30%</td>--%>
<%--                    <td>30%</td>--%>
<%--                    <td>2</td>--%>
<%--                    <td><span class="label label-danger">关闭</span></td>--%>
<%--                    <td><a href="nodes.jsp" class="btn btn-xs">View</a></td>--%>
<%--                </tr>--%>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
<script>
    window.onload = function () {
        charts1()
        charts2()

        function charts1() {
            var mCharts = echarts.init(document.getElementById('cpu-charts'), 'macarons');
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
                    left: '5%',
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
                            color: '#5ab1ef',
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
                                    offset: 0, color: '#5ab1ef'// 0% 处的颜色
                                }, {
                                    offset: 1, color: '#7b68ee' // 100% 处的颜色
                                }],
                                global: false // 缺省为 false
                            }
                        }

                    }
                ]
            }
            mCharts.setOption(option)
        }

        function charts2() {
            var mCharts = echarts.init(document.getElementById('memory-charts'), 'macarons');
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
                    left: '5%',
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
                            color: '#5ab1ef',
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
                                    offset: 0, color: '#5ab1ef'// 0% 处的颜色
                                }, {
                                    offset: 1, color: '#7b68ee'  // 100% 处的颜色
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