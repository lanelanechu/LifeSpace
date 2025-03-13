<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.space.model.*"%>
<%-- 此頁練習採用 EL 的寫法取值 --%>

<%
SpaceService spaceSvc = new SpaceService();
List<SpaceVO> list = spaceSvc.getAll();
pageContext.setAttribute("list", list);
%>


<!DOCTYPE html>
<html lang="zh-TW">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>空間資料 - listAllSpace.jsp</title>
<style>
* {
	box-sizing: border-box;
	font-family: "Microsoft JhengHei", Arial, sans-serif;
}

body {
	padding: 20px;
	background-color: #f5f5f5;
}

.table-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    flex-wrap: wrap;
    gap: 15px;
}

.add-btn {
    background-color: #6AC0BD;
    color: white;
    padding: 10px 15px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 16px;
    transition: all 0.3s;
}

.add-btn:hover {
    background-color: #475757;
}

.search-container {
    display: flex;
    align-items: center;
    gap: 8px;
}

.search-input {
    padding: 8px 12px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
    width: 250px;
}

.search-btn {
    background-color: #6AC0BD;
    color: white;
    padding: 8px 12px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: all 0.3s;
}

.search-btn:hover {
    background-color: #475757;
}

.table-container {
/* 	max-width: 1200px; */
	margin: 0 auto;
	background-color: #fff;
	border-radius: 8px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	padding: 20px;	 
	overflow-x: auto;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-bottom: 20px;
}

th, td {
    min-width: 120px; /* 基本最小寬度 */
    padding: 12px 15px;
    text-align: left;
    border-bottom: 1px solid #ddd;
}

th {
	background-color: #E3F4F4;
	font-weight: bold;
	color: black;
	position: sticky;
	top: 0;
}

tr:hover {
	background-color: #f5f5f5;
}

.btn {
	padding: 6px 12px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 14px;
	transition: all 0.3s;
}

.btn-edit {
	background-color: #6AC0BD;
	color: white;
}

.btn-edit:hover {
	background-color: #475757;
}

.btn-delete {
	background-color: #f44336;
	color: white;
}

.btn-delete:hover {
	background-color: #d32f2f;
}

.pagination {
	display: flex;
	justify-content: center;
	margin-top: 20px;
}

.pagination button {
	padding: 8px 12px;
	margin: 0 5px;
	border: 1px solid #ddd;
	background-color: #fff;
	border-radius: 4px;
	cursor: pointer;
	transition: all 0.3s;
}

.pagination button:hover:not(.active) {
	background-color: #f1f1f1;
}

.pagination button.active {
	background-color: #6AC0BD;
	color: white;
	border-color: #6AC0BD;
}

.pagination button:disabled {
	color: #ccc;
	cursor: not-allowed;
}

