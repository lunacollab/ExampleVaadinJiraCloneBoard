# Jira Clone - Kanban Board Application

Ứng dụng quản lý dự án Kanban Board giống Jira, được xây dựng với Vaadin, Spring Boot, MySQL và Lombok.

## 🚀 Tính năng

- **Kanban Board**: Quản lý tasks theo các column (To Do, In Progress, In Review, Done)
- **Task Management**: Tạo, sửa, xóa tasks với đầy đủ thông tin
- **User Management**: Phân quyền theo roles (Admin, Project Manager, Developer, User)
- **Project Management**: Quản lý nhiều projects, thêm members vào project
- **Soft Delete**: Xóa mềm tất cả entities, có thể khôi phục sau
- **Beautiful UI**: Sử dụng Tailwind CSS với design hiện đại
- **Responsive**: Giao diện responsive, dễ sử dụng

## 📋 Công nghệ sử dụng

- **Backend**: Spring Boot 3.5.7, Vaadin 24.9.4
- **Database**: MySQL 8.0
- **Security**: Spring Security với role-based access
- **ORM**: JPA/Hibernate
- **Build Tool**: Maven
- **Frontend**: Vaadin Flow, Tailwind CSS
- **Code Generation**: Lombok

## 🗂️ Cấu trúc Database

### Entities

- **User**: Người dùng hệ thống
- **Role**: Vai trò (Admin, Project Manager, Developer, User)
- **Project**: Dự án
- **BoardColumn**: Các cột trong board (To Do, In Progress, etc.)
- **Task**: Công việc/card trong board
- **TaskComment**: Bình luận cho task

### Soft Delete

Tất cả entities đều kế thừa `BaseEntity` với các trường:
- `deleted`: Boolean flag
- `deletedAt`: Thời gian xóa
- `createdAt`, `updatedAt`: Audit fields

## 🛠️ Cài đặt và Chạy

### Yêu cầu

- Java 21+
- MySQL 8.0+
- Maven 3.6+

### Bước 1: Cấu hình MySQL

Tạo database:

```sql
CREATE DATABASE jira_clone;
```

Cấu hình trong `application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

### Bước 2: Chạy ứng dụng

#### Cách 1: Sử dụng Maven

```bash
./mvnw spring-boot:run
```

#### Cách 2: Sử dụng IDE

Import project vào IDE (IntelliJ IDEA, Eclipse) và run class `Application.java`

#### Cách 3: Build và chạy JAR

```bash
./mvnw clean package
java -jar target/vaadin-example-1.0-SNAPSHOT.jar
```

### Bước 3: Truy cập ứng dụng

Mở trình duyệt và truy cập: `http://localhost:8084/kanban`

## 👥 Users mẫu

Data seeder tự động tạo các users:

| Username | Password | Role | Email |
|----------|----------|------|-------|
| admin | admin123 | ADMIN | admin@jira.com |
| jdoe | password123 | PROJECT_MANAGER, DEVELOPER | jdoe@jira.com |
| jsmith | password123 | DEVELOPER | jsmith@jira.com |
| mjohnson | password123 | DEVELOPER | mjohnson@jira.com |

## 📊 Dữ liệu mẫu

Ứng dụng tự động tạo:
- 4 roles (Admin, Project Manager, Developer, User)
- 4 users với avatars từ Unsplash
- 2 projects với avatars
- 7 tasks mẫu với đầy đủ thông tin

## 🎨 Tính năng UI

- **Kanban Board**: Drag and drop (coming soon với dnd-kit)
- **Task Cards**: Hiển thị type, priority, assignee, due date
- **Task Dialog**: Form đầy đủ để tạo/sửa task
- **Color Coding**: Mỗi type và priority có màu riêng
- **Responsive Layout**: Tự động điều chỉnh theo screen size

## 🔐 Security

- Spring Security enabled
- Role-based access control
- Password encryption với BCrypt
- Session management

## 📦 Package Structure

```
src/main/java/com/example/application/
├── config/
│   ├── DataSeeder.java          # Seed dữ liệu mẫu
│   ├── JpaConfig.java           # JPA configuration
│   └── SecurityConfig.java      # Security configuration
├── jira/
│   ├── model/                   # Entities
│   │   ├── BaseEntity.java
│   │   ├── User.java
│   │   ├── Role.java
│   │   ├── Project.java
│   │   ├── BoardColumn.java
│   │   ├── Task.java
│   │   ├── TaskType.java
│   │   ├── TaskPriority.java
│   │   └── TaskComment.java
│   ├── repository/              # JPA Repositories
│   ├── service/                 # Business Logic
│   └── ui/                      # Vaadin UI Components
│       ├── MainLayout.java
│       ├── KanbanBoardView.java
│       ├── KanbanColumn.java
│       ├── TaskCard.java
│       └── TaskDialog.java
└── Application.java
```

## 🚧 Roadmap

- [ ] Drag and drop với dnd-kit CDN
- [ ] Comments cho tasks
- [ ] File attachments
- [ ] Real-time updates
- [ ] Advanced filtering và search
- [ ] Reports và analytics
- [ ] Email notifications

## 📝 API Endpoints

Tất cả endpoints được generate bởi Vaadin Flow:
- `/kanban` - Kanban Board View
- `/login` - Login page
- `/register` - Registration page (chưa implement)

## 🐛 Troubleshooting

### Lỗi kết nối MySQL

Đảm bảo MySQL đang chạy và credentials đúng trong `application.properties`.

### Port 8084 đã được sử dụng

Thay đổi port trong `application.properties`:
```properties
server.port=8080
```

### Dữ liệu không tự động tạo

Check logs để xem lỗi. DataSeeder chỉ chạy khi database trống.

## 📄 License

MIT License - Xem file LICENSE.md

## 👤 Author

Created with ❤️ using Vaadin and Spring Boot

## 🙏 Acknowledgments

- [Vaadin](https://vaadin.com/) - Framework UI
- [Unsplash](https://unsplash.com/) - Sample images
- [Tailwind CSS](https://tailwindcss.com/) - Utility-first CSS
- [Lombok](https://projectlombok.org/) - Boilerplate reduction
