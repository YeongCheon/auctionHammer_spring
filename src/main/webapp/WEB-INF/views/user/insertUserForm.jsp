<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String loginID = (String)session.getAttribute("userID");
	if(loginID != null){
		response.sendRedirect("../../index.jsp");
	}
%>	
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원가입 페이지</title>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common.css">
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/button.css">
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mainMenu.css">
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">
<!--
	var reg_userID = /^[(\w+)(_*)(-*)]{6,15}$/i;

	var isAble = false;
	function ajax_idCheck() {
		if (reg_userID.test($('#user_id').val())) {
			$.ajax({
				url : './ajax/ajax_idCheck.do',
				dataType : 'json',
				type : 'POST',
				data : {
					'user_id' : $('#user_id').val()
				},
				success : function(result) {
					$("#ajax_IDcheckResult").html(result["msg"]);
					isAble = result["able"];
					if(isAble == "false"){
						$("#ajax_IDcheckResult").css("color", "red");
						isAble = false;
					}else{
						$("#ajax_IDcheckResult").css("color", "blue");
						isAble = true;
					}
				}
			});
		} else {
			$("#ajax_IDcheckResult").html("사용 불가능");
			$("#ajax_IDcheckResult").css("color", "red");
			isAble = false;
		}
	}

	function onlyNumber(field, div_error) {
		var keycode = window.event.keyCode;

		if (keycode == 9 || keycode == 8 || (keycode >= 35 && keycode <= 40)
				|| (keycode >= 46 && keycode <= 57)
				|| (keycode >= 96 && keycode <= 105) || keycode == 110
				|| keycode == 190) {
			div_error.innerHTML = "";
			window.event.returnValue = true;
			return;
		} else {
			//div_error.innerHTML = "숫자만 입력 가능합니다.";
			window.event.returnValue = false;
			return;
		}

	}

	function nextCall() {
		if (document.getElementById("user_callNum01").value.length >= 4) {
			document.getElementById("user_callNum02").focus();
		}
	}

	function nextPhone() {
		if (document.getElementById("user_phone02").value.length >= 4) {
			document.getElementById("user_phone03").focus();
		}
	}

	function validationCheck() {
		var user_id = frm.user_id.value;
		var result = true;

		if (!reg_userID.test(user_id)) {
			document.getElementById("error_user_id").innerHTML = "ID를 올바르게 입력해주세요.";
			result = false;
		} else if (!isAble) {
			alert("이미 사용중인 아이디입니다.");
		} else {
			document.getElementById("error_user_id").innerHTML = "";
		}

		var user_password = frm.user_password.value;
		var user_passwordRepeat = frm.user_passwordRepeat.value;

		if (user_password.length < 7) {
			document.getElementById("error_user_password").innerHTML = "비밀번호를 7자 이상 입력해주세요.";
			result = false;
		} else if (user_password != user_passwordRepeat) {
			document.getElementById("error_user_password").innerHTML = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";
			result = false;
		} else {
			document.getElementById("error_user_password").innerHTML = "";
		}

		if (user_passwordRepeat == "") {
			document.getElementById("error_user_passwordRepeat").innerHTML = "비밀번호 확인을 입력해주세요.";
			result = false;
		} else {
			document.getElementById("error_user_passwordRepeat").innerHTML = "";
		}

		var user_pwdHint = frm.user_password;
		if (user_pwdHint == "") {
			document.getElementById("error_user_pwdHint").innerHTML = "힌트를 입력해주세요.";
			result = false;
		} else {
			document.getElementById("error_user_pwdHint").innerHTML = "";
		}

		var user_answer = frm.user_answer;
		if (user_answer.value == "") {
			document.getElementById("error_user_answer").innerHTML = "힌트의 정답을 입력해주세요.";
			result = false;
		} else {
			document.getElementById("error_user_answer").innerHTML = "";
		}

		var user_name = frm.user_name;
		if (user_answer.value == "") {
			document.getElementById("error_user_name").innerHTML = "이름을 입력해주세요";
			result = false;
		} else {
			document.getElementById("error_user_name").innerHTML = "";
		}

		var user_nicname = frm.user_nicname;
		if (user_nicname.value == "") {
			document.getElementById("error_user_nicname").innerHTML = "닉네임을 입력해주세요.";
			result = false;
		} else {
			document.getElementById("error_user_nicname").innerHTML = "";
		}

		var user_emailID = frm.user_emailID;
		if (user_emailID.value == "") {
			document.getElementById("error_user_email").innerHTML = "Email을 입력해주세요";
			result = false;
		} else {
			document.getElementById("error_user_email").innerHTML = "";
		}

		var user_emailDomain = frm.user_emailDomain;
		if (user_emailDomain.value == "") {
			document.getElementById("error_user_email").innerHTML = "Email을 입력해주세요";
			result = false;
		} else {
			document.getElementById("error_user_email").innerHTML = "";
		}

		var user_callNum01 = frm.user_callNum01;
		var user_callNum02 = frm.user_callNum02;
		if (user_callNum01.value == "" || user_callNum02.value == "") {
			document.getElementById("error_user_call").innerHTML = "전화번호를 올바르게 입력해주세요.";
			result = false;
		} else if (isNaN(user_callNum01.value) || isNaN(user_callNum02.value)) {
			document.getElementById("error_user_call").innerHTML = "전화번호를 올바르게 입력해주세요.";
			result = false;
		} else {
			document.getElementById("error_user_call").innerHTML = "";
		}

		var user_phone02 = frm.user_phone02;
		var user_phone03 = frm.user_phone03;

		if (user_phone02.value == "" || user_phone03.value == "") {
			document.getElementById("error_user_phone").innerHTML = "핸드폰 번호를 올바르게 입력해주세요.";
			result = false;
		} else if (isNaN(user_phone02.value) || isNaN(user_phone03.value)) {
			document.getElementById("error_user_phone").innerHTML = "핸드폰 번호를 올바르게 입력해주세요.";
			result = false;
		} else {
			document.getElementById("error_user_phone").innerHTML = "";
		}

		var user_zipcode01 = frm.user_zipcode01;
		var user_zipcode02 = frm.user_zipcode02;
		if (user_zipcode01.value == "" || user_zipcode02.value == "") {
			document.getElementById("error_user_addr").innerHTML = "우편번호를 입력해주세요.";
			result = false;
		} else if (isNaN(user_zipcode01.value) || isNaN(user_zipcode02.value)) {
			document.getElementById("error_user_addr").innerHTML = "우편번호를 올바르게 입력해주세요.";
			result = false;
		} else {
			document.getElementById("error_user_addr").innerHTML = "";
		}

		var user_addr01 = frm.user_addr01;
		var user_addr02 = frm.user_addr02;
		if (user_addr01.value == "" || user_addr02 == "") {
			document.getElementById("error_user_addr").innerHTML = "주소를 정확하게 입력해주세요";
			result = false;
		} else {
			document.getElementById("error_user_addr").innerHTML = "";
		}

		var check01 = frm.check01.checked;
		var check02 = frm.check02.checked;
		if (!check01) {
			document.getElementById("error_user_agree").innerHTML = "사용약관에 동의해야 합니다.";
			result = false;
		} else if (!check02) {
			document.getElementById("error_user_agree").innerHTML = "개인정보 수집 및 이용약관에 동의해야 합니다.";
			result = false;
		} else {
			document.getElementById("error_user_agree").innerHTML = "";
		}
		
		if(!isAble){
			result = false;
		}

		return result;
	}

	function selectChange(select_obj, text_obj) {

		if (select_obj.value == "직접입력") {
			text_obj.readOnly = false;	
			text_obj.value = "";
			text_obj.focus();
		} else {
			text_obj.readOnly = true;
			text_obj.value = select_obj.value;
		}
	}

	function zipcode_fadeIn() {
		document.getElementById("popup_zipcode").style.display = 'block';
		document.getElementById("div_fade").style.display = 'block';

		$("#popup_zipcode").fadeOut(0);
		$("#popup_zipcode").fadeIn(300);

		$("#div_fade").fadeOut(0);
		$("#div_fade").fadeIn(300);
	}

	function zipcode_fadeOut() {
		document.getElementById("popup_zipcode").style.display = 'block';
		document.getElementById("div_fade").style.display = 'block';

		$("#popup_zipcode").fadeOut(0);
		$("#div_fade").fadeOut(0);
	}

	function ajax_getZipcodeList() {
		var keyword = document.getElementById("zipcode_keyword").value;

		if (keyword.length <= 0) {
			alert("검색어를 입력해주세요");
			return;
		}

		$.ajax({ //for landNum
			url : './ajax/ajax_getLandnumZipcodeList.do?keyword=' + encodeURI(keyword),
			dataType : 'json',
			type : 'get',
			data : {},
			success : function(result) {
				ajax_deleteLandnumItems();
				ajax_showLandnumItems(result);
			},
			error : function(e) {
				alert("ajax Error!!");
			}
		});

		$.ajax({ //for roadName
			url : './ajax/ajax_getRoadnameZipcodeList.do?keyword=' + encodeURI(keyword),
			dataType : 'json',
			type : 'get',
			data : {},
			success : function(result) {
				ajax_deleteRoadnameItems();
				ajax_showRoadnameItems(result);
			},
			error : function(e) {
				alert("ajax Error!!");
			}
		});
	}

	function ajax_deleteRoadnameItems() {
		var table = document.getElementById("zipcode_searchResult_roadName");

		while (table.rows.length > 1) {
			table.deleteRow(1);
		}
	}

	function ajax_showRoadnameItems(jsonObj) {
		var list = jsonObj.list;

		var table = document.getElementById("zipcode_searchResult_roadName");

		if (list.length <= 0) {
			var row = table.insertRow(-1);
			var cell01 = row.insertCell();

			cell01.colSpan = 2;
			cell01.innerHTML = "검색결과가 없습니다.";
			cell01.style.textAlign = "center";
		}

		for (var i = 0; i < list.length; i++) {

			var row = table.insertRow(-1);
			row.className = "tr_zipcode";

			var zipcode = row.insertCell();
			var addr = row.insertCell();
			zipcode.className = "zipcodeNum";
			addr.className = "addr_detail";

			var data_zipcode1 = list[i].zipcode.substr(0, 3);
			var data_zipcode2 = list[i].zipcode.substr(3, 6);

			var data_addr = list[i].gugun + " " + list[i].road_nm + " "
					+ list[i].building_no + " " + list[i].building_nm;

			var onClickEvent = 'select_zipcode("' + data_zipcode1 + '","'
					+ data_zipcode2 + '","' + data_addr + '");';

			var tag_a = "<a href='javascript:void(0)' onClick='" + onClickEvent
					+ "'>";

			zipcode.innerHTML = tag_a + data_zipcode1 + "-" + data_zipcode2
					+ "</a>";
			addr.innerHTML = tag_a + data_addr + "</a>";

		}
	}

	function ajax_deleteLandnumItems() {
		var table = document.getElementById("zipcode_searchResult_landNum");

		while (table.rows.length > 1) {
			table.deleteRow(1);
		}
	}

	function ajax_showLandnumItems(jsonObj) {
		var list = jsonObj.list;

		var table = document.getElementById("zipcode_searchResult_landNum");

		if (list.length <= 0) {
			var row = table.insertRow(-1);
			var cell01 = row.insertCell();

			cell01.colSpan = 2;
			cell01.innerHTML = "검색결과가 없습니다.";
			cell01.style.textAlign = "center";
		}

		for (var i = 0; i < list.length; i++) {

			var row = table.insertRow(-1);
			row.className = "tr_zipcode";

			var zipcode = row.insertCell();
			var addr = row.insertCell();
			zipcode.className = "zipcodeNum";
			addr.className = "addr_detail";

			var data_zipcode1 = list[i].zipcode.substr(0, 3);
			var data_zipcode2 = list[i].zipcode.substr(3, 6);
			var data_addr = list[i].gugun + " " + list[i].dong + " "
					+ list[i].bunji;

			var onClickEvent = 'select_zipcode("' + data_zipcode1 + '","'
					+ data_zipcode2 + '","' + data_addr + '");';

			var tag_a = "<a href='javascript:void(0)' onClick='" + onClickEvent
					+ "'>";

			zipcode.innerHTML = tag_a + data_zipcode1 + "-" + data_zipcode2
					+ "</a>";
			addr.innerHTML = tag_a + data_addr + "</a>";

		}
	}

	function select_zipcode(data_zipcode1, data_zipcode2, data_addr) {
		var user_zipcode1 = document.getElementById("user_zipcode01");
		var user_zipcode2 = document.getElementById("user_zipcode02");
		var user_addr01 = document.getElementById("user_addr01");

		user_zipcode1.value = data_zipcode1;
		user_zipcode2.value = data_zipcode2;
		user_addr01.value = data_addr;

		zipcode_fadeOut();
	}

	function switchZipcode(num) {
		var div_landNum = document.getElementById("zipcode_landnum");
		var div_roadName = document.getElementById("zipcode_roadName");

		var btn_landNum = document.getElementById("btn_landNum");
		var btn_roadName = document.getElementById("btn_roadName");

		if (num == 1) {
			div_landNum.style.display = "none";
			div_roadName.style.display = "block";

			btn_roadName.className = "btn_active";
			btn_landNum.className = "btn_nonactive";
		} else {
			div_landNum.style.display = "block";
			div_roadName.style.display = "none";

			btn_roadName.className = "btn_nonactive";
			btn_landNum.className = "btn_active";
		}
	}
	-->
