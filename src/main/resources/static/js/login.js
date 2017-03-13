$(function () {

    $('#login-form-link').click(function (e) {
        $("#login-form").delay(100).fadeIn(100);
        $("#register-form").fadeOut(100);
        $('#register-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });

    $('#register-form-link').click(function (e) {
        $("#register-form").delay(100).fadeIn(100);
        $("#login-form").fadeOut(100);
        $('#login-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });

    /*Custom code*/

    $('#register-form').submit(function (event) {
        event.preventDefault();
        var user = {
            userName: $('#register-form input[name=userName]').val(),
            password: $('#register-form input[name=password]').val(),
            confirmPassword: $('#register-form input[name=confirmPassword]').val(),
            fullName: $('#register-form input[name=fullName]').val(),
        }

        user = JSON.stringify(user);

        $.ajax({
            url: "/registration",
            type: "POST",
            contentType: "application/json",
            data: user,
            statusCode: {
                201: function () {
                    document.location.href = "/";
                },
                422: renderErrors
            },
            dataType: "json"
        });
    })
});

var renderErrors = function (response) {
    //--Get all errors--//
    var errors = JSON.parse(response.responseText);

    //--And put its in divs according to their name--//
    $("div .errors").each(function (i) {
        var div = this;
        div.innerHTML = "";
        //--Get messages for current div by name--//
        var messages = errors[this.getAttribute("name")];

        //--Build content for current div if messages exits --//
        if (messages != undefined) {
            var html = "";
            messages.forEach(function (message, i, arr) {
                html += "<span class='label label-warning'>" + message + "</span>";
                if (i != arr.length - 1) html += "<br/>"; // Add break to each except the last
            });
            //--Update content--//
            div.innerHTML += html;
        }
    });
}

var removeErrorMessages = function () {
    $("div .errors").each(function (i) {
        this.innerHTML = "";
    });
}