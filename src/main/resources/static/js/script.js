// script.js – EV Trading UI interactions

document.addEventListener("DOMContentLoaded", function () {
    initSideMenu();
    initUserDropdown();
    initDragScroll(".table-responsive");       // bảng kiểm duyệt
    initDragScroll(".user-list-container");    // list user trong dashboard
    initDragScroll(".complaint-card .table-responsive"); // bảng khiếu nại
    initRevealOnScroll();
    initSmoothAnchorScroll();
});

/**
 * Mở / đóng side menu (mobile / tablet)
 */
function initSideMenu() {
    const menuToggle = document.getElementById("menuToggle");
    const sideMenu = document.getElementById("sideMenu");

    if (!menuToggle || !sideMenu) return;

    menuToggle.addEventListener("click", function (e) {
        e.stopPropagation();
        sideMenu.classList.toggle("open");
    });

    // Click ngoài menu -> đóng
    document.addEventListener("click", function (e) {
        if (!sideMenu.classList.contains("open")) return;
        const clickInsideMenu = sideMenu.contains(e.target);
        const clickOnBtn = menuToggle.contains(e.target);
        if (!clickInsideMenu && !clickOnBtn) {
            sideMenu.classList.remove("open");
        }
    });

    // ESC để đóng
    document.addEventListener("keydown", function (e) {
        if (e.key === "Escape") {
            sideMenu.classList.remove("open");
        }
    });
}

/**
 * Dropdown avatar user ở góc phải header
 */
function initUserDropdown() {
    const userAvatar = document.getElementById("userAvatar");
    const userDropdown = document.getElementById("userDropdown");

    if (!userAvatar || !userDropdown) return;

    userAvatar.addEventListener("click", function (e) {
        e.stopPropagation();
        userDropdown.classList.toggle("show");
    });

    document.addEventListener("click", function (e) {
        const clickInsideDropdown = userDropdown.contains(e.target);
        const clickOnAvatar = userAvatar.contains(e.target);
        if (!clickInsideDropdown && !clickOnAvatar) {
            userDropdown.classList.remove("show");
        }
    });

    document.addEventListener("keydown", function (e) {
        if (e.key === "Escape") {
            userDropdown.classList.remove("show");
        }
    });
}

/**
 * Drag to scroll – kéo chuột để cuộn ngang (giống kéo list trên mobile)
 * selector: chuỗi CSS, ví dụ ".table-responsive"
 */
function initDragScroll(selector) {
    const containers = document.querySelectorAll(selector);
    if (!containers.length) return;

    containers.forEach((container) => {
        let isDown = false;
        let startX;
        let scrollLeft;

        container.classList.add("drag-scroll");

        container.addEventListener("mousedown", (e) => {
            // chỉ kích hoạt nếu có thanh cuộn ngang
            if (container.scrollWidth <= container.clientWidth) return;

            isDown = true;
            container.classList.add("dragging");
            startX = e.pageX - container.offsetLeft;
            scrollLeft = container.scrollLeft;
        });

        container.addEventListener("mouseleave", () => {
            isDown = false;
            container.classList.remove("dragging");
        });

        container.addEventListener("mouseup", () => {
            isDown = false;
            container.classList.remove("dragging");
        });

        container.addEventListener("mousemove", (e) => {
            if (!isDown) return;
            e.preventDefault();
            const x = e.pageX - container.offsetLeft;
            const walk = (x - startX) * 1.2; // tốc độ kéo
            container.scrollLeft = scrollLeft - walk;
        });
    });
}

/**
 * Reveal on scroll – card hiện dần khi cuộn tới
 */
function initRevealOnScroll() {
    const revealTargets = document.querySelectorAll(
        ".stat-card, .chart-card, .complaint-card, .post-card, .profile-card"
    );

    if (!revealTargets.length || !("IntersectionObserver" in window)) {
        return;
    }

    revealTargets.forEach((el) => el.classList.add("reveal-init"));

    const observer = new IntersectionObserver(
        (entries, obs) => {
            entries.forEach((entry) => {
                if (entry.isIntersecting) {
                    entry.target.classList.add("reveal-in");
                    obs.unobserve(entry.target);
                }
            });
        },
        {
            threshold: 0.12,
        }
    );

    revealTargets.forEach((el) => observer.observe(el));
}

/**
 * Smooth scroll khi click anchor dạng #id
 * (Không bắt buộc, nhưng cho cảm giác mượt hơn khi có link nội trang)
 */
function initSmoothAnchorScroll() {
    const links = document.querySelectorAll('a[href^="#"]');
    if (!links.length) return;

    links.forEach((link) => {
        link.addEventListener("click", function (e) {
            const targetId = this.getAttribute("href").substring(1);
            const target = document.getElementById(targetId);
            if (target) {
                e.preventDefault();
                target.scrollIntoView({ behavior: "smooth", block: "start" });
            }
        });
    });
}
// src/main/resources/static/js/script.js

