$(document).ready(function() {
    // 從 URL 獲取要編輯的租借品項 ID
    const urlParams = new URLSearchParams(window.location.search);
    const rentalItemId = urlParams.get('id');
    
    if (!rentalItemId) {
        alert('未指定要編輯的租借品項');
        window.location.href = '/rental_item.html';
        return;
    }
    
    // 載入分點與租借品項資料
    loadBranches();
    loadRentalItemData(rentalItemId);
    
    // 表單提交處理
    $('#editRentalItemForm').on('submit', function(e) {
        e.preventDefault();
        
        // 驗證表單
        if (!validateForm()) {
            return;
        }
        
        const formData = {
            rentalItemId: $('#rentalItemId').val(),
            rentalItemName: $('#rentalItemName').val().trim(),
            rentalItemPrice: parseInt($('#rentalItemPrice').val()),
            totalQuantity: parseInt($('#totalQuantity').val()),
            availableRentalQuantity: parseInt($('#availableRentalQuantity').val()),
            branchId: $('#branchId').val(),
            rentalItemStatus: parseInt($('input[name="rentalItemStatus"]:checked').val())
        };
        
        // 發送 AJAX 請求
        $.ajax({
            url: `/rental-item/update/${rentalItemId}`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function(response) {
                alert('更新租借品項成功');
                window.location.href = '/rental_item.html';
            },
            error: function(xhr, status, error) {
                alert('更新租借品項失敗: ' + error);
            }
        });
    });
    
    // 載入分點資料函數
    function loadBranches() {
        $.ajax({
            url: '/branch/getAll',
            type: 'GET',
            success: function(data) {
                const $branchSelect = $('#branchId');
                
                // 清空現有選項（保留「請選擇分點」選項）
                $branchSelect.find('option:not(:first)').remove();
                
                // 添加分點選項
                data.forEach(function(branch) {
                    $branchSelect.append(`<option value="${branch.branchId}">${branch.branchId} - ${branch.branchName}</option>`);
                });
            },
            error: function(xhr, status, error) {
                alert('載入分點數據失敗: ' + error);
            }
        });
    }
    
    // 載入租借品項資料函數
    function loadRentalItemData(id) {
        $.ajax({
            url: `/rental-item/getByRentalItemId/${id}`,
            type: 'GET',
            success: function(data) {
                if (data && data.length > 0) {
                    const rentalItem = data[0]; // 取第一個結果
                    
                    // 填入表單數據
                    $('#rentalItemId').val(rentalItem.rentalItemId);
                    $('#rentalItemName').val(rentalItem.rentalItemName);
                    $('#rentalItemPrice').val(rentalItem.rentalItemPrice);
                    $('#totalQuantity').val(rentalItem.totalQuantity);
                    $('#availableRentalQuantity').val(rentalItem.availableRentalQuantity);
                    
                    // 選擇對應的分點
                    // 等待分點數據加載完成後再設置選中狀態
                    setTimeout(function() {
                        $('#branchId').val(rentalItem.branchId);
                    }, 500);
                    
                    // 設置租借品項狀態
                    if (rentalItem.rentalItemStatus === 0) {
                        $('#status0').prop('checked', true);
                    } else {
                        $('#status1').prop('checked', true);
                    }
                } else {
                    alert('查無此租借品項資料');
                    window.location.href = '/rental_item.html';
                }
            },
            error: function(xhr, status, error) {
                alert('載入租借品項資料失敗: ' + error);
                window.location.href = '/rental_item.html';
            }
        });
    }
    
    // 表單驗證函數
    function validateForm() {
        // 檢查必填欄位
        const rentalItemName = $('#rentalItemName').val().trim();
        const rentalItemPrice = $('#rentalItemPrice').val().trim();
        const totalQuantity = $('#totalQuantity').val().trim();
        const availableRentalQuantity = $('#availableRentalQuantity').val().trim();
        const branchId = $('#branchId').val();
        
        if (!rentalItemName || !rentalItemPrice || !totalQuantity || !availableRentalQuantity) {
            alert('未輸入資料');
            return false;
        }
        
        // 檢查分點是否已選擇
        if (!branchId) {
            alert('未選擇');
            return false;
        }
        
        // 檢查租借品項狀態是否已選擇
        if (!$('input[name="rentalItemStatus"]:checked').length) {
            alert('未選擇');
            return false;
        }
        
        return true;
    }
});