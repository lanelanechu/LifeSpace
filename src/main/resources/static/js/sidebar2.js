//確保箭頭方向正確的JavaScript
$(document).ready(function () {
    // 取得目前網址最後的檔名
    const currentPage = window.location.pathname.split("/").pop();

    // 檢查側邊選單內的每個連結
    $(".submenu-item").each(function () {
        const linkPage = $(this).attr("href").split("/").pop();

        if (linkPage === currentPage) {
            // 1. 加上 active 樣式
            $(this).addClass("active");

            // 2. 找到父層 ul，展開選單
            const submenu = $(this).closest(".submenu");
            submenu.addClass("show");

            // 3. 把上層 nav-link 也加上 active
            submenu.prev(".nav-link").addClass("active");
        }
    });

    // 添加點擊事件處理
    $(".nav-link").on("click", function(){
        // 動態調整箭頭方向
        const isActive = $(this).hasClass("active");

        // 如果展開了子選單，就設為active（這樣箭頭會向下）
        if($($(this).attr("href")).hasClass("show")) {
            $(this).addClass("active");
        } else {
            // 如果收起了子選單，移除active（箭頭會向右）
            if(!isActive) {
                $(this).removeClass("active");
            }
        }
    });

    // 綁定 Bootstrap collapse 展開事件
    $('.collapse').on('show.bs.collapse', function () {
        $(this).prev('.nav-link').addClass('active');
    });

    // 綁定 Bootstrap collapse 收起事件
    $('.collapse').on('hide.bs.collapse', function () {
        $(this).prev('.nav-link').removeClass('active');
    });

    // 初始檢查已展開的選單，確保箭頭方向正確
    $(".submenu.show").each(function(){
        $(this).prev(".nav-link").addClass("active");
    });
});