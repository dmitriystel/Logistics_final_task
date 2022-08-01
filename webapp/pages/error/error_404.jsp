<%--
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  contentType - указывает кодировку веб-контента, видимую конечным браузером (клиентом) страницы JSP.
  формат данных, отправляемых веб-сервером клиенту в качестве ответа.
Он принимается браузером в клиентской системе и отображается пользователю. Общий формат, а также формат по умолчанию —
text/html, то есть либо простой текст, либо текст с тегами HTML (кодировка ISO-8859-1, включающая все западные символы,
что эквивалентно UTF-8).

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
Основные теги обеспечивают поддержку итерации, условной логики, перехвата исключений, URL-адресов, ответов на
перенаправление или перенаправление и т. д.

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
Эти теги предназначены для форматирования чисел, дат и поддержки i18n через локали и пакеты ресурсов. Вы можете включить
 эти теги в JSP с синтаксисом ниже:
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:setLocale value="${sessionScope.localization}" scope="session"/>
<fmt:setBundle basename="text"/>

<!DOCTYPE html>
<html lang="${sessionScope.localization}">
<head>
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
            crossorigin="anonymous"
    />
    <script
            src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
            crossorigin="anonymous"
    ></script>
    <title>404</title>
</head>
<body>
<div class="d-flex align-items-center justify-content-center vh-100">
    <div class="text-center">
        <h1 class="display-1 fw-bold">404</h1>
        <p class="fs-3"> <span class="text-danger"><fmt:message key="message.opps"/></span><fmt:message key="message.page.not.found"/></p>
        <p class="lead">
            <fmt:message key="message.page.does.not.exist"/>
        </p>
        <a class="btn btn-outline-primary mt-3" href="${pageContext.request.contextPath}/controller?command=home_page"><fmt:message key="label.home_page"/></a>
    </div>
</div>
</body>
</html>
