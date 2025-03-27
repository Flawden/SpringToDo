# Spring ToDo

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen)
![Java](https://img.shields.io/badge/Java-21-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Redis](https://img.shields.io/badge/Redis-7-red)

Spring ToDo — это простое RESTful API веб-приложение для управления списком дел (ToDo). Оно позволяет создавать, просматривать, обновлять и удалять задачи с использованием пагинации, кэширования и валидации данных.

## 📋 Описание проекта

Проект разработан в рамках практического задания для создания RESTful API с использованием Spring Boot. Приложение позволяет вести список дел с поддержкой следующих функций:

- Создание, просмотр, обновление и удаление задач.
- Пагинация с использованием параметров `limit` и `offset`.
- Кэширование с помощью Spring Cache и Redis.
- Валидация входных данных через DTO.
- Документация API с помощью OpenAPI и Swagger.
- Миграции базы данных с использованием Liquibase.
- Юнит и интеграционные тесты.