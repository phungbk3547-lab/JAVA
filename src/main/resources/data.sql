/* ============================================================
   BƯỚC 1: RESET VÀ TẠO MỚI DATABASE EVTrading
   ============================================================ */
USE master;
GO

IF EXISTS (SELECT * FROM sys.databases WHERE name = 'EVTrading')
BEGIN
    ALTER DATABASE EVTrading SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE EVTrading;
END
GO

-- Tạo mới với Collation tiếng Việt
CREATE DATABASE EVTrading COLLATE Vietnamese_CI_AS;
GO

USE EVTrading;
GO

/* ============================================================
   BƯỚC 2: BẢNG NGƯỜI DÙNG (USERS) + DỮ LIỆU MẪU
   ============================================================ */
CREATE TABLE users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name      NVARCHAR(100) COLLATE Vietnamese_CI_AS NOT NULL,
    email     NVARCHAR(100) COLLATE Vietnamese_CI_AS UNIQUE NOT NULL,
    password  NVARCHAR(255) COLLATE Vietnamese_CI_AS NOT NULL,
    role      NVARCHAR(30)  COLLATE Vietnamese_CI_AS NOT NULL
              CONSTRAINT DF_users_role DEFAULT N'ROLE_USER',
    phone     NVARCHAR(20)  COLLATE Vietnamese_CI_AS
);
GO

/*
   ⚠️ MẬT KHẨU ĐÃ ĐƯỢC MÃ HÓA BCRYPT (BCryptPasswordEncoder)
   - admin1@ev.com : admin123
   - các user còn lại: 123456
*/

INSERT INTO users (name, email, password, role, phone) VALUES
(N'Admin',
 N'admin1@ev.com',
 N'$2b$10$zgOmU3fdn9x/rJoT3pXDhe3HKoMKPortTnkFG5SJAiH8EEIccbezK', -- admin123 (BCrypt)
 N'ADMIN',
 N'0901000001'),

    -- 1–50 USER — MK: 123456
    (N'Nguyễn Minh An', 'minhan.nguyen@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000001'),
    (N'Trần Gia Huy', 'giahuy.tran@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000002'),
    (N'Lê Ngọc Trân', 'ngoctran.le@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000003'),
    (N'Phạm Quang Khải', 'quangkhai.pham@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000004'),
    (N'Đỗ Nhật Minh', 'nhatminh.do@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000005'),
    (N'Võ Anh Kiệt', 'anhkiet.vo@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000006'),
    (N'Bùi Thảo My', 'thaomy.bui@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000007'),
    (N'Hoàng Khánh Vy', 'khanhvy.hoang@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000008'),
    (N'Trịnh Minh Khang', 'minhkhang.trinh@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000009'),
    (N'Ngô Thảo Nhi', 'thaonhi.ngo@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000010'),
    (N'Vũ Đức Hoàng', 'duchoang.vu@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000011'),
    (N'Nguyễn Thục Anh', 'thucanh.nguyen@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000012'),
    (N'Phan Bảo Long', 'baolong.phan@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000013'),
    (N'Đinh Trọng Nghĩa', 'trongnghia.dinh@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000014'),
    (N'Nguyễn Mai Chi', 'maichi.nguyen@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000015'),
    (N'Phạm Kim Ngân', 'kimngan.pham@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000016'),
    (N'Trần Bích Hạnh', 'bichhanh.tran@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000017'),
    (N'Lý Anh Dũng', 'anhdung.ly@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000018'),
    (N'Chu Hoài Nam', 'hoainam.chu@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000019'),
    (N'Võ Tường Vi', 'tuongvi.vo@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000020'),

    -- USERS 21 → 50
    (N'Lê Quốc Bảo', 'quocbao.le@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000021'),
    (N'Nguyễn Hải Yến', 'haiyen.nguyen@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000022'),
    (N'Hoàng Tuấn Kiệt', 'tuankiet.hoang@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000023'),
    (N'Trần Hồng Nhung', 'hongnhung.tran@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000024'),
    (N'Phạm Đức Lộc', 'ducloc.pham@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000025'),
    (N'Huỳnh Gia Linh', 'gialinh.huynh@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000026'),
    (N'Nguyễn Tấn Đạt', 'tandat.nguyen@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000027'),
    (N'Đặng Thanh Tùng', 'thanhtung.dang@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa', 'ROLE_USER', '0911000028'),
    (N'Cao Bảo Ngọc', 'baongoc.cao@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000029'),
    (N'Hồ Ngọc Vy', 'ngocvy.ho@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000030'),
    (N'Ngô Quang Vinh', 'quangvinh.ngo@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000031'),
    (N'Bùi Xuân Trường', 'xuantruong.bui@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000032'),
    (N'Lâm Diệu My', 'dieumy.lam@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000033'),
    (N'Phạm Duy Tân', 'duytan.pham@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000034'),
    (N'Lê Hoàng Yến', 'hoangyen.le@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000035'),
    (N'Vũ Ngọc Khánh', 'ngockhanh.vu@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000036'),
    (N'Đoàn Đức Anh', 'ducanh.doan@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000037'),
    (N'Hoàng Mỹ Tiên', 'mytien.hoang@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000038'),
    (N'Trần Gia Bảo', 'giabao.tran@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000039'),
    (N'Nguyễn Hữu Phúc', 'huuphucc.nguyen@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000040'),
    (N'Lê Gia Hân', 'giahan.le@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000041'),
    (N'Phùng Minh Hiếu', 'minhhieu.phung@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000042'),
    (N'Đặng Thảo Linh', 'thaolinh.dang@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000043'),
    (N'Cao Anh Thư', 'anhthu.cao@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000044'),
    (N'Đỗ Quốc Hưng', 'quochung.do@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000045'),
    (N'Nguyễn Thanh Hà', 'thanhha.nguyen@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubUoil58x2oynZMhUlXa','ROLE_USER','0911000046'),
    (N'Lý Phương Anh', 'phuonganh.ly@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubiol58x2oynZMhUlXa','ROLE_USER','0911000047'),
    (N'Đỗ Tấn Phát', 'tanphat.do@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubiol58x2oynZMhUlXa','ROLE_USER','0911000048'),
    (N'Trương Hoàng Lam', 'hoanglam.truong@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubiol58x2oynZMhUlXa','ROLE_USER','0911000049'),
    (N'Bùi Phương Mai', 'phuongmai.bui@ev.com',
     '$2a$10$7QvA9GvOYbKxOAsrV7l.yO8jQpYezrWzxubiol58x2oynZMhUlXa','ROLE_USER','0911000050');
