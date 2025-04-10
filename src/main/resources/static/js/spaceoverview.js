let showMapCheckbox, mapContainer, filterButton, filterPanel, spacesContainer;
let priceRange, minPriceDisplay, maxPriceDisplay, distanceRange;
let minDistanceDisplay, maxDistanceDisplay;

let filters = {
    minPrice: 150,
    maxPrice: 2000,
    minDistance: 100,
    maxDistance: 10000,
    peopleCount: [],
    usage: []
};

// 地圖相關
let map;
let markersData = [];   // 座標位置顯示
let activeInfoWindow = null; // Keep track of the currently open InfoWindow


let activeCardId = null;
let spaces = [];
let usages = [];

// 初始化，網頁載入完成時要做的事
document.addEventListener('DOMContentLoaded', () => {
    showMapCheckbox = document.getElementById('show-map-checkbox');
    mapContainer = document.getElementById('map-container');
    filterButton = document.getElementById('filter-button');
    filterPanel = document.getElementById('filter-panel');
    spacesContainer = document.getElementById('spaces-container');
    priceRange = document.getElementById('price-range');
    minPriceDisplay = document.getElementById('min-price');
    maxPriceDisplay = document.getElementById('max-price');
    distanceRange = document.getElementById('distance-range');
    minDistanceDisplay = document.getElementById('min-distance');
    maxDistanceDisplay = document.getElementById('max-distance');

    // 初始化價格滑桿
    noUiSlider.create(priceRange, {
        start: [100, 2000],
        connect: true,
        step: 10,
        range: {
            min: 100,
            max: 2000
        },
        format: {
            to: value => Math.round(value),
            from: value => parseInt(value)
        }
    });

    priceRange.noUiSlider.on('update', (values) => {
        filters.minPrice = values[0];
        filters.maxPrice = values[1];
        minPriceDisplay.textContent = `$${filters.minPrice}`;
        maxPriceDisplay.textContent = `$${filters.maxPrice}`;
        applyFilters();
    });

    // 初始化距離滑桿
    noUiSlider.create(distanceRange, {
        start: [100, 10000],
        connect: true,
        step: 100,
        range: {
            min: 100,
            max: 10000
        },
        format: {
            to: value => Math.round(value),
            from: value => parseInt(value)
        }
    });

    distanceRange.noUiSlider.on('update', (values) => {
        filters.minDistance = values[0];
        filters.maxDistance = values[1];
        minDistanceDisplay.textContent = filters.minDistance >= 1000
            ? `${(filters.minDistance / 1000).toFixed(1)}km`
            : `${filters.minDistance}m`;
        maxDistanceDisplay.textContent = filters.maxDistance >= 1000
            ? `${(filters.maxDistance / 1000).toFixed(1)}km`
            : `${filters.maxDistance}m`;
        applyFilters();
    });


    fetchSpaces();
    fetchSpaceUsages();
    setupEventListeners();
});

// 開始抓後端的資料
function fetchSpaces() {
    fetch('/spaces')
        .then(response => response.json())
        .then(data => {
            console.log(data);
            spaces = data.map(space => ({
                spaceId: space.spaceId,
                name: space.spaceName,
                location: `${space.branchAddr}${space.spaceFloor + (space.spaceFloor ? "樓" : "")}`, // 可從 space.branchId 取得更多資訊
                price: space.spaceHourlyFee,
                rating: space.spaceRating,
                capacity: space.spacePeople,
                status: space.spaceStatus,
                usage: space.spaceUsageMaps.map(map => map.spaceUsage.spaceUsageName),
                photo: space.spacePhotos.map(map => map.photo),
                coordinates: [space.latitude, space.longitude] // 模擬座標，之後會利用google maps API抓出
            }));
            renderSpaces(spaces);
            if (map) { // Only update markers if map is initialized
                updateMapMarkers(spaces);
            }
        })
        .catch(error => console.error('取得空間資料失敗:', error));
}

