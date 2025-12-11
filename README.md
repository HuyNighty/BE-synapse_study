# Synapse - Knowledge Sharing Social Network (Backend)

Welcome to the backend repository of Synapse. This project provides a RESTful API to handle users, content, and social interactions for a learning-focused social network.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.0-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Security](https://img.shields.io/badge/JWT-OAuth2-red)

## Features

### Authentication & Authorization (RBAC)
- Registration & Login: Secure authentication using JWT (JSON Web Tokens).
- Token Management: Access Token, Refresh Token, and Introspection.
- Logout: Server-side logout handling with Token Blacklisting (Database-backed).
- Permission-based Security: Fine-grained access control using @PreAuthorize.
- Role Management: Dynamic Roles and Permissions assignment (Admin, User).

### Content Management (Core)
- Posts: Full CRUD operations for articles/posts.
- Categories: Manage topics and categories with auto-generated Slugs.
- Assets (File Storage): Upload images to local storage, secure ownership verification.

### Social Interactions
- Comments: Multi-level commenting system (Replies).
- Reactions: Express feelings (Like, Love, Insightful, etc.) with Toggle logic.
- Bookmarks: Save posts for later reading.

### System Utilities
- Global Exception Handling: Standardized API error responses.
- Data Seeding: Automatic initialization of default Admin, Roles, Permissions.
- Auditing: Auto-tracking of created/updated time and Soft Delete mechanism.

## Tech Stack

* Language: Java 21
* Framework: Spring Boot 3.x
* Database: MySQL
* ORM: Spring Data JPA / Hibernate
* Security: Spring Security 6, Nimbus JOSE + JWT
* Tools: Lombok, MapStruct, Spotless, Maven.

## Database Schema

The system is built on a relational database design consisting of 15 tables.

* Identity: users, roles, permissions, user_roles, role_permissions, invalidated_tokens.
* Content: posts, categories, assets, tags, post_tags.
* Interaction: comments, reactions, bookmarks, notifications.

Note: Soft delete is applied to core entities (Users, Posts, Comments).

## Getting Started

### Prerequisites
* Java Development Kit (JDK) 21
* Maven 3.x
* MySQL Server

### Installation

1. Clone the repository
   git clone https://github.com/HuyNighty/BE-synapse_study.git
   cd BE-synapse_study

2. Configure Database
   Create a MySQL database named synapse_study.
   Update src/main/resources/application.yaml with your credentials.

3. Run the Application
   mvn spring-boot:run

   The app will start at http://localhost:8080

## API Documentation

### Auth Module
* POST /api/auth/login: Authenticate user & get tokens.
* POST /api/auth/refresh: Get new access token.
* POST /api/auth/logout: Invalidate current token.
* POST /api/users: Register new account.

### Post Module
* GET /api/posts: Get all posts (Pagination support).
* GET /api/posts/{slug}: Get post detail.
* POST /api/posts: Create new post (Requires Auth).
* PATCH /api/posts/{id}: Update post (Owner only).

### Asset Module
* POST /api/assets: Upload file (Multipart/form-data).
* PUT /api/assets/{id}: Replace file.

## License

Distributed under the MIT License.

Developed by HuyNighty