</script>
<style type="text/css">
#div_title {
	margin: 30px auto 0 auto;
	padding: 5px 0 0 5px;
	background-color: #4b545f;
	color: #ffffff;
	font-size: 14pt;
	width: 600px;
	height: 50px;
	text-align: left;
}

th {
	text-align: right;
	width: 100px;
}

td {
	text-align: left;
}

#ajax_IDcheckResult {
	color: red;
}

.div_error {
	color: red;
}

#popup_zipcode {
	display: none; /* ensures it’s invisible until it’s called */
	position: absolute;
	/* makes the div go into a position that’s absolute to the browser viewing area */
	left: 35%; /* positions the div half way horizontally */
	top: 25%; /* positions the div half way vertically */
	padding: 25px;
	border: 2px solid black;
	background-color: #ffffff;
	width: 400px;
	height: 500px;
	z-index: 100;
	/* makes the div the top layer, so it’ll lay on top of the other content */
}

#div_fade {
	display: none; /* ensures it’s invisible until it’s called */
	position: absolute;
	/* makes the div go into a position that’s absolute to the browser viewing area */
	left: 0%; /* makes the div span all the way across the viewing area */
	top: 0%; /* makes the div span all the way across the viewing area */
	background-color: black;
	-moz-opacity: 0.7;
	/* makes the div transparent, so you have a cool overlay effect */
	opacity: .70;
	filter: alpha(opacity = 70);
	width: 100%;
	height: 100%;
	z-index: 90;
	/* makes the div the second most top layer, so it’ll lay on top of everything else EXCEPT for divs with a higher z-index (meaning the #overlay ruleset) */
}

