$(document).ready(function () {

    $("#dataformat_submit").click(function () {
        console.log($("input[name='dateformatoptions']:checked").val())
        var url="/dateformat/updTDateFormatById/"+$("input[name='dateformatoptions']:checked").val()
        $.ajax({
            type:"POST",
            url:url,
            success:function (data) {
                if(data== true){
                    console.log("应用成功")
                }else{
                    console.log("应用失败")
                }
            },
            error : function(e) {
                console.log(e);
                alert("无权操作");
            }
        });
    })

})