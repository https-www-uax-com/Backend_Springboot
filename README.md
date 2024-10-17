# LINKS

FRONTEND: https://github.com/https-www-uax-com/Frontend_React_Node_.git

BACKEND: https://github.com/https-www-uax-com/Backend_Springboot.git

SCRIPT: https://github.com/https-www-uax-com/Limpieza-de-datos-Python-.git

# PARTICIPANTES

Jaime López Díaz

Juan Manuel Rodriguez

Gwendal Saget

Marcos García Benito Bilbao


# Sistema de Análisis de Datos De Umbrella Corporation

Este proyecto es una aplicación de laboratorio que gestiona muestras, experimentos, investigadores.

## Tabla de Contenidos

* [Introducción](#introducci%C3%B3n)
* [Descripción General de los Servicios](#descripci%C3%B3n-general-de-los-servicios)
  * [UsuarioService](#usuarioservice)
  * [SampleService](#sampleservice)
  * [ResearcherService](#researcherservice)
  * [LabService](#labservice)
  * [ExperimentService](#experimentservice)
  * [BiologicalDataService](#biologicaldataservice)
  * [AuthService](#authservice)
* [Implementación de Concurrencia](#implementaci%C3%B3n-de-concurrencia)
  * [Tipos de Concurrencia Utilizados](#tipos-de-concurrencia-utilizados)
  * [Razones para Implementar Concurrencia](#razones-para-implementar-concurrencia)
  * [Concurrencia en los Servicios](#concurrencia-en-los-servicios)
* [Patrones de Diseño Utilizados](#patrones-de-dise%C3%B1o-utilizados)
* [Casos de Uso de la Aplicación](#casos-de-uso-de-la-aplicaci%C3%B3n)
* [Usuarios Precreados para Pruebas](#usuarios-precreados-para-pruebas)
* [Conclusión](#conclusi%C3%B3n)

## Introducción

La aplicación está diseñada para gestionar la información de un laboratorio, incluyendo muestras biológicas, experimentos compuestos de varias muestras, investigadores que gestionan varios experimentos y usuarios que consultaran datos biologicos de las muestras analizadas. Utiliza programación concurrente para procesar grandes cantidades de datos de manera eficiente. Además, se implementan patrones de diseño y prácticas recomendadas para asegurar la escalabilidad y mantenibilidad del código.

## Descripción General de los Servicios

### UsuarioService

* **Funcionalidad**: Gestiona las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para los usuarios.
* **Características Clave**:
  * Mapeo entre entidades `Usuario` y DTOs `UsuarioDTO`.
  * Validación de existencia de usuarios.
  * Relación con roles y credenciales.


### SampleService

* **Funcionalidad**: Gestiona muestras biológicas, incluyendo su procesamiento y asociación con experimentos.
* **Características Clave**:
  * Procesamiento concurrente de muestras.
  * Asignación automática de muestras a experimentos y laboratorios.
  * Uso de `CompletableFuture` y `ExecutorService` para manejar tareas asíncronas.


### ResearcherService

* **Funcionalidad**: Gestiona investigadores y sus experimentos asociados.
* **Características Clave**:
  * Procesamiento asíncrono de investigadores y sus experimentos.
  * Uso de sincronización para manejar recursos compartidos.
  * Implementación de un pool de hilos personalizado.


### LabService

* **Funcionalidad**: Gestiona laboratorios y los experimentos asociados a ellos.
* **Características Clave**:
  * Procesamiento asíncrono de laboratorios.
  * Coordinación con `ExperimentService` para procesar experimentos relacionados.
  * Uso de `ExecutorService` para tareas concurrentes.


### ExperimentService

* **Funcionalidad**: Gestiona experimentos y las muestras asociadas.
* **Características Clave**:
  * Procesamiento concurrente de experimentos usando `ForkJoinPool`.
  * Sincronización para asegurar consistencia en operaciones críticas.
  * Integración con `SampleService` para procesar muestras.


### BiologicalDataService

* **Funcionalidad**: Gestiona los datos biológicos de las muestras.
* **Características Clave**:
  * Procesamiento de análisis biológicos.
  * Generación de resultados de análisis.
  * Uso de métodos transaccionales para asegurar integridad de datos.


### AuthService

* **Funcionalidad**: Gestiona la autenticación y registro de usuarios.
* **Características Clave**:
  * Validación de credenciales de usuario.
  * Encriptación de contraseñas usando `BCryptPasswordEncoder`.
  * Gestión de roles y permisos.
  * Implementación de validaciones personalizadas.

## Implementación de Concurrencia

### Tipos de Concurrencia Utilizados

* **Programación Asíncrona con `@Async`**: Permite ejecutar métodos de forma asíncrona en un hilo separado.
* **`CompletableFuture`**: Facilita el manejo de tareas asíncronas y la composición de operaciones.
* **`ExecutorService` y `ThreadPool`**: Gestionan un pool de hilos para ejecutar tareas concurrentes.
* **`ForkJoinPool`**: Utilizado para tareas que pueden ser divididas en subtareas más pequeñas y ejecutadas en paralelo.
* **Sincronización con `synchronized`**: Asegura que solo un hilo acceda a un bloque de código crítico a la vez.

### Razones para Implementar Concurrencia

* **Mejorar el Rendimiento**: Procesar múltiples muestras y experimentos en paralelo reduce el tiempo total de ejecución.
* **Escalabilidad**: La aplicación puede manejar grandes volúmenes de datos sin afectar la capacidad de respuesta.
* **Eficiencia en el Uso de Recursos**: Aprovecha al máximo los recursos del sistema al distribuir la carga de trabajo entre múltiples hilos.
* **Respuesta Rápida al Usuario**: Las operaciones intensivas se ejecutan en segundo plano, manteniendo la interfaz de usuario receptiva.

### Concurrencia en los Servicios

* **SampleService**:
  * Procesa muestras de forma concurrente utilizando `ExecutorService` con un pool de hilos.
  * Asigna muestras a experimentos y laboratorios de manera sincronizada para evitar condiciones de carrera.
* **ResearcherService**:
  * Procesa investigadores y sus experimentos asociados de forma asíncrona.
  * Utiliza sincronización para acceder a recursos compartidos como experimentos.
* **LabService**:
  * Procesa laboratorios y coordina con `ExperimentService` para procesar experimentos asociados.
  * Ejecuta tareas concurrentes utilizando un pool de hilos dedicado.
* **ExperimentService**:
  * Procesa experimentos y sus muestras en paralelo utilizando `ForkJoinPool`.
  * Asegura la consistencia de datos mediante sincronización en operaciones críticas.
* **BiologicalDataService**:
  * Procesa análisis de datos biológicos de muestras.
  * Aunque no utiliza concurrencia explícitamente, interactúa con servicios que sí lo hacen y asegura transacciones atómicas.

## Patrones de Diseño Utilizados

* **Data Transfer Object (DTO)**: Separa la capa de presentación de la lógica de negocio y las entidades de dominio.
* **Repository Pattern**: Aísla la capa de acceso a datos, permitiendo intercambiar fácilmente la implementación de persistencia.
* **Service Layer**: Centraliza la lógica de negocio y coordina las operaciones entre repositorios y controladores.
* **Singleton**: Implementado implícitamente en algunos servicios para asegurar una única instancia gestionada por el contenedor de Spring.
