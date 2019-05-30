<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<html>

<head>
    <!-- 页面meta -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>数据 - AdminLTE2定制版</title>
    <meta name="description" content="AdminLTE2定制版">
    <meta name="keywords" content="AdminLTE2定制版">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport">
</head>
<body>
<div id="frameContent" class="content-wrapper" style="margin-left:0px;">
    <!-- 内容头部 -->
    <section class="content-header">
        <h1>
            货运管理
            <small>新增装箱单</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="all-admin-index.html"><i class="fa fa-dashboard"></i> 首页</a></li>
            <li><a href="all-order-manage-list.html">货运管理</a></li>
            <li class="active">新增装箱单</li>
        </ol>
    </section>
    <!-- 内容头部 /-->
    <form id="editForm" action="${ctx}/cargo/packing/edit.do" method="post">
        <input type="hidden" name="packingListId" value="${packingList.packingListId}" >
        <input type="hidden" name="state" value="${packingList.state}" >
    <!-- 正文区域 -->
    <section class="content">
        <div class="panel panel-default">
            <div class="panel-heading">新增装箱单</div>

                <div class="row data-type" style="margin: 0px">
                    <div class="col-md-2 title">卖方</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="卖方" name="seller" value="${packingList.seller}"/>
                    </div>

                    <div class="col-md-2 title">买方</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="买方" name="buyer" value="${packingList.buyer}"/>
                    </div>

                    <div class="col-md-2 title">发票号</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="发票号" name="invoiceNo" value="${packingList.invoiceNo}"/>
                    </div>

                    <div class="col-md-2 title">发票时间</div>
                    <div class="col-md-4 data">
                        <div class="input-group date">
                            <div class="input-group-addon">
                                <i class="fa fa-calendar"></i>
                            </div>
                            <input type="text" placeholder="发票时间"  name="invoiceDate" class="form-control pull-right"
                                   value="<fmt:formatDate value="${packingList.invoiceDate}" pattern="yyyy-MM-dd"/>" id="datepicker">
                        </div>
                    </div>

                    <div class="col-md-2 title">唛头</div>
                    <div class="col-md-4 data">
                        <input type="text" name="marks" class="form-control" placeholder="唛头" value="${packingList.marks}"/>
                    </div>

                    <div class="col-md-2 title">描述</div>
                    <div class="col-md-4 data">
                        <input type="text" name="descriptions" class="form-control" placeholder="描述" value="${packingList.descriptions}">
                    </div>
                </div>

        </div>


        <!-- .box-body -->
        <div class="box box-primary">
            <div class="box-header with-border">
                <h3 class="box-title">报运单货物列表</h3>
            </div>

            <div class="box-body">

                <!-- 数据表格 -->
                <div class="table-box">
                    <!--数据列表-->
                    <table class="table table-bordered table-striped table-hover dataTable" id="mRecordTable">
                        <tr class="rowTitle" align="middle">
                            <td width="33">选择</td>
                            <td width="33">序号</td>
                            <td width="90px">收货人及地址</td>
                            <td width="60px">唛头</td>
                            <td width="90px">装船港</td>
                            <td width="90px">目的港</td>
                            <td width="90px">运输方式</td>
                            <td width="90px">总箱数</td>
                            <td width="90px">总毛重</td>
                            <td width="90px">总体积</td>
                            <td width="90px">信用证号</td>
                        </tr>
                        <c:forEach items="${eps}" var="o" varStatus="status">
                            <tr class="odd" onmouseover="this.className='highlight'"
                                onmouseout="this.className='odd'">
                                <td>
                                    <input type="checkbox" name="exportIds" value="${o.id}"
                                           <c:if test="${fn:contains(packingList.exportIds,o.id)}">checked</c:if>
                                    />
                                </td>
                                <td>${status.index+1}</td>
                                <td>${o.consignee}</td>
                                <td>${o.marks}</td>
                                <td>${o.shipmentPort}</td>
                                <td>${o.destinationPort}</td>
                                <td>${o.transportMode}</td>
                                <td>${o.boxNums}</td>
                                <td>${o.grossWeights}</td>
                                <td>${o.measurements}</td>
                                <td>${o.lcno}</td>
                            </tr>
                        </c:forEach>
                    </table>
                    <!--数据列表/-->
                    <!--工具栏/-->
                </div>
                <!-- 数据表格 /-->
            </div>
            <!-- /.box-body -->

        </div>

        <!--工具栏-->
        <div class="box-tools text-center">
            <button type="submit"  class="btn bg-maroon">保存</button>
            <button type="button" class="btn bg-default" onclick="history.back(-1);">返回</button>
        </div>
        <!--工具栏/-->
    </section>
    </form>
</div>
<!-- 内容区域 /-->
</body>
<script src="../../plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="../../plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<link rel="stylesheet" href="../../css/style.css">
<script>
    $('#datepicker').datepicker({
        autoclose: true,
        format: 'yyyy-mm-dd'
    });
    $('#datepicker1').datepicker({
        autoclose: true,
        format: 'yyyy-mm-dd'
    });
</script>
</html>