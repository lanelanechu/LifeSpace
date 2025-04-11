// ============ 網頁載入後要做的事情 ============

window.addEventListener('DOMContentLoaded', () => {
    // 初始化顯示第一個 tab，其餘隱藏
    tabs.forEach((tab, index) => {
        tab.style.display = index === 0 ? 'block' : 'none';
    });

    // 開始抓資料
    fetchSpace();
    fetchComments();

    setupOrderConfirmModal();
});

// ========== 處理頁籤 ==========

const navLinks = document.querySelectorAll('.main-link');
const tabs = document.querySelectorAll('.tab');

navLinks.forEach(link => {
    link.addEventListener('click', function (e) {
        e.preventDefault();

        // 切換 active 類別
        navLinks.forEach(l => l.classList.remove('active'));
        this.classList.add('active');

        // 取得目標 tab id
        const targetId = this.getAttribute('data-target');

        // 顯示對應內容、隱藏其他
        tabs.forEach(tab => {
            if (tab.id === targetId) {
                tab.style.display = 'block';
            } else {
                tab.style.display = 'none';
            }
        });
    });
});

// =============空間評論 modal=============

// 設定空間評論model位置
function showModalAbsolute(modal, overlay) {

    // 取得當前捲動位置
    const scrollTop = window.scrollY || document.documentElement.scrollTop;
    const scrollLeft = window.scrollX || document.documentElement.scrollLeft;

    // 設定 modal 的位置
    modal.style.top = `${scrollTop + window.innerHeight / 2 - modal.offsetHeight / 2}px`;
    modal.style.left = `${scrollLeft + window.innerWidth / 2 - modal.offsetWidth / 2}px`;

    overlay.style.top = `${scrollTop - overlay.offsetHeight / 2}px`;

    // 顯示 modal 和背景遮罩
    modal.style.display = 'block';

    overlay.classList.remove("hidden");
}

function hideModalFixed(modal, overlay) {
    modal.style.display = "none";
    overlay.classList.add("hidden");
}

// 關閉空間評論modal
const modal = document.querySelector('.modal');
const overlay = document.querySelector(".overlay");
const closeButton = document.querySelector('.close-button');

const btnOpenModal = document.querySelector('.list-all-equip');


closeButton.addEventListener('click', function () {
    hideModalFixed(modal, overlay);
    document.body.style.overflow = '';
});

overlay.addEventListener("click", function () {
    hideModalFixed(modal, overlay);
    document.body.style.overflow = '';
})

btnOpenModal.addEventListener("click", function () {
    showModalAbsolute(modal, overlay);
    document.body.style.overflow = 'hidden';
})

// ===============愛心(我的最愛)切換===============
const heartBtn = document.getElementById('heart-fav-btn');
const heartIcon = document.getElementById('heart-fav-icon');

heartBtn.addEventListener('click', () => {
    if (heartIcon.classList.contains('fa-regular')) {
        heartIcon.classList.remove('fa-regular');
        heartIcon.classList.add('fa-solid');
    } else {
        heartIcon.classList.remove('fa-solid');
        heartIcon.classList.add('fa-regular');
    }
});