GO

/* ============================================================
   BƯỚC 3: BẢNG TIN ĐĂNG (POSTS) + DATA MẪU
   ============================================================ */
CREATE TABLE posts (
    id INT IDENTITY(1,1) PRIMARY KEY,
    title       NVARCHAR(255) COLLATE Vietnamese_CI_AS,
    description NVARCHAR(MAX) COLLATE Vietnamese_CI_AS,
    price       DECIMAL(18,2),
    location    NVARCHAR(100) COLLATE Vietnamese_CI_AS,
    type        NVARCHAR(50)  COLLATE Vietnamese_CI_AS,
    post_date   DATETIME DEFAULT GETDATE(),
    user_id     INT NOT NULL,
    image_path  NVARCHAR(255) COLLATE Vietnamese_CI_AS NULL,
    status      NVARCHAR(50)  COLLATE Vietnamese_CI_AS DEFAULT N'APPROVED',

    CONSTRAINT FK_posts_users FOREIGN KEY (user_id) REFERENCES users(id)
);
GO

INSERT INTO posts (title, description, price, location, type, user_id, image_path, status) VALUES
(N'Xe điện VinFast VF e34 cũ',
 N'Xe gia đình sử dụng, odo ~10.000 km, pin thuê theo tháng, bảo dưỡng định kỳ.',
 350000000, N'Hồ Chí Minh', N'EV', 1,  N'/images/vfe34.jpg', N'APPROVED'),

(N'Xe điện VinFast VF 8 chạy lướt',
 N'Nội thất mới, bảo hành 3 năm.',
 950000000, N'Hà Nội', N'EV', 2,  N'/images/vf8.jpg', N'APPROVED'),

(N'Xe điện Tesla Model 3 2022 nhập Mỹ',
 N'Hỗ trợ sạc nhanh Supercharger.',
 1200000000, N'Đà Nẵng', N'EV', 3,  N'/images/tesla_model3.jpg', N'APPROVED'),

(N'Xe máy điện VinFast Klara S 2023',
 N'Đi được 80km mỗi lần sạc.',
 12000000, N'Cần Thơ', N'EV', 4,  N'/images/klara_s.jpg', N'APPROVED'),

(N'Xe điện Mini Cooper SE 2022',
 N'Màu vàng nổi bật.',
 900000000, N'Hải Phòng', N'EV', 5,  N'/images/mini_se.jpg', N'APPROVED'),

(N'VinFast VF 9 cao cấp - giao ngay',
 N'Xe trưng bày showroom.',
 1500000000, N'Bình Dương', N'EV', 6,  N'/images/vf9.jpg', N'APPROVED'),

(N'VinFast VF 5 Plus đã qua sử dụng',
 N'Xe đi 5.000 km.',
 480000000, N'Đồng Nai', N'EV', 7,  N'/images/vf5.jpg', N'APPROVED'),

(N'Kia EV6 bản GT-Line',
 N'Màn hình cong, công nghệ hỗ trợ lái.',
 1350000000, N'Khánh Hòa', N'EV', 8,  N'/images/kia_ev6.jpg', N'APPROVED'),

(N'Hyundai Ioniq 5 2022',
 N'Xe gia đình sử dụng kỹ.',
 1050000000, N'Lâm Đồng', N'EV', 9,  N'/images/ioniq5.jpg', N'APPROVED'),

(N'MG4 Electric mới 95%',
 N'Odo 3.000 km.',
 650000000, N'An Giang', N'EV', 10, N'/images/mg4.jpg', N'APPROVED'),

