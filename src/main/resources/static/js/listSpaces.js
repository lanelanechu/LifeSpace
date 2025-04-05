// === Configuration ===
const APP_CONTEXT_PATH = "http://localhost:8080";
const GET_ALL_SPACES_URL = `${APP_CONTEXT_PATH}/spaces`;   // 需照抄Controller中@XXXMapping的連結
const GET_SPACE_BY_ID_URL_BASE = `${APP_CONTEXT_PATH}/spaces/id`;   // 後面加空間編號(S001, S002, etc.)
const GET_SPACE_BY_NAME_URL_BASE = `${APP_CONTEXT_PATH}/spaces/name`;   // 後面加空間名稱參數
const TOGGLE_SPACE_STATUS_URL = `${APP_CONTEXT_PATH}/spaces/status`;  // e.g. /spaces/status/S001)

// === DOM Elements ===
const tableBody = document.getElementById('tableBody');
const paginationContainer = document.getElementById('pagination');
const errorDisplay = document.getElementById('errorDisplay');
const searchForm = document.getElementById('searchForm');
const searchInput = document.getElementById('searchInput');
const showAllBtn = document.getElementById('showAllBtn');

// === 分頁設定與功能 ===
const rowsPerPage = 10;   // 每頁10筆
let currentPage = 1;   // 一開始在第1頁
let allSpacesData = []; // 用來存所有的空間資料
let filteredSpacesData = []; // 被篩選後的結果（可用於上下架、人數、費率篩選等）

// === Functions ===
function updatePagination() {
    const totalRows = filteredSpacesData.length;  // 總資料筆數
    const totalPages = Math.ceil(totalRows / rowsPerPage);   // 總頁數
    paginationContainer.innerHTML = '';
    paginationContainer.style.display = totalPages > 1 ? 'flex' : 'none'; // Hide if only one page or less

    if (totalPages <= 1) return; // 如果只有一頁，就不顯示選頁器

    // 上一頁按鈕
    const prevButton = document.createElement('button');
    prevButton.textContent = '上一頁';
    prevButton.disabled = currentPage === 1;
    prevButton.onclick = () => {
        if (currentPage > 1) {
            currentPage--;
            displayPagedData();   // 顯示該頁該有的資料
        }
    };
    paginationContainer.appendChild(prevButton);    // 加入上一頁按鈕

    // 實作顯示頁碼按鈕範例：[1, ..., 4, 5, 6, ... n]
    const maxPagesToShow = 5; // 選頁器一次最多顯示幾個頁碼
    let startPage = Math.max(1, currentPage - Math.floor(maxPagesToShow / 2));
    let endPage = Math.min(totalPages, startPage + maxPagesToShow - 1);
    if (endPage - startPage + 1 < maxPagesToShow) {
        startPage = Math.max(1, endPage - maxPagesToShow + 1);
    }

    // 若目前顯示之頁碼不包含1，則加一顆1按鈕
    if (startPage > 1) {
        const firstButton = document.createElement('button');
        firstButton.textContent = '1';
        firstButton.onclick = () => {
            currentPage = 1;
            displayPagedData();
        };
        paginationContainer.appendChild(firstButton);   // 加一顆1按鈕
        if (startPage > 2) {   // 若中間有跳頁，則加上...
            const ellipsis = document.createElement('span');
            ellipsis.textContent = '...';
            ellipsis.style.margin = '0 5px';
            paginationContainer.appendChild(ellipsis);
        }
    }

    // 顯示中間的頁碼按鈕
    for (let i = startPage; i <= endPage; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i;
        pageButton.className = currentPage === i ? 'active' : '';
        pageButton.onclick = () => {
            currentPage = i;
            displayPagedData();
        };
        paginationContainer.appendChild(pageButton);
    }

    // 後半段的頁碼按鈕抓取
    if (endPage < totalPages) {
        if (endPage < totalPages - 1) {    // 若中間有跳頁，則加上...
            const ellipsis = document.createElement('span');
            ellipsis.textContent = '...';
            ellipsis.style.margin = '0 5px';
            paginationContainer.appendChild(ellipsis);
        }
        // 最後一頁按鈕
        const lastButton = document.createElement('button');
        lastButton.textContent = totalPages;
        lastButton.onclick = () => {
            currentPage = totalPages;
            displayPagedData();
        };
        paginationContainer.appendChild(lastButton);
    }

    // 下一頁按鈕
    const nextButton = document.createElement('button');
    nextButton.textContent = '下一頁';
    nextButton.disabled = currentPage === totalPages;
    nextButton.onclick = () => {
        if (currentPage < totalPages) {
            currentPage++;
            displayPagedData();
        }
    };
    paginationContainer.appendChild(nextButton);
}