// ===============選擇時租、日租===============
function setActive(element, id) {
    // 清除所有按鈕的 active 樣式
    document.querySelectorAll('.rent-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    // 對當前被點擊的那個元素加上active class
    element.classList.add('active');

    // 對當前被點擊的那個radio設為checked
    document.getElementById(id).checked = true;

    // 若選擇時租，才可以選擇開始、結束時間
    const timeSelects = document.querySelector('.time-selects');
    if (id === 'daily') {
        timeSelects.style.display = 'none';
    } else {
        timeSelects.style.display = 'flex';
    }

    // 根據租用類型切換價格及下方費率之顯示
    const hourlyPrice = document.querySelector('.main-hourly-price');
    const dailyPrice = document.querySelector('.main-daily-price');

    const durationRow = document.getElementById('durationRow');
    const rate = document.getElementById('rate');  // 費率那一行

    if (id === 'daily') {
        // 大字費率顯示日租
        hourlyPrice.style.display = 'none';
        dailyPrice.style.display = 'block';

        // 隱藏使用時間
        durationRow.style.display = 'none';
        // 費率文字改成日租 (抓取上面 .main-daily-price 裡的值)
        const dailyPricetext = document.getElementById('daily-price').textContent;
        rate.innerHTML = `$ <span class="rate-span">${dailyPricetext}</span>/d`;
    } else {
        // 大字費率顯示時租
        hourlyPrice.style.display = 'block';
        dailyPrice.style.display = 'none';

        // 顯示使用時間
        durationRow.style.display = 'flex';
        // 費率文字改成時租 (抓取上面 .main-hourly-price 裡的值)
        const hourlyPricetext = document.getElementById('hourly-price').textContent;
        rate.innerHTML = `$ <span class="rate-span">${hourlyPricetext}</span>/hr`;
    }

    updateTotal();
}



// ================ 日期選擇顯示/隱藏 ================
const dateToggleButton = document.getElementById('dateToggleButton');
const calendarContainer = document.getElementById('calendarContainer');
const monthNames = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
const calendarDays = document.getElementById('calendarDays');
const monthName = document.getElementById('monthName');
const yearElement = document.getElementById('year');
const prevMonthButton = document.getElementById('prevMonth');
const nextMonthButton = document.getElementById('nextMonth');

let currentDate = new Date();
let isCalendarVisible = false;

// 計算最大可選日期（當前日期起3個月內）
const now = new Date();
const maxDate = new Date();
maxDate.setMonth(now.getMonth() + 3);
maxDate.setDate(now.getDate()); // 設為3個月後的同一個date

dateToggleButton.addEventListener('click', function () {
    if (isCalendarVisible) {
        // 隱藏日曆
        calendarContainer.classList.add('fade-out');
        setTimeout(() => {
            calendarContainer.style.display = 'none';
            calendarContainer.classList.remove('fade-out');
        }, 450);
    } else {
        // 顯示日曆
        calendarContainer.style.display = 'block';
        calendarContainer.classList.add('fade-in');

        // 確保日曆顯示時更新為當前月份
        renderCalendar(currentDate.getFullYear(), currentDate.getMonth());
    }

    isCalendarVisible = !isCalendarVisible;
});

function renderCalendar(year, month) {
    // 清空日曆日期
    calendarDays.innerHTML = '';

    // 更新月份和年份顯示
    monthName.textContent = monthNames[month];
    yearElement.textContent = year;

    // 獲取當月第一天和最後一天
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);

    // 獲取當前的日期和時間
    const now = new Date();

    // 前一個月的填充日期
    const startDay = firstDay.getDay();
    for (let i = 0; i < startDay; i++) {
        const emptyDay = document.createElement('div');
        emptyDay.classList.add('calendar-day');
        calendarDays.appendChild(emptyDay);
    }

    // 當月的日期
    for (let i = 1; i <= lastDay.getDate(); i++) {
        const dayElement = document.createElement('div');
        dayElement.classList.add('calendar-day');
        dayElement.textContent = i;

        // 檢查這一天是否在過去
        const checkDate = new Date(year, month, i);
        const isPastDate = checkDate < new Date(now.getFullYear(), now.getMonth(), now.getDate());
        const isFutureDate = checkDate > maxDate;  // 檢查是否超過3個月

        if (isPastDate || isFutureDate) {
            // 過去的日期設為禁用狀態
            dayElement.classList.add('disabled-day');
        } else {
            // 標記今天
            if (now.getDate() === i && now.getMonth() === month && now.getFullYear() === year) {
                dayElement.classList.add('current-day');
            }

            // 點擊事件（只有未來日期才能點擊）
            dayElement.addEventListener('click', function () {
                const selectedDate = new Date(year, month, i);

                // 儲存選中的日期，供時間選擇使用
                window.selectedDate = selectedDate;

                dateToggleButton.innerHTML = selectedDate.toLocaleDateString('zh-TW') + `
                                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                                        fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                                        stroke-linejoin="round">
                                        <polyline points="6 9 12 15 18 9"></polyline>
                                        </svg>`;
                calendarContainer.classList.add('fade-out');
                setTimeout(() => {
                    calendarContainer.style.display = 'none';
                    calendarContainer.classList.remove('fade-out');
                }, 450);
                isCalendarVisible = !isCalendarVisible;

                // 當選擇了新日期，重置時間選擇
                resetTimeSelections();
            });
        }

        calendarDays.appendChild(dayElement);
    }
}

// 重置時間選擇
function resetTimeSelections() {
    // 重置開始時間按鈕文字
    const startButton = document.getElementById('startTimeButton');
    startButton.innerHTML = '開始時間 <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="6 9 12 15 18 9"></polyline></svg>';

    // 重置結束時間按鈕文字
    const endButton = document.getElementById('endTimeButton');
    endButton.innerHTML = '結束時間 <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="6 9 12 15 18 9"></polyline></svg>';

    // 清空時間容器
    document.getElementById('startTimeContainer').innerHTML = '';
    document.getElementById('endTimeContainer').innerHTML = '';

    // 清空隱藏的輸入欄位
    document.getElementById('startTimeInput').value = '';
    document.getElementById('endTimeInput').value = '';

    // 重置全域變數
    selectedStartTime = null;
}

// 上個月按鈕
prevMonthButton.addEventListener('click', function () {
    const now = new Date();
    const previousMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1);

    // 只有當上個月不早於當前月份時，才允許導航到上個月
    if (previousMonth.getFullYear() > now.getFullYear() ||
        (previousMonth.getFullYear() === now.getFullYear() &&
            previousMonth.getMonth() >= now.getMonth())) {
        currentDate.setMonth(currentDate.getMonth() - 1);
        renderCalendar(currentDate.getFullYear(), currentDate.getMonth());
    }
});

// 下個月按鈕
nextMonthButton.addEventListener('click', function () {
    // 檢查下個月是否超出3個月的限制
    const nextMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1);
    const threeMonthsLater = new Date();
    threeMonthsLater.setMonth(now.getMonth() + 3);

    // 只有在下個月不超過限制的情況下才允許切換
    if (nextMonth.getFullYear() < threeMonthsLater.getFullYear() ||
        (nextMonth.getFullYear() === threeMonthsLater.getFullYear() &&
            nextMonth.getMonth() <= threeMonthsLater.getMonth())) {
        currentDate.setMonth(currentDate.getMonth() + 1);
        renderCalendar(currentDate.getFullYear(), currentDate.getMonth());
    }
});

// ============開始、結束時間清單選擇============

/**
 * 切換顯示「開始時間」或「結束時間」的清單
 * type: 'start' 或 'end'
 * 結束時間需先選擇開始時間，且僅顯示大於開始時間的選項
 */
