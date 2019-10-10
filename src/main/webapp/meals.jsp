<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<form method="post">
    <input type="hidden" name="id" value="<c:out value="${meal.id}" />">
    <table>
        <tr>
            <td><label>Дата/Время</label></td>

            <td><label>Описание</label></td>

            <td><label>Калории</label></td>
        </tr>

        <tr>
            <td><input type="datetime-local" name="dateTime" required
                       value="<c:out value="${meal.dateTime}" />"></td>

            <td><input type="text" name="description" required
                       value="<c:out value="${meal.description}" />"></td>

            <td><input type="number" name="calories" min="0" required
                       value="<c:out value="${meal.calories}" />"></td>

            <td><input type="submit"></td>
        </tr>
    </table>
</form>
</form>

<table border="1">
    <thead>
    <tr>
        <th>Дата/Время</th>

        <th>Описание</th>

        <th>Калории</th>

        <th colspan=2>Действия</th>
    </tr>
    </thead>

    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach var="meal" items="${meals}">
        <tr style="background-color:${meal.excess ? '#F08080' : '#90EE90'}">
            <td>${dateTimeFormatter.format(meal.dateTime)}</td>

            <td>${meal.description}</td>

            <td>${meal.calories}</td>

            <td><a href="meals?action=edit&id=<c:out value="${meal.id}"/>">Изменить</a></td>

            <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Удалить</a></td>
        </tr>
    </c:forEach>

</table>
</body>
</html>
