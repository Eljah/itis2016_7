<%@page contentType="text/html; charset=UTF-8"%>
<html>
<head>
    <title>Login Page</title>
</head>
<body>
<h2>Hello, please log in:</h2>
<br><br>
<!--<form action="j_security_check" method=post>-->
<form action="login" method=post>
    <p><strong>Please Enter Your User Name: </strong>
    <input type="text" name="j_username" size="25">
    <p><p><strong>Please Enter Your Password: </strong>
    <input type="password" size="15" name="j_password">
    <p><p><strong>Remember me: </strong>
    <input type="checkbox" name ="rememberme" checked="checked"/>
    <input type="hidden" name="url" value="<% if (request.getParameter("url")!= null) {%><%=request.getParameter("url")%><%} else %>${requestScope['javax.servlet.forward.request_uri']}">
    <input type="submit" value="Submit">
    <input type="reset" value="Reset">
    <% if (request.getAttribute("error")!= null) { %><div class="error"><%=request.getAttribute("error")%></div><% }%>
</form>
<br>
<a href="register.jsp">Register</a>
</body>
</html>
