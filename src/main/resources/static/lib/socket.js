<script type="text/javascript">
    var lockReconnect = false;
    var webServerUrl;
    function create(webServerUrl){
        var socket;
        if (!window.WebSocket) {
            window.WebSocket = window.MozWebSocket;
        }
        if (window.WebSocket) {
            socket = new WebSocket(webServerUrl);
            socket.onmessage = function (event) {
                console.log("收到消息", event.data);
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
    function websocketReconnect(url) {
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
        }, 5000);
    }

    function onClose(evt) {
        console.log("连接已关闭...");
    }
    window.onload=function(){
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
</script>