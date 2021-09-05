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
<div class="nodes-select" >
    <div class="node-title">Nodes选择</div>
    <div id="nodes-select">
        <a href="load.jsp" class="selects">Master</a><br><br><br>
        <a href="load.jsp" class="selects">node1</a><br><br><br>
        <a href="load.jsp" class="selects">node2</a><br><br><br>
        <a href="load.jsp" class="selects">node3</a><br><br><br>
    </div>

</div>
<!--使用率-->
<div class="usage">
    <!--    CPU使用率-->
    <div class="node-cpu-usage">
        <div class="cpu-usage-title">
            CPU Usage
        </div>
        <!--        图表-->
        <div id="node-cpu-use">

        </div>

    </div>
    <!--    内存使用率-->
    <div class="node-memory-usage">
        <div class="memory-usage-title">
            Memory Usage
        </div>
        <!--        图表-->
        <div id="node-memory-use">

        </div>

    </div>
</div>
<div class="ratio">
    <!--    CPU使用率-->
    <div class="node-cpu-ratio">
        <div class="cpu-ratio-title">
            CPU Ratio
        </div>
        <!--        图表-->
        <div id="node-cpu-ratio">

        </div>

    </div>
    <!--    内存使用率-->
    <div class="node-memory-ratio">
        <div class="memory-ratio-title">
            Memory Ratio
        </div>
        <!--        图表-->
        <div id="node-memory-ratio">

        </div>

    </div>
</div>
<!--请求-->
<div class="requested">
    <!--    pod使用率-->
    <div class="Pods" >
        <div class="pod-usage-title" >Pod使用情况</div>
        <div id="pods"></div>

    </div>
    <!--    CPU请求-->
    <div class="node-cpu-requested" >
        <div class="cpu-requeset-title">CPU Requested</div>
        <div id="node-cpu-request"></div>

    </div>
    <!--    内存请求-->
    <div class="node-memory-requested" >
        <div class="memory-request-title">Memory Requested</div>
        <div id="node-memory-request"></div>

    </div>
</div>
<!--磁盘情况-->
<div class="disk">
    <div class="disk-throughout" >
        <div class="disk-throughout-title" >Disk</div>
        <div id="disk-throught"></div>
    </div>
    <div class="disk-iops" >
        <div class="disk-iops-title">Disk Ratio</div>
        <div id="disk-iop"></div>

    </div>
</div>

