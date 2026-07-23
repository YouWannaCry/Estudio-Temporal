# Temporal Lab

Laboratorio local para estudiar Temporal.io con Java y Spring Boot.

Plan completo: [PLAN_DE_ESTUDIO.md](PLAN_DE_ESTUDIO.md).

Estado para próximas sesiones: [ESTADO.md](ESTADO.md).

## Ejecutar con Java

```powershell
.\mvnw.cmd spring-boot:run
```

Abrir <http://localhost:8080>.

## Ejecutar con Docker

```powershell
docker compose up --build
```

## Probar

```powershell
.\mvnw.cmd test
```

La versión 1 compila código Java con tipos de Temporal, pero no lo ejecuta. El servidor Temporal local entra en la lección del primer Workflow real.
