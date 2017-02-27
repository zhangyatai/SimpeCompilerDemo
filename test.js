$(document).ready(function(){
	var textNum = 1000;
	var buttonNum = 1000;
	var radioBase = 1000;
	//当前元素id	
	var idStrCurrent;

	$(".textType").click(function(){
		var idNum = textNum + 1;
		textNum = idNum;
		var htmlStr = '<input class="newText" id="' + idNum + '" type="text" name="text name" value = "这是一个文本框">';
		$('.d2').append(htmlStr);
	});

	$(".buttonType").click(function(){

		var idNum = buttonNum + 1;
		bottonNum = idNum;
		var htmlStr = '<input class="newButton" id="' + idNum + '" type="button" value = "这是一个按钮">';
		$('.d2').append(htmlStr);
	});

	$(".radioType").click(function() {
		var idNum = radioBase + 1;
		radioBase = idNum;

		var htmlStr = '<input class="newRadio" id="' + idNum + '" type="button" value = "这是一个单选">';
		$('.d2').append(htmlStr);	
	});


	//控件点击出属性
	$(document).on('click', '.newText,.newButton,newRadio', function(e) {

		var idStr = $(this).attr("id");
		var nameStr =$(this).attr("name");
		var typeStr = $(this).attr("type");

		idStrCurrent = idStr;

	 	$(".textId").val(idStr);
	 	$(".textName").val(nameStr);
	  	$(".textType").val(typeStr);
	});

	//提交按钮点击事件
	$("#btnSubmit").click(function(){
		var idStr = $(".textId").val();
		var nameStr = $(".textName").val();
		var typeStr = $(".textType").val();

		$("#" + idStrCurrent).attr("id",idStr);
		$("#" + idStrCurrent).attr("name",nameStr);
		$("#" + idStrCurrent).attr("type",typeStr);

	});

	//重置按钮点击事件
	$("#btnReset").click(function(){

	});
});
	