<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1> "Hello World!"
</h1>
<br/>hidden
<form action="controller" >
    <input type="hidden" name="command" value="login"/>
    Login: <input type="text" name="login" value=""/>
    <br/>
    Password: <input type="password" name="pass" value="">
    <br/>
    <input type="submit" name="sub" value="Push"/>
    <br/>
    ${login_msg.toUpperCase()}
    <br/>
    ${pageContext.session.id}
    <br/>
    ${filter_attr}
</form>


</body>
</html>