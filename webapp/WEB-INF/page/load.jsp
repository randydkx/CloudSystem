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
    <div class="container-status-title">Pods' Status</div>
    <div id="container-status"></div>
</div>

<div style="margin-left: 17%;width: 75%;">
    <div>
        <c:forEach varStatus="status" items="${sessionScope.nodeList}" var="node" >
            <div class="deploy-title2">
                    ${node.name}中pods的详细信息：
            </div>
            <div style="height: 20px"></div>
            <div style="width: 90%;margin-left: 5%">
                <table class="table table-bordered table-advance table-hover" id="forNode${status.index + 1}">
                    <thead style="border: black">
                    <tr>
                        <th>Pod名称</th>
                        <th>命名空间</th>
                        <th>容器</th>
                        <th>建立时间</th>
                        <th>状态</th>
                        <th>查看</th>
                    </tr>
                    </thead>
                    <tbody>
                        <td>加载中...</td>
                        <td>加载中...</td>
                        <td>加载中...</td>
                        <td>加载中...</td>
                        <td>加载中...</td>
                        <td>加载中...</td>
                    </tbody>
                </table>
            </div>
        </c:forEach>
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
            var pieData = [
                {
                    name: 'Running',
                    value: 20
                },
                {
                    name: 'Pending',
                    value: 30
                },
                {
                    name: 'Others',
                    value: 40
                }
            ];
            var option = {
                legend:{
                    bottom:'2%',
                    left:'center'
                },
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
                        emphasis:{
                            label:{
                                show: true,
                                fontSize:'20',
                                fontWeight:'bold'
                            }
                        },
                        //  radius:20//饼图的半径
                        radius:['40%','60%'],
                        //  roseType:true,
                        selectedMode:'single',//选中后,选中的部分偏离主体一小段距离
                        //  selectedMode:'multiple',//可以选中多个区域,
                         selectedOffset: 60
                    }
                ]
            };

            mCharts.setOption(option);
            mCharts.showLoading();
        }
    };

    function Ajax_get_Pod_Usage(){
        $.ajax({
            type:"GET",
            dataType:"json",
            url: '<%=basePath%>/global/getPodInfoForAllNodes.do',
            async:true,
            contentType: 'application/json;charset=utf-8',
            success:function(data){
                var running_num = 0;
                var pending_num = 0;
                var others_num = 0;
                var map = eval(data)
                var forNode1 = map['forNode1'];
                var forNode2 = map['forNode2'];
                var forNode3 = map['forNode3'];
                var tbody=$('<tbody></tbody>');
                $(forNode1).each(function (index){
                    var val=forNode1[index];
                    var tr=$('<tr></tr>');
                    var typeString = ""
                    if(val.status == "Running"){
                        typeString = "success";
                        running_num += 1;
                    }else if(val.status == "Pending"){
                        typeString = "warning";
                        pending_num += 1;
                    }else{
                        typeString = "danger";
                        others_num += 1;
                    }
                    tr.append(
                        '<td>'+ val.name + '</td>' +
                        '<td>'+ val.namespace + '</td>' +
                        '<td>'+ val.containers + '</td>' +
                        '<td>'+ val.age + '</td>' +
                        '<td><span class=\"label label-'+typeString+'\">'+ val.status + '</span></td>'+
                        '<td><a href=\"<%=basePath%>/to/deployment.do\" class=\"btn btn-xs\">View</a></td>'
                    );
                    tbody.append(tr);
                });
                $('#forNode1 tbody').replaceWith(tbody);

                tbody=$('<tbody></tbody>');
                $(forNode2).each(function (index){
                    var val=forNode2[index];
                    var tr=$('<tr></tr>');
                    var typeString = ""
                    if(val.status == "Running"){
                        typeString = "success";
                        running_num += 1;
                    }else if(val.status == "Pending"){
                        typeString = "warning";
                        pending_num += 1;
                    }else{
                        typeString = "danger";
                        others_num += 1;
                    }
                    tr.append(
                        '<td>'+ val.name + '</td>' +
                        '<td>'+ val.namespace + '</td>' +
                        '<td>'+ val.containers + '</td>' +
                        '<td>'+ val.age + '</td>' +
                        '<td><span class=\"label label-'+typeString+'\">'+ val.status + '</span></td>'+
                        '<td><a href=\"<%=basePath%>/to/deployment.do\" class=\"btn btn-xs\">View</a></td>'
                    );
                    tbody.append(tr);
                });
                $('#forNode2 tbody').replaceWith(tbody);

                tbody=$('<tbody></tbody>');
                $(forNode3).each(function (index){
                    var val=forNode3[index];
                    var tr=$('<tr></tr>');
                    var typeString = ""
                    if(val.status == "Running"){
                        typeString = "success";
                        running_num += 1;
                    }else if(val.status == "Pending"){
                        typeString = "warning";
                        pending_num += 1;
                    }else{
                        typeString = "danger";
                        others_num += 1;
                    }
                    tr.append(
                        '<td>'+ val.name + '</td>' +
                        '<td>'+ val.namespace + '</td>' +
                        '<td>'+ val.containers + '</td>' +
                        '<td>'+ val.age + '</td>' +
                        '<td><span class=\"label label-'+typeString+'\">'+ val.status + '</span></td>'+
                        '<td><a href=\"<%=basePath%>/to/deployment.do\" class=\"btn btn-xs\">View</a></td>'
                    );
                    tbody.append(tr);
                });
                $('#forNode3 tbody').replaceWith(tbody);

            //    更新rubis-deployment对应的CPU使用率折线图
                var cpuUsageChart = echarts.init(document.getElementById('pod-cpu-use'), 'macarons');
                //获取最近一次访问的节点的信息
                var timeLine = map['timeLine'];
                var cpuUsageList = map['cpuUsageList'];
                var memUsageList = map['memUsageList'];
                var option1 = {
                    xAxis: {
                        type: 'category',
                        data: timeLine,
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
                            data: cpuUsageList,
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
                cpuUsageChart.setOption(option1);
                cpuUsageChart.hideLoading();
            //    更新rubis-deploymetn对应的memery使用率折线图
                var memUsageChart = echarts.init(document.getElementById('pod-memory-use'), 'macarons');
                var option2 = {
                    xAxis: {
                        type: 'category',
                        data: timeLine,
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
                            data: memUsageList,
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
                memUsageChart.setOption(option2);
                memUsageChart.hideLoading();

                var containerStatus = echarts.init(document.getElementById('container-status'),'macarons');
                var pieData = [
                    {
                        name: 'Running',
                        value: running_num
                    },
                    {
                        name: 'Pending',
                        value: pending_num
                    },
                    {
                        name: 'Others',
                        value: others_num
                    }
                ];
                var option_container_status = {
                    legend:{
                        bottom:'2%',
                        left:'center'
                    },
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
                            emphasis:{
                                label:{
                                    show: true,
                                    fontSize:'20',
                                    fontWeight:'bold'
                                }
                            },
                            //  radius:20//饼图的半径
                            radius:['40%','60%'],
                            //  roseType:true,
                            selectedMode:'single',//选中后,选中的部分偏离主体一小段距离
                            //  selectedMode:'multiple',//可以选中多个区域,
                            selectedOffset: 60
                        }
                    ]
                };

                containerStatus.setOption(option_container_status);
                containerStatus.hideLoading();
            }
        });
    }

    $(function(){
        setInterval(Ajax_get_Pod_Usage,5000);
    })

</script>
</html>