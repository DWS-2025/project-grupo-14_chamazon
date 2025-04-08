<h1 align="center">Welcome to Chamazon! 🛒</h1>
<h3 align="center">Group 14 — Secure Web Development — "Buy your 10 mark :)"</h3>

<div align="center">
  <img src="https://raw.githubusercontent.com/DWS-2025/project-grupo-14_chamazon/main/content/images/Chamazon.png" alt="Chamazon Logo" width="500"/>
</div>

---

### 🛠 Languages & Tools

<p align="center">
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="Java" width="40" height="40"/>
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/html5/html5-original-wordmark.svg" alt="HTML5" width="40" height="40"/>
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/css3/css3-original-wordmark.svg" alt="CSS3" width="40" height="40"/>
  <img src="https://www.vectorlogo.zone/logos/git-scm/git-scm-icon.svg" alt="Git" width="40" height="40"/>
</p>

---

## 🧩 Core Entities

| **Entity**                | **Description**                                                                 |
|--------------------------|---------------------------------------------------------------------------------|
| `User`                   | Represents users (admins or customers). Can create products, comment, and shop. |
| `Product`                | Items available for purchase. Linked to a user and one or more categories.      |
| `Comment`                | User-written feedback on products. One per product per user.                    |
| `Category`               | Tags used to classify products. Products belong to one category.                |
| `ShoppingCar`            | Stores selected products for each user before purchase.                         |
| `ShoppingCarProductList`| Many-to-many relationship between ShoppingCar and Product.                      |


---

## 🔗 Relationships

- A `User` can create many `Product`.
- A `Product` belongs to a `User`.
- A `Product` can have many `Comment`.
- A `Comment` belongs to one `User` and one `Product`.
- A `Product` can have multiple `Category` tags (many-to-many).
- A `ShoppingCar` belongs to one `User`.
- A `ShoppingCar` can contain multiple `Product` via `ShoppingCarProductList`.

---

## 🖼 Associated Images

- **Product**: Contains an image file (`image_file`) to represent the product visually.

---

## 👑 User Permissions

Currently, the platform runs in **admin-only mode**: the admin can manage all entities and perform all operations (full CRUD).

### Future Roles (Planned):
- **Admin**: Full control over the entire system.
- **Provider**: Can create and manage their own products.
- **Customer**: Can browse, comment, add to cart, and complete purchases.

---

<h3 align="center">📊 Entity-Relationship Diagram</h3>

<div align="center">
  <img src="https://raw.githubusercontent.com/DWS-2025/project-grupo-14_chamazon/main/content/images/RelacionesBBDD.jpeg" alt="ER Diagram" width="650"/>
</div>

---

## 🧙‍♂️ Top Commits by the Chamazon Team

Each member contributed with dedication and unique skills. Below we it is shown the 5 most important commits of each developer and a brief description of our personal contribution to the project.

### 👤 Beatriz Sanz Granados — [btrisss](https://github.com/btrisss)

#### 🌟 Contribution Summary
Beatriz worked extensively on the Comment entity, focusing on its full integration within the REST API. She developed the Comment REST controller, implemented DTOs and Mappers, and adapted both the service and model layers to align with RESTful standards. Additionally, she implemented AJAX-based pagination to dynamically load comments on demand, enhancing performance and improving the overall user experience.

#### 📝 Notable Commits
1. [Creación CommentRestController y poblar de comentarios BBDD](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/48c6d8ef7da2553d01c68d66f82a35a5700b9720)
2. [Refactor Comentario con DTOs de user y product](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/da3aef3a377a849c9d01044f4b19afd282ed9aaf)
3. [AJAX Pagination Completo](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/b052c1a21ecd6ab5f33c0879d580d4ab842b188d)
4. [Creación de CommentDTO y su respectivo Mapper](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/6b7bc64870973c3128ff8562280f37e5ba7d797d)
5. [Corrección en las rutas de Controller](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/0cad28eba12eca4cad795cc444af55e7c2512562)

#### 📁 Most Contributed Files

- `src/main/java/es/urjc/chamazon/models/Comment.java`
- `src/main/java/es/urjc/chamazon/services/CommentService.java`
- `src/main/java/es/urjc/chamazon/restControllers/CommentRestController.java`
- `src/main/java/es/urjc/chamazon/controllers/CommentController.java`
- `src/main/resources/templates/comment/commentList.html`

---

### 👤 Belén Collado Torres — [BecotoGit](https://github.com/BecotoGit)

#### 🌟 Contribution Summary
Belén focused on the User and Category entities, developing their REST controllers and integrating DTOs and Mappers accordingly. She ensured the proper functionality of user and category-related endpoints within the API. Additionally, she contributed to debugging and improving the Product entity, collaborating closely with Mario to resolve integration issues and enhance the structure of the RESTful services. Belén also created the Postman collection used to test the endpoints, helping the team validate the API behavior efficiently.

