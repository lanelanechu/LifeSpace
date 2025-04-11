// === 標籤輪播功能 ===
const tagContainer = $(".tag-container");
const tagWidth = $(".tag").outerWidth(true);

$(".left-tag-btn").click(function() {
    tagContainer.animate({ scrollLeft: "-=" + tagWidth * 1.5 }, 100);
});

$(".right-tag-btn").click(function() {
    tagContainer.animate({ scrollLeft: "+=" + tagWidth * 1.5 }, 100);
});


document.addEventListener('DOMContentLoaded', function () {
    // ... (原有的程式碼)

    const startTimeInput = document.getElementById('eventStartTime');
    const endTimeInput = document.getElementById('eventEndTime');

    // 監聽開始時間的變化
    startTimeInput.addEventListener('change', validateTimeRange);

    // 監聽結束時間的變化
    endTimeInput.addEventListener('change', validateTimeRange);

    // ... (原有的程式碼)
});

// 新增的驗證時間範圍的函式
function validateTimeRange() {
    const startTime = document.getElementById('eventStartTime').value;
    const endTime = document.getElementById('eventEndTime').value;

    if (startTime && endTime && startTime > endTime) {
        alert('開始時間不能超過結束時間！');
        document.getElementById('eventStartTime').value = ''; // 清空開始時間
        // 或者，你也可以選擇不清空，而是將焦點設定回開始時間欄位
        document.getElementById('eventStartTime').focus();
    }
}