function toggleTimeSelect(type) {
    let containerId, buttonId, inputId;

    if (type === 'start') {
        // 在顯示開始時間前先檢查是否已選擇日期
        const dateToggleButton = document.getElementById('dateToggleButton');
        if (dateToggleButton.textContent.trim() === "選擇日期") {
            alert("請先選擇日期");
            return;
        }
        containerId = 'startTimeContainer';
        buttonId = 'startTimeButton';
        inputId = 'startTimeInput';
    } else if (type === 'end') {
        // 在顯示結束時間前先檢查是否已選擇開始時間
        const startButton = document.getElementById('startTimeButton');
        if (dateToggleButton.textContent.trim() === "選擇日期") {
            alert("請先選擇日期");
            return;
        }
        if (startButton.textContent.trim() === "開始時間") {
            alert("請先選擇開始時間");
            return;
        }
        containerId = 'endTimeContainer';
        buttonId = 'endTimeButton';
        inputId = 'endTimeInput';
    }

    const container = document.getElementById(containerId);

    // 若該容器還未生成清單（注意：因為註解或空白也可能導致 hasChildNodes() 為 true，可改用 children 判斷是否已經有元素子節點）
    if (container.children.length === 0) {
        if (type === 'start') {
            generateStartTimeOptions(containerId, buttonId, inputId);
        } else if (type === 'end') {
            // 傳入 selectedStartTime 作為下限，只顯示大於它的時間選項
            generateTimeOptions(containerId, buttonId, inputId, selectedStartTime);
        }
    }

    // 顯示/隱藏切換
    if (container.style.display === 'block') {
        container.style.display = 'none';
    } else {
        container.style.display = 'block';
    }
}

/**
 * 產生 24 小時、每 30 分鐘的清單
 * containerId: 容器的 ID（例如 'startTimeContainer'）
 * buttonId:    按鈕的 ID（例如 'startTimeButton'），用來更新顯示文字
 * inputId:     隱藏輸入欄的 ID (e.g. 'startTimeInput')
 * minTime (選填): 若有提供，僅顯示大於此時間的選項 (格式 "HH:MM")
 */

let selectedStartTime = null;

// 設定開始時間清單項目
function generateStartTimeOptions(containerId, buttonId, inputId, minTime) {
    const container = document.getElementById(containerId);
    let html = '<ul class="time-list">';

    // 檢查選中的日期是否為今天
    const now = new Date();
    const isToday = window.selectedDate &&
        window.selectedDate.getDate() === now.getDate() &&
        window.selectedDate.getMonth() === now.getMonth() &&
        window.selectedDate.getFullYear() === now.getFullYear();

    // 獲取當前小時和分鐘
    const currentHour = now.getHours();
    const currentMinute = now.getMinutes();

    // 顯示出8:00~21:30的開始時間選擇清單
    for (let hour = 8; hour <= 21; hour++) {
        for (let minute = 0; minute < 60; minute += 30) {
            // 補 0 成兩位數字
            const hh = hour.toString().padStart(2, '0');
            const mm = minute.toString().padStart(2, '0');
            const timeStr = `${hh}:${mm}`;

            // 如果是今天，只顯示當前時間之後的選項
            let isDisabled = false;
            if (isToday) {
                if (hour < currentHour || (hour === currentHour && minute <= currentMinute)) {
                    isDisabled = true;
                }
            }

            // 增加 disabled 類別到不可選的時間
            if (isDisabled) {
                html += `
                    <li class="disabled-time">
                        ${timeStr}
                    </li>`;
            } else {
                html += `
                    <li onclick="selectTime('${containerId}', '${buttonId}', '${inputId}', '${timeStr}')">
                        ${timeStr}
                    </li>`;
            }
        }
    }

    html += '</ul>';
    container.innerHTML = html;
}

// 設定結束時間清單項目
function generateTimeOptions(containerId, buttonId, inputId, minTime) {
    const container = document.getElementById(containerId);
    let html = '<ul class="time-list">';
    // 若有設定最小時間，轉換成分鐘數
    let minTimeMinutes = 0;
    if (minTime) {
        const parts = minTime.split(':');
        minTimeMinutes = parseInt(parts[0]) * 60 + parseInt(parts[1]);
    }

    // 顯示出8:00~22:00的開始時間選擇清單
    for (let hour = 8; hour <= 22; hour++) {
        for (let minute = 0; minute < 60; minute += 30) {
            // 若到 22:00 時，僅允許 minute 為 0
            if (hour === 22 && minute > 0) {
                break;
            }

            // 若提供最小時間，僅顯示大於該時間的選項
            let currentTimeMinutes = hour * 60 + minute;
            if (minTime && currentTimeMinutes <= minTimeMinutes) {
                continue;
            }

            // 補 0 成兩位數字
            const hh = hour.toString().padStart(2, '0');
            const mm = minute.toString().padStart(2, '0');
            const timeStr = `${hh}:${mm}`;

            // 點擊 li 時，呼叫 selectTime()，將時間帶入
            html += `
                <li onclick="selectTime('${containerId}', '${buttonId}', '${inputId}', '${timeStr}')">
                    ${timeStr}
                </li>`;
        }
    }

    html += '</ul>';
    container.innerHTML = html;
}

/**
 * 選擇時間後：
 * 1. 更新按鈕文字
 * 2. 隱藏時間清單
 * 3. 若為開始時間，記錄所選值，並重置結束時間選項
 * 4. 將時間存入hidden input
 */
