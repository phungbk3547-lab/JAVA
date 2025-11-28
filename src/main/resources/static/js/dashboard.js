// src/main/resources/static/js/script.js
document.addEventListener("DOMContentLoaded", function () {

    // =========================================================
    // 1. SIDEBAR & USER DROPDOWN
    // =========================================================

    const menuToggle   = document.getElementById("menuToggle");      // nút 3 gạch
    const sideNav      = document.querySelector(".side-nav");        // menu trái (home + admin dùng class này)
    const userAvatar   = document.getElementById("userAvatar");      // avatar tròn
    const userDropdown = document.getElementById("userDropdown");    // dropdown user

    // Toggle sidebar (mobile / tablet)
    if (menuToggle && sideNav) {
        menuToggle.addEventListener("click", function (e) {
            e.stopPropagation();
            sideNav.classList.toggle("open");
        });
    }

    // Toggle dropdown user
    if (userAvatar && userDropdown) {
        userAvatar.addEventListener("click", function (e) {
            e.stopPropagation();
            userDropdown.classList.toggle("show");
        });
    }

    // Click ra ngoài thì đóng sidebar + dropdown
    document.addEventListener("click", function (e) {
        if (sideNav && sideNav.classList.contains("open")
            && !sideNav.contains(e.target)
            && !menuToggle.contains(e.target)) {
            sideNav.classList.remove("open");
        }

        if (userDropdown && userDropdown.classList.contains("show")
            && !userAvatar.contains(e.target)
            && !userDropdown.contains(e.target)) {
            userDropdown.classList.remove("show");
        }
    });

    // =========================================================
    // 2. MƯỢT HƠN KHI CUỘN (scroll smooth cho anchor link)
    // =========================================================
    const smoothLinks = document.querySelectorAll('a[href^="#"]');
    smoothLinks.forEach(function (link) {
        link.addEventListener("click", function (e) {
            const targetId = this.getAttribute("href").substring(1);
            const targetEl = document.getElementById(targetId);
            if (targetEl) {
                e.preventDefault();
                targetEl.scrollIntoView({ behavior: "smooth", block: "start" });
            }
        });
    });

    // =========================================================
    // 3. CHART.JS – THỐNG KÊ TIN ĐĂNG (DASHBOARD)
    // =========================================================
    if (typeof Chart !== "undefined") {
        Chart.defaults.font.family = "'Poppins', system-ui, -apple-system, BlinkMacSystemFont, sans-serif";
        Chart.defaults.color = "#6b7280";

        const ctxPost = document.getElementById("postChart");
        if (ctxPost) {
            const ctx = ctxPost.getContext("2d");

            // Gradient pastel: xanh dương nhạt -> trong suốt
            const gradient = ctx.createLinearGradient(0, 0, 0, 260);
            gradient.addColorStop(0, "#63BFF4");            // xanh dương nhạt
            gradient.addColorStop(1, "rgba(99,191,244,0)"); // nhạt dần xuống

            new Chart(ctxPost, {
                type: "bar",
                data: {
                    labels: ["Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11"],
                    datasets: [{
                        label: "Tin đăng mới",
                        data: [3, 4, 1, 2],
                        backgroundColor: gradient,
                        borderColor: "#63BFF4",
                        borderWidth: 1,
                        borderRadius: 10,
                        barThickness: 40,
                        maxBarThickness: 56
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: { display: false },
                        tooltip: {
                            backgroundColor: "#1f2933",
                            titleColor: "#ffffff",
                            bodyColor: "#f9fafb",
                            displayColors: false,
                            padding: 10
                        }
                    },
                    layout: {
                        padding: { top: 18, bottom: 10, left: 10, right: 10 }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            grid: {
                                color: "rgba(148,163,184,0.35)",
                                borderDash: [4, 4]
                            },
                            ticks: {
                                color: "#6b7280",
                                font: { size: 12 }
                            }
                        },
                        x: {
                            grid: { display: false },
                            ticks: {
                                color: "#4b5563",
                                font: { size: 12, weight: "500" }
                            }
                        }
                    }
                }
            });
        }
    }
});
// src/main/resources/static/js/script.js
document.addEventListener("DOMContentLoaded", function () {
    // 3. CHART.JS – THỐNG KÊ TIN ĐĂNG (DASHBOARD)
    // =========================================================
    if (typeof Chart !== "undefined") {
        Chart.defaults.font.family = "'Poppins', system-ui, -apple-system, BlinkMacSystemFont, sans-serif";
        Chart.defaults.color = "#6b7280";

        const ctxPost = document.getElementById("postChart");
        if (ctxPost) {
            const ctx = ctxPost.getContext("2d");

            // Gradient pastel: xanh dương nhạt -> trong suốt
            const gradient = ctx.createLinearGradient(0, 0, 0, 260);
            gradient.addColorStop(0, "#63BFF4");            // xanh dương nhạt
            gradient.addColorStop(1, "rgba(99,191,244,0)"); // nhạt dần xuống

            new Chart(ctxPost, {
                type: "bar",
                data: {
                    labels: ["Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11"],
                    datasets: [{
                        label: "Tin đăng mới",
                        data: [3, 4, 1, 2],
                        backgroundColor: gradient,
                        borderColor: "#63BFF4",
                        borderWidth: 1,
                        borderRadius: 10,
                        barThickness: 40,
                        maxBarThickness: 56
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: { display: false },
                        tooltip: {
                            backgroundColor: "#1f2933",
                            titleColor: "#ffffff",
                            bodyColor: "#f9fafb",
                            displayColors: false,
                            padding: 10
                        }
                    },
                    layout: {
                        padding: { top: 18, bottom: 10, left: 10, right: 10 }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            grid: {
                                color: "rgba(148,163,184,0.35)",
                                borderDash: [4, 4]
                            },
                            ticks: {
                                color: "#6b7280",
                                font: { size: 12 }
                            }
                        },
                        x: {
                            grid: { display: false },
                            ticks: {
                                color: "#4b5563",
                                font: { size: 12, weight: "500" }
                            }
                        }
                    }
                }
            });
        }
    }
});
