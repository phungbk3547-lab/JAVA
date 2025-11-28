document.addEventListener("DOMContentLoaded", function() {

    // =========================================================
    // 1. XỬ LÝ MENU SIDEBAR & DROPDOWN (Toggle Logic)
    // =========================================================

    const menuToggle = document.getElementById('menuToggle');
    const sideMenu = document.getElementById('sideMenu');
    const userAvatar = document.getElementById('userAvatar');
    const userDropdown = document.getElementById('userDropdown');

    // --- Xử lý click nút 3 gạch (Mở/Đóng Sidebar) ---
    if (menuToggle && sideMenu) {
        menuToggle.addEventListener('click', function(e) {
            e.stopPropagation(); // Ngăn chặn sự kiện click lan ra ngoài
            sideMenu.classList.toggle('open');
        });
    }

    // --- Xử lý click Avatar (Mở/Đóng Dropdown User) ---
    if (userAvatar && userDropdown) {
        userAvatar.addEventListener('click', function(e) {
            e.stopPropagation();
            userDropdown.classList.toggle('show');
        });
    }

    // --- Xử lý click ra ngoài (Đóng Menu/Dropdown) ---
    document.addEventListener('click', function(e) {
        // Đóng Sidebar nếu click ra ngoài vùng menu và nút toggle
        if (sideMenu && sideMenu.classList.contains('open') &&
            !sideMenu.contains(e.target) && !menuToggle.contains(e.target)) {
            sideMenu.classList.remove('open');
        }

        // Đóng Dropdown nếu click ra ngoài vùng avatar và dropdown
        if (userDropdown && userDropdown.classList.contains('show') &&
            !userAvatar.contains(e.target) && !userDropdown.contains(e.target)) {
            userDropdown.classList.remove('show');
        }
    });

    // =========================================================
    // 2. CẤU HÌNH BIỂU ĐỒ (CHART.JS)
    // =========================================================

    // Kiểm tra xem thư viện Chart.js đã tải chưa
    if (typeof Chart !== 'undefined') {

        // Cấu hình Font chữ chung
        Chart.defaults.font.family = "'Poppins', sans-serif";
        Chart.defaults.color = '#666';

        // --- A. BIỂU ĐỒ CỘT: THỐNG KÊ TIN ĐĂNG (postChart) ---
                const ctxPost = document.getElementById('postChart');
                if (ctxPost) {
                    // TẠO MÀU MỚI: Gradient từ Xám đậm sang Xám nhạt pha xanh
                    const gradient = ctxPost.getContext('2d').createLinearGradient(0, 0, 0, 400);
                    gradient.addColorStop(0, '#696969');       // DimGray (Màu chủ đạo của bạn)
                    gradient.addColorStop(1, 'rgba(105, 105, 105, 0.2)'); // Nhạt dần xuống dưới

                    new Chart(ctxPost, {
                        type: 'bar',
                        data: {
                            labels: ['Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11'],
                            datasets: [{
                                label: 'Tin đăng mới',
                                data: [3, 4, 1, 2],
                                backgroundColor: gradient, // Dùng màu Gradient Xám
                                borderColor: '#303030',    // Viền đen xám
                                borderWidth: 1,
                                borderRadius: 4,
                                barThickness: 30,          // Cột mảnh hơn chút cho tinh tế
                                maxBarThickness: 50
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false, // Bắt buộc false để fill theo CSS
                            plugins: {
                                legend: { display: false }
                            },
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    grid: { borderDash: [2, 4], color: '#f0f0f0' },
                                    ticks: { color: '#888' }
                                },
                                x: {
                                    grid: { display: false },
                                    ticks: { color: '#303030', font: { weight: '500' } }
                                }
                            },
                            layout: {
                                padding: { top: 20, bottom: 10, left: 10, right: 10 }
                            }
                        }
                    });
                }
    }
};