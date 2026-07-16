# Employee Leave Management System (ELMS)

A production-ready full-stack Employee Leave Management System built with Spring Boot 3.x and React 18.

## Features

- **Role-Based Access Control**: Admin, Manager, and Employee roles
- **JWT Authentication**: Secure token-based authentication
- **Leave Management**: Apply, approve, reject, and cancel leave requests
- **Leave Balance Tracking**: Real-time leave balance updates
- **Dashboard Analytics**: Role-specific dashboards with charts and statistics
- **Report Generation**: CSV export and summary reports
- **Responsive Design**: Mobile-friendly UI with Material-UI
- **Working Day Calculation**: Automatically excludes weekends from leave calculations

## Tech Stack

### Backend
- Java 21
- Spring Boot 3.2.0
- Spring Security with JWT
- Spring Data JPA (Hibernate)
- MySQL Database
- Maven
- Lombok
- Bean Validation
- Swagger/OpenAPI Documentation

### Frontend
- React 18
- React Router DOM
- Axios
- Context API for Authentication
- Material UI (MUI)
- React Hook Form
- Recharts for analytics
- Vite

## Database Schema

### Tables
- **users**: Employee information and authentication
- **leave_types**: Leave type definitions (Sick, Casual, Earned)
- **leave_balances**: Leave balance tracking per employee
- **leave_requests**: Leave request records with status tracking

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- Node.js 18+ and npm
- MySQL 8.0+

## Installation

### Backend Setup

1. **Navigate to backend directory**
   ```bash
   cd backend
   ```

2. **Configure Database**
   - Create a MySQL database named `elms_db`
   - Update `src/main/resources/application.properties` with your MySQL credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/elms_db
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   ```

3. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   
   The backend will start on `http://localhost:8080`

4. **Access Swagger Documentation**
   - Open `http://localhost:8080/swagger-ui.html` in your browser
   - Explore and test all REST APIs

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd frontend
   ```

2. **Install Dependencies**
   ```bash
   npm install
   ```

3. **Start Development Server**
   ```bash
   npm run dev
   ```
   
   The frontend will start on `http://localhost:3000`

## Demo Credentials

The system comes pre-loaded with sample data:

### Admin
- **Email**: admin@elms.com
- **Password**: admin123

### Manager
- **Email**: manager1@elms.com
- **Password**: manager123

### Employee
- **Email**: alice@elms.com
- **Password**: employee123

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `GET /api/auth/profile` - Get user profile
- `PUT /api/auth/change-password` - Change password

### Users
- `GET /api/users` - Get all users (Admin only)
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user (Admin only)
- `PUT /api/users/{id}` - Update user (Admin only)
- `DELETE /api/users/{id}` - Delete user (Admin only)
- `GET /api/users/role/{role}` - Get users by role
- `GET /api/users/team/{managerId}` - Get team members

### Leave Types
- `GET /api/leave-types` - Get all leave types
- `GET /api/leave-types/{id}` - Get leave type by ID
- `POST /api/leave-types` - Create leave type (Admin only)
- `PUT /api/leave-types/{id}` - Update leave type (Admin only)
- `DELETE /api/leave-types/{id}` - Delete leave type (Admin only)

### Leave Balances
- `GET /api/leave-balances` - Get all leave balances (Admin only)
- `GET /api/leave-balances/user/{userId}` - Get user leave balances
- `GET /api/leave-balances/user/{userId}/leave-type/{leaveTypeId}` - Get specific balance
- `POST /api/leave-balances` - Create leave balance (Admin only)
- `PUT /api/leave-balances/user/{userId}/leave-type/{leaveTypeId}` - Update balance
- `DELETE /api/leave-balances/user/{userId}/leave-type/{leaveTypeId}` - Delete balance

