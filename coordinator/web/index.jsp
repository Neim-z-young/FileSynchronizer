<%--
  Created by IntelliJ IDEA.
  User: HASEE
  Date: 2018/1/18
  Time: 0:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  $END$
  <form action="FileSync" method="post">
    源文件属性：<br/>
    数据资源名: <input type="text" name="sourceFile"/><br>
    数据资源类型：文件<br/>
    数据资源所属IP：<input type="text" name="sourceIp"/><br>
    端口号：<input type="text" name="sourcePort"/><br>
    路径：<input type="text" name="sourcePath"/><br>

    <br/>目标文件属性：<br/>
    数据资源名: <input type="text" name="targetDir"/><br>
    数据资源类型：文件<br/>
    数据资源所属IP：<input type="text" name="targetIp"/><br>
    端口号：<input type="text" name="targetPort"/><br>
    路径：<input type="text" name="targetPath"/><br>

    <br/>同步方式<br/>
    <select name="syncType">
      <option value="0" selected="true">默认（每隔10秒同步）</option>
      <option value ="1">手动同步</option>
      <option value ="2">周期同步--天</option>
      <option value="3">周期同步--周</option>
      <option value="4">周期同步--月</option>
      <option value="5">周期同步--每周第x天</option>
      <option value="6">周期同步--每月第x天</option>
      <option value="7">周期同步--时</option>
      <option value="8">周期同步--每天第x小时</option>
    </select><br/>
    周期/第x天（时）：<input type="text" name="period"/><br>
    <input type="submit"/><br>
  </form>
  </body>
</html>