// 顯示單筆資料(來自getSpaceById()的請求)
function displaySingleSpace(space) {
    tableBody.innerHTML = '';   // 清除目前的HTML後重建資料

    const tr = document.createElement('tr');   // 建立一個table row

    // 產生每列資料
    showRowData(space);

    paginationContainer.style.display = 'none'; // 因為只有單筆資料，所以可以先把選頁器隱藏
    showAllBtn.style.display = 'inline-block'; // 顯示「顯示全部」按鈕
    errorDisplay.textContent = '';  // 清除之前的錯誤資訊
}

// 顯示單頁所需表格資料 (來自getAll()的請求)
function displayPagedData() {
    tableBody.innerHTML = ''; // 將之前查詢記錄移除

    // 透過分頁邏輯，計算出那一頁要顯示哪些資料
    const startIndex = (currentPage - 1) * rowsPerPage;
    const endIndex = Math.min(startIndex + rowsPerPage, filteredSpacesData.length);  // 處理最後一頁的顯示方式
    const spacesToDisplay = filteredSpacesData.slice(startIndex, endIndex);

    // 在資料改變後，調整顯示方式
    if (spacesToDisplay.length === 0 && filteredSpacesData.length > 0) {     // 搜尋結果沒有資料，且當前頁數不在第1頁時，重設到第1頁
        currentPage = 1;
        displayPagedData();
        return;
    }
    if (spacesToDisplay.length === 0) {     //  沒有資料時，選頁器隱藏
        tableBody.innerHTML = `<tr><td colspan="12" style="text-align:center;">查無空間資料。</td></tr>`;
        paginationContainer.style.display = 'none';
        return;
    }

    // 產生每列資料
    spacesToDisplay.forEach(space => {
        showRowData(space);
    });

    updatePagination(); // 根據上面新增的資料數，調整選頁器的配置
    showAllBtn.style.display = 'none'; // 隱藏「顯示全部」按鈕
    errorDisplay.textContent = '';
}