### Leave Requests
- `GET /api/leaves` - Get all leave requests (Admin only)
- `GET /api/leaves/{id}` - Get leave request by ID
- `GET /api/leaves/my-requests` - Get my leave requests
- `GET /api/leaves/team-requests` - Get team leave requests (Manager)
- `POST /api/leaves/apply` - Apply for leave
- `PUT /api/leaves/{id}/approve` - Approve leave (Manager/Admin)
- `PUT /api/leaves/{id}/reject` - Reject leave (Manager/Admin)
- `PUT /api/leaves/{id}/cancel` - Cancel leave
- `GET /api/leaves/status/{status}` - Get requests by status

### Reports
- `GET /api/reports/leave-summary` - Get leave summary (Admin only)
- `GET /api/reports/department-summary` - Get department summary (Admin only)
- `GET /api/reports/monthly-report` - Get monthly report (Admin only)
- `GET /api/reports/export-csv` - Export to CSV (Admin only)

## User Roles and Permissions

### Admin
- Full access to all features
- Manage users (create, update, delete)
- Manage leave types
- View and manage all leave requests
- Access reports and analytics
- Export data

### Manager
- View team members
- View team leave requests
- Approve/reject team leave requests
- Add comments to decisions
- View dashboard statistics

### Employee
- Apply for leave
- View leave balance
- View leave history
- Cancel pending leave requests
- Update profile

## Business Rules

1. **Working Days**: Weekends (Saturday and Sunday) are excluded from leave calculations
2. **Leave Balance**: Deducted only after approval
3. **Cancellation**: Employees can only cancel pending requests
4. **Manager Approval**: Managers can only approve their team's requests
5. **Admin Access**: Admin has complete system access
6. **JWT Token**: Required for all protected API requests
7. **Password Security**: All passwords encrypted with BCrypt

## Project Structure

### Backend
```
backend/
├── src/main/java/com/elms/
│   ├── config/          # Configuration classes
│   ├── controller/      # REST controllers
│   ├── dto/             # Data Transfer Objects
│   ├── entity/          # JPA entities
│   ├── exception/       # Global exception handling
│   ├── repository/      # JPA repositories
│   ├── security/        # Security configuration
│   ├── service/         # Service interfaces
│   ├── serviceImpl/     # Service implementations
│   └── util/            # Utility classes (JWT)
└── src/main/resources/
    └── application.properties
```

### Frontend
```
frontend/
├── src/
│   ├── components/      # Reusable components
│   ├── contexts/        # React contexts (Auth)
│   ├── pages/           # Page components
│   │   ├── employee/    # Employee pages
│   │   ├── manager/     # Manager pages
│   │   └── admin/       # Admin pages
│   ├── services/        # API service calls
│   ├── App.jsx          # Main app component
│   └── main.jsx         # Entry point
```

## Development

### Backend Development
- Use Maven for dependency management
- Follow Spring Boot best practices
- Use Lombok to reduce boilerplate
- Test APIs using Swagger UI

### Frontend Development
- Use Vite for fast development
- Follow React best practices
- Use Material UI components
- Implement responsive design

## Production Deployment

### Backend
1. Build the JAR file:
   ```bash
   mvn clean package
   ```

2. Run the JAR:
   ```bash
   java -jar target/elms-backend-1.0.0.jar
   ```

3. Configure production database in `application.properties`

### Frontend
1. Build for production:
   ```bash
   npm run build
   ```

2. Deploy the `dist` folder to your web server

## Security Features

- JWT-based authentication
- BCrypt password encryption
- Role-based authorization
- CORS configuration
- Input validation
- SQL injection prevention (JPA)
- XSS protection

## Troubleshooting

### Backend Issues
- **Database Connection**: Check MySQL credentials in `application.properties`
- **Port Conflict**: Change server port in `application.properties`
- **JWT Errors**: Verify JWT secret and expiration settings

### Frontend Issues
- **API Connection**: Ensure backend is running on port 8080
- **CORS Errors**: Check CORS configuration in SecurityConfig
- **Build Errors**: Clear node_modules and reinstall dependencies

## License

This project is for educational purposes.

## Support

For issues and questions, please refer to the Swagger documentation or contact the development team.
