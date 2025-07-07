# spring-jpa-console-relationship

Here's a **well-organized and professional summary** of your complete guide to **Database Relationships in Spring JPA**, suitable for documentation, learning, or presentations:

---

# 📘 **Database Relationships in Spring JPA – Complete Guide**

## 🔰 **Introduction to Spring Components**

* **Spring Core**: Provides the **Inversion of Control (IoC)** container (`ApplicationContext`) to manage beans.
* **Spring MVC**: Handles **HTTP requests/responses** via `DispatcherServlet`.
* **Spring Data JPA**: Simplifies data access with **JPA (Java Persistence API)**, allowing easy handling of database operations.

---

## 🔗 **What is a Database Relationship?**

A **database relationship** connects data across tables to:

* Ensure **data integrity**
* Eliminate **redundancy**
* Establish **logical associations** between entities

---

## 🔄 **Types of Relationships**

### 1️⃣ **One-to-One (1:1)**

📌 **Definition**: One record in Table A ↔️ one record in Table B
📘 **Example**: One `User` has one `UserProfile`

```java
// User Entity
@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private UserProfile profile;

// UserProfile Entity
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", nullable = false)
private User user;
```

---

### 2️⃣ **One-to-Many (1\:N)**

📌 **Definition**: One record in Table A → many records in Table B
📘 **Example**: One `User` writes many `Post`s

```java
// User Entity (One Side)
@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Post> posts;

// Post Entity (Many Side)
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "author_id", nullable = false)
private User author;
```

---

### 3️⃣ **Many-to-One (N:1)**

📌 **Definition**: Many records in Table A → one record in Table B
📘 **Example**: Many `Employee`s belong to one `Department`

```java
// Employee Entity
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "department_id")
private Department department;

// Department Entity
@OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
private List<Employee> employees;
```

---

### 4️⃣ **Many-to-Many (N\:N)**

📌 **Definition**: Many records in Table A ↔️ many records in Table B
📘 **Example**: A `User` can have many `Role`s, and each `Role` can belong to many `User`s

```java
// User Entity (Owning Side)
@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
@JoinTable(
    name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
)
private Set<Role> roles;

// Role Entity (Inverse Side)
@ManyToMany(mappedBy = "roles")
private Set<User> users;
```

---

## ⚙️ **Advanced Concepts in JPA Relationships**

### ✅ **1. Fetch Types**

* `EAGER`: Loads related data immediately
* `LAZY`: Loads data only when needed (**default** for collections)

```java
@OneToMany(fetch = FetchType.LAZY)
private List<Post> posts;
```

---

### 🔁 **2. Cascade Types**

Controls operations on child entities:

```java
@OneToMany(cascade = CascadeType.ALL)
```

* `PERSIST`, `MERGE`, `REMOVE`, `REFRESH`, `DETACH`, `ALL`

---

### 🧹 **3. Orphan Removal**

Removes child records if they're no longer referenced:

```java
@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
```

---

### 🔁 **4. Bidirectional vs Unidirectional**

* **Bidirectional**: Both entities refer to each other.
* **Unidirectional**: Only one entity holds the reference.

```java
// Bidirectional Example
@OneToMany(mappedBy = "author")
private List<Post> posts;

@ManyToOne
@JoinColumn(name = "author_id")
private User author;
```

---

## 🏁 **Conclusion**

Understanding and implementing proper **entity relationships** in Spring Data JPA:

* Promotes **clean architecture**
* Enables **efficient querying**
* Maintains **data consistency**


