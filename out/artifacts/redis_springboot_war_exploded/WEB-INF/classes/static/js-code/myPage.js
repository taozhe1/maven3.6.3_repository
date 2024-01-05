$(function (){
    $('#ajaxButton').on('click',function (){
        $.ajax({
            url:$.ajaxSetup().url+'devRedis/getBean2',
            type:"get",
            data:null,
            dataType:'json',
            async:true,
            success:function (data) {
                alert(data);
                console.log(data);
            },
            error:function(error){
                console.log("error",error);
                alert('error');
            }
        })

    });
});