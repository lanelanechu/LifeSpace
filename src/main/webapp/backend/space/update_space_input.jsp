<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.space.model.*"%>
<%@ page import="com.space.util.*"%>
<%@ page import="java.util.*"%>

<%
//見com.emp.controller.EmpServlet.java第163行存入req的empVO物件 (此為從資料庫取出的empVO, 也可以是輸入格式有錯誤時的empVO物件)
SpaceVO spaceVO = (SpaceVO) request.getAttribute("spaceVO");
%>

<!DOCTYPE html>
<html lang="zh-TW">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>空間資料修改 - update_space_input.jsp</title>

<style>
* {
	box-sizing: border-box;
	font-family: "Microsoft JhengHei", Arial, sans-serif;
}

body {
	padding: 20px;
	background-color: #f5f5f5;
}

.container {
	max-width: 800px;
	margin: 0 auto;
	background-color: #fff;
	border-radius: 8px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	padding: 20px;
}

h1 {
	text-align: center;
	color: #333;
	margin-bottom: 30px;
	font-size: 24px;
}

.form-group {
	margin-bottom: 20px;
	display: flex;
	align-items: center;
}

.form-group label {
	width: 150px;
	font-weight: bold;
	margin-right: 10px;
}

.form-control {
	flex: 1;
	padding: 10px;
	border: 1px solid #ddd;
	border-radius: 4px;
	font-size: 16px;
}

.form-control:focus {
	border-color: #6AC0BD;
	outline: none;
	box-shadow: 0 0 0 2px rgba(106, 192, 189, 0.2);
}

.radio-group {
	display: flex;
	align-items: center;
	gap: 20px;
}

.radio-label {
	display: flex;
	align-items: center;
	cursor: pointer;
}

.radio-input {
	margin-right: 8px;
	width: 18px;
	height: 18px;
	accent-color: #6AC0BD;
}

textarea.form-control {
	min-height: 100px;
	resize: vertical;
}

.submit-btn {
	background-color: #6AC0BD;
	color: white;
	padding: 10px 20px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 16px;
	transition: all 0.3s;
	margin-top: 10px;
}

.submit-btn:hover {
	background-color: #475757;
}

.error-message {
	color: #f44336;
	font-size: 14px;
	margin-top: 5px;
}

@media screen and (max-width: 768px) {
	.form-group {
		flex-direction: column;
		align-items: flex-start;
	}
	.form-group label {
		margin-bottom: 5px;
		width: 100%;
	}
	.form-control {
		width: 100%;
	}
}
</style>
<script src="https://maps.googleapis.com/maps/api/js?key=<%= ApiKeyManager.getGoogleMapsApiKey() %>&libraries=places"></script>

</head>
<body>
	<div class="container">
		<h1>資料修改:</h1>

		<%-- 錯誤表列 --%>
		<c:if test="${not empty errorMsgs}">
			<font style="color: red">請修正以下錯誤:</font>
			<ul>
				<c:forEach var="message" items="${errorMsgs}">
					<li style="color: red">${message}</li>
				</c:forEach>
			</ul>
		</c:if>

		<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/backend/space/space.do" name="form1">
			<jsp:useBean id="branchSvc" scope="page" class="com.branch.model.BranchService" />
			<div class="form-group">
				<label for="spaceId">空間編號:</label>
				<input type="text" id="spaceId" name="spaceId" class="form-control" value="<%=spaceVO.getSpaceId()%>" disabled>
			</div>
			
			<div class="form-group">
                <label for="branchId">所屬分店:</label>
                <select id="branchId" name= "branchId" class="form-control" size="1">
                	<c:forEach var="branchVO" items="${branchSvc.all}">
						<option value="${branchVO.branchId}" ${(param.branchId==branchVO.branchId) ? 'selected':'' } >${branchVO.branchName}
					</c:forEach>
                </select>
            </div>
			
            <div class="form-group">
                <label for="spaceName">空間名稱:</label>
                <input type="text" id="spaceName" name="spaceName" class="form-control" value="<%=spaceVO.getSpaceName()%>">
            </div>
            
            <div class="form-group">
                <label for="spacePeople">空間人數:</label>
                <input type="number" id="spacePeople" name="spacePeople" class="form-control" value="<%=spaceVO.getSpacePeople()%>">
            </div>
            
            <div class="form-group">
                <label for="spaceSize">空間大小:</label>
                <input type="number" id="spaceSize" name="spaceSize" class="form-control" value="<%=spaceVO.getSpaceSize()%>">
            </div>
            
            <div class="form-group">
                <label for="spaceHourlyFee">時租費率:</label>
                <input type="number" id="spaceHourlyFee" name="spaceHourlyFee" class="form-control" value="<%=spaceVO.getSpaceHourlyFee()%>">
            </div>
            
            <div class="form-group">
                <label for="spaceDailyFee">日租費率:</label>
                <input type="number" id="spaceDailyFee" name="spaceDailyFee" class="form-control" value="<%=spaceVO.getSpaceDailyFee()%>">
            </div>
            
            <div class="form-group">
                <label for="spaceDesc">空間介紹:</label>
                <textarea id="spaceDesc" name="spaceDesc" class="form-control" ><%=spaceVO.getSpaceDesc() == null ? "" : spaceVO.getSpaceDesc()%></textarea>
            </div>
            
            <div class="form-group">
                <label for="spaceAlert">重要公告:</label>
                <textarea id="spaceAlert" name="spaceAlert" class="form-control"><%=spaceVO.getSpaceAlert() == null ? "" : spaceVO.getSpaceAlert()%></textarea>
            </div>
