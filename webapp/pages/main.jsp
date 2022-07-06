<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 27.04.2022
  Time: 16:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main</title>
</head>
<body>
Hello (forward) = ${user}
<hr/>
Hi (redirect/forward) = ${user_name}
<hr/>
${filter_attr}
<hr/>
<form action = "controller">
    <input type="hidden" name="command" value="logout"/>
    <input type="submit" value="logout"/>
</form>
</body>
</html>