// 產生空間的card
function renderSpaces(spacesToRender) {
    spacesContainer.innerHTML = '';

    // 利用迴圈一個一個生出資料
    spacesToRender.forEach(space => {

        if (space.status === 0) {    // 如果是「未上架」，則不生成此空間資料
            return;   // forEach需要用return以執行迴圈continue功能
        }

        const spaceCard = document.createElement('div');
        spaceCard.className = 'space-card';
        spaceCard.dataset.id = space.spaceId;
        spaceCard.innerHTML = `
            <div class="space-image">    
                <img src="${getFirstPhoto(space.photo)}" alt="空間圖片">
            </div>
            <div class="space-info">
                <div class="space-title">
                    <span>${space.name}</span>
                    <button class="favorite-btn" data-id="${space.spaceId}">
                        <i class="far fa-heart"></i>
                    </button>
                </div>
                <div class="space-location">
                    <div class="location-text">
                        <i class="fas fa-map-marker-alt"></i> ${space.location}
                    </div>
                    <div class="people-count">
                        <i class="fas fa-user"></i> ${space.capacity}
                    </div>
                </div>
                <div class="space-rating">
                    <span class="space-price">$${space.price}/hr</span>
                    <div class="rating-stars">
                        <i class="fas fa-star"></i> ${space.rating.toFixed(1)}</div>
                </div>
            </div>
        `;
        spacesContainer.appendChild(spaceCard);

        // 地圖放大
        spaceCard.addEventListener('click', () => {
            if (showMapCheckbox.checked) highlightMarker(space.spaceId);
        });

        // 加入最愛
        spaceCard.querySelector('.favorite-btn').addEventListener('click', (event) => {
            event.stopPropagation();
            const icon = event.currentTarget.querySelector('i');
            icon.classList.toggle('far');
            icon.classList.toggle('fas');
            event.currentTarget.classList.toggle('active');
        });

        // 點擊卡片後，跳轉到個別空間頁面
        spaceCard.addEventListener('click', () => {
            window.location.href = `individual_space.html?spaceId=${space.spaceId}`;
        });
    });
}

function getFirstPhoto(photo) {
    if (photo.length === 0) {
        return "default.jpg";
    }
    return "data:image/jpeg;base64," + photo[0];
}

function fetchSpaceUsages() {
    fetch("/space-usages")
        .then(response => response.json())
        .then(data => {
            usages = data.map(space => space.spaceUsageName);
            renderUsages(usages);
        })
}


function renderUsages(usages) {
    const usageOptions = document.querySelector(".usage-options");
    usageOptions.innerHTML = '';

    usages.forEach(usage => {
        // 建立 label 元素
        const label = document.createElement("label");
        label.classList.add("checkbox-container");
        label.textContent = usage;

        // 建立 input 元素
        const input = document.createElement("input");
        input.type = "checkbox";
        input.name = "usage";
        input.value = usage;

        // 建立 span 元素（樣式勾選用）
        const span = document.createElement("span");
        span.classList.add("checkmark");

        // 把 input 和 span 加到 label 裡
        label.appendChild(input);
        label.appendChild(span);

        // 把 label 加到 usageOptions 容器裡
        usageOptions.appendChild(label);
    })

    // 用途篩選
    document.querySelectorAll('input[name="usage"]').forEach(checkbox => {
        console.log("usages");
        checkbox.addEventListener('change', () => {
            filters.usage = Array.from(document.querySelectorAll('input[name="usage"]:checked')).map(input => input.value);
            applyFilters();
        });
    });
}

// ============= 地圖相關 =============
function initMap() {
    console.log("Google Maps API Loaded - initMap called");
    try {
        const Map = google.maps.Map;   // 導入Google Maps

        map = new Map(document.getElementById("map"), {
            center: { lat: 25.0497, lng: 121.5380 },   // 預設以台北市為中心
            zoom: 13,
            mapId: "37060db895f0c169",
            disableDefaultUI: true,
            zoomControl: true
        });

        if (spaces && spaces.length > 0) {
            updateMapMarkers(spaces);
        }

    } catch (error) {
        console.error("Error initializing Google Map:", error);
        alert("無法載入地圖，請稍後再試。");
    }
}


