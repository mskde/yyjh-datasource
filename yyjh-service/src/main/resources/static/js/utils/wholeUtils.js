/**
 * cookie读值
 */
function getCookie(name){
    let cookie = document.cookie.trim();
    let value ="";
    let flag = cookie.indexOf(name);
    if(flag == -1)
        return null;
    let prev_index = flag+name.length+1;
    value = cookie.substr(prev_index);
    let next_index = value.indexOf(";");
    if(next_index == -1)
        next_index = cookie.length-1;
    value = cookie.substr(prev_index,next_index);
    return value;
}
/**
 * 判断是否已经登陆
 */
function isLogin(){
    console.log("cookie:"+"   "+document.cookie);
    console.log("user:"+" "+getCookie("user"));
    console.log("pwd:"+" "+getCookie("pwd"));
    let user = getCookie("user");
    let password = getCookie("pwd");
    if(user != null && password != null && user != "" && password != "")
        return true;
    else
        return false;
}