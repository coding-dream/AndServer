$(function(){
    var clipboard = new Clipboard('.btn');
    clipboard.on("success", function(e){
        e.clearSelection();
        $(".p_copy").remove();
        showTips("复制成功");
    });
});

function showTips(msg){
    var tipMsg = `
       <center><div class="tipMsg">></div></center>
    `;
    $("body").prepend(tipMsg);
    $(".tipMsg").html(`<span style='color:red;'>${msg}</span>`).hide(3000);// 这个是渐渐消失
}

function handleChatGet(){
    $.get("/chat_get", function(data, status) {
        // 复制方式一（利用clipboard.min.js插件实现）
        var existDom = $('.p_copy').length;
        if(existDom > 0){
            $(".p_copy").find(".btn").text(`复制-${data}`);
            $(".p_copy").find(".btn").attr("data-clipboard-text", data);
            $(".p_copy").show();
        } else {
            var text = `
                <p class="p_copy"><button class="btn" data-clipboard-text="${data}">复制-${data}</button></p>
            `;
            $("body").prepend(text);
        }

        // 给复制方式二（原生实现）
        $("#copy").text(data);

    });
}

function handleChat(){
    var word = prompt("请输入一段文字", "");
    if(word){
        $.get("/chat", {message: word}, function(data, status) {
            showTips("已发送信息到App客户端");
        });
    }
}

// 复制文本方式二
function copyText(){
    var e = document.getElementById("copy");
    e.select(); // 选择对象
    document.execCommand("Copy"); // 执行浏览器复制命令
    showTips("内容复制成功！");
}