function updateMapMarkers(spacesList) {
    if (!map) {
        console.warn("updateMapMarkers called before map was initialized.");
        return;
    }

    const AdvancedMarkerElement = google.maps.marker?.AdvancedMarkerElement;
    const InfoWindow = google.maps.InfoWindow;
    const Size = google.maps.Size;

    // 若資訊小彈窗開著，把他關了，並清除所有資訊小彈窗的資料
    if (activeInfoWindow) {
        activeInfoWindow.close();
        activeInfoWindow = null;
    }

    // 清除所有座標
    markersData.forEach(data => {
        if (data.markerElement) {
            data.markerElement.map = null;
        }
    });
    markersData = [];

    // 建立新的座標、資訊
    spacesList.forEach(space => {
        // 座標超出範圍之檢查
        if (!isValidCoordinates(space.coordinates)) {
            console.warn(`Skipping marker for space "${space.name}" due to invalid coordinates:`, space.coordinates);
            return;
        }

        // 建立座標
        const position = {
            lat: parseFloat(space.coordinates[0]),
            lng: parseFloat(space.coordinates[1])
        };

        let markerElement;
        try {
            markerElement = new AdvancedMarkerElement({
                position: position,
                map: map,
            });
        } catch (error) {
            console.error(`Error creating AdvancedMarkerElement for space ${space.spaceId}:`, error, "Position:", position);
            return;
        }


        // 建立資訊小彈窗
        const infoWindowContent = `<b>${space.name || '未命名'}</b><br>${space.location || ''}<br>$${space.price != null ? space.price : '??'}/hr`;
        const infoWindow = new InfoWindow({
            content: infoWindowContent,
            pixelOffset: new Size(0, -10)
        });

        // 點擊座標時觸發
        markerElement.addEventListener("click", () => {
            if (activeInfoWindow) {
                activeInfoWindow.close();
            }
            infoWindow.open({ map: map, anchor: markerElement });
            activeInfoWindow = infoWindow;
            highlightCard(space.spaceId); // 強調該空間card
        });

        // 儲存座標、資訊彈窗、spaceId
        markersData.push({
            markerElement: markerElement,
            infoWindow: infoWindow,
            spaceId: space.spaceId
        });
    });
}

function highlightMarker(spaceId) {

    if (!map)
        return;

    const data = markersData.find(d => d.spaceId === spaceId);
    if (data && data.markerElement) {
        const position = data.markerElement.position;
        if(position) {
            map.setCenter(position);
            map.setZoom(15);

            // 確保一次只能開一個彈窗
            if (activeInfoWindow) {
                activeInfoWindow.close();
            }
            data.infoWindow.open({ map: map, anchor: data.markerElement });
            activeInfoWindow = data.infoWindow;
        } else {
            console.warn(`Could not get position for marker with spaceId: ${spaceId}`);
        }

    } else {
        console.warn(`Marker data not found for spaceId: ${spaceId}`);
    }
}

// 經緯度檢查
function isValidCoordinates(coords) {
    return Array.isArray(coords) &&
        coords.length === 2 &&
        !isNaN(parseFloat(coords[0])) && isFinite(coords[0]) && Math.abs(coords[0]) <= 90 &&
        !isNaN(parseFloat(coords[1])) && isFinite(coords[1]) && Math.abs(coords[1]) <= 180;
}

// 點擊座標時，該空間card會有光暈
function highlightCard(spaceId) {
    document.querySelectorAll('.space-card').forEach(card => card.style.boxShadow = 'none');
    const card = document.querySelector(`.space-card[data-id="${spaceId}"]`);
    if (card) {
        card.style.boxShadow = '0 0 10px #4CAF50';
        card.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }
    activeCardId = spaceId;
}

