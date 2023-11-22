## Сущности (Entities)

### 1. User (Пользователь)

#### Поле `userId`:
- Тип: Long
- Описание: Уникальный идентификатор пользователя.

#### Поле `username`:
- Тип: String
- Описание: Уникальное имя пользователя, используемое для входа в систему.

#### Поле `email`:
- Тип: String
- Описание: Уникальный адрес электронной почты пользователя.

#### Поле `phoneNumber`:
- Тип: String
- Описание: Уникальный номер телефона пользователя (необязательное поле).

#### Поле `telegram`:
- Тип: String
- Описание: Уникальное имя пользователя в Telegram (необязательное поле).

#### Поле `fullName`:
- Тип: String
- Описание: Полное имя пользователя (ФИО).

#### Поле `position`:
- Тип: String
- Описание: Должность пользователя.

#### Поле `password`:
- Тип: String
- Описание: Захешированный пароль пользователя.

#### Поле `createdAt`:
- Тип: LocalDateTime
- Описание: Время создания записи о пользователе.

#### Поле `updatedAt`:
- Тип: LocalDateTime
- Описание: Время последнего обновления записи о пользователе.

#### Поле `isAccountLocked`:
- Тип: boolean
- Описание: Флаг, указывающий, заблокирован ли аккаунт пользователя.

#### Поле `roles`:
- Тип: List\<UserRole\>
- Описание: Список ролей, присвоенных пользователю.


```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String telegram;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean isAccountLocked;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UserRole> roles;

    // Геттеры и сеттеры, конструкторы и другие методы
}
```

### 2. Role (Роль)

#### Поле `roleId`:
- Тип: Long
- Описание: Уникальный идентификатор роли.

#### Поле `roleName`:
- Тип: String
- Описание: Наименование роли.

#### Поле `description`:
- Тип: String
- Описание: Описание роли.

```java
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(nullable = false)
    private String roleName;

    private String description;
    // Геттеры и сеттеры, конструкторы и другие методы
}
```

### 3. UserRole (Роль пользователя)

#### Поле `id`:
- Тип: UserRoleId
- Описание: Составной ключ, объединяющий userId и roleId.

#### Поле `user`:
- Тип: User
- Описание: Связь с сущностью User.

#### Поле `role`:
- Тип: Role
- Описание: Связь с сущностью Role.

#### Поле `department`:
- Тип: Department
- Описание: Связь с сущностью Department (необязательное поле).

```java
@Entity
@Table(name = "user_roles")
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    // Геттеры и сеттеры, конструкторы и другие методы
}
```

### 4. UserRoleId (Идентификатор роли пользователя)

#### Поле `userId`:
- Тип: Long
- Описание: Идентификатор пользователя.

#### Поле `roleId`:
- Тип: Long
- Описание: Идентификатор роли.

```java
@Embeddable
public class UserRoleId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;

    // Геттеры и сеттеры, конструкторы и другие методы
}
```

### 5. Department (Департамент)

#### Поле `departmentId`:
- Тип: Long
- Описание: Уникальный идентификатор департамента.

#### Поле `departmentName`:
- Тип: String
- Описание: Наименование департамента.

#### Поле `description`:
- Тип: String
- Описание: Описание департамента.

#### Поле `headUser`:
- Тип: User
- Описание: Связь с сущностью User, представляющей руководителя департамента.

```java
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    @Column(nullable = false)
    private String departmentName;

    private String description;

    @ManyToOne
    @JoinColumn(name = "head_user_id")
    private User headUser;

    // Геттеры и сеттеры, конструкторы и другие методы
}
```

## Репозитории (Repositories)

### 1. UserRepository

#### Метод `findByUsername(String username)`:
- Описание: Поиск пользователя по имени пользователя.

#### Метод `findByEmail(String email)`:
- Описание: Поиск пользователя по адресу электронной почты.

#### Метод `findByPhoneNumber(String phoneNumber)`:
- Описание: Поиск пользователя по номеру телефона.

#### Метод `findByTelegram(String telegram)`:
- Описание: Поиск пользователя по Telegram имени.

### 2. RoleRepository

#### Метод `findByRoleName(String roleName)`:
- Описание: Поиск роли по наименованию.

### 3. UserRoleRepository

#### Метод `findByUserUserId(Long userId)`:
- Описание: Получение списка ролей пользователя по его идентификатору.

## Сервисные интерфейсы (Service Interfaces)

### 1. UserService

#### Метод `getAllUsers()`:
- Описание: Получение списка всех пользователей.

#### Метод `getUserById(Long userId)`:
- Описание: Получение пользователя по его идентификатору.

#### Метод `createUser(User user)`:
- Описание: Создание нового пользователя.

#### Метод `updateUser(Long userId, User user)`:
- Описание: Обновление информации о пользователе.

#### Метод `deleteUser(Long userId)`:
- Описание: Удаление пользователя.

#### Метод `lockUser(Long userId)`:
- Описание: Блокировка аккаунта пользователя.

```java
public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long userId);

    User createUser(User user);

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);

    User lockUser(Long userId);
}
```

### 2. RoleService

#### Метод `getAllRoles()`:
- Описание: Получение списка всех ролей.

#### Метод `getRoleById(Long roleId)`:
- Описание: Получение роли по её идентификатору.

#### Метод `createRole(Role role)`:
- Описание: Создание новой роли.

#### Метод `updateRole(Long roleId, Role role)`:
- Описание: Обновление информации о роли.

#### Метод `deleteRole(Long roleId)`:
- Описание: Удаление роли.

```java
public interface RoleService {

    List<Role> getAllRoles();

    Role getRoleById(Long roleId);

    Role createRole(Role role);

    Role updateRole(Long roleId, Role role);

    void deleteRole(Long roleId);
}
```

### 3. UserRoleService

#### Метод `getRolesByUserId(Long userId)`:
- Описание: Получение списка ролей пользователя по его идентификатору.

#### Метод `assignRoleToUser(Long userId, UserRole userRole)`:
- Описание: Присвоение роли пользователю.

#### Метод `removeRoleFromUser(Long userId, Long roleId)`:
- Описание: Удаление роли у пользователя.

```java
public interface UserRoleService {

    List<UserRole> getRolesByUserId(Long userId);

    void assignRoleToUser(Long userId, UserRole userRole);

    void removeRoleFromUser(Long userId, Long roleId);
}
```

### 4. DepartmentService

#### Метод `getAllDepartments()`:
- Описание: Получение списка всех департаментов.

#### Метод `getDepartmentById(Long departmentId)`:
- Описание: Получение департамента по его идентификатору.

#### Метод `createDepartment(Department department)`:
- Описание: Создание нового департамента.

#### Метод `updateDepartment(Long departmentId, Department department)`:
- Описание: Обновление информации о департаменте.

#### Метод `deleteDepartment(Long departmentId)`:
- Описание: Удаление департамента.

```java
public interface DepartmentService {

    List<Department> getAllDepartments();

    Department getDepartmentById(Long departmentId);

    Department createDepartment(Department department);

    Department updateDepartment(Long departmentId, Department department);

    void deleteDepartment(Long departmentId);
}
```