#### 📝 Notable Commits
1. [Funcionalidad completa de Category y cambios en User, Chamazon funcional](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/8c48f807ce954124f336697fe9e76e6bb861be86)
2. [Nueva implementación mejorada de los DTOs](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/ce782e0a718ddc2542ac7969d02530d388358f21)
3. [Rest completo de las entidades Category y User](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/e1943b659b03d395261d53a11240db1c3d8a864c)
4. [Cambio de las entidades Category y User a tipo de dato DTO](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/57002d6817fc6fce6c5aea4d6b91ddb006d690c9)
5. [Operación editar solucionada en User y Category](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/5025a14d9a6338fc1fc58d928b14e61907e842c9)

#### 📁 Most Contributed Files

- `src/main/java/es/urjc/chamazon/restControllers/CategoryRestController.java`
- `src/main/java/es/urjc/chamazon/restControllers/UserRestController.java`
- `src/main/java/es/urjc/chamazon/services/CategoryService.java`
- `src/main/java/es/urjc/chamazon/services/UserService.java`
- `src/main/java/es/urjc/chamazon/dto/CategoryMapper.java`

---

### 👤 Mario Martín Arriscado Santos — [M44ri0](https://github.com/M44ri0)

#### 🌟 Contribution Summary
Mario focused on enhancing the Product entity and its integration within the REST API. He developed the new Product REST controller, implemented DTOs and Mappers, and refactored the service and model layers accordingly. His work also included the implementation of dynamic queries to filter products by price, category, and rating, as well as improving filter reset and selection logic for a cleaner and more efficient user experience.


#### 📝 Notable Commits
1. [Añadir Filtrado - Consulta dinamica](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/3f4087193f8aa9f0584537c2b528b926f605be6f)
2. [Integracion mapStruct para hacer mapping de entidades DTO](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/a75d4433ba3f777730528faa5c1d8824d8b12ada)
3. [Añadir DTO Producto con su respectivo Mapper](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/47a3e3d77621375f01a9c91db066c0ad8d75da77)
4. [Integración API REST y modificaciones de Entidad Product, Controllers, Service, DTO entre otros](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/ff6b87b4c6c78b0e58d93ffbbaa3b17984cdb9d2)
5. [Commits sucesivos modificando rest, controller service](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/bcb287bf6597fa03521344c8d784cef289465e97)

#### 📁 Most Contributed Files

- `src/main/java/es/urjc/chamazon/controllers/ProductController.java`
- `src/main/java/es/urjc/chamazon/restControllers/ProductRestController.java`
- `src/main/java/es/urjc/chamazon/exception/NoSuchElementExceptionREST.java`
- `src/main/java/es/urjc/chamazon/services/ProductService.java`
- `src/main/java/es/urjc/chamazon/dto/ProductDTO.java`

---

### 👤 Víctor Espinosa — [CapitanPescanova](https://github.com/CapitanPescanova)

#### 🌟 Contribution Summary
Víctor was responsible for the ShoppingCar entity, implementing its model, service, and full REST controller, along with the corresponding DTOs and Mappers. He enabled the logic behind adding and removing products from the shopping cart and ensured seamless integration with other entities like Product and User. Furthermore, he contributed to the migration process to a persistent database, helping the team transition smoothly from in-memory data. He also played a key role in debugging and fixing critical issues throughout the project.

#### 📝 Notable Commits
1. [Relacción de entidades para BDD](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/38a996216d4a11ae64d1106968bc5bdf3e1f87df)
2. [Cambio de generación de objetos en ChamazonBDD. Creacion de carrito al crear un usuario.](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/372f14db3420a9f2fb54484f14968a3f5d853a17)
3. [Creacion DTO para shoppingCar](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/ff812628487efd6dea7f50ed156fe863fc9fca30)
4. [Modificacion para uso de ShoppingCar](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/d5789e2521cabfa8c3bf7001db31932269833ff3)
5. [Funcionalidad ShoppingCar con DTO por fin funcionando](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/242945a1f600e4c62e8660ca232782dab444cdf9)

#### 📁 Most Contributed Files

- `src/main/java/es/urjc/chamazon/services/UserService.java`
- `src/main/java/es/urjc/chamazon/services/ShoppingCarService.java`
- `src/main/java/es/urjc/chamazon/restControllers/RestShoppingCarController.java`
- `src/main/java/es/urjc/chamazon/dto/ShoppingCarDTO.java`
- `src/main/java/es/urjc/chamazon/controllers/ShoppingCarController.java`

---

## 👥 Team Members

- 👩‍💻 **Beatriz Sanz Granados** — [btrisss](https://github.com/btrisss) — `b.sanz.2022@alumnos.urjc.es`
- 👩‍💻 **Belén Collado Torres** — [BecotoGit](https://github.com/BecotoGit) — `b.collado.2021@alumnos.urjc.es` 
- 👨‍💻 **Mario Martín Arriscado Santos** — [M44ri0](https://github.com/M44ri0) — `m.martins.2021@alumnos.urjc.es`
- 👨‍💻 **Víctor Espinosa** — [CapitanPescanova](https://github.com/CapitanPescanova) — `v.espinosar.2023@alumnos.urjc.es`

---

<h4 align="center">Chamazon — Where security meets e-commerce, and code meets creativity.</h4>