// ============= Event Listeners集中設定 =============
function setupEventListeners() {
    // 顯示/隱藏地圖
    showMapCheckbox.addEventListener('change', () => {
        if (showMapCheckbox.checked) {
            mapContainer.classList.remove('hidden');
            setTimeout(() => google.maps.event.trigger(map, 'resize'), 100);
        } else {
            mapContainer.classList.add('hidden');
        }
    });

    // ======= 篩選條件相關 =======
    // 篩選條件
    filterButton.addEventListener('click', () => {
        filterPanel.classList.toggle('hidden');
    });

    // 價錢範圍
    priceRange.addEventListener('input', () => {
        filters.maxPrice = parseInt(priceRange.value);
        maxPriceDisplay.textContent = `$${filters.maxPrice}`;
        applyFilters();
    });

    // 距離範圍
    distanceRange.addEventListener('input', () => {
        filters.maxDistance = parseInt(distanceRange.value);
        maxDistanceDisplay.textContent = filters.maxDistance >= 1000
            ? `${(filters.maxDistance / 1000).toFixed(1)}km`
            : `${filters.maxDistance}m`;
        applyFilters();
    });

    // 人數勾選
    document.querySelectorAll('input[name="people"]').forEach(checkbox => {
        console.log("people");
        checkbox.addEventListener('change', () => {
            filters.peopleCount = Array.from(document.querySelectorAll('input[name="people"]:checked')).map(input => input.value);
            applyFilters();
        });
    });

    // 重置篩選
    document.querySelectorAll('.reset-button').forEach(button => {
        button.addEventListener('click', event => {
            const parentClass = event.currentTarget.parentElement.className;
            if (parentClass.includes('price-range')) {
                priceRange.noUiSlider.set([100, 2000]);
            } else if (parentClass.includes('distance-range')) {
                distanceRange.noUiSlider.set([100, 10000]);
            }
            applyFilters();
        });
    });

    // === 篩選條件相關 END ===
}

// ========== 執行篩選 ==========
function applyFilters() {
    const filteredSpaces = spaces.filter(space => {
        if (space.price < filters.minPrice || space.price > filters.maxPrice) return false;

        // if (space.distance < filters.minDistance || space.distance > filters.maxDistance) return false;

        if (filters.peopleCount.length > 0) {
            let passes = false;
            for (const range of filters.peopleCount) {
                if ((range === '2' && space.capacity <= 2) ||
                    (range === '2-6' && space.capacity > 2 && space.capacity <= 6) ||
                    (range === '7-9' && space.capacity >= 7 && space.capacity <= 9) ||
                    (range === '10-19' && space.capacity >= 10 && space.capacity <= 19) ||
                    (range === '20' && space.capacity >= 20)) {
                    passes = true;
                    break;
                }
            }
            if (!passes) return false;
        }

        if (filters.usage.length > 0) {
            console.log(space.usage);
            if (!filters.usage.some(u => space.usage.includes(u))) return false;
        }

        return true;
    });

    renderSpaces(filteredSpaces);
    if (map) { // Only update markers if map is initialized
        updateMapMarkers(filteredSpaces);
    }
}


// ========= 搜尋相關 =========

document.querySelector(".search-button").addEventListener("click", function(e) {
    e.preventDefault();

    const input = document.querySelector(".search-input");
    const keyword = input.value.trim();

    if (!keyword) {
        alert("搜尋欄不得空白！");
        return;
    }

    fetch(`/spaces/name?keyword=${encodeURIComponent(keyword)}`)
        .then(response => {
            if (!response.ok) {
                return response.json().catch(() => null).then(errorData => {
                    if (errorData && errorData.message) {
                        throw new Error(errorData.message);
                    }
                });
            }
            return response.json();
        })
        .then(data => {
            // 將搜尋到的空間資料轉換成前端需要的格式
            const searchedSpaces = data.map(space => ({
                spaceId: space.spaceId,
                name: space.spaceName,
                location: `台北市松山區${space.spaceFloor}`,
                price: space.spaceHourlyFee,
                rating: space.spaceRating,
                capacity: space.spacePeople,
                status: space.spaceStatus,
                usage: space.spaceUsageMaps.map(map => map.spaceUsage.spaceUsageName),
                photo: space.spacePhotos.map(map => map.photo),
                coordinates: [space.latitude, space.longitude]
            }));

            // 重設所有篩選條件
            document.querySelectorAll(".reset-button").forEach(button => {
                button.click();
            });
            document.querySelectorAll('input[type="checkbox"][name="people"]').forEach(checkbox => {
                checkbox.checked = false;
            });
            document.querySelectorAll('input[type="checkbox"][name="usage"]').forEach(checkbox => {
                checkbox.checked = false;
            });

            // 渲染搜尋結果
            renderSpaces(searchedSpaces);
            if (map) { // Only update markers if map is initialized
                updateMapMarkers(searchedSpaces);
            }
        })
        .catch(error => {
            alert(error.message);
        });
});

// 按Enter也可以搜尋
document.querySelector(".search-input").addEventListener("keydown", function (e) {
    if (e.key === "Enter") {
        document.querySelector(".search-button").click();
    }
});



