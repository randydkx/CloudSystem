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
    </ul>
</nav>
<!--选择-->
<!--<div class="pod-select" >-->
<!--    <div class="pod-title">Pods选择</div>-->
<!--    <div id="pod-select">-->

<!--    </div>-->

<!--</div>-->
<!--使用率-->
<div class="pod-usage">
    <!--    CPU使用率-->
    <div class="pod-cpu-usage">
        <div class="pod-cpu-title">
            CPU Usage
        </div>
        <!--        图表-->
        <div id="pod-cpu-use">

        </div>

    </div>
    <!--    内存使用率-->
    <div class="pod-memory-usage">
        <div class="pod-memory-title">
            Memory Usage
        </div>
        <!--        图表-->
        <div id="pod-memory-use">

        </div>

    </div>
</div>
<!--请求-->
<div class="container-status">
    <div class="container-status-title">Containers' Status</div>
    <div id="container-status"></div>
</div>

<div style="margin-left: 17%;width: 75%;">
    <div>
        <div class="pod-title2">
            Pods详细信息
        </div>
        <div style="height: 20px"></div>
        <div style="width: 90%;margin-left: 5%">
            <table class="table table-bordered table-advance table-hover">
                <thead style="border: black">
                <tr>
                    <th> Pod名称</th>
                    <th> Pod IP</th>
                    <th>容器数目</th>
                    <th>使用镜像</th>
                    <th>开始时间</th>
                    <th>状态</th>
                    <th>查看</th>
                </tr>
                </thead>
                <tbody>
<%--                查看request信息--%>
                <tr>
                    <td>myapp-pod</td>
                    <td>192.168.1.1</td>
                    <td>4</td>
                    <td>docker.1.0.3</td>
                    <td>1 days</td>
                    <td><span class="label label-success">运行</span></td>
                    <td><a href="<%=basePath%>/to/load" class="btn btn-xs">View</a></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div style="height: 5%"></div>

</body>
<script>
    window.onload = function () {
        charts1();
        charts2();
        charts3();

        function charts1() {
            var mCharts = echarts.init(document.getElementById('pod-cpu-use'), 'macarons');
            //获取最近一次访问的节点的信息
            <%--var xDataArr = ${sessionScope.chart1x};--%>
            <%--var yDataArr = ${sessionScope.chart1y};--%>
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
            };
            mCharts.setOption(option);
            mCharts.showLoading();
        }
        function charts2() {
            var mCharts = echarts.init(document.getElementById('pod-memory-use'), 'macarons');
            var xDataArr = ['7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09',
                '7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09',
                '7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11'];
            var yDataArr = [0.7, 0.83, 0.62, 0.4, 0.7, 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62, 0.7, 0.83, 0.62, 0.4, 0.7, 0.65,
                0.45, 0.86, 0.42, 0.74, 0.75, 0.62, 0.7, 0.83, 0.62, 0.4, 0.7, 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62, 0.7, 0.83, 0.62, 0.4, 0.7, 0.65,
                0.45, 0.86, 0.42, 0.74, 0.75, 0.62];
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
            };
            mCharts.setOption(option);
            mCharts.showLoading();
        }
        function charts3() {
            var mCharts = echarts.init(document.getElementById('container-status'),'macarons');
            var xDataArr = ['7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11'];
            var yDataArr1 = [0.7, 0.83, 0.62, 0.4, 0.7, 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62];
            var yDataArr2 = [0.5, 0.7, 0.6, 0.2, 0.4, 0.6, 0.4, 0.6, 0.32, 0.8, 0.7, 0.6];
            var yDataArr3 = [0.8, 0.4, 0.5, 0.3, 0.7, 0.4, 0.46, 0.86, 0.42, 0.74, 0.75, 0.62];
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
                    left: '3%',
                    top: '5%',
                    bottom: '8%',
                    right: '3%',
                    // containLabel: true
                },
                legend:{
                    data:['1','2']
                },
                series: [
                    {
                        symbol:'none',
                        name: '占比',
                        data: yDataArr1,
                        type: 'line',
                        smooth: true,
                        lineStyle: {
                            color: '#f4a460',
                            type: 'solid'//虚线
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
                                    offset: 0, color: '#ffdead'// 0% 处的颜色
                                }, {
                                    offset: 1, color: '#f4a460' // 100% 处的颜色
                                }],
                                global: false // 缺省为 false
                            }
                        }
                    },
                    {
                        symbol:'none',
                        name: '占比',
                        data: yDataArr2,
                        type: 'line',
                        smooth: true,
                        lineStyle: {
                            color: '#00bfff',
                            type: 'solid'//虚线
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
                                    offset: 0, color: '#87ceeb'// 0% 处的颜色
                                }, {
                                    offset: 1, color: '#00bfff' // 100% 处的颜色
                                }],
                                global: false // 缺省为 false
                            }
                        }
                    },
                    {
                        symbol:'none',
                        name: '占比',
                        data: yDataArr3,
                        type: 'line',
                        smooth: true,
                        lineStyle: {
                            color: '#800080',
                            type: 'solid'//虚线
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
                                    offset: 0, color: '#9400d3'// 0% 处的颜色
                                }, {
                                    offset: 1, color: '#9932cc' // 100% 处的颜色
                                }],
                                global: false // 缺省为 false
                            }
                        }
                    }
                ]
            };

            mCharts.setOption(option);
            mCharts.showLoading();
        }
    }

</script>
</html>