document.addEventListener("DOMContentLoaded", () => {
    const userAvatar = document.getElementById("userAvatar");
    const userDropdown = document.getElementById("userDropdown");
    const menuToggle = document.getElementById("menuToggle");
    const sideNav = document.querySelector(".side-nav");

    // Avatar -> dropdown login/register / profile
    if (userAvatar && userDropdown) {
        userAvatar.addEventListener("click", (e) => {
            e.stopPropagation();
            userDropdown.classList.toggle("show");
        });

        document.addEventListener("click", (e) => {
            if (!userDropdown.contains(e.target)) {
                userDropdown.classList.remove("show");
            }
        });
    }

    // Nút menu trên mobile
    if (menuToggle && sideNav) {
        menuToggle.addEventListener("click", (e) => {
            e.stopPropagation();
            sideNav.classList.toggle("open");
        });

        document.addEventListener("click", (e) => {
            if (!sideNav.contains(e.target) && !menuToggle.contains(e.target)) {
                sideNav.classList.remove("open");
            }
        });
    }

    // Nhỏ nhỏ: hiệu ứng click cho mọi .side-link
    document.querySelectorAll(".side-link").forEach(link => {
        link.addEventListener("mousedown", () => {
            link.style.transform = "scale(0.97)";
        });
        link.addEventListener("mouseup", () => {
            link.style.transform = "";
        });
        link.addEventListener("mouseleave", () => {
            link.style.transform = "";
        });
    });
});
// static/js/script.js – EV Trading UI interactions

document.addEventListener("DOMContentLoaded", function () {
    initSideMenu();
    initUserDropdown();
    initDragScroll(".table-responsive");
    initDragScroll(".user-list-container");
    initRevealOnScroll();
    initSmoothAnchorScroll();
    initButtonEffects();
});

/**
 * 1. Dropdown Avatar User (Header)
 * Logic: Click avatar -> toggle class 'show' để hiện menu
 */
function initUserDropdown() {
    const userAvatar = document.getElementById("userAvatar");
    const userDropdown = document.getElementById("userDropdown");

    if (!userAvatar || !userDropdown) return;

    userAvatar.addEventListener("click", function (e) {
        e.stopPropagation();
        userDropdown.classList.toggle("show");
    });

    // Click ra ngoài thì đóng
    document.addEventListener("click", function (e) {
        if (!userAvatar.contains(e.target) && !userDropdown.contains(e.target)) {
            userDropdown.classList.remove("show");
        }
    });
}

/**
 * 2. Mở / Đóng Side Menu (Mobile)
 */
function initSideMenu() {
    const menuToggle = document.getElementById("menuToggle");
    const sideMenu = document.querySelector(".side-nav"); // Tìm theo class

    if (!menuToggle || !sideMenu) return;

    menuToggle.addEventListener("click", function (e) {
        e.stopPropagation();
        sideMenu.classList.toggle("open");
    });

    document.addEventListener("click", function (e) {
        if (sideMenu.classList.contains("open") && !sideMenu.contains(e.target) && !menuToggle.contains(e.target)) {
            sideMenu.classList.remove("open");
        }
    });
}

// Các hàm phụ trợ giữ nguyên
function initButtonEffects() {
    document.querySelectorAll(".side-link").forEach(link => {
        link.addEventListener("mousedown", () => link.style.transform = "scale(0.97)");
        link.addEventListener("mouseup", () => link.style.transform = "");
        link.addEventListener("mouseleave", () => link.style.transform = "");
    });
}

function initDragScroll(selector) {
    const containers = document.querySelectorAll(selector);
    containers.forEach((container) => {
        let isDown = false, startX, scrollLeft;
        container.classList.add("drag-scroll");
        container.addEventListener("mousedown", (e) => {
            if (container.scrollWidth <= container.clientWidth) return;
            isDown = true;
            container.classList.add("dragging");
            startX = e.pageX - container.offsetLeft;
            scrollLeft = container.scrollLeft;
        });
        container.addEventListener("mouseleave", () => { isDown = false; container.classList.remove("dragging"); });
        container.addEventListener("mouseup", () => { isDown = false; container.classList.remove("dragging"); });
        container.addEventListener("mousemove", (e) => {
            if (!isDown) return;
            e.preventDefault();
            const x = e.pageX - container.offsetLeft;
            const walk = (x - startX) * 1.5;
            container.scrollLeft = scrollLeft - walk;
        });
    });
}

function initRevealOnScroll() {
    const targets = document.querySelectorAll(".stat-card, .chart-card, .complaint-card, .post-card, .profile-card");
    if (!targets.length || !("IntersectionObserver" in window)) return;
    targets.forEach((el) => el.classList.add("reveal-init"));
    const observer = new IntersectionObserver((entries, obs) => {
        entries.forEach((entry) => {
            if (entry.isIntersecting) {
                entry.target.classList.add("reveal-in");
                obs.unobserve(entry.target);
            }
        });
    }, { threshold: 0.1 });
    targets.forEach((el) => observer.observe(el));
}

function initSmoothAnchorScroll() {
    document.querySelectorAll('a[href^="#"]').forEach((link) => {
        link.addEventListener("click", function (e) {
            const id = this.getAttribute("href").substring(1);
            const target = document.getElementById(id);
            if (target) {
                e.preventDefault();
                target.scrollIntoView({ behavior: "smooth", block: "start" });
            }
        });
    });
}