var exchange = {
    //截取url中的字符串
    getQueryString:function(){
        var src = location.href;
        var pos = src.indexOf('?');
        var substring = src.substr(pos + 1);
        var qs = substring.match(/[^&]*=[^&]*/g),
            param = {},
            t,
            i = qs && qs.length;
        while (i--) {
            t = qs[i].split("=");
            param[t[0]] = t[1];
        }
        return param;
    },
    //时间戳
    format:function(time){
        var s="";
        var time_Mouth = (time.getMonth() + 1) >= 10 ? (time.getMonth() + 1) : '0'+(time.getMonth() + 1);
        var time_Date = time.getDate() >= 10 ? time.getDate() : '0'+time.getDate();
        s+=time.getFullYear()+"-"+time_Mouth+'-'+time_Date+'   '+time.getHours()+':'+time.getMinutes();
        return s;
    },
    //调取接口
    ajax:function(encryptParams,num){
        var html='',
            rate='';
        $.ajax({
            url:'/api/score/exchangeRecord.do',
            type:'GET',
            data:{
                encryptParams:encryptParams,
                pageNo:num,
                pageSize:'10'
            },
            success:function(data){
                var data = typeof data === 'string' ? JSON.parse(data) : data;
                var recordlist = data.recordList;
                if(data.ret==0){

                    if(recordlist.length > 0){
                        for(var n= 0,m=recordlist.length; n<m; n++){
                            var time = data.recordList[n].exchangeTime;
                            var date=new Date(time);

                            html += '<li> <a href="#" class="item-link item-content" style="padding-left:0.55em; "><div class="item-media"><img src="'+data.recordList[n].flowIconUrl+'" width="80"></div><div class="item-inner" style="padding-right: 0; background: none; margin-left: 0.55em;"> <div class="item-title-row col-50" style="margin: 0"><div class="item-title result_exchange"><i class="fisrt">'+data.recordList[n].flowName+'流量包</i><i>兑换积分:<span class="orange">'+data.recordList[n].creditUsed+'</span></i><i>兑换号码：'+data.recordList[n].phoneNumber+'</i> <i>'+exchange.format(date)+'</i></div><div class="item-after"></div> </div></div></a></li>';
                            if(data.recordList[n].status==1){
                                rate = '<span id="result_r" class="success_rate">兑换成功</span>';
                            }else if(data.recordList[n].status==0){
                                rate = '<span id="result_r" class="disposal">处理中..</span>';
                            }else{
                                rate = '<span id="rate_fail" class="success_rate">兑换失败</span>';
                            }
                        }
                        $('#result_ul').append(html);
                        $('.item-after').append(rate);
                    }else{
                        $('.ratefail_none').show();
                    }


                }else{
                    $('.conversion').hide();
                    $('.ratefail_box').show();
                    $('body,html').one('click',function(){
                        location.reload();
                    });
                }
            }
        });
    }

};

var encryptParams = exchange.getQueryString().encryptParams || "";
//var encryptParams = window.jsInterface.getEncryptParams(); //加密串
var totalCredit = exchange.getQueryString().totalCredit || "";
var exchange_con  = decodeURIComponent(exchange.getQueryString().firstLi) || ""; //判断url是否含有firstLi
var exchange_num  = exchange.getQueryString().num || ""; //判断url是否含有num
var exchange_orange  = exchange.getQueryString().orange || ""; //判断url是否含有orange
var exchange_grouth  = decodeURIComponent(exchange.getQueryString().list_grouth) || ""; //判断url是否含有
var Open  = decodeURIComponent(exchange.getQueryString().Open) || ""; //判断url是否含有// list_grouth
var flow  = decodeURIComponent(exchange.getQueryString().flow) || ""; //判断url是否含有// list_grouth






