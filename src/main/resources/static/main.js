var currentAccountID = "";
var backEndPrefix = "/account/"
var frontEndPrefix = "/fe/"

function clearInfos() {
	currentAccountID = "";
	$('#accountDetail').text("--/--");
	$('#balanceDetail').text("--/--");
	$('#availableMoney').text("--/--");
	$('#balanceStatus').text("--/--");
	$('#accountStatus').text("--/--");
	$("#warning").fadeIn();
}

function enterKey(event) {
	if(event.keyCode == 13) {

		var searchID = $('#accountID-search').val();

		if (searchID != '') {
			$.post(backEndPrefix+"info", {accountId: searchID},
				function (data) {
					console.log(data);
					currentAccountID = searchID;
					var rtnJsonObj = data;
					console.log(rtnJsonObj);
					var rtnCode = rtnJsonObj.code;
					var rtnData = rtnJsonObj.data;
					if (rtnCode == 0) {
						$('#accountDetail').text(rtnData.securities_account_id);
						$('#balanceDetail').text(rtnData.balanceInfo.balance);
						$('#availableMoney').text(rtnData.balanceInfo.available_balance);
						if (rtnData.balanceInfo.status == 0)
							$('#balanceStatus').text("正常");
						else
							$('#balanceStatus').text("冻结");
						if (rtnData.status == 0)
							$('#accountStatus').text("正常");
						else
							$('#accountStatus').text("冻结");
					} else {
						alert("找不到该用户");
						clearInfos();
					}
				});
		} else {
			clearInfos();
		}
	}
}

