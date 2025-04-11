$(document).ready(function () {
           // 設定當前會員ID - 實際使用時應從會話或登入狀態獲取
           const memberId = 'M001';
           
           // 目前活動類型和頁碼
           let currentTab = 'registered-events';
           let currentPage = 0;
           let pageSize = 5;
           let totalPages = 0;
           
           // 儲存取消的活動ID
           let cancelEventId = '';
           let cancelOrganizerId = '';
           
           // 初始化頁籤切換功能
           $(".tab").click(function () {
               const tabId = $(this).data("tab");
               $(".tab").removeClass("active");
               $(this).addClass("active");
               $(".tab-content").hide();
               $("#" + tabId).show();
               
               // 重置頁碼並加載新頁籤的資料
               currentTab = tabId;
               currentPage = 0;
               loadEventData();
           });
           
           // 加載事件數據的函數
           function loadEventData() {
               // 顯示載入中
               $(`#${currentTab} .loading-spinner`).show();
               $(`#${currentTab} .empty-state`).addClass('d-none');
               $(`#${currentTab} .event-container`).remove();
               
               // 設定API參數
               let userCategory;
               switch(currentTab) {
                   case 'registered-events':
                       userCategory = '已報名但尚未舉辦';
                       break;
                   case 'queued-events':
                       userCategory = '候補活動';
                       break;
                   case 'held-events':
                       userCategory = '已舉辦且已參加';
                       break;
                   case 'created-events':
                       userCategory = '自己建立的活動';
                       break;
               }
               
               // 請求API
               $.ajax({
                   url: 'http://localhost:8080/lifespace/event/search/ByMember',
                   method: 'GET',
                   data: {
                       userCategory: userCategory,
                       memberId: memberId,
                       page: currentPage,
                       size: pageSize
                   },
                   success: function(response) {
                       // 隱藏載入中
                       $(`#${currentTab} .loading-spinner`).hide();
                       
                       // 處理返回數據
                       const events = response.content;
                       totalPages = response.totalPages;
                       
                       if (events && events.length > 0) {
                           // 有數據，渲染活動卡片
                           events.forEach(event => {
                               renderEventCard(event, currentTab);
                           });
                           
                           // 更新分頁
                           updatePagination();
                       } else {
                           // 無數據，顯示空狀態
                           $(`#${currentTab} .empty-state`).removeClass('d-none');
                       }
                   },
                   error: function(xhr) {
                       // 隱藏載入中
                       $(`#${currentTab} .loading-spinner`).hide();
                       
                       // 顯示錯誤
                       $(`#${currentTab}`).append(`
                           <div class="alert alert-danger">
                               加載數據失敗: ${xhr.responseText || '請稍後再試'}
                           </div>
                       `);
                   }
               });
           }
           
           // 渲染活動卡片
           function renderEventCard(event, tabId) {
               const startTime = new Date(event.eventStartTime);

               const formattedStartDate = `${startTime.getFullYear()}/${(startTime.getMonth()+1).toString().padStart(2, '0')}/${startTime.getDate().toString().padStart(2, '0')}`;
               const formattedStartTime = `${startTime.getHours().toString().padStart(2, '0')}:${startTime.getMinutes().toString().padStart(2, '0')}`;
               
               // 活動狀態標籤樣式
               let statusClass = '';
               let statusText = event.eventStatus;
               
               switch(event.eventStatus) {
                   case 'SCHEDULED':
                       statusClass = '';
                       statusText = '尚未舉辦';
                       break;
                   case 'HELD':
                       statusClass = 'status-held';
                       statusText = '已舉辦';
                       break;
                   case 'CANCELLED':
                       statusClass = 'status-cancelled';
                       statusText = '已取消';
                       break;
               }
               
               if (event.participateStatus === 'QUEUED') {
                   statusClass = 'status-queued';
                   statusText = '候補中';
               }
               
               // 活動操作按鈕
               let actionButtons = '';
               
			   console.log(event);
			   
			   // 根據不同條件決定是否顯示取消參與按鈕
			   if ((tabId === 'registered-events' || tabId === 'queued-events') 
						&& event.organizerId !== event.memberId ) {
			       // 已報名或候補的活動可以取消參與，但舉辦人不能取消參與自己的活動
			       actionButtons += `
			           <button class="action-button cancel-button" data-event-id="${event.eventId}" data-event-name="${event.eventName}">
			               取消參與
			           </button>
			       `;
			   }
               
               if (tabId === 'created-events' && event.eventStatus === 'SCHEDULED') {
                   // 自己建立且尚未舉辦的活動可以取消舉辦
                   actionButtons = `
                       <button class="action-button cancel-button" data-organizer-id="${memberId}" data-event-id="${event.eventId}" data-event-name="${event.eventName}">
                           取消舉辦
                       </button>
                   `;
               }
               
               // 訊息板按鈕 - 對所有活動都顯示
               const messageButton = `
                   <a href="event_message.html?eventId=${event.eventId}" class="action-button message-button">
                       <i class="fas fa-comments"></i> 活動留言板
                   </a>
               `;
               
               // 組合所有按鈕
               actionButtons = messageButton + actionButtons;
               
               // 圖片處理
               let imageUrl = 'images/img.bootstrap/property-1.jpg'; // 預設圖片
               if (event.photoUrls && event.photoUrls.length > 0) {
                   imageUrl = event.photoUrls[0]; // 使用第一張活動圖片
               }
               
               // 活動卡片 HTML
			   const cardHtml = `
			       <div class="event-container" data-event-id="${event.eventId}">
			           <div class="event-img">
			               <img src="${imageUrl}" alt="${event.eventName}">
			           </div>
			           <div class="event-info">
			               <span class="event-status ${statusClass}">${statusText}</span>
			               <h4>${event.eventName}</h4>
			               <span class="event-category">${event.eventCategoryName}</span>
			               <div class="event-time">
			                   <i class="fas fa-calendar-alt"></i>
			                   ${formattedStartDate} ${formattedStartTime}
			               </div>
			               <div class="event-location">
			                   <i class="fas fa-users"></i>
			                   參與人數: ${event.numberOfParticipants}/${event.maximumOfParticipants}
			               </div>
			               <div class="event-actions">
			                   ${actionButtons}
			               </div>
			           </div>
			       </div>
			   `;
               
               // 附加到對應頁籤
               $(`#${tabId}`).append(cardHtml);
           }
           
		   // 活動卡片點擊事件 - 導向活動詳情頁面
		   $(document).on('click', '.event-container', function(e) {
		       // 如果點擊的是按鈕或按鈕內部的元素，不執行導航
		       if ($(e.target).closest('.action-button').length > 0) {
		           return;
		       }
		       
		       const eventId = $(this).data('event-id');
		       if (eventId) {
		           // 導向活動詳情頁面
		           window.location.href = `http://localhost:8080/event_detail.html?eventId=${eventId}`;
		       }
		   });
		   
           // 更新分頁控制
           function updatePagination() {
               const paginationContainer = $('.pagination-container');
               paginationContainer.empty();
               
               if (totalPages <= 1) {
                   return; // 只有一頁不顯示分頁
               }
               
			   // 如果沒有數據（通過檢查事件容器是否存在），則不顯示分頁
			      if ($(`#${currentTab} .event-container`).length === 0) {
			          return; // 沒有事件時不顯示分頁
			      }
				  
               // 上一頁按鈕
               const prevBtn = $(`
                   <button class="pagination-btn prev ${currentPage === 0 ? 'disabled' : ''}">
                       <i class="fas fa-angle-left"></i>
                   </button>
               `);
               
               if (currentPage > 0) {
                   prevBtn.click(function() {
                       currentPage--;
                       loadEventData();
                   });
               }
               
               paginationContainer.append(prevBtn);
               
               // 頁碼按鈕
               const maxPageButtons = 5; // 最大顯示按鈕數
               let startPage = Math.max(0, currentPage - Math.floor(maxPageButtons / 2));
               let endPage = Math.min(totalPages - 1, startPage + maxPageButtons - 1);
               
               // 調整起始頁以確保顯示適當數量的按鈕
               if (endPage - startPage + 1 < maxPageButtons && startPage > 0) {
                   startPage = Math.max(0, endPage - maxPageButtons + 1);
               }
               
               for (let i = startPage; i <= endPage; i++) {
                   const pageBtn = $(`
                       <button class="pagination-btn ${i === currentPage ? 'active' : ''}">
                           ${i + 1}
                       </button>
                   `);
                   
                   if (i !== currentPage) {
                       pageBtn.click(function() {
                           currentPage = i;
                           loadEventData();
                       });
                   }
                   
                   paginationContainer.append(pageBtn);
               }
               
               // 下一頁按鈕
               const nextBtn = $(`
                   <button class="pagination-btn next ${currentPage >= totalPages - 1 ? 'disabled' : ''}">
                       <i class="fas fa-angle-right"></i>
                   </button>
               `);
               
               if (currentPage < totalPages - 1) {
                   nextBtn.click(function() {
                       currentPage++;
                       loadEventData();
                   });
               }
               
               paginationContainer.append(nextBtn);
           }
           
		   
           // 處理取消參與活動點擊
           $(document).off('click', '.cancel-button').on('click', '.cancel-button', function() {
               const eventId = $(this).data('event-id');
               const eventName = $(this).data('event-name');
               const organizerId = $(this).data('organizer-id'); // 若有值表示是取消舉辦
               
               cancelEventId = eventId;
               cancelOrganizerId = organizerId;
               
               if (organizerId) {
                   // 取消舉辦活動
                   $('#cancel-host-event-name').text(eventName);
                   $('#cancelHostEventModal').modal('show');
               } else {
                   // 取消參與活動
                   $('#cancel-event-name').text(eventName);
                   $('#cancelEventModal').modal('show');
               }
           });
           

           // 確認取消舉辦活動
           $('#confirmCancelHostBtn').one('click', function() {
               if (!cancelEventId || !cancelOrganizerId) return;
               
               $('#cancelHostEventModal').modal('hide');
               
			   // 顯示全局 spinner
			   showLoadingSpinner();
					   
               // 顯示處理中狀態
               $(`#${currentTab}`).append(`
                   <div class="alert alert-info cancel-processing">
                       正在處理您的請求，請稍候...
                   </div>
               `);
               
               // 發送取消舉辦請求
               $.ajax({
                   url: 'http://localhost:8080/lifespace/event/cancell',
                   method: 'PUT',
                   data: {
                       organizerId: cancelOrganizerId,
                       eventId: cancelEventId
                   },
                   success: function(response) {
                       $('.cancel-processing').remove();
                       
                       // 顯示成功訊息
                       $(`#${currentTab}`).append(`
                           <div class="alert alert-success">
                               已成功取消舉辦活動，系統將通知所有報名者
                           </div>
                       `);
					   
					   // 更新活動卡片狀態和移除按鈕
					   const eventCard = $(`.event-container[data-event-id="${cancelEventId}"]`);
					             eventCard.find('.event-status')
					                       .removeClass('status-scheduled') // 移除原有的狀態 class
					                       .addClass('status-cancelled')     // 新增 "已取消" 的狀態 class
					                       .text('已取消');                 // 更新文字
					             eventCard.find('.cancel-button').remove(); // 移除取消按鈕 

                       
						// 移除成功訊息 (延後2秒後移除)
						setTimeout(function() {
								   $('.alert').fadeOut(function() {
								           $(this).remove();
								    });
						}, 2000);
										 
                   },
                   error: function(xhr) {
                       $('.cancel-processing').remove();
                       
                       // 顯示錯誤訊息
                       $(`#${currentTab}`).append(`
                           <div class="alert alert-danger">
                               取消舉辦失敗: ${xhr.responseText || '請稍後再試'}
                           </div>
                       `);
                       
                       setTimeout(function() {
                           $('.alert').fadeOut(function() {
                               $(this).remove();
                           });
                       }, 3000);
                   },
				   complete: function() {
				   	               // 無論成功或失敗都隱藏 spinner
				   	               hideLoadingSpinner();
				   	           }
               });
           });
           
           // 初始加載數據
           loadEventData();
           
           // 設置定時刷新 - 每60秒刷新一次當前頁面的數據
           //setInterval(function() {
          //     if (document.visibilityState === 'visible') {
           //       loadEventData();
         //     }
         //  }, 60000); // 60秒
		   
		   
		   // 新增一個全局的 spinner 顯示/隱藏函數
		   function showLoadingSpinner() {
		       // 在頁面中間創建一個遮罩層和 spinner
		       $('body').append(`
		           <div id="global-loading-overlay" style="
		               position: fixed;
		               top: 0;
		               left: 0;
		               width: 100%;
		               height: 100%;
		               background: rgba(0, 0, 0, 0.5);
		               display: flex;
		               justify-content: center;
		               align-items: center;
		               z-index: 9999;
		           ">
		               <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
		                   <span class="sr-only">載入中...</span>
		               </div>
		           </div>
		       `);
		   }

		   function hideLoadingSpinner() {
		       $('#global-loading-overlay').remove();
		   }

		   // 修改確認取消按鈕事件
		   $('#confirmCancelBtn').click(function() {
		       if (!cancelEventId) return;

		       // 關閉彈窗
		       $('#cancelEventModal').modal('hide');

		       // 顯示全局 spinner
		       showLoadingSpinner();

		       // 發送取消參與請求
		       $.ajax({
		           url: 'http://localhost:8080/lifespace/event/removeMemFromEvent',
		           method: 'PUT',
		           data: {
		               eventId: cancelEventId,
		               memberId: memberId
		           },
		           success: function() {
		               // 重新加載當前頁面的數據
		               loadEventData();
		           },
		           error: function(xhr) {
		               // 顯示錯誤訊息
		               alert('取消參與失敗: ' + (xhr.responseText || '請稍後再試'));
		           },
		           complete: function() {
		               // 無論成功或失敗都隱藏 spinner
		               hideLoadingSpinner();
		           }
		       });
		   });
		   
		   
		   //按下瀏覽可參加的活動後導向活動瀏覽頁面
		   		   $('.btn.btn-outline-primary.mt-3').click(function() {
							const events_url = 'http://localhost:8080/event_overview.html';
		   		       		window.location.href = events_url;
		   		       });
		  
		   	   
       });