(N'BYD Atto 3',
 N'Nội thất hiện đại.',
 780000000, N'Kiên Giang', N'EV', 11, N'/images/byd_atto3.jpg', N'APPROVED'),

(N'VinFast Feliz S',
 N'Xe đẹp 95%.',
 10000000, N'Nghệ An', N'EV', 12, N'/images/feliz_s.jpg', N'APPROVED'),

(N'VinFast VF 6 mới nhận xe',
 N'Xe chưa dùng nhiều.',
 780000000, N'Thanh Hóa', N'EV', 13, N'/images/vf6.jpg', N'APPROVED'),

(N'VinFast VF 8 Plus - chủ nữ',
 N'Nội thất nâu sạch đẹp.',
 980000000, N'Thừa Thiên Huế', N'EV', 14, N'/images/vf8_plus.jpg', N'APPROVED'),

(N'Tesla Model Y Long Range',
 N'Khoang hành lý rộng.',
 1650000000, N'Quảng Ninh', N'EV', 15, N'/images/tesla_modely.jpg', N'APPROVED'),

-- PIN
(N'Pin VinFast 42kWh',
 N'Dung lượng tốt.',
 30000000, N'Hồ Chí Minh', N'PIN', 16, N'/images/pin_vinfast.jpg', N'APPROVED'),

(N'Pin lithium-ion 60Ah',
 N'Còn ~85%.',
 5000000, N'Hà Nội', N'PIN', 17, N'/images/pin_60ah.jpg', N'APPROVED'),

(N'Pin Tesla 75kWh',
 N'Cell đồng đều.',
 150000000, N'Đồng Nai', N'PIN', 18, N'/images/pin_tesla.jpg', N'APPROVED'),

(N'Pin LiFePO4 100Ah',
 N'Hỗ trợ sạc nhanh.',
 10000000, N'Bình Dương', N'PIN', 19, N'/images/pin_lifepo4.jpg', N'APPROVED'),

(N'Pin Feliz chính hãng',
 N'Dùng cho Feliz.',
 7000000, N'Cần Thơ', N'PIN', 20, N'/images/pin_feliz.jpg', N'APPROVED');
GO

/* ============================================================
   BƯỚC 4: BẢNG BÁO CÁO (COMPLAINTS) + DATA
   ============================================================ */
CREATE TABLE complaints (
    id INT IDENTITY(1,1) PRIMARY KEY,
    created_at DATETIME DEFAULT GETDATE(),
    reason     NVARCHAR(MAX) COLLATE Vietnamese_CI_AS,
    status     NVARCHAR(50)  COLLATE Vietnamese_CI_AS DEFAULT N'PENDING',
    post_id    INT NOT NULL,
    user_id    INT NOT NULL,

    CONSTRAINT FK_complaints_posts FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    CONSTRAINT FK_complaints_users FOREIGN KEY (user_id) REFERENCES users(id)
);
GO

INSERT INTO complaints (created_at, reason, status, post_id, user_id) VALUES
('2025-08-13 19:48:15', N'Giá xe thấp bất thường.', N'RESOLVED', 1, 5),
('2025-08-24 20:10:00', N'Mâu thuẫn thông tin người bán.', N'PENDING', 2, 7),
('2025-09-10 14:22:30', N'Yêu cầu đặt cọc bất thường.', N'RESOLVED', 3, 10),
('2025-09-05 19:48:15', N'Hình xe giống ảnh mạng.', N'PENDING', 5, 2),
('2025-09-17 20:15:30', N'Odo không đúng thực tế.', N'PENDING', 7, 12),
('2025-10-01 09:05:00', N'Không rõ phí sang tên.', N'RESOLVED', 8, 4),
('2025-10-21 19:48:15', N'Dung lượng pin sai mô tả.', N'PENDING', 16, 3),
('2025-11-05 11:30:10', N'Không có giấy tờ nguồn gốc.', N'PENDING', 18, 9),
('2025-11-12 16:40:25', N'Xe từng va chạm.', N'RESOLVED', 10, 6),
('2025-11-25 20:20:45', N'Nội dung trùng lặp.', N'PENDING', 20, 13);
GO

/* ============================================================
   BƯỚC 5: BẢNG TOKEN QUÊN MẬT KHẨU
   ============================================================ */
CREATE TABLE password_reset_token (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    token NVARCHAR(255) COLLATE Vietnamese_CI_AS NOT NULL UNIQUE,
    email NVARCHAR(100) COLLATE Vietnamese_CI_AS NOT NULL,
    expiry_date DATETIME2 NOT NULL,

    CONSTRAINT FK_passwordreset_users
        FOREIGN KEY (email) REFERENCES users(email)
);
GO

/* ============================================================
   BƯỚC 6: KIỂM TRA DỮ LIỆU
   ============================================================ */
SELECT * FROM users;
SELECT * FROM posts;
SELECT * FROM complaints;
SELECT * FROM password_reset_token;
GO
