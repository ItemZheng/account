$(document).ready(function () {
    $("#login").click(function () {
        var emailVal = $("#email-input").val();
        var passwordVal = $("#password-input").val();
// Checking for blank fields.
        if (emailVal == '' || passwordVal == '') {
            $("#warning").text("Please fill all the fields");
            $("#warning").fadeIn();
        } else {
        	var backEndPrefix = "/admin/"
			var frontEndPrefix = "/fe/"
            $.post(backEndPrefix+"login", {username: emailVal, password: passwordVal},
                function (data) {
                    var rtnJsonObj = data;
                    var rtnCode = rtnJsonObj.code;
                    if (rtnCode == 0) {
                    	window.location.replace(frontEndPrefix+"main");
					} else {
						$("#warning").text("Invalid username/password");
            			$("#warning").fadeIn();
					}
                });
        }
    });
});