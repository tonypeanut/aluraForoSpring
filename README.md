# ForoSpring

## Descripción

**ForoSpring** es un proyecto desarrollado como parte del programa Oracle Next Education G6 en colaboración con Alura. Este proyecto tiene como objetivo crear un foro de discusión funcional con un back-end robusto utilizando Spring Boot 3. El foro permite a los usuarios gestionar tópicos de discusión a través de una API RESTful.

## Características

- **API REST Completa**: Soporta operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para la gestión de tópicos.
- **Gestión de Tópicos**: Permite a los usuarios crear nuevos tópicos, visualizar todos los tópicos, ver detalles de un tópico específico, actualizar información y eliminar tópicos.
- **Validaciones y Seguridad**: Implementa validaciones según las reglas de negocio y un sistema de autenticación/autorización.
- **Persistencia de Datos**: Utiliza una base de datos relacional para la gestión eficiente de la información.
- **Desarrollo Ágil**: Uso de la metodología ágil con Trello para gestionar y seguir el progreso del proyecto.

## Requisitos

- Java 17 o superior
- Spring Boot 3
- Maven
- Base de datos relacional (MySQL, PostgreSQL, etc.)
- Postman o cualquier otra herramienta para pruebas de API

## Instalación

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/ForoSpring.git
   cd ForoSpring

    Configurar la base de datos:
        Crear una base de datos en tu SGBD preferido.
        Actualizar el archivo application.properties en src/main/resources con tus credenciales de base de datos.

    Compilar y ejecutar la aplicación:

    bash

    mvn clean install
    mvn spring-boot:run

Uso
Endpoints Principales

    Crear un nuevo tópico:
        POST /api/topicos
        Body: { "titulo": "Nuevo Tópico", "mensaje": "Contenido del tópico", "autor": "Usuario" }

    Mostrar todos los tópicos:
        GET /api/topicos

    Mostrar un tópico específico:
        GET /api/topicos/{id}

    Actualizar un tópico:
        PUT /api/topicos/{id}
        Body: { "titulo": "Título Actualizado", "mensaje": "Contenido Actualizado" }

    Eliminar un tópico:
        DELETE /api/topicos/{id}

Autenticación

Para acceder a algunos endpoints, es necesario estar autenticado. Implementa tu servicio de autenticación y autorización preferido.
Contribuir

    Haz un fork del proyecto.
    Crea una nueva rama (git checkout -b feature/nueva-funcionalidad).
    Realiza tus cambios y haz commit (git commit -am 'Agrega nueva funcionalidad').
    Sube tus cambios (git push origin feature/nueva-funcionalidad).
    Abre un Pull Request.

Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo LICENSE para más detalles.
Contacto

Para más información, puedes contactarnos en tonypeanut93@gmail.com.

¡Gracias por contribuir a ForoSpring!