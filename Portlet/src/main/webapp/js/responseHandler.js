//General Response Handler for jqGrid
function getResponse(response, postdata){
    return getResponseTextInfo(response.responseText);
           
}
function getResponseTextInfo(responseText){
    var success = true;  
    var msg = "";
    if(responseText != "") {  
        var obj = jQuery.parseJSON(responseText);  
        if(obj != null && obj.error !== 'undefined') { 
            success = false;
            msg = "";  
            $.each(obj.error, function(index, value) {  
                msg += " " + value + " ";  
            });  
        }
    }
    return [success, msg, null];   
}