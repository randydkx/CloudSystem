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
        <li class="li">
            <a href="<%=basePath%>/to/container" class="nav-text">Containers</a>
        </li>
    </ul>
</nav>
<!--选择-->
<div class="nodes-select" >
    <div class="node-title">Nodes选择</div>
    <div id="nodes-select">
        <c:forEach items="${requestScope.nodeList}" var="node" varStatus="status">
            <a href="<%=basePath%>/to/nodes.do?nodeIndex=${status.index}" class="selects">${node.name}</a><br><br><br>
        </c:forEach>
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
        charts1();
        charts2();
        charts3();
        charts4();
        charts5();
        charts6();
        charts7();
        charts8();
        charts9();
        function charts1() {
            var mCharts = echarts.init(document.getElementById('node-cpu-use'), 'macarons');
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
        function charts2() {
            var mCharts = echarts.init(document.getElementById('node-memory-use'), 'macarons');
            var xDataArr = ['7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09',
                '7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11'];
            var yDataArr = [ 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62, 0.7, 0.83, 0.62, 0.4, 0.7, 0.65,
                0.45, 0.86, 0.42, 0.74, 0.75, 0.7, 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62, 0.7, 0.83, 0.62, 0.4, 0.7, 0.65,
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
            mCharts.setOption(option);
            mCharts.showLoading();
        }
        function charts3() {
            var mCharts = echarts.init(document.getElementById('disk-throught'),'macarons');
            //var mCharts = echarts.init(document.querySelector('cpu-charts'))
            var xDataArr = ['7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11'];
            var yDataArr = [0.7, 0.83, 0.62, 0.4, 0.7, 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62];
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
            };
            mCharts.setOption(option);
            mCharts.showLoading();
        }
        function charts4() {
            var mCharts = echarts.init(document.getElementById('disk-iop'),'macarons');
            var xDataArr = ['7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11'];
            var yDataArr = [0.7, 0.83, 0.62, 0.4, 0.7, 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62];
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
            };
            mCharts.setOption(option);
            mCharts.showLoading();
        }
        function charts5() {
            var mCharts = echarts.init(document.getElementById('pods'),'macarons');
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
            ];
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
            };
            mCharts.setOption(option);
            mCharts.showLoading();
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
            };
            mCharts.setOption(option);
            mCharts.showLoading();
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
            };
            mCharts.setOption(option);
            mCharts.showLoading();
        }
        function charts8() {
            var mCharts = echarts.init(document.getElementById('node-cpu-ratio'), 'macarons');
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
            };
            mCharts.setOption(option);
            mCharts.showLoading();
        }
        function charts9() {
            var mCharts = echarts.init(document.getElementById('node-memory-ratio'), 'macarons');
            var xDataArr = ['7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09',
                '7:10', '7:11', '7:00', '7:01', '7:02', '7:03', '7:04', '7:05', '7:06', '7:07', '7:08', '7:09', '7:10', '7:11'];
            var yDataArr = [ 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62, 0.7, 0.83, 0.62, 0.4, 0.7, 0.65,
                0.45, 0.86, 0.42, 0.74, 0.75, 0.7, 0.65, 0.45, 0.86, 0.42, 0.74, 0.75, 0.62, 0.7, 0.83, 0.62, 0.4, 0.7, 0.65,
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
            };
            mCharts.setOption(option);
            mCharts.showLoading();
        }
    };

    function ajax_update_all() {
        $.ajax({
            type:"GET",
            dataType:"json",
            url: '<%=basePath%>/global/nodeInfoUpdate.do',
            async:true,
            contentType: 'application/json;charset=utf-8',
            success:function(data){
                var obj = eval(data);
                let chart1 = echarts.init(document.getElementById('node-cpu-use'),'macarons');
                let chart2 = echarts.init(document.getElementById('node-memory-use'), 'macarons');
                var chart1x = obj['chart1x'];
                var chart1y = obj['chart1y'];
                var chart2x = obj['chart2x'];
                var chart2y = obj['chart2y'];
                var chart3x = obj['chart3x'];
                var chart3y = obj['chart3y'];
                var chart4x = obj['chart4x'];
                var chart4y = obj['chart4y'];
                var chart8x = obj['chart8x'];
                var chart8y = obj['chart8y'];
                var chart9x = obj['chart9x'];
                var chart9y = obj['chart9y'];
                var chart5 = obj['chart5'];
                var chart6 = obj['chart6'];
                var chart7 = obj['chart7'];

                //图表1 CPU使用量的修改
                var option1 = {
                    xAxis: {
                        type: 'category',
                        data: chart1x,
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
                            name: '用量',
                            data: chart1y,
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
                chart1.setOption(option1);
                chart1.hideLoading();


            //    图表2MEMERY使用量的修改
                var option2 = {
                    xAxis: {
                        type: 'category',
                        data: chart2x,
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
                            data: chart2y,
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
                chart2.setOption(option2);
                chart2.hideLoading();

            //    图表3：磁盘利用率
                var disk_through = echarts.init(document.getElementById('disk-throught'),'macarons');
                var option8 = {
                    xAxis: {
                        type: 'category',
                        data: chart8x,
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
                            data: chart8y,
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
                };
                disk_through.setOption(option8);
                disk_through.hideLoading();

                // disk吞吐量
                var disk_iop = echarts.init(document.getElementById('disk-iop'),'macarons');
                var option9 = {
                    xAxis: {
                        type: 'category',
                        data: chart9x,
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
                            data: chart9y,
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
                };
                disk_iop.setOption(option9);
                disk_iop.hideLoading();

            //    cpu利用率图
                var chart3 = echarts.init(document.getElementById('node-cpu-ratio'), 'macarons');
                var option3 = {
                    xAxis: {
                        type: 'category',
                        data: chart3x,
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
                            data: chart3y,
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
                };
                chart3.setOption(option3);
                chart3.hideLoading();

            //    mem利用率
                var memRatio = echarts.init(document.getElementById('node-memory-ratio'), 'macarons');
                var option4 = {
                    xAxis: {
                        type: 'category',
                        data: chart4x,
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
                            data: chart4y,
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
                };
                memRatio.setOption(option4);
                memRatio.hideLoading();

            //    pod使用情况
                var podUsage = echarts.init(document.getElementById('pods'),'macarons');
                // pieData就是需要设置给饼图的数据, 数组,数组中包含一个又一个的对象, 每一个对象中, 需要有name和value
                var pieData = [
                    {
                        name: '运行',
                        value: chart5.running
                    },
                    {
                        name: '挂起',
                        value: chart5.pending
                    },
                    {
                        name: '其他',
                        value: chart5.others
                    }
                ];
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
                };
                podUsage.setOption(option);
                podUsage.hideLoading();
            //    CPU request
                var cpuR = echarts.init(document.getElementById('node-cpu-request'),'macarons');
                var option_cpuR = {
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
                            value: chart6
                        }]
                    }]
                };
                cpuR.setOption(option_cpuR);
                cpuR.hideLoading();
            //    memery request
                var memR = echarts.init(document.getElementById('node-memory-request'),'macarons');
                var option_memR = {
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
                            value: chart7
                        }]
                    }]
                };
                memR.setOption(option_memR);
                memR.hideLoading();
            }
        });
    }

    $(function(){
        setInterval(ajax_update_all,5000);
    })


</script>
</html>