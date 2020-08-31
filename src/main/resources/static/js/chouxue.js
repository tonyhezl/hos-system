(function (){
	/**初始化数据**/
	var initurl='/cdroho/window/cxdata';
	var ghxxurl="/cdroho/window/getPassBlo";
	var flag;
	var zsmc;
	var is_yinsi=true;
	var initdata=function(){
		$.ajax({
			type:"POST",
			url:initurl,
			data:{loginType:1},
			success:function(data){
				if(data.msg){
					//未登录状态
					showWDlwindow(1);
					showDoctor();
				}else{
					//追加数据
					flag=data.data[0].flag;
					var  zhxxdata=data.data;
					waitWinow(zhxxdata);//综合信息显示
					initghxx();//过号信息

				}
			},
			error: function () {
				showWDlwindow(1);
				showDoctor();
			}
		})
	}
	/**过号信息**/
	var initghxx=function(){
		$.ajax({
            type: "GET",
            async: false,
            url: ghxxurl,
            contentType: "application/text;charset=utf-8",
			data:{'flag':flag},
            dataType: "json",
            cache: false,
            success: function (data) {
			  var str='';
			  for(var i=0;i<data.length;i++){
				  var number=data[i].sickNumber
				  str=str+data[i].sickName+'&nbsp';
				  
			  }
			  $('#ghry').html(str);
            },
            error: function (data) {
              
            }
        });
	}
	/**呼叫信息**/
	var  hjxxWindow=function(data){
		if(data){
		  var number=data[0].number;
		  if(number < 10){
			  number='00'+number
		  }else if(number < 100){
			   number='0'+number
		  }
		  var brname=hideword(data[0].name);
		  var str=number+'号--'+brname;
		  $('#zjjz').html(str);
		}
	}
	/**姓名隐藏**/
	var hideword=function(str) {
        if (is_yinsi) {
            if (str.length > 3)
                return str.replace(/^(\S{1})\S{2}(.*)$/, '$1**$2');
            else 
                return str.replace(/^(\S{1})\S{1}(.*)$/, '$1*$2');
        } else
            return str;
    }
	/**等候信息**/
	var waitWinow=function(data){
		var count=5;
		if(data.length >0){
		  //封装字符串
          if(data.length < 5){
			 var  count=data.length;
		  }
		  var str='';
		  for(var i=0;i<count;i++){
			  //拼装字符串
			  var waitstr=waitString(data[i].wait);
			  if(data[i].now.length > 0){
				  var zzjz=data[i].now[0].name
			  }else{
				  var zzjz='&nbsp;';
			  }
			  if (waitstr) {
				  str = str + '<ul><li class="zhhp_ksname">' + data[i].zsmc + '</li><li class="zhhp_zzjz">' + zzjz + '</li><li class="zhhp_dhjz">' + waitstr + '</li></ul>';
			  }else{
				  str = str + '<ul><li class="zhhp_ksname">' + data[i].zsmc + '</li><li class="zhhp_zzjz">' + zzjz + '</li><li class="zhhp_dhjz">' + "" + '</li></ul>';
			  }		  }
		 $('#jzxx').html(str);
		}
	}
	/**候诊信息封装**/
	var waitString=function(data){
		var count=5;
		var str='';
		if(data.length > 0){
			if(data.length < 5){
				var  count=data.length;
			}
			
			for(var i=0;i<count;i++){
				 str="&nbsp&nbsp"+str+data[i].name+"&nbsp&nbsp";
			}
		}
		return str;
	}
	/**呼叫信息显示**/
	var showHjxx=function(data,zsmc){
		hjxxWindow(data);
		$("#hjxx").css("display","block");
		var str='请'+data[0].name+'到'+zsmc+'抽血';
		/*const u = new SpeechSynthesisUtterance('No warning should arise');
		u.text = str;
		u.lang = 'zh';
		u.rate = 0.7;
		u.volume = 6;
		u.pitch = 10;
		try {
			let spe = window.speechSynthesis;
			window.speechSynthesis.speak(u);
			let vocies=[];
			vocies=spe.getVoices();
			/!*for(i = 0; i < vocies.length; i++) {
				alert(vocies[i].name)
			}*!/
		} catch (e) {
			alert(window.speechSynthesis);
		}*/
		$("#hjxx").html(str);
		var show=setInterval(function(){
		   $("#hjxx").css("display","none");
		   clearInterval(show);
        },3000);
	}
	/**执行初始化方法**/
	initdata();	
	/**循环遍历**/
	var xhinitdata=setInterval(function(){
	  initdata();;
    },5000);
	/**sockit方法**/
	var lockReconnect = false;
	var webServerUrl;
	var create=function(webServerUrl){
		var socket;
		if (!window.WebSocket) {
			window.WebSocket = window.MozWebSocket;
		}
		if (window.WebSocket) {
			socket = new WebSocket(webServerUrl);
			socket.onmessage = function (event) {
				if (event.data){
					var jzxxdata=JSON.parse(event.data);
					hjxxWindow(jzxxdata.now);//呼叫信息
					//waitWinow(jzxxdata.wait);//等候信息
					showHjxx(jzxxdata.now,jzxxdata.zsmc);//显示呼叫信息
				}

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
