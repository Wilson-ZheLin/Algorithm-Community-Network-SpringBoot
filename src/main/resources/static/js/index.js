$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	// let token = $("meta[name='_csrf']").attr("content");
	// let header = $("meta[name='_csrf_header']").attr("content");
	// $(document).ajaxSend(function (e, xhr, options) {
	// 	xhr.setRequestHeader(header, token);
	// })

	let title = $("#recipient-name").val();
	let content = $("#message-text").val();
	$.post(
		CONTEXT_PATH + "/discuss/add",
		{"title":title, "content":content},
		function(data){
			data = $.parseJSON(data);
			$("#hintBody").text(data.msg);
			$("#hintModal").modal("show");
			$("#publishBtn").text("发布");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				if(data.code == 0) {
					window.location.reload();
				}
			}, 2000);
	});
}