.hidden {
    display: none;
}
</style>
</head>
<body>

	<%-- 錯誤表列 --%>
	<c:if test="${not empty errorMsgs}">
		<ul>
		    <c:forEach var="message" items="${errorMsgs}">
				<li style="color:red">${message}</li>
			</c:forEach>
		</ul>
	</c:if>
	
	<%-- 新增＆搜尋欄位 --%>
	<div class="table-actions">
		<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/backend/space/addSpace.jsp">
			<button type="submit" class="add-btn">
				<i class="fas fa-plus"></i> 新增資料
			</button>
			<!-- <input type="hidden" name="action" value="getOne_For_Insert"> -->
		</FORM>
		
		<div class="search-container">
			<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/backend/space/space.do">
				<input type="text" id="searchInput" class="search-input" name="spaceId" placeholder="輸入空間編號 (如S001)">
				<input type="hidden" name="action" value="getOne_For_Display">
				<button type="submit" id="searchBtn" class="search-btn">搜尋</button>
			</FORM>
		</div>
	</div>
	
	<%-- 資料表格 --%>
	<div class="table-container">
		<table id="dataTable">
			<thead>
				<tr>
					<th>空間編號</th>
					<th>所屬分店編號</th>
					<th>空間名稱</th>
					<th>空間人數</th>
					<th>空間大小</th>
					<th>時租費率</th>
					<th>日租費率</th>
					<th>空間介紹</th>
					<th>空間評分</th>
					<th>重要公告</th>
					<th>近24小時總使用時間</th>
					<th>近7天總使用時間</th>
					<th>空間狀態</th>
					<th>空間地址</th>
					<th>緯度</th>
					<th>經度</th>
					<th>最後更新時間</th>
					<th>修改</th>
					<th>刪除</th>
				</tr>
			</thead>
			<tbody id="tableBody">
				<c:forEach var="spaceVO" items="${list}" varStatus="status">
					<tr class="data-row">
						<td>${spaceVO.spaceId}</td>
						<td>${spaceVO.branchId}</td>
						<td>${spaceVO.spaceName}</td>
						<td>${spaceVO.spacePeople}</td>
						<td>${spaceVO.spaceSize}</td>
						<td>${spaceVO.spaceHourlyFee}</td>
						<td>${spaceVO.spaceDailyFee}</td>
						<td>${spaceVO.spaceDesc}</td>
						<td>${spaceVO.spaceRating}</td>
						<td>${spaceVO.spaceAlert}</td>
						<td>${spaceVO.spaceUsed24hr}</td>
						<td>${spaceVO.spaceUsed7d}</td>
						<td>${spaceVO.spaceStatusText}</td> <!-- 以文字顯示 -->
						<td>${spaceVO.spaceAddress}</td>
						<td>${spaceVO.latitude}</td>
						<td>${spaceVO.longitude}</td>
						<td>${spaceVO.createdTime}</td>

						<td>
							<FORM METHOD="post"
								ACTION="<%=request.getContextPath()%>/backend/space/space.do">
								<input class="btn btn-edit" type="submit" value="修改"> 
								<input type="hidden" name="spaceId" value="${spaceVO.spaceId}"> 
								<input type="hidden" name="action" value="getOne_For_Update">
							</FORM>
						</td>
						<td>
<%-- 							<FORM METHOD="post"
								ACTION="<%=request.getContextPath()%>/backend/space/space.do">
								<input class="btn btn-delete" type="submit" value="刪除"> 
								<input type="hidden" name="spaceId" value="${spaceVO.spaceId}"> 
								<input type="hidden" name="action" value="delete">
							</FORM> --%>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<div class="pagination" id="pagination"></div>
	</div>

	<script>
		// 配置
		const rowsPerPage = 10;
		let currentPage = 1;
		
		// 獲取所有資料行
		const rows = document.querySelectorAll('.data-row');
		const totalRows = rows.length;
		
		// 更新分頁
		function updatePagination() {
			// 計算總頁數
			const totalPages = Math.ceil(totalRows / rowsPerPage);
			
			// 清空分頁容器
			const paginationContainer = document.getElementById('pagination');
			paginationContainer.innerHTML = '';
			
			// 上一頁按鈕
			const prevButton = document.createElement('button');
			prevButton.textContent = '上一頁';
			prevButton.disabled = currentPage === 1;
			prevButton.onclick = () => {
				if (currentPage > 1) {
					currentPage--;
					displayData();
				}
			};
			paginationContainer.appendChild(prevButton);
			
			// 頁碼按鈕
			for (let i = 1; i <= totalPages; i++) {
				const pageButton = document.createElement('button');
				pageButton.textContent = i;
				pageButton.className = currentPage === i ? 'active' : '';
				pageButton.onclick = () => {
					currentPage = i;
					displayData();
				};
				paginationContainer.appendChild(pageButton);
			}
			
			// 下一頁按鈕
			const nextButton = document.createElement('button');
			nextButton.textContent = '下一頁';
			nextButton.disabled = currentPage === totalPages;
			nextButton.onclick = () => {
				if (currentPage < totalPages) {
					currentPage++;
					displayData();
				}
			};
			paginationContainer.appendChild(nextButton);
		}
		
		// 顯示資料
		function displayData() {
			// 計算當前頁面的開始和結束索引
			const startIndex = (currentPage - 1) * rowsPerPage;
			const endIndex = Math.min(startIndex + rowsPerPage, totalRows);
			
			// 隱藏所有行
			rows.forEach(row => {
				row.classList.add('hidden');
			});
			
			// 顯示當前頁面的行
			for (let i = startIndex; i < endIndex; i++) {
				rows[i].classList.remove('hidden');
			}
			
			// 更新分頁
			updatePagination();
		}
		
		// 初始顯示
		displayData();
	</script>
</body>
</html>