function selectTime(containerId, buttonId, inputId, time) {
    const container = document.getElementById(containerId);
    const button = document.getElementById(buttonId);
    const input = document.getElementById(inputId);

    // 將使用者選到的時間字串 (例如 '09:30') 存入 hidden input
    input.value = time;

    // 更新按鈕上的文字（你可以改成放到 input 或其他地方）
    button.innerHTML = time + ` <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                                        fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                                        stroke-linejoin="round">
                                        <polyline points="6 9 12 15 18 9"></polyline>
                                </svg>`;

    // 收起下拉選單
    container.style.display = 'none';

    // 如果為開始時間選擇，儲存所選時間並重置結束時間選項
    if (containerId === 'startTimeContainer') {
        selectedStartTime = time;   // selectedStartTime為全域變數
        // 重置結束時間按鈕文字與清單
        const endButton = document.getElementById('endTimeButton');
        endButton.innerHTML = '結束時間 <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="6 9 12 15 18 9"></polyline></svg>';
        document.getElementById('endTimeContainer').innerHTML = '';
        document.getElementById('endTimeInput').value = '';
    }

    // 呼叫計算函式
    updateTotal();
}

// ============租賃品項數量選擇============
function changeQuantity(id, change) {
    const input = document.getElementById(id);
    const maxQuantity = parseInt(input.getAttribute('data-max'));
    let currentValue = parseInt(input.value) || 0;

    let value = currentValue + change;
    if (value < 0) {
        value = 0;
    } else if (value > maxQuantity) {
        alert(`抱歉，目前該品項最多只能租借${maxQuantity}個`);
        value = maxQuantity;
    }

    input.value = value;
    updateTotal();
}

// ============計算總金額============
function updateTotal() {
    // 判斷目前是哪種租用類型
    const isDaily = document.getElementById('daily').checked;

    // 先抓取目前下方顯示的費率(因為 setActive 已經同步調整了 .rate-span)
    // 或者也可以直接抓 #daily-price / #hourly-price
    let rateValue = parseInt(document.querySelector('.rate-span').textContent);

    let durationHours = 0;
    if (!isDaily) {  // 若為時租
        // 解析開始時間與結束時間，並計算使用時數
        const startTimeStr = document.getElementById('startTimeInput').value; // e.g. "10:30"
        const endTimeStr = document.getElementById('endTimeInput').value;   // e.g. "15:00"
        const endButton = document.getElementById('endTimeButton');

        if (startTimeStr && endTimeStr && !endButton.innerHTML.startsWith("結束")) {  // 確保都已有值再做解析
            // 解析 "HH:MM"
            const [startHour, startMin] = startTimeStr.split(':').map(Number);
            const [endHour, endMin] = endTimeStr.split(':').map(Number);

            // 轉換成「總分鐘數」再相減
            const startTotalMin = startHour * 60 + startMin;
            const endTotalMin = endHour * 60 + endMin;

            // 若有需要，先判斷是否 end > start
            const diffMin = endTotalMin - startTotalMin;
            if (diffMin > 0) {
                durationHours = diffMin / 60; // 轉回小時
            }
        }

        // 將「使用時間」顯示在網頁上
        document.querySelector('.duration-span').textContent = durationHours;
    } else {  // 若為日租
        document.querySelector('.duration-span').textContent = 0;
    }


    // 計算租借品項
    let equipmentTotal = 0;
    const equipmentInputs = document.querySelectorAll('.quantity-input');   // 抓租借品項輸入的數量

    // 計算各租借品項的金額
    equipmentInputs.forEach(input => {
        const quantity = parseInt(input.value);
        const price = parseInt(input.dataset.price);
        equipmentTotal += quantity * price;
    });

    document.getElementById('equipmentCost').textContent = '$ ' + equipmentTotal;

    // 計算總金額
    // --- 空間使用費 & 總金額 ---
    let spaceCost = 0;
    let total = 0;

    if (isDaily) {
        // 日租：直接取日租費率
        spaceCost = rateValue;
        total = spaceCost + equipmentTotal;  // 空間使用費 + 租借品項
    } else {
        // 時租：費率 × 使用時數
        spaceCost = rateValue * durationHours;
        total = spaceCost + equipmentTotal;
    }
    // const total = spaceTotal * durationHours + equipmentTotal;


    document.getElementById('spaceCost').textContent = '$ ' + spaceCost;
    document.getElementById('totalCost').textContent = '$ ' + total;
}


// =========== AJAX部分 ===========

let branchId = null;

// 載入空間資訊

function fetchSpace() {
    const urlParams = new URLSearchParams(window.location.search);
    const spaceId = urlParams.get('spaceId');


    fetch(`/spaces/id/${spaceId}`)
        .then(response => response.json())
        .then(space => {
            console.log(space);   // 檢查回傳的json是否正確
            branchId = space.branchId;
            insertPhotos(space.spacePhotos);
            insertAlert(space.spaceAlert);
            insertInfo(space.spacePeople, space.spaceUsageMaps, space.spaceFloor, space.spaceSize, space.spaceDesc);
            insertSpaceEquips(space.spaceEquipments);
            insertPublicEquips(space.publicEquipments);
            insertTransportation(space.branchAddr, space.spaceFloor);  // 地址到時候改成branchAddress
            insertAsideInfo(space.spaceName, space.spaceHourlyFee, space.spaceDailyFee);
            insertRentalItems(space.rentalItems);
        })
        .catch(error => console.log(error));
}

