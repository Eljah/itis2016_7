<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="i18n.TestBundle" />
<!DOCTYPE html>
<html lang="${language}">
<head>
    <title>JSP/JSTL i18n demo</title>
</head>
<body>
<form>
    <select id="language" name="language" onchange="submit()">
        <option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
        <option value="nl" ${language == 'nl' ? 'selected' : ''}>Nederlands</option>
        <option value="es" ${language == 'es' ? 'selected' : ''}>Español</option>
        <option value="ru" ${language == 'ru' ? 'selected' : ''}>Русский</option>
    </select>
</form>
<form method="post" action="login">
    <label for="j_username"><fmt:message key="login.label.username" />:</label>
    <input type="text" id="j_username" name="j_username">
    <br>
    <label for="j_password"><fmt:message key="login.label.password" />:</label>
    <input type="password" id="j_password" name="j_password">
    <br>
    <fmt:message key="login.button.submit" var="buttonValue" />
    <input type="submit" name="submit" value="${buttonValue}">
</form>
</body>
</html>
