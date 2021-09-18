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
            请求详细信息
        </div>
        <div style="height: 20px"></div>
        <div style="width: 90%;margin-left: 5%">
            <table class="table table-bordered table-advance table-hover" id="requestTable">
                <thead style="border: black">
                <tr>
                    <th>请求ID</th>
                    <th>发送IP</th>
                    <th>接收IP</th>
                    <th>请求时间</th>
                    <th>请求状态</th>
                    <th>报文长度</th>
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
                    <td>加载中...</td>

                </tbody>
            </table>


            <div class="deploy-title2">
                deployment详细信息：
            </div>
            <table class="table table-bordered table-advance table-hover" id="deploymentTable">
                <thead style="border: black">
                <tr>
                                <th>name</th>
                                <th>ready</th>
                                <th>uptodate</th>
                                <th>available</th>
                                <th>namespace</th>
                                <th>operation(+)</th>
                                <th>operation(-)</th>
                            </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>加载中...</td>
                                    <td>加载中...</td>
                                    <td>加载中...</td>
                                    <td>加载中...</td>
                                    <td>加载中...</td>
                                    <td>加载中...</td>
                                    <td>加载中...</td>
                                </tr>
                            </tbody>
                        </table>

        </div>
    </div>
</div>
<div style="height: 5%"></div>

</body>
<script>
    var mCharts;
    window.onload = function () {
        charts1()

        function charts1() {
            mCharts = echarts.init(document.getElementById('responseTime'), 'macarons');
            var xDataArr = [];
            var yDataArr = [];
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
                        name: '时间(s)',
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
            mCharts.setOption(option);
            mCharts.showLoading();
        }

    };

    function Ajax_get_request_table(){
        $.ajax({
            type:"GET",
            dataType:"json",
            url: '<%=basePath%>/global/requestList.do',
            async:true,
            contentType: 'application/json;charset=utf-8',
            success:function(data){
                var requestList=eval(data)['requestList'];
                var responseTimeList = eval(data)['responseTimeList'];
                var tbody=$('<tbody></tbody>');
                $(requestList).each(function (index){
                    var val=requestList[index];
                    var tr=$('<tr></tr>');
                    tr.append(
                        '<td>'+ val.reqID + '</td>' +
                        '<td>'+ val.requestFromAddr + '</td>' +
                        '<td>'+ val.requestToAddr + '</td>' +
                        '<td>'+ val.requestTime + '</td>'+
                        '<td>'+ val.response.status + '</td>'+
                        '<td>'+ val.bodySendLength + '</td>' +
                        '<td><a href=\"<%=basePath%>/to/deployment.do\" class=\"btn btn-xs\">View</a></td>'
                    );
                    tbody.append(tr);
                });
                $('#requestTable tbody').replaceWith(tbody);


                //渲染折线图
                let mCharts = echarts.init(document.getElementById('responseTime'),'macarons');
                var x = [];
                var y = [];
                if (responseTimeList) {
                    for(var i=0;i<responseTimeList.length;i++){
                        x.push(i);
                        y.push(responseTimeList[i]);
                    }
                    var option = {
                        xAxis: {
                            type: 'category',
                            data: x,
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
                                name: '时间(s)',
                                data: y,
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
                    };
                    mCharts.setOption(option);
                    mCharts.hideLoading();
                }
            }
        });
    }

    function Ajax_get_deployment_table(){
        $.ajax({
            type:"GET",
            dataType:"json",
            url: '<%=basePath%>/global/getDeploymentList.do',
            async:true,
            contentType: 'application/json;charset=utf-8',
            success:function(data){
                let deploymentList = eval(data);
                var tbody=$('<tbody></tbody>');
                $(deploymentList).each(function (index){
                    var val=deploymentList[index];
                    var tr=$('<tr></tr>');
                    tr.append(
                        '<td>'+ val.name + '</td>' +
                        '<td>'+ val.ready + '</td>' +
                        '<td>'+ val.upToData + '</td>' +
                        '<td>'+ val.available + '</td>'+
                        '<td>'+ val.namespace + '</td>'+
                        '<td><a href="<%=basePath%>/global/increase.do?name='+ val.name + '&num=' + val.upToData +'&namespace='+val.namespace+'">增加pod</a></td>' +
                        '<td><a href="<%=basePath%>/global/decrease.do?name='+ val.name + '&num=' + val.upToData +'&namespace='+val.namespace+'">减少pod</a></td>'
                    );
                    tbody.append(tr);
                });
                $('#deploymentTable tbody').replaceWith(tbody);
            }
        });
    }

    //设置ajax轮询
    $(function(){
        setInterval(Ajax_get_request_table,5000);
        setInterval(Ajax_get_deployment_table,5000);
    })

</script>
</html>