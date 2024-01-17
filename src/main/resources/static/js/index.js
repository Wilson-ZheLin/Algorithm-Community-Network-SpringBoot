$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	let title = $("#recipient-name").val();
	let content = $("#message-text").val();
	$.post(
		CONTENT_PATH + "/discuss/add",
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