function insertPhotos(photos) {
    const indicatorsContainer = document.querySelector('.carousel-indicators');
    const innerContainer = document.querySelector('.carousel-inner');

    indicatorsContainer.innerHTML = '';
    innerContainer.innerHTML = '';

    photos.forEach((photoObj, index) => {
        const imgSrc = `data:image/jpeg;base64,${photoObj.photo}`;

        // indicators
        const indicator = document.createElement('button');
        indicator.type = 'button';
        indicator.setAttribute('data-bs-target', '#carouselExampleIndicators');
        indicator.setAttribute('data-bs-slide-to', index);
        indicator.setAttribute('aria-label', `Slide ${index + 1}`);
        if (index === 0) {
            indicator.classList.add('active');
            indicator.setAttribute('aria-current', 'true');
        }
        indicatorsContainer.appendChild(indicator);

        // carousel item
        const item = document.createElement('div');
        item.className = `carousel-item${index === 0 ? ' active' : ''}`;
        item.innerHTML = `<img src="${imgSrc}" class="d-block w-100" alt="空間圖片${index + 1}">`;
        innerContainer.appendChild(item);
    });
}

function insertAlert(msg) {
    const alertDetail = document.querySelector('.alert-detail');
    const alertContainer = document.querySelector('.space-alert');

    alertDetail.innerHTML = '';
    if (!msg || msg.trim() === "") {
        alertContainer.style.display = "none";
    } else {
        alertContainer.style.display = "block";
        alertDetail.innerHTML = msg;
    }
}

function insertInfo(people, usageMaps, floor, size, desc) {
    document.querySelector('.space-people').innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
                                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                                         stroke-linejoin="round">
                                        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                                        <circle cx="12" cy="7" r="4"></circle>
                                    </svg> 
                                    可容納人數：${people} 人`;

    let usageStr = '';
    usageMaps.forEach(map => {
        usageStr = usageStr.concat(`${map.spaceUsage.spaceUsageName}、`);
    })

    document.querySelector('.space-usages').innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
                                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                        <path d="M2 4h6a4 4 0 0 1 4 4v12a4 4 0 0 0-4-4H2z"></path>
                                        <path d="M22 4h-6a4 4 0 0 0-4 4v12a4 4 0 0 1 4-4h6z"></path>
                                    </svg> 本空間適用於：<b>${usageStr.length > 0 ? usageStr.slice(0, -1) : usageStr}</b>`;

    document.querySelector('.space-size').innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
                                        fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                                        stroke-linejoin="round">
                                        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
                                        <polyline points="9 22 9 12 15 12 15 22"></polyline>
                                    </svg> 
                                    空間大小：${size} 坪`;
    document.querySelector('.space-description').innerHTML = desc;
}

function insertSpaceEquips(equips) {
    const spaceEquips = document.querySelector('.space-equips');

    spaceEquips.innerHTML = '';

    if (!equips || equips.length === 0) {
        spaceEquips.innerHTML = '<p class="text-muted">此空間尚未提供任何設備</p>';
        return;
    }

    // 空間設備導入
    equips.forEach(equip => {
        const col = document.createElement('div');
        col.className = 'col-md-6 mb-2';
        col.innerHTML = `
            <div class="d-flex align-items-center">
                <i class="bi bi-check-circle me-2 text-primary"></i>
                <span>${equip.spaceEquipName}</span>
            </div>
        `;
        spaceEquips.appendChild(col);
    })
}

function insertPublicEquips(equips) {
    const publicEquips = document.querySelector('.public-equips');

    publicEquips.innerHTML = '';

    if (!equips || equips.length === 0) {
        publicEquips.innerHTML = '<p class="text-muted">此空間的公共區尚未提供任何設備</p>';
        return;
    }

    // 空間設備導入
    equips.forEach(equip => {
        const col = document.createElement('div');
        col.className = 'col-md-6 mb-2';
        col.innerHTML = `
            <div class="d-flex align-items-center">
                <i class="bi bi-check-circle me-2 text-primary"></i>
                <span>${equip.publicEquipName}</span>
            </div>
        `;
        publicEquips.appendChild(col);
    })
}

function insertTransportation(address, floor) {

    // 組合地址
    const fullAddress = `${address}${floor}${floor.trim() ? "樓" : ""}`;
    let iframeHTML = '';

    // 顯示地址資訊
    document.querySelector('.space-address').innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
                                        fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                                        stroke-linejoin="round">
                                        <path d="M21 10c0 6-9 12-9 12s-9-6-9-12a9 9 0 0 1 18 0z"></path>
                                        <circle cx="12" cy="10" r="3"></circle>
                                    </svg> ${fullAddress}`;

    // 將地址encode，並製作Google Maps 嵌入式地圖
    const encodedAddress = encodeURIComponent(fullAddress);
    iframeHTML = `
        <iframe
            src="https://www.google.com/maps?q=${encodedAddress}&output=embed"
            width="100%" height="450" style="border:0;" allowfullscreen=""
            loading="lazy" referrerpolicy="no-referrer-when-downgrade">
        </iframe>`;

    // 插入 iframe 到在 .transportation 下方
    document.querySelector('.transportation').insertAdjacentHTML('beforeend', iframeHTML);
}

