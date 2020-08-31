(function (){
	/**当前时间**/
	var  nowtime=function(){
		var myDate = new Date();
		var hour = myDate.getHours();
		var minutes = myDate.getMinutes();
		 var seconds = myDate.getSeconds()
		
	    if (hour >= 0 && hour <= 9) {
                hour = "0" + hour;
        }
	    if (minutes >= 0 && minutes <= 9) {
                minutes = "0" + minutes;
        }
		
		var nowtime=hour+":"+minutes;
		$("#nowtime").html(nowtime);
	}
 
   /**日期**/
   var todayDate=function(){
	   	var myDate = new Date();
	   var todaydate=myDate.getFullYear()+'-'+(myDate.getMonth() +1)+'-'+myDate.getDate();
	   /*日期*/
	   var week = new Date().getDay(); 
	   if (week == 0) { 
			str = "星期日"; 
		} else if (week == 1) { 
			str = "今星期一"; 
		} else if (week == 2) { 
			str = "星期二"; 
		} else if (week == 3) { 
			str = "星期三"; 
		} else if (week == 4) { 
			str = "星期四"; 
		} else if (week == 5) { 
			str = "星期五"; 
		} else if (week == 6) { 
			str = "星期六"; 
	   } 
	   $("#nowdate").html(todaydate+'&nbsp;'+str);
   }
   nowtime();
   todayDate();
   /**刷新时间**/
   var addtime=setInterval(function(){
	  nowtime();
  },1000);
}());
