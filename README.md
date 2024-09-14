## Social Network Platform - Spring Boot & Vue.js

This project is a full-stack social network platform built using **Spring Boot** for the backend and **Vue.js** for the frontend. The platform allows users to create profiles, post updates, interact with other users, and more. It focuses on scalability, performance, and a clean, modular architecture.

### Key Features:
- **User Authentication & Authorization**: Secure registration, login, and session management with Spring Security & JWT. Supports **Google OAuth** for social login.
- **Real-time Notifications**: Receive instant notifications for likes, comments, and new friend requests via WebSocket.
- **Real-time Messaging**: Chat with friends in real-time using WebSocket-based messaging.
- **News Feed**: Displaying posts from users.
- **Profile Management**: Users can customize profiles, upload avatars, and manage privacy settings.
- **Posts & Comments**: Users can create, like, comment, and share posts.
- **Friendship System**: Send, accept, and manage friend requests.
- **Admin Dashboard**: Manage users, moderate content, and review reported posts from users.
- **Responsive Design**: Mobile-friendly user interface using Vue.js and modern CSS frameworks.

### Technologies Used:
- **Backend**: Spring Boot, Spring Security, JPA, Hibernate, MySQL, RESTful API, JWT, WebSocket, Google OAuth
- **Frontend**: Vue.js, Vuex, Vue Router, Axios, Bootstrap, Element-plus
- **Real-time Features**: WebSocket for real-time notifications (friend requests, likes, comments) and chat messages
- **Database**: MySQL for persistent data storage

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
   git clone https://github.com/your-username/social-network-platform.git
   ```
2. **Backend**: Navigate to the `backend` directory, configure the application.properties file, and run the Spring Boot server.
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```
3. **Frontend**: Navigate to the `frontend` directory and start the Vue.js development server.
   ```bash
   cd frontend
   npm install
   npm run serve
   ```

### Showcase of Features:

Once the project is successfully run, you can explore the following key features in the application:

1. **Login & Google OAuth**:
   - Users can register or log in using their credentials or Google account.

   ![Login Interface](https://via.placeholder.com/400x200)

2. **Real-time Notifications & Messaging**:
   - Users will receive real-time notifications for new friend requests, likes, and comments.
   - Engage in live chat with friends.

   ![Real-time Messaging](https://via.placeholder.com/400x200)

3. **News Feed**:
   - Users can view the latest posts from their friends on a personalized feed.

   ![News Feed](https://via.placeholder.com/400x200)

4. **Admin Dashboard**:
   - Administrators can view and manage user reports, as well as handle user roles and permissions.

   ![Admin Dashboard](https://via.placeholder.com/400x200)

5. **User Profile & Friend Management**:
   - Customize personal profiles, add friends, and manage friend requests.

   ![User Profile](https://via.placeholder.com/400x200)

### Contribution:
Feel free to contribute to the project! Submit pull requests or raise issues for improvements, bugs, or new features.