<%-- 			<tr>
				<td>空間狀態:</td>
				<td><input type="TEXT" name="spaceStatus"
					value="<%=spaceVO.getSpaceStatus()%>" size="45" /></td>
			</tr> --%>
			
          	<div class="form-group">	
                <label>空間狀態:</label>
                <div class="radio-group">
                    <label class="radio-label">
                        <input type="radio" name="spaceStatus" value="0" class="radio-input" ${spaceVO.spaceStatus == 0 ? 'checked' : ''}>
                        未上架
                    </label>
                    <label class="radio-label">
                        <input type="radio" name="spaceStatus" value="1" class="radio-input" ${spaceVO.spaceStatus == 1 ? 'checked' : ''}>
                        上架
                    </label>
                </div>
            </div>
            
            <div class="form-group">
                <label for="spaceAddress">空間地址:</label>
                <input type="text" id="spaceAddress" name="spaceAddress" class="form-control" value="<%=spaceVO.getSpaceAddress()%>">
            </div>
            
            <div class="form-group">
                <label for="latitude">緯度:</label>
                <input type="number" id="latitude" name="latitude" class="form-control" step="0.000001" value="<%=spaceVO.getLatitude()%>">
            </div>

            <div class="form-group">
                <label for="longitude">經度:</label>
                <input type="number" id="longitude" name="longitude" class="form-control" step="0.000001" value="<%=spaceVO.getLongitude()%>">
            </div>
            
			<div style="text-align: center;">            
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="spaceId" value="<%=spaceVO.getSpaceId()%>">
                <button type="submit" class="submit-btn">完成</button>
            </div>
            
		</FORM>
	</div>
</body>

<script>
document.addEventListener('DOMContentLoaded', function() {
    // 獲取相關 DOM 元素
    const addressInput = document.getElementById('spaceAddress');
    const latitudeInput = document.getElementById('latitude');
    const longitudeInput = document.getElementById('longitude');
    
    // 初始化地址自動完成
    const autocomplete = new google.maps.places.Autocomplete(addressInput, {
        // 限制在台灣範圍內搜尋
        componentRestrictions: { country: 'tw' }
    });
    
    // 當用戶選擇一個地點時的事件處理
    autocomplete.addListener('place_changed', function() {
        const place = autocomplete.getPlace();
        
        // 確認選定的地方有經緯度資訊
        if (!place.geometry || !place.geometry.location) {
            console.error('沒有找到此地址的地理資訊');
            return;
        }
        
        // 獲取經緯度並填入表單
        const lat = place.geometry.location.lat();
        const lng = place.geometry.location.lng();
        
        // 設定精確度為小數點後6位
        latitudeInput.value = lat.toFixed(6);
        longitudeInput.value = lng.toFixed(6);
        
        // 如果需要，可以更新完整地址
        addressInput.value = place.formatted_address;
        
        console.log('已成功獲取經緯度:', lat, lng);
    });
});
</script>

</html>