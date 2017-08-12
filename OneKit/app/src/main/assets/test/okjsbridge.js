;
// for choose image
var chooseImageCallBack;
var chooseImageResult;
function onChooseImageResult(data){
     chooseImageResult = data;
     chooseImageCallBack(chooseImageResult);
};

//for http request
var requestCallBack;
function onRequestResult(data){
     requestCallBack(data);
};

//for download file request
var downloadCallBack;
function onDownloadFileResult(data){
    downloadCallBack(data);
};

//for upload file request
var uploadCallBack;
function onUploadFileResult(data){
    uploadCallBack(data);
};

//for select mobile data
var selectMobileDataCb;
function onSelectMobileDataResult(data){
    selectMobileDataCb(data);
};

(function(window){
    window.jsok = {
        checkJsApi: function(){
            return ok.checkJsApi();
        },
        getNetworkType: function(){
            return ok.getNetworkType();
        },
        logout: function(){
            ok.logout();
        },
        chooseImage: function(compressRatio, sizeType, sourceType, callback){
            chooseImageCallBack = callback;
            ok.chooseImage(compressRatio, sizeType, sourceType);
        },
        previewImage: function(){
            if (typeof chooseImageResult == 'undefined'
                || typeof chooseImageResult.path == 'undefined') {
                ok.showShortToast('get an empty img path');
            } else {
                ok.previewImage(chooseImageResult.path);
            }
        },
        previewDownloadedImage: function(path){
            ok.previewImage(path);
        },
        showNavigationBar: function(){
            ok.showNavigationBar();
        },
        hideNavigationBar: function(){
            ok.hideNavigationBar();
        },
        setNavigationBarTitle: function(title){
            ok.setNavigationBarTitle(title)
        },
        setNavigationBarColor: function(color){
            ok.setNavigationBarColor(color);
        },
        setViewBgColor: function(color){
            ok.setViewBgColor(color);
        },
        setScreenBrightness: function(num){
            ok.setScreenBrightness(num);
        },
        getScreenBrightness: function(){
            return ok.getScreenBrightness();
        },
        setKeepScreenOn: function(){
            ok.setKeepScreenOn();
        },
        vibrate: function(num){
            ok.vibrate(num);
        },
        setClipboardData: function(content){
            ok.setClipboardData(content);
        },
        getClipboardData: function(){
            return ok.getClipboardData();
        },
        makePhoneCall: function(phoneNum){
            ok.makePhoneCall(phoneNum);
        },
        setStorage: function(k, v){
            ok.setStorage(k, v);
        },
        getStorage: function(key){
            return ok.getStorage(key);
        },
        removeStorage: function(key){
            ok.removeStorage(key);
        },
        clearStorage: function(){
            ok.clearStorage();
        },
        showShortToast: function(msg){
            ok.showShortToast(msg);
        },
        showLongToast: function(msg){
            ok.showLongToast(msg);
        },
        systemShare: function(obj){
            ok.systemShare(obj.title,obj.content,obj.imageUrl);
        },
        getSystemInfo: function(){
            return ok.getSystemInfo();
        },
        getLoginInfo: function(){
            return ok.getLoginInfo();
        },
        request: function(obj, callback){
            requestCallBack = callback;
            ok.request(obj.method,obj.url,obj.data);
        },
        uploadFile: function(url, filePath, callback){
            uploadCallBack = callback;
            ok.uploadFile(url, filePath);
        },
        downloadFile: function(url, fileName, callback){
            downloadCallBack = callback;
            ok.downloadFile(url, fileName);
        },
        selectMobileDataToRequest: function(url, callback){
            selectMobileDataCb = callback;
            ok.selectMobileDataToRequest(url);
        }
    };
})(window);