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
    }
};

var encryptParams = window.jsInterface.getEncryptParams(); //加密串
var totalCredit = exchange.getQueryString().totalCredit || "";
var exchange_con  = decodeURIComponent(exchange.getQueryString().firstLi) || ""; //判断url是否含有firstLi
var exchange_num  = exchange.getQueryString().num || ""; //判断url是否含有num
var exchange_orange  = exchange.getQueryString().orange || ""; //判断url是否含有orange
var exchange_grouth  = decodeURIComponent(exchange.getQueryString().list_grouth) || ""; //判断url是否含有
var Open  = decodeURIComponent(exchange.getQueryString().Open) || ""; //判断url是否含有// list_grouth
var flow  = decodeURIComponent(exchange.getQueryString().flow) || ""; //判断url是否含有// list_grouth






