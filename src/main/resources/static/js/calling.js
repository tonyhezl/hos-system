$(function (){
//全局变量
var loginCode;//医生工号
var type;//类型
var loginUrl='login.html';
var waiteTable;
var overdueDataTable;
var queueId;//队列ID
var brid;//病人ID
var sickName;//病人姓名
var timer=5;//重复倒计时
var hjstat=false;//呼叫状态
var conflag=false;
var tab='demo1';
var socketData;
var initUrl='/cdroho/callogic/queryNowSick'
var queueUrl='/cdroho/wait/machinedatatwo';//队列数据
var docallUrl='/cdroho/callogic/call';//呼叫
var callendUrl='/cdroho/callogic/setFinish';//结诊
var reCallUrl='/cdroho/callogic/reCall';//重呼
var setOver='/cdroho/callogic/setPass';//过号


window.onload=function(){
	var url = window.location.href;
	var pulicObj=GetRequest(url);
	//赋值全局变量
	loginCode=pulicObj.longId;
	queueId=pulicObj.queueId;
	//初始化表格数据源
	initWait();
	layui.use(['form','element','table'],
		function() {
			layer = layui.layer;
			element = layui.element;
			table = layui.table;
			init();
	});
}

	var initWait=function (){
		$.ajax({
			type:"POST",
			url:queueUrl,
			data:{loginCode:loginCode,queueId:queueId,type:1},
			success:function(data){
				console.log( data.data instanceof Array)
				socketData=data.data
			},
			error: function () {
				showWDlwindow(1);
				showDoctor();
			}
		})
	}
	//初始化
	var init=function(){
		/*var url = window.location.href;
		var pulicObj=GetRequest(url);
		//赋值全局变量
		loginCode=pulicObj.longId;
		queueId=pulicObj.queueId;*/
		if(loginCode && queueId){
			//当前就诊数据
			nowSick(queueId);
			//等候数据
			waitData(loginCode,queueId);
			//过号数据
			overdueData(loginCode,queueId);
		}else{
			layer.msg('参数错误');
			//返回登录界面
			layer.msg('参数错误,即将跳转',{offset:['50%'],time: 2000 },function(){
				 //location.href=loginUrl;
			});
		}
	}
	//当前就诊数据
	var nowSick=function(queueId){
	  $.ajax({
			  type:"POST",
			  url:initUrl,
			  data:{queueId:queueId},
			  success:function(r){
				if(r.data){
					brid=r.data.id;
					sickName=r.data.sickName;
					$('#hjbrname').val(sickName);

					$('#calling').addClass('layui-btn-disabled');
					$("#calling").attr("disabled",true);
				}
			},
			error:function(r){
				 layer.msg('连接错误，请联系管理员！');
			}
		 })
	}
	//等候就诊
	var waitData=function(loginCode,queueId){
	   waiteTable=table.render({
			elem: '#demo1'
			,height: 'full-200'
			,data:socketData
			,cellMinWidth: 80
			,limit:30
			,limit:5
			,cols: [[ //表头
			   //{type:'radio'},
			   {field: 'id', title: 'ID'}
			  ,{field: 'sickName', title: '姓名',width:105,}
			  ,{field: 'sickNumber', title: '号序',width:110,}
			  ,{field: 'sickState', title: '状态',width:180, templet:function(d){
				  if(d.sickState==1){
					  return '未到过号';
				  }else if(d.sickState==2){
					  return '复诊';
				  }else if(d.sickState==5){
					  return '优先';
				  }
				  else if(d.sickState==6){
					  return '过号';
				  }
				  else{
					  return '初诊';
				  }

			  }}
			]],done: function () {
			   $("[data-field='id']").css('display','none');
		   }
	   });
	}
	//过号
	var overdueData=function(doctorCode,type){
	   overdueDataTable=table.render({ // render重新渲染占用资源太多
			elem: '#demo2'
			,height: 'full-200'
			//,url: queueUrl
			,cellMinWidth: 80
			,limit:30
			//,method: 'POST'
			//,where:{loginCode:loginCode,queueId:queueId,type:2}
			,cols:[[ //表头
			   {field: 'id', title: 'ID', width:60,  }
			  ,{field: 'sickName', title: '姓名', width:105}
			  ,{field: 'sickNumber', title: '号序', width:110,}
			  ,{field: 'sickState', title: '状态', width:180, templet:function(d){
				  if(d.sickState==1){
					  return '未到过号';
				  }else if(d.sickState==2){
					  return '复诊';
				  }else if(d.sickState==5){
					  return '优先';
				  }
				  else if(d.sickState==6){
					  return '过号';
				  }
				  else{
					  return '初诊';
				  }

			  }}
			]],done: function () {
			   $("[data-field='id']").css('display','none');
		   }
	   });
	}
	//选中等候队列
	$(document).on('click',"#dhjz",function(){
		tab='demo1';
	});
	//选中过号队列
	$(document).on('click',"#ygh",function(){
		tab='demo2';
	});


	//触发呼叫
	$(document).on('click',"#calling",function(){
		var data =table.checkStatus(tab).data;
		//呼叫后10S不能点击呼叫
		if(data.length > 0){
			//呼叫后重新加载数据
			brid=data[0].id;
			sickName=data[0].sickName;
			docall(sickName);
			//执行禁止呼叫
		}else{
			//layer.msg('请选择一个病人',{offset:"l"});
			var data=table.cache[tab];
			if(data.length > 0){
				brid=data[0].id;
				sickName=data[0].sickName;
				docall(sickName);
			}else{
				layer.msg('暂无病人！');
			}
		}
	});
	//触发过号
	$(document).on('click',"#overed",function(){
		if(hjstat){
			layer.msg('当前正在呼叫状态！',{offset:"l"});
			return false;
		}
		if(brid){
			var postdata={sickId:brid,queueId:queueId};
			$.ajax({
				type:"POST",
				url:setOver,
				data:postdata,
				success:function(r){
					if(r.code=='success'){
						$('#hjbrname').val('');
						$('#calling').removeClass('layui-btn-disabled');
						$("#calling").attr("disabled",false);
						brid='';
						name='';
						if(tab=='demo1'){
							waiteTable.reload();
						}else{
							overdueDataTable.reload();
						}

					}else{
						layer.msg(r.msg);
					}
				},
				error:function(r){
					layer.msg('连接错误，请联系管理员！');
				}
			})

			//ajax结诊
			// brid='';
			//$('#hjbrname').val('');
			//waiteTable.reload();
		}else{
			layer.msg('请呼叫一个病人',{offset:"l"});return false;
		}
	});
	//触发重呼叫
	$(document).on('click',"#recall",function(){
	  var data={sickId:brid,queueId:queueId};
	  if(brid){
		 $.ajax({
			  type:"POST",
			  url:reCallUrl,
			  data:data,
			  success:function(r){
				if(r.code=='success'){
					var show=setInterval(function(){
						 $('#recall').addClass('layui-btn-disabled');
						 $("#recall").attr("disabled",true);
						 $('#callend').addClass('layui-btn-disabled');
						 $("#callend").attr("disabled",true);
						 $("#recall").html(timer);
						 timer-=1;
						 if( timer<=0){
							  clearInterval(show);
							  timer=5;
							  $('#recall').removeClass('layui-btn-disabled');
							  $("#recall").attr("disabled",false);
							  $('#callend').removeClass('layui-btn-disabled');
							  $("#callend").attr("disabled",false);
							  $("#recall").html('重呼');
						 }
					   },1000);

				}else{
					layer.msg('呼叫失败！');
			  }
			},
			error:function(r){
				 layer.msg('连接错误，请联系管理员！');
			}
		 })
	  }else{
		 layer.msg('当前没有呼叫病人，无法	！',{offset:"l"});
	  }
	});
	//触发结诊
	$(document).on('click',"#callend",function(){
	  if(hjstat){
		  layer.msg('当前正在呼叫状态！',{offset:"l"});
		  return false;
	  }
	  if(brid){
		   var postdata={sickId:brid,queueId:queueId};
		   $.ajax({
			  type:"POST",
			  url:callendUrl,
			  data:postdata,
			  success:function(r){
				if(r.code=='success'){
					$('#hjbrname').val('');
					$('#calling').removeClass('layui-btn-disabled');
					$("#calling").attr("disabled",false);
					brid='';
					sickName='';
					if(tab=='demo1'){
						waiteTable.reload();
					}else{
						overdueDataTable.reload();
					}

				}else{
					layer.msg('呼叫失败！');
			  }
			},
			error:function(r){
				 layer.msg('连接错误，请联系管理员！');
			}
		 })

		   //ajax结诊
		  // brid='';
		   //$('#hjbrname').val('');
		   //waiteTable.reload();
	   }else{
		   layer.msg('请呼叫一个病人',{offset:"l"});return false;
	   }
	});


	//执行呼叫
	var docall=function(sickName){
	   var data={sickId:brid,queueId:queueId};
	   //ajax执行呼叫
	   $.ajax({
		  type:"POST",
		  url:docallUrl,
		  data:data,
		  success:function(r){
			if(r.code=='success'){
				$('#hjbrname').val(sickName);
				lockcallbutton();
				if(tab=='demo1'){
					console.log(socketData)
					waiteTable.reload();
				}else{
					overdueDataTable.reload();
				}
			}else{
				layer.msg('呼叫失败！');
		  }
		},
		error:function(r){
			 layer.msg('连接错误，请联系管理员！');
		}
	  })
	}
	//按钮锁定
	var lockcallbutton=function(){
		$('#calling').addClass('layui-btn-disabled'); //追加样式
		$("#calling").attr("disabled",true);
		//十秒后返回原来样式
		/* var show=setInterval(function(){
			 $("#calling").html(timer);
			 timer-=1;
			 hjstat=true;
			 if( timer<=0){
				 clearInterval(show);
				 timer=10;
				  $('#calling').removeClass('layui-btn-disabled');
				  $("#calling").attr("disabled",false);
				  $("#calling").html('呼叫');
				  hjstat=false;
			 }
	   },1000); */
	}
	//获取URL参数
	var GetRequest=function(urlStr){
		if (typeof urlStr == "undefined") {
		  var url = decodeURI(location.search); //获取url中"?"符后的字符串
		 } else {
			 var url = "?" + urlStr.split("?")[1];
		 }
		 var theRequest = new Object();
		 if (url.indexOf("?") != -1) {
			 var str = url.substr(1);
			strs = str.split("&");
			for (var i = 0; i < strs.length; i++) {
				theRequest[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
			 }
		 }
		 return theRequest;
	}


	//增加叫号websocket
	var webServerUrl;
	var create=function(webServerUrl){
		var socket;
		if (!window.WebSocket) {
			window.WebSocket = window.MozWebSocket;
		}
		if (window.WebSocket) {
			socket = new WebSocket(webServerUrl);
			socket.onmessage = function (event) {
				var jzxxdata=JSON.parse(event.data);
				waiteTable.reload({data:jzxxdata.wait});
			};
			socket.onopen = function (event) {
				console.log("websocket 打开了");
			};
			socket.onclose = function (event) {
				console.log("websocket 关闭了");
				try {
					lockReconnect = false;
					websocketReconnect(webServerUrl);
					onClose(evt);
				}catch (e) {
					websocketReconnect(webServerUrl);
					onClose(evt);
				}
			};
		}
	}
	var tt = null;
	var  websocketReconnect=function(url){
		if (lockReconnect) {       // 是否已经执行重连
			return;
		};
		lockReconnect = true;
		console.log("重来")
		//没连接上会一直重连，设置延迟避免请求过多
		tt && clearTimeout(tt);
		tt = setTimeout(function () {
			create(url);
			lockReconnect = false;
		}, 2000);
	}
	var  onClose=function(evt) {
		console.log("连接已关闭...");
	}
	var initsocket=function(){
		var serverUrl = (window.location.href).split("//")[1].split("/")[0];
		$.ajax({
			type: "GET",
			async: false,
			url: "/cdroho/callogic/clientinfo",
			contentType: "application/text;charset=utf-8",
			dataType: "json",
			cache: false,
			success: function (data) {
				if(data.code == 'success'){
					webServerUrl = "ws://" + serverUrl + "/cdroho/productWebSocket/"+ data.ip ;
					create(webServerUrl);
				}
			},
			error: function () {
				datas = { "count": 0, "fztname": "未知" };
			}
		});
	}
	/**执行sockite**/
	initsocket();

}());