<div style="height: 150px"></div>
</body>
<script>
    window.onload = function () {
        charts1()
        charts2()
        charts3()
        charts4()
        charts5()
        charts6()
        charts7()
        charts8()
        charts9()
        function charts1() {
            var mCharts = echarts.init(document.getElementById('node-cpu-use'), 'macarons');
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
            }
            mCharts.setOption(option)
        }
        function charts2() {
            var mCharts = echarts.init(document.getElementById('node-memory-use'), 'macarons');
            var xDataArr = ['7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09',
                '7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11']
            var yDataArr = [ 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62, 0.7, 0.83, 0.62, 0.4, 0.7, 0.65,
                0.45, 0.86, 0.42, 0.74, 0.75, 0.7, 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62, 0.7, 0.83, 0.62, 0.4, 0.7, 0.65,
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
                smooth:false,
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
        function charts3() {
            var mCharts = echarts.init(document.getElementById('disk-throught'),'macarons');
            //var mCharts = echarts.init(document.querySelector('cpu-charts'))
            var xDataArr = ['7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11']
            var yDataArr = [0.7, 0.83, 0.62, 0.4, 0.7, 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62]
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
                tooltip: {
                    trigger: 'axis'
                },
                grid: {
                    left: '6%',
                    top: '5%',
                    bottom: '8%',
                    right: '2%',
                    // containLabel: true
                },
                series: [
                    {
                        symbol:'none',
                        name: '占比',
                        data: yDataArr,
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
                    }
                ]
            }
            mCharts.setOption(option)
        }
        function charts4() {
            var mCharts = echarts.init(document.getElementById('disk-iop'),'macarons');
            //var mCharts = echarts.init(document.querySelector('cpu-charts'))
            var xDataArr = ['7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11']
            var yDataArr = [0.7, 0.83, 0.62, 0.4, 0.7, 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62]
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
                tooltip: {
                    trigger: 'axis'
                },
                grid: {
                    left: '6%',
                    top: '5%',
                    bottom: '8%',
                    right: '2%',
                    // containLabel: true
                },
                series: [
                    {
                        symbol:'none',
                        name: '占比',
                        data: yDataArr,
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
                    }
                ]
            }
            mCharts.setOption(option)
        }
        function charts5() {
            var mCharts = echarts.init(document.getElementById('pods'),'macarons');
            // var mCharts = echarts.init(document.querySelector("pods"))
            // pieData就是需要设置给饼图的数据, 数组,数组中包含一个又一个的对象, 每一个对象中, 需要有name和value
            var pieData = [
                {
                    name: '运行',
                    value: 20
                },
                {
                    name: '挂起',
                    value: 30
                },
                {
                    name: '未使用',
                    value: 40
                }
            ]
            var option = {
                series: [
                    {
                        type: 'pie',
                        data: pieData,
                        label:{
                            show:true,//显示文字
                            formatter:function (arg) {
                                return arg.name + arg.percent +'%'
                            }
                        },
                        //  radius:20//饼图的半径
                        radius:['40%','60%']
                        //  roseType:true,
                        // // selectedMode:'single'//选中后,选中的部分偏离主体一小段距离
                        //  selectedMode:'multiple',//可以选中多个区域,
                        //  selectedOffset: 60
                    }
                ]
            }
            mCharts.setOption(option)
        }
        function charts6() {
            var mCharts = echarts.init(document.getElementById('node-cpu-request'),'macarons');
            var option = {
                series: [{
                    type: 'gauge',
                    progress: {
                        show: true,
                        width: 18
                    },
                    axisLine: {
                        lineStyle: {
                            width: 18
                        }
                    },
                    axisTick: {
                        show: false
                    },
                    anchor: {
                        show: true,
                        showAbove: true,
                        size: 5,
                        itemStyle: {
                            borderWidth: 10
                        }
                    },
                    title: {
                        show: false
                    },
                    detail: {
                        valueAnimation: true,
                        fontSize: 20,
                        offsetCenter: [0, '70%']
                    },
                    data: [{
                        value: 56.2
                    }]
                }]
            }
            mCharts.setOption(option)
        }
        function charts7() {
            var mCharts = echarts.init(document.getElementById('node-memory-request'),'macarons');
            var option = {
                series: [{
                    type: 'gauge',
                    progress: {
                        show: true,
                        width: 18
                    },
                    axisLine: {
                        lineStyle: {
                            width: 18
                        }
                    },
                    axisTick: {
                        show: false
                    },
                    anchor: {
                        show: true,
                        showAbove: true,
                        size: 5,
                        itemStyle: {
                            borderWidth: 10
                        }
                    },
                    title: {
                        show: false
                    },
                    detail: {
                        valueAnimation: true,
                        fontSize: 20,
                        offsetCenter: [0, '70%']
                    },
                    data: [{
                        value: 84
                    }]
                }]
            }
            mCharts.setOption(option)
        }
        function charts8() {
            var mCharts = echarts.init(document.getElementById('node-cpu-ratio'), 'macarons');
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
                            color: '#faebd7',
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
                                    offset: 0, color: '#fff5ee'// 0% 处的颜色
                                }, {
                                    offset: 1, color: '#faebd7' // 100% 处的颜色
                                }],
                                global: false // 缺省为 false
                            }
                        }

                    }
                ]
            }
            mCharts.setOption(option)
        }
        function charts9() {
            var mCharts = echarts.init(document.getElementById('node-memory-ratio'), 'macarons');
            var xDataArr = ['7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09',
                '7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11']
            var yDataArr = [ 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62, 0.7, 0.83, 0.62, 0.4, 0.7, 0.65,
                0.45, 0.86, 0.42, 0.74, 0.75, 0.7, 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62, 0.7, 0.83, 0.62, 0.4, 0.7, 0.65,
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
                smooth:false,
                series: [
                    {
                        symbol: "none",
                        name: '占比',
                        data: yDataArr,
                        type: 'line',
                        smooth: true,
                        lineStyle: {
                            color: '#faebd7',
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
                                    offset: 0, color: '#fff5ee'// 0% 处的颜色
                                }, {
                                    offset: 1, color: '#faebd7' // 100% 处的颜色
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