.popup_zipcodeResult {
	width: 100%;
	overflow: scroll;
	height: 350px;
	border:1px solid #000000;
	border-top:0;
}

.zipcode_searchResult th {
	text-align: center;
}

.zipcode_searchResult td {
	margin: 0;
	padding: 0;
}

.zipcodeNum {
	
}

.addr_detail {
	width: 300px;
}

.zipcode_searchResult a {
	display: block;
	width: 100%;
	margin: 0;
	padding: 5px;
}

.zipcode_searchResult tr {
	display: block;
	width: 100%;
	height: 30px;
	float: left;
}

#btn_landNum {
	width: 50%;
	height: 30px;
	padding-top:20px;
	float: right;
	cursor:pointer;
}

#btn_roadName {
	width: 50%;
	height: 30px;
	padding-top:20px;
	cursor:pointer;
}

.btn_active {
	background: white;
	border-bottom:1px solid white;
	border: 1px solid #000000;
	border-bottom:1px solid white;
}

.btn_nonactive {
	background: #D1D1D1;
	border: 1px solid #000000;
}

.tr_zipcode:hover {
	background: #4b545f;
}

.tr_zipcode:hover a {
	color: white;
}
</style>
</head>
<body>
	<div id="layout">
		<%@include file="../header.jsp"%>
		<article id="mainArticle">
			<form name="frm" onsubmit="return validationCheck();"
				action="./insertUser.do" method="post">
				<div id="div_title">
					회원가입
					<div style="float: right; margin-top: 25px; font-size: 9pt;">
						<sub>'*'은 필수입력 정보입니다.</sub>
					</div>
				</div>
				<table align="center"
					style="width: 605px; border: 1px solid #4b545f;">
					<tr>
						<th>타입</th>
						<td>
							<select name="user_type">
								<option value="CUSTOMER" selected="selected">구매자</option>
								<option value="SELLER">판매자</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>* 회원 아이디 :</th>
						<td><input type="text" name="user_id" id="user_id"
							onkeyup="ajax_idCheck();" />영문, 숫자, 특수문자(_ , -)포함 6~15글자, 대소문자
							구분 x
							<div id="ajax_IDcheckResult"></div>
							<div id="error_user_id" class="div_error"></div></td>
					</tr>
					<tr>
						<th>* 비밀번호 :</th>
						<td><input type="password" name="user_password" />
							<div id="error_user_password" class="div_error"></div></td>
					</tr>
					<tr>
						<th>* 비밀번호 확인 :</th>
						<td><input type="password" name="user_passwordRepeat" />
							<div id="error_user_passwordRepeat" class="div_error"></div></td>
					</tr>
					<tr>
						<th>* 비밀번호 힌트:</th>
						<td>질문 <select name="select_pwdHint"
							onchange="selectChange(select_pwdHint, user_pwdHint);">
								<option value="">-</option>
								<option value="느그 아부지 뭐하시노?">느그 아부지 뭐하시노?</option>
								<option value="자다가 이불킥 할만한 경험은?">자다가 이불킥 할만한 경험은?</option>
								<option value="니 연봉 얼마임??">니 연봉 얼마임??</option>
								<option value="베프 이름은?">베프 이름은?</option>
								<option value="직접입력">직접입력</option>
						</select> <input type="text" name="user_pwdHint" readonly /><br />
							<div id="error_user_pwdHint" class="div_error"></div> 답변 <input
							type="text" name="user_answer" />
							<div id="error_user_answer" class="div_error"></div>
						</td>
					</tr>
					<tr>
						<th>* 이름 :</th>
						<td><input type="text" name="user_name" />
							<div id="error_user_name" class="div_error"></div></td>
					</tr>
					<tr>
						<th>* 별명 :</th>
						<td><input type="text" name="user_nicname" />
							<div id="error_user_nicname" class="div_error"></div></td>
					</tr>
					<tr>
						<th>생년월일</th>
						<td>
							<input type="text" size="4" name="birthday_year"/>년 <input type="text" size="2" name="birthday_month"/>월<input type="text" size="2" name="birthday_day"/>일
						</td>
					</tr>
					<tr>
						<th>* 성별 :</th>
						<td><select name="user_gender">
								<option value="M">남성</option>
								<option value="F">여성</option>
						</select>
							<div id="error_user_gender"></div></td>
					</tr>
					<tr>
						<th>* Email :</th>
						<td><input type="text" name="user_emailID" />@<input
							type="text" name="user_emailDomain" readonly/> <select
							name="select_emailDomain"
							onchange="selectChange(select_emailDomain, user_emailDomain);">
								<option value="">-</option>
								<option value="gmail.com">gmail.com</option>
								<option value="naver.com">naver.com</option>
								<option value="daum.net">daum.net</option>
								<option value="yopmail.com">yopmail.com</option>
								<option value="직접입력">직접입력</option>
						</select>
							<div id="error_user_email" class="div_error"></div></td>
					</tr>
					<tr>
						<th>* 전화번호 :</th>
						<td><select name="user_localNum">
								<option value="">-</option>
								<option value="02">02</option>
								<option value="031">031</option>
								<option value="112">112</option>
								<option value="119">119</option>
						</select> - <input type="text" name="user_callNum01" id="user_callNum01"
							size="4" maxlength="4"
							onkeyDown="return onlyNumber(document.getElementById('user_callNum01'), document.getElementById('error_user_call'));"
							onkeyup="nextCall();" /> - <input type="text"
							id="user_callNum02" name="user_callNum02"
							onkeyDown="return onlyNumber(document.getElementById('user_callNum02'), document.getElementById('error_user_call'));"
							size="4" maxlength="4" />
							<div id="error_user_call" class="div_error"></div></td>
					</tr>
					<tr>
						<th>* 핸드폰 번호 :</th>
						<td><select name="user_phone01">
								<option value="010">010</option>
								<option value="016">016</option>
								<option value="019">019</option>
								<option value="011">011</option>
						</select> - <input type="text" name="user_phone02" id="user_phone02"
							size="4" maxlength="4" onKeyup="nextPhone();"
							onkeyDown="return onlyNumber(document.getElementById('user_phone02'), document.getElementById('error_user_phone'));" />
							- <input type="text" name="user_phone03" size="4" maxlength="4"
							onkeyDown="return onlyNumber(document.getElementById('user_phone03'), document.getElementById('error_user_phone'));" />
							<div id="error_user_phone" class="div_error"></div></td>
					</tr>
					<tr>
						<th>* 주소</th>
						<td><input type="text" name="user_zipcode01"
							id="user_zipcode01" size="3" readonly /> - <input type="text"
							name="user_zipcode02" id="user_zipcode02" size="3" readonly />
							<button type="button" class="button white small"
								onclick="zipcode_fadeIn();">우편번호 찾기</button> <br /> <input
							type="text" name="user_addr01" id="user_addr01" size="50"
							readonly /> <br /> <input type="text" name="user_addr02"
							id="user_addr02" size="50" />
							<div id="error_user_addr" class="div_error"></div></td>
					</tr>
				</table>
				<input type="checkbox" name="check01" /> <a href="#">사용약관</a>을 읽었으며
				이에 동의합니다. <br /> <input type="checkbox" name="check02" /> <a
					href="#">개인정보 수집 및 이용안내</a>를 읽고 이에 동의합니다.<br />
				<div id="error_user_agree" class="div_error"></div>
				<br /> <input type="submit" class="button white medium" value="확인" />
				<input type="reset" class="button white medium" value="취소" />
			</form>
		</article>
		<%@ include file="../footer.html"%>
	</div>








	<div id="popup_zipcode">
		<div style="clear: both;margin-top:10px;">
			<b>우편번호 검색</b>
		</div>
		
		<div style="text-align: center;"">
			<input type="text" name="keyword" size="10" id="zipcode_keyword"
				onkeypress="{if (window.event.keyCode==13)ajax_getZipcodeList();}" />
			<input type="button" class="button white medium" value="검색"
				onClick="ajax_getZipcodeList();" />
		</div>
		<div style="clear: both;margin-top:25px;width:402px;">
			<div id="btn_landNum" class="btn_nonactive"
				onClick="switchZipcode(2);">지번검색</div>
			<div id="btn_roadName" class="btn_active" onClick="switchZipcode(1);">도로명
				검색</div>
		</div>
		
		<div style="position:relative;">
			<div id="zipcode_landnum" style="clear: both; display: none;">
				<div class="popup_zipcodeResult">
					<table id="zipcode_searchResult_landNum"
						class="zipcode_searchResult" align="center">
						<thead>
							<tr>
								<th align="center" style="text-align: center;">우편번호</th>
								<th align="center" style="text-align: center;">주소</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
			<div id="zipcode_roadName">
				<div class="popup_zipcodeResult">
					<table id="zipcode_searchResult_roadName"
						class="zipcode_searchResult" align="center">
						<thead>
							<tr>
								<th align="center" style="text-align: center;">우편번호</th>
								<th align="center" style="text-align: center;">주소</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
		<button type="button" class="button white medium" style="margin-top:10px;"
			onClick="zipcode_fadeOut();">닫기</button>
	</div>
	<div id="div_fade" onClick="zipcode_fadeOut();"></div>
</body>
</html>