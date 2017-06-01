# ProjectAndroid
Môi trường phát triển ứng dụng
-	Hệ điều hành : Android 5.0
-	Hệ quản trị cơ sở dữ liệu: Firebase Realtime Database (NoSQL Cloud Database)
-	Công cụ dùng để phân tích, thiết kế: lucidchart.com, creately.com
-	Công cụ đã dùng để xây dựng ứng dụng : Android Studio
-	Các thư viện đã sử dụng : Firebase, google API,…
Môi trường triển khai ứng dụng:
-	Hệ điều hành: Android 4.4 kitkat trở lên
-	Cần mạng hoặc wifi trong quá trình sử dụng để kết nối với cloud database.
	Kết quả đạt được
Các chức năng đã phân tích, thiết kế
-	Đăng ký bằng email, password hoặc bằng tài khoản Google cho Người muốn quyên góp.
-	Đăng nhập bằng email, password đã đăng ký hoặc đăng nhập bằng tài khoản Google
-	Kiểm tra các lỗi phát sinh khi đăng nhập, đăng ký.
-	Từ tài khoản đăng nhập, xác định đối tượng người dùng là Tổ chức từ thiện hay Người muốn quyên góp và load các chức năng và thông tin tương ứng.

-	Tổ chức từ thiện: 
o	Xem danh sách hoạt động của tổ chức từ thiện
o	Tạo các hoạt động quyên góp.
o	Chỉnh sửa thông tin hoạt động
o	Xem lịch sử quyên góp cho các hoạt động
o	Chấp nhận/từ chối món đồ của người tặng.
o	Thay đổi cập nhật trạng thái nhận đồ quyên góp từ người tặng.
o	Thay đổi thông tin tổ chức.
o	Nhận thông báo khi có người quyên góp đến tổ chức.

-	Người muốn quyên góp
o	Xem danh sách các tổ chức tình nguyện.
o	Xem danh sách các hoạt động tình nguyện
o	Xem các thông tinhoạt động.
o	Gửi yêu cầu quyên góp đến các tổ chức từ thiện.
o	Thay đổi thông tin đợt quyên góp.
o	Xem lịch sử quyên góp của cá nhân.
o	Thay đổi thông tin cá nhân.
o	Chức năng chấm điểm cho các nhà từ thiện (người tham gia tình nguyện và người tặng được phép chấm điểm), điểm càng cao thì nhà từ thiện đó được ưu tiên trong quá trình tìm kiếm và hiển thị.
Các chức năng hoàn chỉnh:
-	Đăng ký bằng email, password hoặc bằng tài khoản Google cho Người muốn quyên góp.
-	Đăng nhập bằng email, password đã đăng ký hoặc đăng nhập bằng tài khoản Google
-	Kiểm tra các lỗi phát sinh khi đăng nhập, đăng ký.
-	Từ tài khoản đăng nhập, xác định đối tượng người dùng là Tổ chức từ thiện hay Người muốn quyên góp và load các chức năng và thông tin tương ứng.
-	Đối với Tổ chức từ thiện: 
o	Tạo các hoạt động quyên góp và đăng lên diễn đàn.
o	Xem lịch sử các đợt quyên góp của người muốn quyên góp dành các các hoạt động của họ.
o	Chấp nhận/từ chối đợt quyên góp của người tặng.
o	Thay đổi cập nhật trạng thái nhận đồ quyên góp từ người tặng (đã nhận?).
-	Người muốn quyên góp
o	Xem danh sách các tổ chức tình nguyện.
o	Xem danh sách các hoạt động tình nguyện
o	Gửi yêu cầu quyên góp đến các tổ chức từ thiện.
o	Xem các thông tin các hoạt động.
o	Xem lịch sử quyên góp của cá nhân.
Chức năng cài đặt nhưng chưa hoàn chỉnh: 
-	Tổ chức từ thiện
o	Thay đổi thông tin tổ chức từ thiện. 
o	Chỉnh sửa thông tin hoạt động.
-	Người muốn quyên góp
o	Thay đổi thông tin quyên góp.
o	Thay đổi thông tin cá nhân

Chức năng chưa xử lý:
-	Tổ chức từ thiện
o	Chức năng chấm điểm cho các nhà từ thiện (người tham gia tình nguyện và người tặng được phép chấm điểm), điểm càng cao thì nhà từ thiện đó được ưu tiên trong quá trình tìm kiếm và hiển thị.
-	Người muốn quyên góp
o	Nhận thông báo khi có người quyên góp đến tổ chức.
Điểm đặc sắc:
-	Ứng dụng sử dụng công nghệ mới của Google đó chính là Firebase, giúp kiểm soát chặt chẽ định danh người dùng cũng như kết nối với cơ sở dữ liệu Real Time Database. Qua đó các thông tin về người dùng cũng như về tài khoản của họ đều được bảo mật.

	Hướng phát triển
-	Hướng đến quản lý các thông tin giao dịch bằng tiền mặt đến các tổ chức từ thiện.
-	Cho phép người dùng được chuyển khoản đến các tổ chức từ thiện.
-	Có một số chức năng thông báo hằng ngày với mục đích kêu gọi người dùng trích một phần nhỏ trong quỹ thường ngày để dành cho những người khó khăn hơn.

GoodBox
Code chính: VanTron
Code & design: ThanhTruc
