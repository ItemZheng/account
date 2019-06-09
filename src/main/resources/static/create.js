$(document).ready(function () {
	$("#submit-btn").click(function () {
		var ID = $("#identity-id").val();
		var accountID = $("#account-id").val();
		var tradePasswd = $("#trade-passwd").val();
		var wthdrawPasswd = $("#wthdraw-passwd").val();
		var currencyType = ($("#currency-cb").val()=='on') ? 1 : 0;

		// Checking for blank fields.
		if (ID == '' || accountID == '' || tradePasswd == '' || wthdrawPasswd == '') {
			$("#warning").text("Please fill all the fields");
			$("#warning").fadeIn();
		} else {
			// For test
			console.log(ID);
			console.log(accountID);
			console.log(tradePasswd);
			console.log(wthdrawPasswd);
			console.log(currencyType);
			$("#warning").text("Check the info agin");
			$("#warning").fadeIn();
			// Test end

			var backEndPrefix = "/account/"
			var frontEndPrefix = "/fe/"
			$.post(backEndPrefix+"create", {identityId: ID, securitiesAccountId: accountID, transactionPwd: tradePasswd, withdrawalPwd: wthdrawPasswd, currency: currencyType},
				function (data) {
					var rtnJsonObj = data;
					var rtnCode = rtnJsonObj.code;
					if (rtnCode == 0) {
						window.location.replace(frontEndPrefix+"main");
					} else {
						$("#warning").text("Check the info agin");
						$("#warning").fadeIn();
					}
				});
		}
	});
});