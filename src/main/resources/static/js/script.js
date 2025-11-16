// ==== EV Trading Navigation Control ====

document.addEventListener("DOMContentLoaded", function () {
    const menuBtn = document.getElementById("menuToggle");
    const sideMenu = document.getElementById("sideMenu");
    const userAvatar = document.getElementById("userAvatar");
    const userDropdown = document.getElementById("userDropdown");

    // --- Toggle side menu ---
    if (menuBtn && sideMenu) {
        menuBtn.addEventListener("click", function (event) {
            event.stopPropagation(); // tránh click lan ra document
            sideMenu.classList.toggle("open");
        });
    }

    // --- Toggle user dropdown ---
    if (userAvatar && userDropdown) {
        userAvatar.addEventListener("click", function (event) {
            event.stopPropagation();
            userDropdown.classList.toggle("show");
        });
    }

    // --- Đóng dropdown khi click ra ngoài ---
    document.addEventListener("click", function (e) {
        if (userDropdown && !userDropdown.contains(e.target) && e.target !== userAvatar) {
            userDropdown.classList.remove("show");
        }
    });

    // --- Đóng side menu khi click ra ngoài ---
    document.addEventListener("click", function (e) {
        if (sideMenu && sideMenu.classList.contains("open")) {
            if (!sideMenu.contains(e.target) && e.target !== menuBtn) {
                sideMenu.classList.remove("open");
            }
        }
    });
});
