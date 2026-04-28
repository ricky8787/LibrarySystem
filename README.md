# 圖書館借閱系統 (Library Borrowing System)

本專案為一個基於 Spring Boot 和 Vue.js 開發的現代化圖書館管理系統，具備書籍查詢、個人借閱、會員註冊與登入等核心功能。

## 🚀 功能介紹

### 後端 (Backend - Spring Boot)
*   **RESTful API**：提供標準的 REST API 接口供前端調用。
*   **資料庫整合**：使用 Hibernate 進行 ORM 操作，並透過 Stored Procedure 處理商業邏輯（如：註冊）。
*   **安全性**：整合 **Spring Security** 及 **JWT (JSON Web Tokens)**，確保使用者資料安全與身份驗證。
*   **加密**：密碼使用 `BCryptPasswordEncoder` 進行雜湊與加鹽處理。

### 前端 (Frontend - Vue)
*   **響應式介面**：採用 Vue 3 Composition API 開發，提供流暢的使用者體驗。
*   **路由管理**：使用 `vue-router` 進行頁面導航，並實作路由守衛 (Route Guards) 保護頁面。


## 🔌 環境建置與執行

### 前置需求
*   Java 21
*   Node.js
*   MySQL

### 1. 後端 (Backend)
1.  **資料庫設定**：修改 `backend/src/main/resources/application.properties` 中的資料庫連線資訊。
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/library
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```
    請先將 DB 資料夾內 DDL DML 檔案執行，建立 Schema 與 Test Data。

2.  **啟動服務**：
    ```bash
    cd backend
    mvn clean spring-boot:run
    ```
    服務預設啟動於 `http://localhost:8080`。

### 2. 前端 (Frontend)
1.  **安裝依賴**：
    ```bash
    cd frontend
    npm install
    ```
2.  **啟動服務**：
    ```bash
    npm run dev
    ```
    前端服務預設啟動於 `http://localhost:5173`。


## 🔄 流程說明
1.  **註冊**：使用者提供手機、密碼和姓名
2.  **登入**：使用者輸入手機和密碼
3.  **借閱**：使用者登入後，可瀏覽書籍並進行借閱操作