// 產生每列資料
function showRowData(space) {
    const tr = document.createElement('tr');    // 建立一個table row物件

    // 處理關聯資料
    const equipmentNames = (space.spaceEquipments && space.spaceEquipments.length > 0)
        ? space.spaceEquipments.map(eq => escapeHtml(eq.spaceEquipName)).join(', ')
        : '無';   	// 設備A, 設備B, ...

    const usageNames = (space.spaceUsageMaps && space.spaceUsageMaps.length > 0)
        ? space.spaceUsageMaps.map(map => escapeHtml(map.spaceUsage?.spaceUsageName || '未知')).join(', ') // Added check for nested spaceUsage
        : '無';		// 用途A, 用途B, ...

    const photoCount = (space.spacePhotos && Array.isArray(space.spacePhotos))
        ? space.spacePhotos.length
        : 0;   // 照片數量

    // 照片處理
    let photoData;
    if (photoCount == 0) {
        photoData = `<span>0 張照片</span>`;
    }
    else {
        let photos = space.spacePhotos.map(p => `data:image/jpeg;base64,${p.photo}`);
        let escapedPhotosJson = JSON.stringify(photos).replace(/'/g, '&#39;').replace(/"/g, '&quot;');   // 檢查有沒有跳脫字元
        photoData = `<span class="photo-link" data-photos='${escapedPhotosJson}' style="text-decoration: underline; color: blue; cursor: pointer;">${photoCount} 張照片</span>`;
    }

    // 在Table row裡塞進以下HTML
    tr.innerHTML = `
                        <td>${escapeHtml(space.spaceId)}</td>
                        <td>${escapeHtml(space.branchId)}</td>
                        <td>${escapeHtml(space.spaceName)}</td>
                        <td>${escapeHtml(space.spacePeople)} 人</td>
                        <td>${escapeHtml(space.spaceSize)} 坪</td>
                        <td>$${escapeHtml(space.spaceHourlyFee)}/hr</td>
                        <td>$${escapeHtml(space.spaceDailyFee)}/d</td>
                        <td title="${escapeHtml(space.spaceDesc)}">${truncateText(escapeHtml(space.spaceDesc), 20)}</td>
                        <td>${escapeHtml(space.spaceRating)}</td>
                        <td title="${escapeHtml(space.spaceAlert)}">${truncateText(escapeHtml(space.spaceAlert), 20)}</td>
                        <td>${escapeHtml(space.spaceStatusText)}</td>
                        <td title="${escapeHtml(space.spaceFloor)}">${truncateText(escapeHtml(space.spaceFloor), 20)} 樓</td>
                        <td title="${equipmentNames}">${truncateText(equipmentNames, 20)}</td>
                        <td title="${usageNames}">${truncateText(usageNames, 20)}</td>
                        <td>${photoData}</td>
                        <td>${escapeHtml(space.createdTime)}</td>
                        <td>
                            <button type="button" class="btn btn-edit edit-space-btn" data-space-id="${escapeHtml(space.spaceId)}">
                                修改
                            </button>
                        </td>
                        <td>
                            <button type="button" class="btn toggle-status-btn" data-space-id="${escapeHtml(space.spaceId)}" data-current-status="${escapeHtml(space.spaceStatus)}">
                                ${space.spaceStatus === 1 ? '下架' : '上架'}
                            </button>
                        </td>
                    `;
    // escapeHtml(): 處理跳脫字元
    // truncateText(): 利用...處理過長的資料，20就是只顯示前20個字
    // *spaceFloor記得跟branch地址連結，合併成完整地址
    tableBody.appendChild(tr);
}

// === Utility Functions ===
// 處理基本的跳脫字元
function escapeHtml(unsafe) {
    if (unsafe === null || typeof unsafe === 'undefined') return '';
    return String(unsafe)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

// 利用...壓縮過長的文字資料
function truncateText(text, maxLength) {
    if (!text) return '';
    text = String(text);
    return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
}

// 處理照片輪播
function updateCarousel() {
    const img = document.getElementById('carouselImage');
    const indicator = document.getElementById('photoIndicator');

    if (!currentPhotos.length) return;

    img.src = currentPhotos[currentPhotoIndex];
    indicator.textContent = `${currentPhotoIndex + 1} / ${currentPhotos.length}`;
}

// 上一張
document.getElementById('prevBtn').addEventListener('click', () => {
    if (currentPhotos.length === 0) return;
    currentPhotoIndex = (currentPhotoIndex - 1 + currentPhotos.length) % currentPhotos.length;
    updateCarousel();
});

// 下一張
document.getElementById('nextBtn').addEventListener('click', () => {
    if (currentPhotos.length === 0) return;
    currentPhotoIndex = (currentPhotoIndex + 1) % currentPhotos.length;
    updateCarousel();
});


// 點擊關閉按鈕關閉 modal
document.querySelector('#photoModal .close').addEventListener('click', function () {
    document.getElementById('photoModal').style.display = 'none';
});
// 點擊 modal 區域外關閉 modal
window.addEventListener('click', function (event) {
    const modal = document.getElementById('photoModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
});

// 篩選上架中、下架中之空間
document.getElementById('statusFilter').addEventListener('change', function () {
    const selectedStatus = this.value;  // 抓下拉式選單中選到的value

    if (selectedStatus === 'all') {
        filteredSpacesData = [...allSpacesData]; // 顯示全部
    } else {
        filteredSpacesData = allSpacesData.filter(space => String(space.spaceStatus) === selectedStatus);   // 0: 未上架, 1: 已上架
    }

    currentPage = 1;
    displayPagedData();
});

// ============= AJAX CRUD =============

// === 1. GET: 搜尋所有空間 ===
function fetchSpaces() {
    tableBody.innerHTML = '<tr><td colspan="12" style="text-align:center;">載入中...</td></tr>'; // Show loading state

    // 載入過程要做的事...
    errorDisplay.textContent = ''; // 把之前的錯誤訊息刪除
    paginationContainer.style.display = 'none';
    showAllBtn.style.display = 'none';

    fetch(GET_ALL_SPACES_URL)   // 回傳一個promise物件
        .then(response => {  // 處理promise物件，提取JSON
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            // 因為有檔案相關欄位，所以回傳JSON前要先檢查content-type是否正確
            const contentType = response.headers.get("content-type");

            if (contentType && contentType.indexOf("application/json") !== -1) {
                return response.json(); // 解析回應的JSON資料
            } else {
                throw new Error(`Expected JSON but received ${contentType}`);
            }
        })
        .then(data => {   // 處理JSON資料
            if (!Array.isArray(data)) {     // 回傳的JSON格式必須為Array (Array裡面的每一個Object都是一組空間資料)
                throw new Error("Received data is not an array");
            }
            allSpacesData = data; // 儲存所有資料
            filteredSpacesData = [...data]; // 一進網頁因為沒有篩選，所以一樣顯示全部
            currentPage = 1; // 取得請求後，記得把頁數回到第1頁
            displayPagedData(); // 顯示第一頁資料
        })
        .catch(error => {
                console.error('Error fetching spaces:', error);
                errorDisplay.textContent = `無法載入空間資料：${error.message}。請稍後再試。`;
                tableBody.innerHTML = `<tr><td colspan="12" style="text-align:center; color: red;">載入失敗</td></tr>`;
            }
        );
}

// === 2. GET: 透過ID或空間名稱搜尋單一空間 ===
searchForm.addEventListener('submit', function (e) {
    e.preventDefault();
    const searchType = document.getElementById('searchType').value;


    const keyword = searchInput.value.trim();   // 抓輸入欄的值

    if (!keyword) {     // 未輸入資料時
        alert("輸入欄不得空白！");
        return;
    }

    // 先清空畫面與錯誤
    tableBody.innerHTML = `<tr><td colspan="12" style="text-align:center;">搜尋中...</td></tr>`;
    errorDisplay.textContent = '';
    paginationContainer.style.display = 'none';

    // 取得URL
    let searchUrl;
    if (searchType === 'id') {  // 根據空間ID
        searchUrl = `${GET_SPACE_BY_ID_URL_BASE}/${encodeURIComponent(keyword)}`;    // (http://localhost:8080/spaces/id/S001)
    } else if (searchType === 'name') { // 根據空間名稱
        searchUrl = `${GET_SPACE_BY_NAME_URL_BASE}?keyword=${encodeURIComponent(keyword)}`;    // (http://localhost:8080/spaces/name?keyword=Cozy)
    }

    fetch(searchUrl)
        .then(response => {
            if (!response.ok) {
                // 解析ResourceNotFoundException回傳的JSON
                return response.json().catch(() => null).then(errorData => { // 解析json，如果無法解析就直接忽略
                    if (errorData && errorData.message) {
                        throw new Error(errorData.message);  // 從後端的錯誤訊息拿來這邊顯示
                    }
                });
            }
            return response.json();
        })
        .then(data => {
            if (Array.isArray(data)) {
                allSpacesData = data;
                currentPage = 1;
                displayPagedData();  // 顯示多筆（模糊查詢）
            } else {
                displaySingleSpace(data); // 顯示單筆（精準查詢）
            }
        })
        .catch(error => {
            console.error(`Error fetching space ID ${keyword}:`, error);
            errorDisplay.textContent = `搜尋失敗：${error.message}`;
            tableBody.innerHTML = `<tr><td colspan="12" style="text-align:center; color: red;">搜尋失敗</td></tr>`;
            showAllBtn.style.display = 'inline-block'; // 報錯時也可以顯示全部
        });
})

// 點擊「新增資料」按鈕時...
document.getElementById('addSpaceBtn').addEventListener('click', function () {
    window.location.href = 'addSpace.html'; // 前端路由跳轉
});

// 點擊「顯示全部」按鈕時...
showAllBtn.addEventListener('click', () => {
    searchInput.value = ''; // Clear search input
    fetchSpaces(); // Fetch and display all spaces
});

// 點擊 "n 張照片" 連結時，讀取 data-photos 屬性，並以 modal 顯示
// Carousel 狀態變數
let currentPhotoIndex = 0;
let currentPhotos = [];

// 在表格做事件間聽
tableBody.addEventListener('click', function (e) {
    const target = e.target;

    // 點擊「n 張照片時」，顯示照片輪播預覽
    if (target.classList.contains('photo-link')) {
        const photos = JSON.parse(target.getAttribute('data-photos'));
        if (!photos || photos.length === 0) return;

        currentPhotos = photos;
        currentPhotoIndex = 0;

        updateCarousel(); // 顯示第一張

        document.getElementById('photoModal').style.display = 'block';
    }

    // 點擊「修改」按鈕時，跳轉到updateSpace.html
    else if (target.classList.contains('edit-space-btn') || target.closest('.edit-space-btn')) {
        const button = target.closest('.edit-space-btn');       // 找到你剛剛按的「修改」按鈕
        const spaceId = button.getAttribute('data-space-id');   // 有在data-space-id綁定空間編號，以利於在修改表單中顯示目前的資料狀況

        const targetUrl = `updateSpace.html?spaceId=${spaceId}`;
        window.location.href = targetUrl;
    }

    // === 4. PUT: 空間上/下架、調整狀態 ===
    else if (target.classList.contains('toggle-status-btn') || target.closest('.toggle-status-btn')) {
        const button = target.closest('.toggle-status-btn');    // 找到你剛剛按的「上/下架」按鈕
        const spaceId = button.getAttribute('data-space-id');
        const currentStatus = button.getAttribute('data-current-status');   // 取得目前的狀態，用來在HTML上顯示正確的狀態
        console.log(currentStatus);
        const newStatus = currentStatus === "1" ? "0" : "1";  // 先將目前的狀態存到newStatus變數裡
        let confirmStatus = confirm(`確定要${currentStatus == "1" ? "下架" : "上架"}此空間？`);

        if (confirmStatus) {
            fetch(`${TOGGLE_SPACE_STATUS_URL}/${spaceId}`, {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({status: newStatus})     // Request Body只傳空間狀態丟給後端處理
            })
                .then(response => {
                    return response.json();
                })
                .then(updatedSpace => {
                    button.textContent = newStatus === "1" ? '下架' : '上架';   // 更新按鈕文字
                    button.setAttribute('data-current-status', newStatus);  // 設定attribute，以在表格欄位中顯示正確的文字

                    const statusCell = button.closest('tr').querySelector('td:nth-child(11)');
                    statusCell.textContent = newStatus === "1" ? '上架中' : '未上架';   // 欄位更新狀態

                    // 同步更新 allSpacesData 的狀態
                    const index = allSpacesData.findIndex(s => s.spaceId === spaceId);  // 迭代找出目前的空間編號
                    if (index !== -1) { // 若有找到
                        allSpacesData[index].spaceStatus = parseInt(newStatus);
                        allSpacesData[index].spaceStatusText = newStatus === "1" ? "上架中" : "未上架";
                    }
                })
                .catch(error => {
                    console.error('狀態更新錯誤:', error);
                });
        }
    }
});

// === Initialization ===
document.addEventListener('DOMContentLoaded', fetchSpaces);