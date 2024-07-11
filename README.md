<h1 align="center" style="font-weight: bold;">RestfullAPI-Spring 💻</h1>

<p align="center">
 <a href="#tech">Technologies</a> • 
 <a href="#started">Getting Started</a> • 
  <a href="#routes">API Endpoints</a> •
 <a href="#colab">Collaborators</a> •
 <a href="#contribute">Contribute</a>
</p>

<p align="center">
    <b>A RestFull API that registers products with CRUD operations.</b>
</p>

<h2 id="technologies">💻 Technologies</h2>

- Java
- PostgreSQL
- SpringBoot
- Docker

<h2 id="started">🚀 Getting started</h2>

<h3>Prerequisites</h3>

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Git](https://git-scm.com/)
- [Docker](https://www.docker.com/products/docker-desktop/)

<h3>Cloning</h3>

```bash
git clone https://github.com/GuilhermeLuan/RestfullAPI-Spring
```

<h3>Starting</h3>

```bash
cd RestfullAPI-Sping
docker compose up
./mvnw clean install
./mvnw spring-boot:run
```

<h2 id="routes">📍 API Endpoints</h2>

| route                                  | description                                                   |
|----------------------------------------|---------------------------------------------------------------|
| <kbd>GET /api/products </kbd>          | retrieves a list of all products.                             |
| <kbd>GET /api/products/{UUID} </kbd>   | retrieves a specific product by its unique identifier (UUID). |
| <kbd>PUT /api/products/{UUID}</kbd>    | modifies a specific product by its unique identifier (UUID).  |
| <kbd>POST /api/products</kbd>          | creates a product in the database.                            |
| <kbd>DELETE /api/products/{UUID}</kbd> | deletes a specific product by its unique identifier (UUID).   |

<h3 id="get-auth-detail">GET /api/products</h3>

**RESPONSE**
```json
[
  {
    "idProduct": "2de63670-64b8-4333-9365-30b980cc3f16",
    "name": "Iphone 14 PRO",
    "value": 7900.00,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/products/2de63670-64b8-4333-9365-30b980cc3f16"
      }
    ]
  }
]
```

<h3 id="get-auth-detail">GET /api/products/{UUID}</h3>

**RESPONSE**
```json
[
  {
    "idProduct": "2de63670-64b8-4333-9365-30b980cc3f16",
    "name": "Iphone 14 PRO",
    "value": 7900.00,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/products/2de63670-64b8-4333-9365-30b980cc3f16"
      }
    ]
  }
]
```

<h3 id="post-auth-detail">PUT /api/products/{UUID}</h3>


**REQUEST**
```json
{
  "name": "Iphone 14 PRO Red",
  "value": 7900
}
```

**RESPONSE**
```json
{
  "idProduct": "2de63670-64b8-4333-9365-30b980cc3f16",
  "name": "Iphone 14 PRO Red",
  "value": 7900
}
```

<h3 id="post-auth-detail">POST /api/products</h3>


**REQUEST**
```json
{
  "name": "Iphone 14",
  "value": 5000
}
```

**RESPONSE**
```json
{
  "idProduct": "2de63670-64b8-4333-9365-30b980cc3f16",
  "name": "Iphone 14",
  "value": 5000
}
```

<h3 id="post-auth-detail">DELETE /api/products/{UUID}</h3>


**RESPONSE**
```text
"Product deleted successfully"
```

<h2 id="colab">🤝 Collaborators</h2>

Special thank you for all people that contributed for this project.

<table>
  <tr>
    <td align="center">
      <a href="#">
        <img src="https://avatars.githubusercontent.com/u/32211071?v=4" width="100px;" alt="Guilherme Luan Profile Picture"/><br>
        <sub>
          <b>Guilherme Luan</b>
        </sub>
      </a>
    </td>
  </tr>
</table>

<h2 id="contribute">📫 Contribute</h2>


1. `git clone https://github.com/GuilhermeLuan/RestfullAPI-Spring`
2. `git checkout -b feature/NAME`
3. Follow commit patterns
4. Open a Pull Request explaining the problem solved or feature made, if exists, append screenshot of visual modifications and wait for the review!

<h3>Documentations that might help</h3>

[📝 How to create a Pull Request](https://www.atlassian.com/br/git/tutorials/making-a-pull-request)

[💾 Commit pattern](https://gist.github.com/joshbuchea/6f47e86d2510bce28f8e7f42ae84c716)