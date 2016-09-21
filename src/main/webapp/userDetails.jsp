<%@ page contentType="text/html; charset=utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/logout.tld" prefix="logout"%>
<html>
<head>
    <title>useBean Example</title>
</head>
<body>
<jsp:useBean id="user" class="students.logic.RegisteredUser">
    <jsp:setProperty name="user" property="username"
                     param="username"/>
    <jsp:setProperty name="user" property="password"
                     param="password"/>
    <jsp:setProperty name="user" property="passwordHash"
                     param="passwordHash"/>
    <jsp:setProperty name="user" property="email"
                     param="email"/>
</jsp:useBean>

<p>The user name is <%= user.getUsername() %><br>
The user password hash is <%= user.getPasswordHash() %><br>
The user email <%= user.getEmail() %></p><br>
<br><img src="avatar.jpg?username=<%= user.getUsername()%>"><br>
<form action="edituser" method=post enctype="multipart/form-data">
    <input type="hidden" name="oldusername" size="25" value="<%= user.getUsername() %>">
    <p><strong>Please Enter New Email: </strong></p>
        <input type="text" name="email" size="25" value="<%= user.getEmail() %>">
    <p><strong>Please Enter Your New User Name: </strong></p>
        <input type="text" name="username" size="25" value="<%= user.getUsername() %>">
    <p><strong>Please Enter Your New Password: </strong></p>
        <input type="password" size="15" name="password" value="<%= request.getParameter("password") %>">
    <p></p>
    <p><strong>Choose Image:</strong>
        <input type="file" name="photo" size="10"
                   /></p>
    <input type="submit" value="Submit">
        <input type="reset" value="Reset">
</form>

</body>
</html>