$(document).ready(function () {

	$('#form1-container').fadeOut("fast", function () {
		$('#form1-container').css("visibility", "visible");
	});
	$('#form2-container').fadeOut("fast", function () {
		$('#form2-container').css("visibility", "visible");
	});
	$('#form3-container').fadeOut("fast", function () {
		$('#form3-container').css("visibility", "visible");
	});
	$('#form4-container').fadeOut("fast", function () {
		$('#form4-container').css("visibility", "visible");
	});
	$('#form5-container').fadeOut("fast", function () {
		$('#form5-container').css("visibility", "visible");
	});
	$('#form6-container').fadeOut("fast", function () {
		$('#form6-container').css("visibility", "visible");
	});

	$('#cancel').click(function () {
		if ($('#accountStatus').text() == "正常") {
			$('#form1-container').fadeIn();
		}
		else {
			// TODO: Error Message
			alert("当前账户暂时不可被销户")
		}
	});
	$('#editBalance').click(function () {
		$('#form6-container').fadeIn();
	});
	$('#editTradePwd').click(function () {
		$('#form2-container').fadeIn();
	});
	$('#editWthdrPwd').click(function () {
		$('#form3-container').fadeIn();
	});
	$('#LossOrReissue').click(function () {
		if ($('#balanceStatus').text() == "正常")
			$('#form4-container').fadeIn();
		else
			$('#form5-container').fadeIn();
	});


	$('#go-back1').click(function() {
		$('#form1-container').fadeOut();
	});
	$('#go-back2').click(function() {
		$('#form2-container').fadeOut();
	});
	$('#go-back3').click(function() {
		$('#form3-container').fadeOut();
	});
	$('#go-back4').click(function() {
		$('#form4-container').fadeOut();
	});
	$('#go-back5').click(function() {
		$('#form5-container').fadeOut();
	});
	$('#go-back6').click(function() {
		$('#form6-container').fadeOut();
	});

	$('#accountID-search').keydown(enterKey);

	$('#newAccount').click(function () {
		window.open(frontEndPrefix+"create");
	});

	$('#cancelBtn').click(function() {
		var inputId = $('#identity-idc').val();
		var inputSecId = $('#secIDc').val();

		console.log(inputId, inputSecId);

		if (inputId != "" && inputSecId != "") {
			$.post(backEndPrefix+"cancel", {identityId: inputId, securitiesAccountId: inputSecId, accountId: currentAccountID},
				function (data) {
					var rtnJsonObj = data;
					var rtnCode = rtnJsonObj.code;
					if (rtnCode == 0) {
						currentAccountID = "";
						$('#accountDetail').text("--/--");
						$('#balanceDetail').text("--/--");
						$('#availableMoney').text("--/--")
						$('#balanceStatus').text("--/--");
						$('#accountStatus').text("--/--");
						$('#identity-idc').text("");
						$('#secIDc').text("");
						$('#form1-container').fadeOut();
					} else {
						alert("销户失败");
					}
				});
		}
	});
	$('#tpBtn').click(function() {
		var inputOri = $('#originTradePwd').val();
		var inputNew = $('#newTradePwd').val();

		console.log(inputOri, inputNew);

		if (inputOri != "" && inputNew != "") {
			$.post(backEndPrefix + "updateTransactionPwd", {
					accountId: currentAccountID,
					oriPassword: inputOri,
					newPassword: inputNew
				},
				function (data) {
					var rtnJsonObj = data;
					var rtnCode = rtnJsonObj.code;
					if (rtnCode == 0) {
						$('#form2-container').fadeOut();
						$('#originTradePwd').text();
						$('#newTradePwd').text();
					} else {
						alert("修改密码失败");
					}
				});
		}
	});
	$('#wpBtn').click(function() {
		var inputOri = $('#originWthdrPwd').val();
		var inputNew = $('#newWthdrPwd').val();

		console.log(inputOri, inputNew);

		if (inputOri != "" && inputNew != "") {
			$.post(backEndPrefix + "updateWithdrawalPwd", {
					accountId: currentAccountID,
					oriPassword: inputOri,
					newPassword: inputNew
				},
				function (data) {
					var rtnJsonObj = data;
					var rtnCode = rtnJsonObj.code;
					if (rtnCode == 0) {
						$('#form3-container').fadeOut();
						$('#originWthdrPwd').text("");
						$('#newWthdrPwd').text("");
					} else {
						alert("修改密码失败");
					}
				});
		}
	});
	$('#lossBtn').click(function() {
		var inputID = $('#identity-idl').val();
		var inputSedID = $('#secIdl').val();

		console.log(inputID, inputSedID);

		if (inputID != "" && inputSedID != "") {
			$.post(backEndPrefix + "reportLoss", {
					identityId: inputID,
					securitiesAccountId: inputSedID
				},
				function (data) {
					var rtnJsonObj = data;
					var rtnCode = rtnJsonObj.code;
					if (rtnCode == 0) {
						$('#form4-container').fadeOut();
						$('#identity-idl').text("");
						$('#secIdl').text("");
						var event = Object.create({keyCode:13});
						enterKey(event);
					} else {
						alert("挂失失败");
					}
				});
		}
	});
	$('#reBtn').click(function() {
		var inputID = $('#identity-idr').val();
		var inputSedID = $('#secIdr').val();
		var tPwd = $('#tPassword').val();
		var wPwd = $('#wPassword').val();

		console.log(inputID, inputSedID, tPwd, wPwd);

		if (inputID != "" && inputSedID != "" && tPwd != "" && wPwd != "") {
			$.post(backEndPrefix + "reissue", {
					identityId: inputID,
					securitiesAccountId: inputSedID,
					transactionPwd: tPwd,
					withdrawalPwd: wPwd
				},
				function (data) {
					var rtnJsonObj = data;
					var rtnCode = rtnJsonObj.code;
					if (rtnCode == 0) {
						currentAccountID = "";
						$('#accountDetail').text("--/--");
						$('#balanceDetail').text("--/--");
						$('#availableMoney').text("--/--");
						$('#balanceStatus').text("--/--");
						$('#accountStatus').text("--/--");
						$('#identity-idr').text("");
						$('#secIdr').text("");
						$('#tPassword').text("");
						$('#wPassword').text("");
						$('#form5-container').fadeOut();
					} else {
						alert("补办失败");
					}
				});
		}
	});
	$('#ioBtn').click(function() {
		var inputAmount = $('#MoneyAmount').val();
		var inputPwd = $('#ioPasswd').val();
		var outCheck = $('').length;

		console.log(outCheck);

		var crtRequest;
		if (outCheck == 0) {
			crtRequest = backEndPrefix + 'save';
		} else {
			crtRequest = backEndPrefix + 'withdrawal';
		}

		if (inputAmount != "" && inputPwd != "") {
			$.post(crtRequest, {
					accountId: currentAccountID,
					password: inputPwd,
					amount: inputAmount
				},
				function (data) {
					var rtnJsonObj = data;
					var rtnCode = rtnJsonObj.code;
					if (rtnCode == 0) {
						$('#form6-container').fadeOut();
						$('#ioPasswd').text("");
						$('#MoneyAmount').text("");
						$('#secIdl').text("");
						var event = Object.create({keyCode:13});
						enterKey(event);
					} else {
						alert("充值/提现失败");
					}
				});
		}
	});
});