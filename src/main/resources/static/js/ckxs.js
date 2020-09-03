(function (){
	/**初始化数据**/
	var initurl='/cdroho/wait/small';
	var ghxxurl="/cdroho/wait/getPass";
	var is_yinsi=true;
	var flag=true;
	var zsmc='';
	var queueId;
	var p = $(this), c = p.children(),speed=3000; //值越大速度越小
	var cw = c.width(),pw=p.width();
	var t = (cw / 100) * speed;
	var f = null, t1 = 0;
	var initdata=function(){
		$.ajax({
			type: "GET",
			async: false,
			url: initurl,
			contentType: "application/text;charset=utf-8",
			dataType: "json",
			cache: false,
			success: function (data) {
				if (flag) {
					jQuery(".txtMarquee-left").slide({
						mainCell: ".bd ul",
						autoPlay: true,
						effect: "leftMarquee",
						vis: 3,
						interTime: 50
					});
					flag = false;
				}
				if(data.msg=='faild'){
					//未登录状态
					showWDlwindow(1);
					showDoctor();
				}else{
					queueId=data.queueId;
					//登录状态
					showDoctor(data.doctor);	//医生信息
					showWDlwindow(0);//清除未登录
					var jzxxdata=JSON.parse(data.data[0]);
					zsmc=jzxxdata.zsmc;
					$('#ksname').html(zsmc);
					if (jzxxdata.now) {
						hjxxWindow(jzxxdata.now);
					}else{
						$('#zjjz').html('');
					}
					//呼叫信息
					waitWinow(jzxxdata.wait);//等候信息
					initghxx();//过号信息

				}
			},
			error: function () {
				showWDlwindow(1);
				showDoctor();
			}
		});
	}

	var initghxx=function(){
		$.ajax({
			type: "GET",
			async: false,
			url: ghxxurl,
			contentType: "application/text;charset=utf-8",
			data:{'state':3,'queueId':queueId},
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
	/**显示医生信息**/
	var showDoctor=function(data){
		if(data){
			var data=JSON.parse(data);
			//执行追加
			//$('#ksname').html(zsmc);
			$('#yyname').html(data.doctorName);
			$('#yyzc').html(data.title);
			$('#yyjs').html(data.profile);
			var img='../images/'+data.doctorCode+'.jpg';
			var doctorimg="<img class= 'events-profilePic' width='258' height='348' src='" + img +
				"' alt=''/>"
			$("#doctorimg").html(doctorimg);

		}else{
			//清空信息
			$('#ksname').html('');
			$('#yyname').html('');
			$('#yyzc').html('');
			$('#yyjs').html('');
			$("#doctorimg").html('');
		}
	}
	/**显示未登录**/
	var showWDlwindow=function(state){
		if(state==1){
			//显示弹框
			$("#wdl").css("display","block");
			//清空显示

		}else{
			$("#wdl").css("display","none");
		}

	}
	var hideword=function(str) {
		if (is_yinsi) {
			if (str.length > 3)
				return str.replace(/^(\S{1})\S{2}(.*)$/, '$1**$2');
			else
				return str.replace(/^(\S{1})\S{1}(.*)$/, '$1*$2');
		} else
			return str;
	}
	/**呼叫信息**/
	var  hjxxWindow=function(data){
		if(data){
			var number=data[0].sickNumber;
			var str=number+'号--'+data[0].sickName;
			$('#zjjz').html(str);
		}
	}
	/**等候信息封装，显示两个等候患者**/
	var waitWinow=function(data){
		var str = '';
		if (data) {
			if (data.length > 0) {
				//封装字符串
				if (data.length >= 2) {
					for (var i = 0; i < 2; i++) {
						var number = data[i].sickNumber
						var brname = hideword(data[i].sickName);
						if (data[i].sickState == 1) {
							var state = '(未到过号)';
						} else if (data[i].sickState == 2) {
							var state = '(复诊)';
						} else if (data[i].sickState == 5) {
							var state = '(优先)';
						} else {
							var state = '';
						}
						str = str + '<li>' + number + '号--' + brname + state + '</li>';
					}
				} else if (data.length < 2) {
					for (var i = 0; i < data.length; i++) {
						var number = data[i].sickNumber
						var brname = hideword(data[i].sickName);
						if (data[i].sickState == 1) {
							var state = '(未到过号)';
						} else if (data[i].sickState == 2) {
							var state = '(复诊)';
						} else if (data[i].sickState == 5) {
							var state = '(优先)';
						} else {
							var state = '';
						}
						str = str + '<li>' + number + '号--' + brname + state + '</li>';
					}
				} else if (data.length == 0) {
					$('#dhjz').html(str);
				}
				$('#dhjz').html(str);
			}
		}else{
			$('#dhjz').html(str);
		}
	}
	/**呼叫信息显示**/
	var showHjxx=function(data,ksmc){
		hjxxWindow(data);
		$("#hjxx").css("display","block");
		var str='请'+data[0].sickName+'到'+ksmc+'就诊';
		$("#hjxx").html(str);
		var show=setInterval(function(){
			$("#hjxx").css("display","none");
			clearInterval(show);
		},5000);
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
				var jzxxdata=JSON.parse(event.data);
				hjxxWindow(jzxxdata.now);//呼叫信息
				waitWinow(jzxxdata.wait);//等候信息
				showHjxx(jzxxdata.now,zsmc);//显示呼叫信息


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
