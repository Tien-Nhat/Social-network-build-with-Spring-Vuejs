## Social Network Platform - Spring Boot & Vue.js

This project is a full-stack social network platform built using **Spring Boot** for the backend and **Vue.js** for the frontend. The platform allows users to create profiles, post updates, interact with other users, and more.

### Key Features:
- **User Authentication & Authorization**: Secure registration, login, and session management with Spring Security & JWT. Supports **Google OAuth** for social login.
- **Real-time Notifications**: Receive instant notifications for likes, comments, and new friend requests via WebSocket.
- **Real-time Messaging**: Chat with friends in real-time using WebSocket-based messaging.
- **News Feed**: A personalized feed displaying posts from followed users.
- **Profile Management**: Users can customize profiles, upload avatars, and manage privacy settings.
- **Posts & Comments**: Users can create, like, comment, and share posts.
- **Friendship System**: Send, accept, and manage friend requests.
- **Admin Dashboard**: Manage users, moderate content, and review reported posts from users.
- **Search with Debounce**: Efficient user and content search with debounce functionality to reduce unnecessary requests.
- **Online Status Indicator**: Friend list shows online/offline status of users with dot indicators.

### Technologies Used:
- **Backend**: Spring Boot, Spring Security, JPA, Hibernate, MySQL, RESTful API, JWT, WebSocket, Google OAuth
- **Frontend**: Vue.js, Vuex, Vue Router, Axios, Bootstrap, Element-plus
- **Real-time Features**: WebSocket for real-time notifications (friend requests, likes, comments), chat messages, and online status updates
- **Database**: MySQL for persistent data storage
### Setup Guide:

Before running the project, you will need to provide specific configuration values in both the frontend and backend.

#### 1. Vue.js Frontend:
In the `.env` file, update the following variables:
```bash
VITE_GOOGLE_CLIENT_ID=YOUR_GOOGLE_CLIENT_ID
VITE_API_URL=http://localhost:8080
```

- Replace `YOUR_GOOGLE_CLIENT_ID` with your actual Google OAuth client ID.
- `VITE_API_URL` should point to the URL of your backend API.

#### 2. Spring Boot Backend:
In the `application.properties` file, update the following properties:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/YOUR_DATABASE_NAME
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

google.client-id=YOUR_CLIENT-ID
google.client-secret=YOUR_CLIENT_SECRET
google.redirect-uri=YOUR_REDIRECT_URI
```
- Replace `YOUR_DATABASE_NAME`, `YOUR_DB_USERNAME`, and `YOUR_DB_PASSWORD` with your actual database name, username, and password.
- Replace `YOUR_CLIENT-ID`, `YOUR_CLIENT_SECRET`, and `YOUR_REDIRECT_URI` with the corresponding values from your Google OAuth setup.

### Database Setup:
1. **Run the Spring project**: First, run the project once to let Spring automatically create the database tables.

2. **Run the following MySQL commands** to insert necessary data:

```sql
INSERT INTO report_type (name)
VALUES 
('INAPPROPRIATE_CONTENT'), 
('INAPPROPRIATE_IMAGES'), 
('IMPERSONATION_OR_MIMICRY'), 
('ABUSE_OR_HARASSMENT'), 
('COPYRIGHT_VIOLATION'),
('SPAM_OR_FRAUD'), 
('OTHER_REASON');

INSERT INTO roles (name)
VALUES ('ROLE_USER'), ('ROLE_MODERATOR'), ('ROLE_ADMIN');
```

### How to Run:
1. Clone this repository:
   ```bash
   git clone https://github.com/Tien-Nhat/Social-network-build-with-Spring-Vuejs.git
   ```
2. **Backend**: Navigate to the `backend` directory, configure the application.properties file, and run the Spring Boot server.
   ```bash
   cd Spring_backend
   ./mvnw spring-boot:run
   ```
3. **Frontend**: Navigate to the `frontend` directory and start the Vue.js development server.
   ```bash
   cd Vue_frontend
   npm install
   npm run serve
   ```

### Showcase of Features:

Once the project is successfully run, you can explore the following key features in the application:

1. **Login & Google OAuth**:
   - Users can log in using Google account.
![logingg](https://github.com/user-attachments/assets/85455050-7f4f-42b4-9cbb-070637e5f1ad)
   - Register or log in using their credentials.
![loginuser](https://github.com/user-attachments/assets/6087e00d-f0d6-4f99-8bb3-3513c42a9d0f)

2. **Real-time Notifications & Messaging**:
   - Users will receive real-time notifications for new friend requests, likes, and comments.
   ![noti](https://github.com/user-attachments/assets/f6bab4c7-a574-47e8-b514-813953eb74d2)

   - Chat with friends.
![chat](https://github.com/user-attachments/assets/36c1f169-67fc-46ff-b8b1-f8e7532a84f2)


3. **Search with Debounce**:
   - Search for users or content with a debounce mechanism.

   ![search](https://github.com/user-attachments/assets/1757503a-a32f-44c4-935b-1adaf5ad0653)


4. **Friend List Online Status**:
   - The friend list shows dot indicators for online (green) or offline (gray) status in real-time.

   ![dot](https://github.com/user-attachments/assets/cafc2c32-f449-4ce7-99a9-08840317c9c6)


5. **News Feed**:
   - Users can view the latest posts from user.

   ![viewPost](https://github.com/user-attachments/assets/c6195180-4542-400b-a3f7-418de88897f3)



6. **Admin Dashboard**:
   - Administrators can view and manage user reports, as well as handle user roles and permissions.

   ![admin board](https://github.com/user-attachments/assets/88681a34-0140-4809-85c5-495310a391f3)


7. **User Profile & Friend Management**:
   - Customize personal profiles, add friends, and manage friend requests.

   ![user](https://github.com/user-attachments/assets/c0f598bb-1a7d-4e34-8df4-0e46670484b9)


### Contribution:
Feel free to contribute to the project! Submit pull requests or raise issues for improvements, bugs, or new features.
