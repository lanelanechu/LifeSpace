// 前端 JS，透過 AJAX 串接後端 /spaces API

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

let map;
let markers = [];
let activeCardId = null;
let spaces = [];

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

    fetchSpaces();
    initializeMap();
    setupEventListeners();
});

// 開始抓後端的資料
function fetchSpaces() {
    fetch('/spaces')
        .then(response => response.json())
        .then(data => {
            //
            spaces = data.map(space => ({
                spaceId: space.spaceId,
                name: space.spaceName,
                location: `台北市松山區${space.spaceFloor}`, // 可從 space.branchId 取得更多資訊
                price: space.spaceHourlyFee,
                rating: space.spaceRating,
                capacity: space.spacePeople,
                usage: space.spaceUsageMaps.map(map => map.spaceUsage.spaceUsageName),
                photo: space.spacePhotos.map(map => map.photo),
                coordinates: [25.0497 + Math.random() * 0.01, 121.5380 + Math.random() * 0.01] // 模擬座標，之後會利用google maps API抓出
            }));
            renderSpaces(spaces);
            updateMapMarkers(spaces);
        })
        .catch(error => console.error('取得空間資料失敗:', error));
}

// 產生空間的card
function renderSpaces(spacesToRender) {
    spacesContainer.innerHTML = '';

    // 利用迴圈一個一個生出資料
    spacesToRender.forEach(space => {
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
                        <i class="fas fa-map-marker-alt"></i> ${space.location}樓
                    </div>
                    <div class="people-count">
                        <i class="fas fa-user"></i> ${space.capacity}
                    </div>
                </div>
                <div class="space-rating">
                    <span class="space-price">$${space.price}/hr</span>
                    <div class="rating-stars">
                        <i class="fas fa-star"></i> ${space.rating}</div>
                </div>
            </div>
        `;
        spacesContainer.appendChild(spaceCard);

        // 地圖放大（會改掉）
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
    console.log(photo);

    if (photo.length === 0) {
        return "default.jpg";
    }
    return "data:image/jpeg;base64," + photo[0];
}


// ======== 篩選相關 ========
function applyFilters() {
    const filteredSpaces = spaces.filter(space => {
        if (space.price > filters.maxPrice) return false;

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
            if (!filters.usage.some(u => space.usage.includes(u))) return false;
        }

        return true;
    });

    renderSpaces(filteredSpaces);
    updateMapMarkers(filteredSpaces);
}

// ============= 地圖相關 =============
function initializeMap() {
    map = L.map('map').setView([25.0497, 121.5380], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);
}

function updateMapMarkers(spacesList) {
    markers.forEach(marker => map.removeLayer(marker));
    markers = [];

    spacesList.forEach(space => {
        const marker = L.marker(space.coordinates)
            .addTo(map)
            .bindPopup(`<b>${space.name}</b><br>${space.location}<br>$${space.price}/hr`);

        marker.spaceId = space.spaceId;
        markers.push(marker);

        marker.on('click', () => highlightCard(space.spaceId));
    });
}

function highlightCard(spaceId) {
    document.querySelectorAll('.space-card').forEach(card => card.style.boxShadow = 'none');
    const card = document.querySelector(`.space-card[data-id="${spaceId}"]`);
    if (card) {
        card.style.boxShadow = '0 0 10px #4CAF50';
        card.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }
    activeCardId = spaceId;
}

function highlightMarker(spaceId) {
    const marker = markers.find(m => m.spaceId === spaceId);
    if (marker) {
        map.setView(marker.getLatLng(), 15);
        marker.openPopup();
    }
}

// ============= Event Listeners集中設定 =============
function setupEventListeners() {
    // 顯示/隱藏地圖
    showMapCheckbox.addEventListener('change', () => {
        if (showMapCheckbox.checked) {
            mapContainer.classList.remove('hidden');
            setTimeout(() => map.invalidateSize(), 100);
        } else {
            mapContainer.classList.add('hidden');
        }
    });

    // === 篩選條件相關 ===
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
        checkbox.addEventListener('change', () => {
            filters.peopleCount = Array.from(document.querySelectorAll('input[name="people"]:checked')).map(input => input.value);
            applyFilters();
        });
    });

    // 用途篩選
    document.querySelectorAll('input[name="usage"]').forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            filters.usage = Array.from(document.querySelectorAll('input[name="usage"]:checked')).map(input => input.value);
            applyFilters();
        });
    });

    // 重置篩選
    document.querySelectorAll('.reset-button').forEach(button => {
        button.addEventListener('click', event => {
            const parentClass = event.currentTarget.parentElement.className;
            if (parentClass.includes('price-range')) {
                priceRange.value = 2000;
                filters.maxPrice = 2000;
                maxPriceDisplay.textContent = `$${filters.maxPrice}`;
            } else if (parentClass.includes('distance-range')) {
                distanceRange.value = 10000;
                filters.maxDistance = 10000;
                maxDistanceDisplay.textContent = '10km';
            }
            applyFilters();
        });
    });
    // === 篩選條件相關 END ===
}








