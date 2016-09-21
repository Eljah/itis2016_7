<%@page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Register page</title>
</head>
<body>
<h2>Hello, please insert your email, username and password</h2>
<br><br>

<form action="register" method=post id="register-form"><!--novalidate="novalidate"-->
    <p><strong>Please Enter Your Email: </strong>
        <input type="text" name="email" id="email" size="25">

    <p><strong>Please Enter Your User Name: </strong>
        <input type="text" name="username" id="username" size="25">

    <p><strong>Please Enter Your Password: </strong>
        <input type="password" size="15" name="password">

    <p>
        <input type="submit" value="Submit">
        <input type="reset" value="Reset">
</form>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.14.0/jquery.validate.min.js"></script>
<script>// jQuery Form Validation code -->


// When the browser is ready...
$(function () {

    // Setup form validation on the #register-form element
    $("#register-form").validate({
        onkeyup: false,
        // Specify the validation rules
        rules: {
            username: {
                cache: false,
                required: true,
                //minlength: 5,
                remote: {
                    url: "checkusername",
                    type: "post"
                }
            },
            email: {
                required: true,
                email: true

            },
            password: {
                required: true,
                minlength: 5
            },
            agree: "required"
        },

        // Specify the validation error messages
        messages: {
            firstname: "Please enter your first name",
            lastname: "Please enter your last name",
            password: {
                required: "Please provide a password",
                minlength: "Your password must be at least 5 characters long"
            },
            username: {
                required: "Please provide a username",
                minlength: "Your username must be at least 5 characters long",
                remote: "This username is already taken"
            },
            email: "Please enter a valid email address",
            agree: "Please accept our policy"
        },

        submitHandler: function (form) {

            form.submit();

        }
    });
    $("#register-form").change(function () {
        $("#username").removeData("previousValue"); //clear cache when changing group
        $("#register-form").data('validator').element("#username"); //retrigger remote call
        //my validator is stored in .data() on the form
    });
});

</script>
</body>
</html>

