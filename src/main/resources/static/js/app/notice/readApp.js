

function initApp(id) {

	$(document).ready(function() {
		jQuery("#id").val(id);
		
		initInfo(id);
	});

}

function initInfo(id) {
	
	fncJsonRequest2('GET', contextPath+'/api/notice/'+id, true, null, 
		function(resultCode, resultMessage, resultValue) {
			if (resultCode == RESULT_CODE_OK) {
				$("#id").val(resultValue.id);

				$("#title").text(resultValue.title);
				$("#instUser").text(resultValue.instUser);
				
				$("#instTime").text(resultValue.formInstTime);
				$("#updTime").text(resultValue.formUpdTime);
				
				$("#content").val(resultValue.content);
			} else {
				alert(resultMessage);
			}
		}
	);
	
	initAttachFiles(id);
}

function initAttachFiles(id) {
	$.ajax({
		url:contextPath+'/api/noticeattach/'+id+"?page=1&rows=1000&useYN=Y",
		cache: false,
		contentType: "application/json;charset=UTF-8",
		type: 'GET',
	    success: function(response)
	    {
	    	var noticeattachArray = response.rows;
	    	if(typeof noticeattachArray != 'undefined' && noticeattachArray != null) {
		    	for(var i=0; i<noticeattachArray.length; i++) {
		    		$("#attachFiles").prepend($("<a href='"+noticeattachArray[i].url+"' target='_blank' style='padding:0 5px 0 0'>"+ noticeattachArray[i].filename+"</a>"));
		    	}
	    	}
	    	
	    },
	    error: function(x, e)
	    {
	         alert("state:"+x.readyState + "\nstatus:"+ x.status +"	message:"+ e.msg);   
	    }
	
	});
}

function validateNotice() {
	var title = jQuery("#title").val();
	var content = jQuery("#content").val();
	
	if (title == "") {
		alert("제목을 입력해 주세요");
		$("#title").focus();
		return false;
	}
	
	if (content == "") {
		alert("내용을 입력해 주세요");
		$("#content").focus();
		return false;
	}
	
	return true;
}


function deleteNotice() {
	var id = jQuery("#id").val();
	
	var gridUrl = contextPath+"/api/notice/"+id;
	fncJsonDelete(gridUrl);
}

