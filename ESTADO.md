# Estado del proyecto

Última actualización: 23 de julio de 2026.

## Propósito

Laboratorio web local para aprender Temporal.io con Java y Spring Boot. La interfaz muestra teoría a la izquierda y un editor con compilación Java a la derecha.

## Ubicación

```text
C:\Users\arodr\Documents\Estudio\Temporal
```

## Estado actual

Versión 1 terminada.

- Módulo 1 implementado: modelo mental de Workflow, Activity, Worker y Temporal Server.
- Módulos 2 a 8 visibles pero bloqueados.
- Editor Java funcional.
- Compilación real mediante el JDK; el código recibido no se ejecuta.
- Progreso guardado en `localStorage` del navegador.
- Ejecución local con Maven Wrapper.
- Dockerfile y Compose creados.
- Plan de los ocho módulos documentado en `PLAN_DE_ESTUDIO.md`.

## Tecnologías fijadas

- Java 17.
- Spring Boot 4.1.0.
- Temporal Java SDK 1.37.0.
- Maven Wrapper.
- JUnit 5.
- HTML, CSS y JavaScript sin framework frontend.

Mantener las dependencias `temporal-sdk` y `temporal-testing` en la misma versión cuando se agregue testing de Workflows.

## Verificaciones realizadas

- `mvnw test`: 2 pruebas correctas.
- Página principal: HTTP 200.
- Endpoint `/api/compile`: compila código válido y rechaza código roto.
- `docker compose config`: configuración válida.
- La imagen Docker todavía no fue construida ni ejecutada de extremo a extremo.
- La aplicación no queda ejecutándose después de las verificaciones.

## Cómo ejecutar

```powershell
cd C:\Users\arodr\Documents\Estudio\Temporal
.\mvnw.cmd spring-boot:run
```

Abrir <http://localhost:8080>.

Con Docker:

```powershell
docker compose up --build
```

Detener con `Ctrl+C`.

## Decisiones importantes

1. Una sola aplicación Spring Boot; no React ni frontend separado.
2. Servidor ligado a `127.0.0.1` por defecto.
3. Docker publica únicamente `127.0.0.1:8080`.
4. El compilador acepta hasta 50.000 caracteres.
5. El compilador verifica código, pero nunca lo ejecuta.
6. Temporal Server no se agregó todavía: entra cuando exista el primer Workflow real.
7. La teoría debe ser breve y cada lección debe terminar con práctica verificable.
8. No desbloquear un módulo solo por leerlo; debe existir una comprobación automática.

## Archivos principales

```text
PLAN_DE_ESTUDIO.md                         plan completo
README.md                                  ejecución rápida
pom.xml                                    dependencias
compose.yaml                               ejecución Docker
src/main/java/.../CompileController.java   API del compilador
src/main/java/.../JavaCompileService.java  compilación segura
src/main/resources/static/index.html       contenido del módulo 1
src/main/resources/static/app.js           editor y progreso
src/main/resources/static/styles.css       interfaz
src/test/java/...                          comprobaciones automáticas
```

## Limitaciones conocidas

- Solo existe contenido interactivo para el módulo 1.
- El editor no tiene autocompletado ni resaltado de sintaxis.
- El progreso es local al navegador y puede perderse al borrar sus datos.
- El compilador requiere un JDK completo.
- No hay Temporal Server, Temporal UI, Worker ni Workflow en ejecución todavía.
- No hay repositorio Git inicializado dentro del proyecto.

## Próxima sesión

Construir la versión 2:

1. Revisar `PLAN_DE_ESTUDIO.md`, módulo 2.
2. Elegir la forma mínima y oficial de levantar Temporal para desarrollo local.
3. Agregar Temporal Server y Temporal UI.
4. Crear `SaludoWorkflow`, su implementación y Worker.
5. Iniciar el Workflow desde Java.
6. Mostrar su Workflow ID e historial en la aplicación web.
7. Agregar una prueba automática.
8. Desbloquear el módulo 2 solo cuando la ejecución real funcione.

## Frase para retomar

> Continuá `C:\Users\arodr\Documents\Estudio\Temporal`. Leé `ESTADO.md` y `PLAN_DE_ESTUDIO.md`, verificá la versión 1 y construí la versión 2 sin rehacer lo existente.

