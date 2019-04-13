<%--
  Created by IntelliJ IDEA.
  User: lei02
  Date: 2019/4/12
  Time: 8:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
    <style type="text/css">
      body{
        color:pink;
        font-size:32px;
      }
    </style>
  </head>
  <body>
    <form action="insertServlet?dataBase=chapter7&tableName=produce" method="post">
      添加记录：
        <table border="1">
          <tr>
            <td>产品号：</td>
            <td><input type="text" name="number" /></td>
          </tr>
          <tr>
            <td>名称：</td>
            <td><input type="text" name="name" /></td>
          </tr>
          <tr>
            <td>生产日期：</td>
            <td><input type="text" name="mdadetime"/></td>
          </tr>
          <tr>
            <td>价格：</td>
            <td><input type="text" name="price" /></td>
          </tr>
        </table>
      <input type="submit" name="b" value="提交"/>
    </form>
  </body>
</html>