function insertAsideInfo(name, hourly, daily) {
    document.querySelector('.header-title').textContent = name;
    document.querySelector('.main-hourly-price').innerHTML = `$<span id="hourly-price">${hourly}</span>/hr`;
    document.querySelector('.main-daily-price').innerHTML = `$<span id="daily-price">${daily}</span>/d`;

}

function insertRentalItems(itemObj) {
    const rentalItemContainer = document.querySelector(".rental-item-container");

    // 清空舊的租借品項，只留標題
    rentalItemContainer.innerHTML = `<div class="rental-item-title section-title">租借品項加購</div>`;

    itemObj.forEach(item => {
        if (item.rentalItemStatus === 0 || item.availableRentalQuantity <= 0) {
            return;
        }

        const itemHtml = `
                    <div class="rental-item">
                        <div class="equipment-name">${item.rentalItemName} (+$${item.rentalItemPrice})</div>
                        <div class="quantity-control">
                            <button class="quantity-btn" onclick="changeQuantity('${item.rentalItemId}', -1)">-</button>
                            <input type="number" id="${item.rentalItemId}" class="quantity-input" value="0" 
                            min="0" max="${item.availableRentalQuantity}"
                            data-price="${item.rentalItemPrice}" data-max="${item.availableRentalQuantity}" 
                            onchange="updateTotal(this)" oninput="validateQuantity(this)">
                            <button class="quantity-btn" onclick="changeQuantity('${item.rentalItemId}', 1)">+</button>
                        </div>
                    </div>
                `;
        rentalItemContainer.insertAdjacentHTML('beforeend', itemHtml);  // 把上述HTML接在下面
    })
}

// 驗證目前輸入的數量不得超過可租借數量
function validateQuantity(input) {
    const maxQuantity = parseInt(input.getAttribute('data-max'));   // 取得可租借數量
    let value = parseInt(input.value) || 0;

    if (value < 0) {
        value = 0;
    }

    // 确保值不超过最大可租借数量
    if (value > maxQuantity) {
        alert(`抱歉，目前該品項最多只能租借${maxQuantity}個`);
        value = maxQuantity;
    }

    // 更新输入框的值
    input.value = value;

    updateTotal();
}

// 載入空間評價資訊
function fetchComments() {
    const urlParams = new URLSearchParams(window.location.search);
    const spaceId = urlParams.get('spaceId');

    fetch(`/spaces/comments/${spaceId}`)
        .then(response => response.json())
        .then(data => {
            insertHeadComments(data.content);
        });
    // .catch(error => console.log("報錯：" + error));
}

function insertHeadComments(comments) {


    // 計算平均滿意度
    let totalRating = 0;
    comments.forEach(c => totalRating += c.satisfaction);
    const avgRating = comments.length ? (totalRating / comments.length).toFixed(1) : "0.0";
    document.querySelector(".avg-rating").innerHTML = avgRating;

    // 計算評論總數量
    let totalComments = comments.length;
    document.querySelector(".total-comments").innerHTML = `(${totalComments}則評論)`;

    const commentsContainer = document.querySelector(".container-comment-row");
    commentsContainer.innerHTML = ""; // 清空舊資料

    comments.forEach(comment => {
        const {
            commentContent,
            satisfaction,
            commentTime,
            photosUrls
        } = comment;

        // 滿意度星星生成
        let starsHtml = "";
        for (let i = 0; i < 5; i++) {
            starsHtml += `<i class="fa-${i < satisfaction ? 'solid' : 'regular'} fa-star"></i>`;
        }

        // 評論時間處理
        const timeString = formatRelativeTime(commentTime);

        // 照片處理
        let photosHtml = "";
        if (Array.isArray(photosUrls) && photosUrls.length > 0) {
            photosUrls.forEach(url => {
                photosHtml += `<img src="${url}" alt="評論照片" class="img-thumbnail me-2 mb-2" style="max-width: 100px;">`;
            });
        }

        // 評論HTML建立
        const commentCard = document.createElement("div");
        commentCard.className = "col-md-6 mb-4";
        commentCard.innerHTML = `
                <div class="card p-3">
                    <div class="d-flex justify-comments-between align-items-start">
                        <div class="d-flex">
                            <div class="avatar me-3">
                                <i class="fas fa-user"></i>
                            </div>
                            <div>
                                <div class="d-flex align-items-center">
                                    <h5 class="mb-0 me-2">匿名用戶</h5>
                                    <small class="text-muted">${timeString}</small>
                                </div>
                                <div class="star mt-1">${starsHtml}</div>
                            </div>
                        </div>
                    </div>
                    <div class="mt-3">
                        <p>${commentContent}</p>
                        ${photosHtml ? `<div class="mt-2 d-flex flex-wrap">${photosHtml}</div>` : ""}
                    </div>
                </div>
            `;

        commentsContainer.appendChild(commentCard);

    })

    const commentButton = document.querySelector(".comment-button");
    // 處理「查看所有評論」按鈕
    if (comments.length > 3) {
        commentButton.style.display = "block"; // 顯示按鈕
        insertAllComments(comments);
    } else {
        commentButton.style.display = "none"; // 隱藏按鈕
    }
}

