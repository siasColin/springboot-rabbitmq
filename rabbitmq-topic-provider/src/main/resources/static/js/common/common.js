/*一些基础的js方法，基础的业务js方法在lib.js里*/
var Common = {
    //当前项目名称
    ctxPath: "/rabbitmq-topic-provider/",
    version:"",
};



// JQuery方法定制
(function($){
  //例如$.isEmpty("")
  $.extend({
  	//非空判断
  	isEmpty: function(value) {
  		if (value === null || value == undefined || value === '') { 
  			return true;
  		}
  		return false;
    },
        //是否以某个字符开头
    startsWith : function(value,target){
    	return value.indexOf(target) == 0;
    },
    //设置sessionStorage
    setSessionStorage:function(key, data){
    	sessionStorage.setItem(key, data);
    },
    //获取sessionStorage
    getSessionStorage:function(key){
    	return sessionStorage.getItem(key) == null ? "" : sessionStorage.getItem(key);
    },
    //删除sessionStorage
    removeSessionStorage:function(key){
    	sessionStorage.removeItem(key);
    },
    //清除sessionStorage
    clearSessionStorage:function(){
    	sessionStorage.clear();
    },
    uuid : function(){
  		return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		    var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
		    return v.toString(16);
  		});
    }
  });

}(jQuery));
