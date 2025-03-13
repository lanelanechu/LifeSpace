<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>IBM Space: Home</title>

<style>

  table#table-1 {
	width: 450px;
	background-color: #CCCCFF;
	margin-top: 5px;
	margin-bottom: 10px;
    border: 3px ridge Gray;
    height: 80px;
    text-align: center;
  }
  table#table-1 h4 {
    color: red;
    display: block;
    margin-bottom: 1px;
  }
  h4 {
    color: blue;
    display: inline;
  }
</style>

</head>
<body bgcolor='white'>

<table id="table-1">
   <tr><td><h3>IBM Space: Home</h3><h4>( MVC )</h4></td></tr>
</table>

<p>This is the Home page for IBM Space: Home</p>

<h3>資料查詢:</h3>
	
<%-- 錯誤表列 --%>
<c:if test="${not empty errorMsgs}">
	<font style="color:red">請修正以下錯誤:</font>
	<ul>
	    <c:forEach var="message" items="${errorMsgs}">
			<li style="color:red">${message}</li>
		</c:forEach>
	</ul>
</c:if>

<ul>
  <li><a href='listAllSpace.jsp'>List</a> all Space.  <br><br></li>
  
  
  <li>
    <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/backend/space/space.do" >
        <b>輸入空間編號 (如S001):</b>
        <input type="text" name="spaceId">
        <input type="hidden" name="action" value="getOne_For_Display">
        <input type="submit" value="送出">
    </FORM>
  </li>

  <jsp:useBean id="spaceSvc" scope="page" class="com.space.model.SpaceService" />
 
  <li>
     <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/backend/space/space.do" >
       <b>選擇空間編號:</b>
       <select size="1" name="spaceId">
         <c:forEach var="spaceVO" items="${spaceSvc.all}" > 
          <option value="${spaceVO.spaceId}">${spaceVO.spaceId}
         </c:forEach>   
       </select>
       <input type="hidden" name="action" value="getOne_For_Display">
       <input type="submit" value="送出">
    </FORM>
  </li>
  
  <li>
     <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/backend/space/space.do" >
       <b>選擇空間名稱:</b>
       <select size="1" name="spaceId">
         <c:forEach var="spaceVO" items="${spaceSvc.all}" > 
          <option value="${spaceVO.spaceId}">${spaceVO.spaceName}
         </c:forEach>   
       </select>
       <input type="hidden" name="action" value="getOne_For_Display">
       <input type="submit" value="送出">
     </FORM>
  </li>
</ul>


<h3>空間管理</h3>

<ul>
  <li><a href='addSpace.jsp'>Add</a> a new Space.</li>
</ul>

</body>
</html>