function insertAllComments(comments) {
    // 抓取評論container，並初始化
    const reviewContainer = document.querySelector(".review-container");
    reviewContainer.innerHTML = "";
    comments.forEach(comment => {
        const {
            commentContent,
            satisfaction,
            commentTime,
            photosUrls
        } = comment;

        // 滿意度星星生成
        let starsHtml = "";

        for (let i = 0; i < 5; i++) {
            starsHtml += `<i class="fa-${i < satisfaction ? 'solid' : 'regular'} fa-star"></i>`;
        }

        // 評論時間處理
        const timeString = formatRelativeTime(commentTime);

        // 照片處理
        let photosHtml = "";
        if (Array.isArray(photosUrls) && photosUrls.length > 0) {
            photosUrls.forEach(url => {
                photosHtml += `<img src="${url}" alt="評論照片" class="img-thumbnail me-2 mb-2" style="max-width: 100px;">`;
            });
        }

        const reviewDiv = document.createElement("div");
        reviewDiv.className = "review-item p-3";
        reviewDiv.innerHTML = `
                    <div class="d-flex justify-content-between align-items-start">
                        <div class="d-flex">
                            <div class="avatar me-3">
                                <i class="fas fa-user"></i>
                            </div>
                            <div>
                                <div class="d-flex align-items-center">
                                    <h5 class="mb-0 me-2">匿名用戶</h5>
                                    <small class="text-muted">${timeAgo}</small>
                                </div>
                                <div class="star mt-1">
                                    ${starsHtml}
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="mt-3">
                        <p>${commentText}</p>
                        <a href="#" class="text-dark text-decoration-none fw-medium">查看完整内容</a>
                        <div class="comment-images mt-2">${imagesHtml}</div>
                    </div>
                `;

        reviewContainer.appendChild(reviewDiv);
    })
}


// 點擊「開始預訂」時
document.querySelector(".pay-button").addEventListener("click", () => {
    // 檢查選購欄資料是否接確實填寫
    const isDaily = document.getElementById("daily").checked;   // 檢查時租日租勾哪一個
    let dateSelected = document.getElementById("dateToggleButton").textContent.trim() !== "選擇日期";

    // 檢查時間是否已選擇 (僅時租需要)
    const startTimeSelected = document.getElementById("startTimeButton").textContent.trim() !== "開始時間";
    const endTimeSelected = document.getElementById("endTimeButton").textContent.trim() !== "結束時間";

    // 驗證條件
    let isValid = false;
    let errorMessage = "";

    if (isDaily) {
        if (!dateSelected)
            errorMessage = "請選擇預訂日期";
        else {
            isValid = true;
        }
    } else {
        if (!dateSelected || !startTimeSelected || !endTimeSelected) {
            errorMessage = "請選擇預訂日期、開始時間及結束時間";
        } else {
            isValid = true;
        }
    }

    // 驗證失敗時，跳出警告
    if (!isValid) {
        alert(errorMessage);
        return;
    }

    // 顯示modal及遮罩(overlay)
    document.getElementById("orderConfirmModal").style.display = "block";
    document.querySelector(".overlay").classList.remove("hidden");

    // 重置所有modal內容
    document.querySelector(".space-name").textContent = "";
    document.getElementById("rentTypeText").textContent = "";
    document.getElementById("rentFeeText").textContent = "";
    document.getElementById("usagePeriodText").textContent = "";
    document.getElementById("spaceCostText").textContent = "";
    document.getElementById("equipmentCostText").textContent = "";
    document.getElementById("totalCostText").textContent = "";
    document.getElementById("rentalItemList").innerHTML = "";

    // 填入新數據

    // 直接從選購欄抓資料
    const spaceName = document.querySelector(".header-title").textContent;

    // 抓費率

    const rate = isDaily ? parseInt(document.getElementById("daily-price").textContent) : parseInt(document.getElementById("hourly-price").textContent);

    const rentTypeText = isDaily ? "日租" : "時租";
    const rentFeeText = `$${rate}/${isDaily ? "d" : "hr"}`;

    // 抓開始、結束時間
    let selectedDate = document.getElementById("dateToggleButton").textContent.trim();
    let startTime = document.getElementById("startTimeInput").value;
    let endTime = document.getElementById("endTimeInput").value;
    let usageText = selectedDate + (isDaily ? "" : ` ${startTime} ~ ${endTime}`);

    // 抓租借時數
    const durationHours = parseFloat(document.querySelector(".duration-span").textContent);
    if (!isDaily) usageText += ` (${durationHours}hr)`;

    // 將資料丟進確認訂單的modal
    document.querySelector(".space-name").textContent = spaceName;
    document.getElementById("rentTypeText").textContent = rentTypeText;
    document.getElementById("rentFeeText").textContent = rentFeeText;
    document.getElementById("usagePeriodText").textContent = usageText;

    // 抓租借品項並丟進modal
    const itemList = document.getElementById("rentalItemList");
    itemList.innerHTML = "";
    const allInputs = document.querySelectorAll(".quantity-input");  // 抓各租借品項的數量

    allInputs.forEach(input => {
        const qty = parseInt(input.value);
        if (qty > 0) {  // 如果不是0才顯示
            const name = input.closest(".rental-item").querySelector(".equipment-name").textContent;
            const li = document.createElement("li");
            li.textContent = `${name} *${qty}`;
            itemList.appendChild(li);
        }
    });

    // 將金額相關資料丟進modal
    document.getElementById("spaceCostText").textContent = document.getElementById("spaceCost").textContent;
    document.getElementById("equipmentCostText").textContent = document.getElementById("equipmentCost").textContent;
    document.getElementById("totalCostText").textContent = document.getElementById("totalCost").textContent;
})

