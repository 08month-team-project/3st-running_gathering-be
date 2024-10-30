-- 사용자 데이터 삽입
INSERT INTO users (email, name, nickname, gender, user_status, user_role, profile_image_url, local_id, created_at, modified_at,report_count)
VALUES
-- 1월 사용자
('user01@example.com', NULL, 'User01', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-01-01 00:00:00', '2024-01-01 00:00:00',0),
('user02@example.com', NULL, 'User02', 'NONE', 'BANNED', 'USER', NULL, NULL, '2024-01-05 00:00:00', '2024-01-05 00:00:00',4),
('user03@example.com', NULL, 'User03', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-01-10 00:00:00', '2024-01-10 00:00:00',0),
('user04@example.com', NULL, 'User04', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-01-15 00:00:00', '2024-01-15 00:00:00',2),
('user05@example.com', NULL, 'User05', 'NONE', 'DISABLED', 'USER', NULL, NULL, '2024-01-20 00:00:00', '2024-01-20 00:00:00',1),
('user06@example.com', NULL, 'User06', 'NONE', 'DISABLED', 'USER', NULL, NULL, '2024-01-20 00:00:00', '2024-01-20 00:00:00',0),

-- 2월 사용자
('user07@example.com', NULL, 'User07', 'NONE', 'BANNED', 'USER', NULL, NULL, '2024-02-01 00:00:00', '2024-02-01 00:00:00',12),
('user08@example.com', NULL, 'User08', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-02-05 00:00:00', '2024-02-05 00:00:00',2),
('user09@example.com', NULL, 'User09', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-02-10 00:00:00', '2024-02-10 00:00:00',0),
('user10@example.com', NULL, 'User10', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-02-15 00:00:00', '2024-02-15 00:00:00',0),
('user11@example.com', NULL, 'User11', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-02-20 00:00:00', '2024-02-20 00:00:00',0),

-- 3월 사용자
('user12@example.com', NULL, 'User12', 'NONE', 'BANNED', 'USER', NULL, NULL, '2024-03-01 00:00:00', '2024-03-01 00:00:00',5),
('user13@example.com', NULL, 'User13', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-03-05 00:00:00', '2024-03-05 00:00:00',0),
('user14@example.com', NULL, 'User14', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-03-10 00:00:00', '2024-03-10 00:00:00',0),
('user15@example.com', NULL, 'User15', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-03-15 00:00:00', '2024-03-15 00:00:00',1),
('user16@example.com', NULL, 'User16', 'NONE', 'DISABLED', 'USER', NULL, NULL, '2024-03-20 00:00:00', '2024-03-20 00:00:00',0),
('user17@example.com', NULL, 'User17', 'NONE', 'DISABLED', 'USER', NULL, NULL, '2024-03-20 00:00:00', '2024-03-20 00:00:00',0),
('user18@example.com', NULL, 'User18', 'NONE', 'DISABLED', 'USER', NULL, NULL, '2024-03-20 00:00:00', '2024-03-20 00:00:00',0),

-- 4월 사용자
('user19@example.com', NULL, 'User19', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-04-01 00:00:00', '2024-04-01 00:00:00',0),
('user20@example.com', NULL, 'User20', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-04-05 00:00:00', '2024-04-05 00:00:00',0),
('user21@example.com', NULL, 'User21', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-04-10 00:00:00', '2024-04-10 00:00:00',0),
('user22@example.com', NULL, 'User22', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-04-15 00:00:00', '2024-04-15 00:00:00',0),
('user23@example.com', NULL, 'User23', 'NONE', 'DISABLED', 'USER', NULL, NULL, '2024-04-20 00:00:00', '2024-04-20 00:00:00',0),
('user24@example.com', NULL, 'User24', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-04-01 00:00:00', '2024-04-01 00:00:00',0),
('user25@example.com', NULL, 'User25', 'NONE', 'BANNED', 'USER', NULL, NULL, '2024-04-05 00:00:00', '2024-04-05 00:00:00',5),
('user26@example.com', NULL, 'User26', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-04-10 00:00:00', '2024-04-10 00:00:00',0),
('user27@example.com', NULL, 'User27', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-04-15 00:00:00', '2024-04-15 00:00:00',0),
('user28@example.com', NULL, 'User28', 'NONE', 'DISABLED', 'USER', NULL, NULL, '2024-04-20 00:00:00', '2024-04-20 00:00:00',0),

-- 5월 사용자
('user29@example.com', NULL, 'User29', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-05-01 00:00:00', '2024-05-01 00:00:00',0),
('user30@example.com', NULL, 'User30', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-05-05 00:00:00', '2024-05-05 00:00:00',5),
('user31@example.com', NULL, 'User31', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-05-10 00:00:00', '2024-05-10 00:00:00',0),
('user32@example.com', NULL, 'User32', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-05-15 00:00:00', '2024-05-15 00:00:00',0),
('user33@example.com', NULL, 'User33', 'NONE', 'DISABLED', 'USER', NULL, NULL, '2024-05-20 00:00:00', '2024-05-20 00:00:00',0),
('user34@example.com', NULL, 'User34', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-05-01 00:00:00', '2024-05-01 00:00:00',0),
('user35@example.com', NULL, 'User35', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-05-05 00:00:00', '2024-05-05 00:00:00',0),
('user36@example.com', NULL, 'User36', 'NONE', 'BANNED', 'USER', NULL, NULL, '2024-05-10 00:00:00', '2024-05-10 00:00:00',36),
('user37@example.com', NULL, 'User37', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-05-15 00:00:00', '2024-05-15 00:00:00',0),
('user38@example.com', NULL, 'User38', 'NONE', 'DISABLED', 'USER', NULL, NULL, '2024-05-20 00:00:00', '2024-05-20 00:00:00',0),

-- 6월 사용자
('user39@example.com', NULL, 'User39', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-06-01 00:00:00', '2024-06-01 00:00:00',1),
('user40@example.com', NULL, 'User40', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-06-05 00:00:00', '2024-06-05 00:00:00',4),
('user41@example.com', NULL, 'User41', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-06-10 00:00:00', '2024-06-10 00:00:00',0),
('user42@example.com', NULL, 'User42', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-06-15 00:00:00', '2024-06-15 00:00:00',0),
('user43@example.com', NULL, 'User43', 'NONE', 'DISABLED', 'USER', NULL, NULL, '2024-06-20 00:00:00', '2024-06-20 00:00:00',0),
('user44@example.com', NULL, 'User44', 'NONE', 'BANNED', 'USER', NULL, NULL, '2024-06-01 00:00:00', '2024-06-01 00:00:00',5),
('user45@example.com', NULL, 'User45', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-06-05 00:00:00', '2024-06-05 00:00:00',0),
('user46@example.com', NULL, 'User46', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-06-10 00:00:00', '2024-06-10 00:00:00',0),
('user47@example.com', NULL, 'User47', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-06-15 00:00:00', '2024-06-15 00:00:00',2),
('user48@example.com', NULL, 'User48', 'NONE', 'DISABLED', 'USER', NULL, NULL, '2024-06-20 00:00:00', '2024-06-20 00:00:00',0),

-- 7월 사용자
('user49@example.com', NULL, 'User49', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-07-01 00:00:00', '2024-07-01 00:00:00',0),
('user50@example.com', NULL, 'User50', 'NONE', 'BANNED', 'USER', NULL, NULL, '2024-07-05 00:00:00', '2024-07-05 00:00:00',5),
('user51@example.com', NULL, 'User51', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-07-10 00:00:00', '2024-07-10 00:00:00',0),
('user52@example.com', NULL, 'User52', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-07-15 00:00:00', '2024-07-15 00:00:00',0),
('user53@example.com', NULL, 'User53', 'NONE', 'DISABLED', 'USER', NULL, NULL, '2024-07-20 00:00:00', '2024-07-20 00:00:00',0),

-- 8월 사용자
('user54@example.com', NULL, 'User54', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-08-01 00:00:00', '2024-08-01 00:00:00',0),
('user55@example.com', NULL, 'User55', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-08-05 00:00:00', '2024-08-05 00:00:00',0),
('user56@example.com', NULL, 'User56', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-08-10 00:00:00', '2024-08-10 00:00:00',0),
('user57@example.com', NULL, 'User57', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-08-15 00:00:00', '2024-08-15 00:00:00',2),
('user58@example.com', NULL, 'User58', 'NONE', 'DISABLED', 'USER', NULL, NULL, '2024-08-20 00:00:00', '2024-08-20 00:00:00',1),

-- 9월 사용자
('user59@example.com', NULL, 'User59', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-09-01 00:00:00', '2024-09-01 00:00:00',0),
('user60@example.com', NULL, 'User60', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-09-05 00:00:00', '2024-09-05 00:00:00',0),
('user61@example.com', NULL, 'User61', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-09-10 00:00:00', '2024-09-10 00:00:00',0),
('user62@example.com', NULL, 'User62', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-09-15 00:00:00', '2024-09-15 00:00:00',0),
('user63@example.com', NULL, 'User63', 'NONE', 'ACTIVE', 'USER', NULL, NULL, '2024-09-20 00:00:00', '2024-09-20 00:00:00',0);

INSERT INTO black_list (user_id, reason, black_count ,created_at, modified_at,expires_at)
VALUES
(2,'불법정보개시',4,'2024-09-01 00:00:00','2024-09-01 00:00:00','2024-09-04 00:00:00'),
(7,'욕설/인신공격',12,'2024-09-01 00:00:00','2024-09-01 00:00:00','2024-09-04 00:00:00'),
(12,'음란성/선정성',5,'2024-09-01 00:00:00','2024-09-01 00:00:00','2024-09-04 00:00:00'),
(25,'영리목적/홍보성',5,'2024-09-01 00:00:00','2024-09-01 00:00:00','2024-09-04 00:00:00'),
(36,'개인정보노출',36,'2024-09-01 00:00:00','2024-09-01 00:00:00','2024-09-04 00:00:00'),
(44,'같은내용반복게시',5,'2024-09-01 00:00:00','2024-09-01 00:00:00','2024-09-04 00:00:00'),
(50,'잦은노쇼',5,'2024-09-01 00:00:00','2024-09-01 00:00:00','2024-09-04 00:00:00');