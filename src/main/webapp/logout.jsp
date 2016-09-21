<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title></head>
<body><% session.removeAttribute("username");
    session.removeAttribute("password");
    session.invalidate(); %> <h1>Logout was done successfully.</h1></body>
</html>
<%
    Cookie cookie = null;
    Cookie[] cookies = null;
    // Get an array of Cookies associated with this domain
    cookies = request.getCookies();
    if( cookies != null ){
        for (int i = 0; i < cookies.length; i++){
            cookie = cookies[i];
            if((cookie.getName( )).compareTo("studentsUUID") == 0 ){
                cookie.setMaxAge(0);
                cookie.setValue("");
                cookie.setPath("/studentsApp/");
                response.addCookie(cookie);
                out.print("Deleted cookie: " +
                       cookie.getName( ) + "<br/>");
            }
            out.print("Name : " + cookie.getName( ) + ",  ");
            out.print("Value: " + cookie.getValue( )+" <br/>");
        }
    }else{
        out.println(
                "<h2>No cookies founds</h2>");
    }
%>