// 關閉確認訂單modal
function setupModalCloseListeners() {
    // 按modal的叉叉關閉
    const closeModalBtn = document.querySelector(".close-modal");
    closeModalBtn.onclick = function() {
        document.getElementById("orderConfirmModal").style.display = "none";
        document.querySelector(".overlay").classList.add("hidden");
    };

    // 按modal外面任意處關閉
    const overlay = document.querySelector(".overlay");
    overlay.onclick = function() {
        document.getElementById("orderConfirmModal").style.display = "none";
        this.classList.add("hidden");
    };
}

// 當DOM載入完成時設置關閉eventListener
if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", setupModalCloseListeners);
} else {
    setupModalCloseListeners();
}

// 檢查「評論」是多久前發的（n天前、n小時前）
function formatRelativeTime(dateStr) {
    const now = new Date();
    const past = new Date(dateStr);
    const diffMs = now - past;

    const seconds = Math.floor(diffMs / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);
    const months = Math.floor(days / 30);
    const years = Math.floor(days / 365);

    if (years > 0) return `${years}年前`;
    if (months > 0) return `${months}個月前`;
    if (days > 0) return `${days}天前`;
    if (hours > 0) return `${hours}小時前`;
    if (minutes > 0) return `${minutes}分鐘前`;
    return `剛剛`;
}

// 修正modal顯示位置要在可視區域中心
function setupOrderConfirmModal() {
    const orderModal = document.getElementById('orderConfirmModal');

    // 設置樣式
    orderModal.style.position = 'fixed';   // 使用fixed而不是absolute
    orderModal.style.top = '50%';
    orderModal.style.left = '50%';
    orderModal.style.transform = 'translate(-50%, -50%)';
    orderModal.style.zIndex = '10080';     // 確保z-index足夠高

    // 為modal內容設置最大高度，以確保在小屏幕上不會超出視窗
    const modalContent = orderModal.querySelector('.modal-content');
    if (modalContent) {
        modalContent.style.maxHeight = '80vh';
        modalContent.style.overflowY = 'auto';
    }

    // 修正遮罩層樣式
    const overlay = document.querySelector('.overlay');
    overlay.style.position = 'fixed';      // 使用fixed而不是absolute
    overlay.style.top = '0';
    overlay.style.left = '0';
    overlay.style.width = '100%';
    overlay.style.height = '100%';
}

// ============= POST: 送出訂單 =============
document.querySelector('.payment-btn').addEventListener("click", function() {
    // 取得spaceId
    const urlParams = new URLSearchParams(window.location.search);
    const spaceId = urlParams.get("spaceId");

    const isDaily = document.getElementById("daily").checked;
    const dateStr = document.getElementById("dateToggleButton").textContent.trim(); // yyyy/mm/dd

    const startTime = document.getElementById("startTimeInput").value;
    const endTime = document.getElementById("endTimeInput").value;

    // 時租、日租費率
    const hourlyRate = parseInt(document.getElementById("hourly-price").textContent);
    const dailyRate = parseInt(document.getElementById("daily-price").textContent);

    const rate = isDaily ? dailyRate : hourlyRate;

    // 小步的表格沒有$
    const spaceCost = parseInt(document.getElementById("spaceCost").textContent.replace(/\$| /g, ''));
    const equipmentCost = parseInt(document.getElementById("equipmentCost").textContent.replace(/\$| /g, ''));
    const totalPrice = parseInt(document.getElementById("totalCost").textContent.replace(/\$| /g, ''));

    const selectedDate = new Date(dateStr);
    const yyyy = selectedDate.getFullYear();
    const mm = (selectedDate.getMonth() + 1).toString().padStart(2, '0');
    const dd = selectedDate.getDate().toString().padStart(2, '0');

    // 不要動，就這樣放
    let orderStart = `${yyyy}-${mm}-${dd} ${startTime || '00:00'}:00`;
    let orderEnd = `${yyyy}-${mm}-${dd} ${endTime || '23:59'}:00`;

    // 建立rentalItemDeatilsDTOList
    const rentalItemList = [];
    const allInputs = document.querySelectorAll(".quantity-input");     // 抓租借品項的數字

    allInputs.forEach(input =>  {
        const quantity = parseInt(input.value);
        if (quantity > 0) {
            rentalItemList.push({
                rentalItemId: input.id,
                rentalItemName: input.closest(".rental-item").querySelector(".equipment-name").textContent.replace(/\s\(\+\$\d+\)/, ""),
                // rentalItemPrice: parseInt(input.dataset.price),
                rentalItemQuantity: quantity,
                // rentalTotalPrice: parseInt(input.dataset.price) * quantity
            });
        }
    })

    // 建立ordersDTO的結構
    const ordersDTO = {
        spaceId,
        branchId: branchId,
        orderStart,
        orderEnd,
        totalPrice: spaceCost,
        accountsPayable: totalPrice,
        paymentDatetime: Date.now(),
        memberId: "M001",   // TODO: 先寫死，之後串會員之後會再改
        eventDTO: null,     // 活動要在付款之後才放上去 (利用修改的方式)
        rentalItemDetailsDTOList: rentalItemList
    };

    fetch("/orders", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(ordersDTO)
    })
        .then(res => res.json())
        .then(data => {
            console.log(data);

            console.log("預訂成功");
            // window.location.href = "/frontend_orders.html";   // 或改成付款成功頁面
        })
        .catch(error => console.error(error.message));

})
