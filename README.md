<h1 align="center">Welcome to Chamazon! 🛒</h1>
<h3 align="center">Group 14 — Secure Web Development :)"</h3>

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

## 👑 User Permissions - ROLES

- **Admin**: Full control over the entire system.
- **Anonymus**: Can browse products and categories.
- **User**: Can browse, comment, add to cart, and complete purchases.

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
Beatriz developed and implemented the user registration form, ensuring proper validation and prevention of duplicate accounts. Secured user authentication by storing encrypted passwords in the database. Additionally, enhanced the comment system by integrating rich text editing, improving both frontend display and backend handling of formatted user input.

#### 📝 Notable Commits
1. [Evitar usuarios duplicados](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/5e980b87103c7c3b6ed0d6234284e4b2d4b991c8)
2. [Form de Registro](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/1f4db0cdbe4d70a25a8aca7ae5cd31f7a02caf51)
3. [Guardar password cifrada en BD al guardar usuario en BD](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/42686d6cbdd93e36d4e7274e288c58b77bbc74d0)
4. [Texto Enriquecido Completo](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/407a1d6e4d2580c5fc73423e1cb3bc922c1ef49d)
5. [Texto enriquecido en Front](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/1e649209fb403afd8a1a1f2a1a2b2a4b30cf5173)

#### 📁 Most Contributed Files

- `src/main/java/resources/js/quill.js`
- `src/main/java/es/urjc/chamazon/services/CommentService.java`
- `src/main/resources/templates/comment/commentForm.html`
- `src/main/java/es/urjc/chamazon/controllers/CommentController.java`
- `src/main/resources/templates/comment/commentList.html`

---

### 👤 Belén Collado Torres — [BecotoGit](https://github.com/BecotoGit)

#### 🌟 Contribution Summary
Belén focused on the User and Category entities, developing their REST controllers and integrating DTOs and Mappers accordingly. She ensured the proper functionality of user and category-related endpoints within the API. Additionally, she contributed to debugging and improving the Product entity, collaborating closely with Mario to resolve integration issues and enhance the structure of the RESTful services. Belén also created the Postman collection used to test the endpoints, helping the team validate the API behavior efficiently.

#### 📝 Notable Commits
1. [Nuevo primera implementación de las APIs rest](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/ab4e2c03c6d80a73edfd510072c87c7c3abc20ae)
2. [Arreglo de producto](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/315846d50845d0e682a094e4e95b5a1524c3075e)
3. [Creación de archivo para producto](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/0c14a1902bacb55398730eec94ab8ddac820de46)
4. [Nuevas redirecciones, ajustes de visualización de botones según ROL y corrección de errores](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/8da2a4821f4f4c0f5f61fa2643c68d9226c9c3ef)
5. [Página login y creación ROLES de usuario](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/354149da2dc9cefba9c6aa95c636204adecd5a09)

#### 📁 Most Contributed Files

- `src/main/java/es/urjc/chamazon/configurations/SecurityConfiguration.java`
- `src/main/java/es/urjc/chamazon/configurations/SecurityUtils.java`
- `src/main/java/es/urjc/chamazon/services/FileStorageService.java`
- `src/main/java/es/urjc/chamazon/restControllers/CommentRestController.java`
- `src/main/java/es/urjc/chamazon/restControllers/UserRestController.java`

---

### 👤 Mario Martín Arriscado Santos — [M44ri0](https://github.com/M44ri0)

#### 🌟 Contribution Summary
Mario took responsibility for enhancing problematic elements, improving filters, securing confidential data in DTOs from phase 2, and integrating/improving new features implemented for phase 3. He implemented a sanitization service using JSOUP, fixed rich text functionality issues, and made several security improvements to protect user data. His work focused on application's stability making sure the user experience is good.

#### 📝 Notable Commits
1. [Mejora Filtro Productos - Funcional - Consulta dinamica (Commits sucesivos)](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/defc37c208b778623ae6e27719969d02447dd4e8)
2. [Integracion Certificado - KeyStore](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/6101c3c226499d5e13855d76666d22a2145ec8cf)
3. [JSOUP - Sanitizar Entradas](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/74b843342da9d81298e4b4d8b0cb2141b537608e)
4. [Modificación DTOs - Seguridad (Commits sucesivos)](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/5f7b6223d1601fbe420cb76ecb92045877611de3)
5. [Arreglos Texto Enriquecido - QuillJS (Commits sucesivos)](https://github.com/DWS-2025/project-grupo-14_chamazon/commit/bf691fb4174a455f5051286c563dc9b53d5f05bd)

#### 📁 Most Contributed Files

- `src\main\java\es\urjc\chamazon\services\SanitizationService.java`
- `src\main\java\es\urjc\chamazon\services\ProductService.java`
- `src\main\java\es\urjc\chamazon\controllers\ProductController.java`
- `src\main\resources\static\js\quill.js`
- `src\main\java\es\urjc\chamazon\dto\CommentUserDTO.java`

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


