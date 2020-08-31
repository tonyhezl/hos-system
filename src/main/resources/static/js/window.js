$(function (){
   //全局变量
   var loginCode;//登陆工号
   var flag;//窗口类型
   var type;//类型
   var loginUrl='login.html';
   var waiteTable;
   var loginType;
   var overdueDataTable;
   var queueId;//队列ID
   var brid;//病人ID
   var name;//病人姓名
   var timer=5;//重复倒计时
   var hjstat=false;//呼叫状态
   var tab='demo1';
   var initUrl='/cdroho/window/queryNow'

   var queueUrl='/cdroho/window/windowDetail';//窗口队列数据
   var docallUrl='/cdroho/window/windowCall';//呼叫
   var callendUrl='/cdroho/window/windowSetFinish';//结诊
   var reCallUrl='/cdroho/window/windowReCall';//重呼
	var setOver='/cdroho/window/setPass';//过号

	var phaCall='/cdroho/window/phaCall';//呼叫
	var phaReCall='/cdroho/window/phaReCall';//重呼
	var PhaFinish='/cdroho/window/setPhaFinish';//结诊
	var setPhaOver='/cdroho/window/setPhaPass';//过号

	window.onload=function(){
		layui.use(['form','element','table'],
			function() {
				layer = layui.layer;
				element = layui.element;
				table = layui.table;
				init();
			});
		setTimeout(schedule, 30000);
	}
   //初始化
   var init=function(){
	    var url = window.location.href;
	    var pulicObj=GetRequest(url);
        //赋值全局变量
		loginCode=pulicObj.loginCode;
	    flag=pulicObj.flag;
	    loginType=pulicObj.loginType;
		if(flag){
			//正在就诊数据
			nowData(flag,loginType);
			//等候数据
			waitData(loginCode,flag,loginType);
			//过号数据
			overdueData(loginCode,flag,loginType);
		}else{
			layer.msg('参数错误');
			//返回登录界面
			layer.msg('参数错误,即将跳转',{offset:['50%'],time: 2000 },function(){
				 //location.href=loginUrl;
			});
		}
   }
   //登录数据
   var nowData=function(flag,loginType){
	  $.ajax({
			  type:"POST",
			  url:initUrl,
			  data:{flag:flag,loginType:loginType},
			  success:function(r){
				if(r.data){
					brid=r.data.id;
					name=r.data.name;
					$('#hjbrname').val(name);
					//$('#ksname').html(r.ksname);
					$('#calling').addClass('layui-btn-disabled');
				    $("#calling").attr("disabled",true);
				}else{
					//$('#ksname').html(r.ksname);
					return;
				}
			},
			error:function(r){
				 layer.msg('连接错误，请联系管理员！');
			}
	     })  
   }
   //等候就诊
   var waitData=function(loginCode,flag,loginType){
	   waiteTable=table.render({
			elem: '#demo1'
			,height: 320
			,url:queueUrl //数据接口
			//,page: true //开启分页
			,where:{loginCode:loginCode,flag:flag,type:1,loginType:loginType}
			,method: 'POST'
			,limit:5
			,cols: [[ //表头
			   {type:'radio'}
			  ,{field: 'id', title: 'ID', width:60,  }
			  ,{field: 'name', title: '姓名', width:110}
			  ,{field: 'number', title: '号序', width:60,}
			  ,{field: 'state', title: '状态', width:70, templet:function(d){
				  if(d.state==1){
					  return '未到过号';
				  }else if(d.state==2){
					  return '复诊';
				  }else if(d.state==5){
					  return '优先';
				  }
				  else{
					  return '初诊';
				  }

			  }}
			]]
	   });
   }
   //过号
   var overdueData=function(loginCode,flag,loginType){
	   overdueDataTable=table.render({
			elem: '#demo2'
			,height: 250
			,url: queueUrl //数据接口
			,method: 'POST'
			//,page: true //开启分页
			,where:{loginCode:loginCode,flag:flag,type:2,loginType}
			,cols:[[ //表头
			   {type:'radio'}
			  ,{field: 'id', title: 'ID', width:60,  }
			  ,{field: 'name', title: '姓名', width:110}
			  ,{field: 'number', title: '号序', width:60,}
			  ,{field: 'state', title: '状态', width:70, templet:function(d){
				  console.info(d);
				  if(d.state==1){
					  return '未到过号';
				  }else if(d.state==2){
					  return '复诊';
				  }else if(d.state==5){
					  return '优先';
				  }
				  else if(d.state==6){
					  return '过号';
				  }
				  else{
					  return '初诊';
				  }

			  }}
			]]
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
		name=data[0].name;
		docall(name);
		//waiteTable.reload();
		//执行禁止呼叫
	 }else{
	   //layer.msg('请选择一个病人',{offset:"l"});
	   var data=table.cache[tab];
	   if(data.length > 0){
		   brid=data[0].id;
	       name=data[0].name;
	       docall(name);
	   }else{
		    layer.msg('暂无病人！');  
	   } 
	 }
	 
   });
   //触发重呼叫
   $(document).on('click',"#recall",function(){ 
   	  var queueId;
      var url;
	   if (loginType==1){
		   url = reCallUrl;
		   queueId=6444;
	   }else if(loginType==2){
		   url = phaReCall;
		   queueId=6443;
	   }else{
		   url = phaReCall;
		   queueId=6445;
		   flag=0;
	   }
	   var data={sickId:brid,flag:flag,queueId:queueId};
      if(brid){
		 $.ajax({
			  type:"POST",
			  url:url,
			  data:data,
			  success:function(r){
				console.info(r);
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
					layer.msg('结诊失败！');
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
	   var url;
	   if (loginType==1){
		   url = callendUrl;
	   }else{
		   url = PhaFinish;
	   }
      if(brid){
		   var postdata={sickId:brid,flag:flag};
		   $.ajax({
			  type:"POST",
			  url:url,
			  data:postdata,
			  success:function(r){
				console.info(r);
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

   //触发过号
   $(document).on('click',"#overed",function(){
		if(hjstat){
			layer.msg('当前正在呼叫状态！',{offset:"l"});
			return false;
		}
		var url;
		if (loginType==1){
			url = setOver;
		}else{
			url = setPhaOver;
		}
		if(brid){
			var postdata={sickId:brid,flag:flag};
			$.ajax({
				type:"POST",
				url:url,
				data:postdata,
				success:function(r){
					console.info(r);
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
	
   //执行呼叫
   var docall=function(name){
	   var url;
	   if (loginType==1){
	   	   url = docallUrl;
		   queueId=6444;
	   }else if(loginType==2) {
	   	   url = phaCall;
		   queueId=6443;
	   }else{
		   url = phaCall;
		   queueId=6445;
		   flag=0;
	   }
	   var data={sickId:brid,flag:flag,queueId:queueId};
	   //ajax执行呼叫
	   $.ajax({
		  type:"POST",
		  url:url,
		  data:data,
	      success:function(r){
			if(r.code=='success'){
				$('#hjbrname').val(name);
				lockcallbutton();
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

	function schedule(){
		var k=0;
		try {
			$.ajax({
				type: "POST",
				url: queueUrl,
				data: {loginCode:loginCode,flag:flag,type:1,loginType:loginType},
				success: function (r) {
					if (r.count > 0) {
						k = r.count*1000
						setTimeout(schedule, k);
					} else {
						waiteTable.reload();
						k = 30000;
						setTimeout(schedule, k);
					}
				},
				error: function (r) {
					layer.msg('连接错误，请联系管理员！');
				}
			})
		} catch (err) {
			setTimeout(schedule, 30000);
		}
	}

}());
