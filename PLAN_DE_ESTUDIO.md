# Plan de estudio — Temporal.io con Java y Spring Boot

## Objetivo

Aprender Temporal desde el modelo mental hasta una aplicación Spring Boot desplegable. Cada módulo combina teoría breve, código Java, una prueba y un criterio claro de finalización.

Duración sugerida: 4 semanas, 45 a 60 minutos por sesión.

## Cómo usar este laboratorio

1. Leer la teoría del módulo.
2. Revisar el ejemplo y predecir qué ocurrirá.
3. Modificar el código en el editor.
4. Compilar o ejecutar la prueba.
5. Provocar un error de forma intencional.
6. Corregirlo y explicar por qué funciona.
7. Completar el desafío antes de desbloquear el siguiente módulo.

Comandos principales:

```powershell
.\mvnw.cmd spring-boot:run
.\mvnw.cmd test
docker compose up --build
```

## Equivalencias con Spring

| Temporal | Idea cercana en Spring | Diferencia importante |
|---|---|---|
| Workflow | Servicio orquestador | Se reconstruye mediante replay y debe ser determinista |
| Activity | Servicio de integración | Puede hacer HTTP, SQL, correo y otros efectos externos |
| Worker | Proceso consumidor | Consulta una Task Queue y ejecuta código registrado |
| Workflow Client | Cliente de servicio | Inicia, consulta y envía mensajes a Workflows |
| Task Queue | Cola lógica | Temporal administra entrega y persistencia |
| Temporal Server | Infraestructura durable | Guarda historial; no ejecuta el código Java del negocio |

---

## Módulo 1 — Modelo mental

**Estado:** implementado en la versión 1.

### Objetivo

Comprender qué problema resuelve Temporal y separar Workflow, Activity, Worker y Temporal Server.

### Teoría

- Ejecución durable e historial de eventos.
- Diferencia entre guardar estado y reconstruir estado.
- Responsabilidad del Temporal Server.
- Responsabilidad del Worker Java.
- Task Queues como unión entre ambos.

### Práctica

- Compilar una interfaz marcada con `@WorkflowInterface`.
- Reconocer el método marcado con `@WorkflowMethod`.
- Romper el código, leer el error del compilador y corregirlo.

### Criterio de finalización

Poder explicar con palabras propias:

1. Dónde vive el estado durable.
2. Quién ejecuta el código Java.
3. Por qué una Activity y un Workflow tienen reglas diferentes.

---

## Módulo 2 — Primer Workflow con Java SDK

### Objetivo

Ejecutar un Workflow real contra un servidor Temporal local.

### Teoría

- `@WorkflowInterface` y `@WorkflowMethod`.
- Implementación del Workflow.
- `WorkflowClient`, `WorkerFactory` y `Worker`.
- Workflow ID, Workflow Type y Task Queue.
- Inicio síncrono y asíncrono.

### Implementación

1. Levantar Temporal Server y Temporal UI para desarrollo local.
2. Crear `SaludoWorkflow` y `SaludoWorkflowImpl`.
3. Registrar la implementación en un Worker.
4. Iniciar el Workflow desde un cliente Java.
5. Buscar la ejecución en Temporal UI.
6. Detener el Worker, reiniciarlo y observar continuidad.

### Ejercicio

Crear un Workflow que reciba un nombre y devuelva un saludo con un identificador de ejecución.

### Criterio de finalización

- El Workflow termina correctamente.
- Aparece en Temporal UI.
- Su historial puede leerse y explicarse.

---

## Módulo 3 — Integración con Spring Boot

### Objetivo

Usar Temporal dentro de una aplicación Spring sin ocultar el funcionamiento aprendido en el módulo anterior.

### Teoría

- Starter de Temporal para Spring Boot.
- Configuración de conexión y namespace.
- Registro explícito y auto-discovery de Workers.
- Inyección de `WorkflowClient`.
- Activities como beans de Spring.

### Implementación

1. Agregar `temporal-spring-boot-starter`.
2. Configurar la conexión local en `application.yml`.
3. Registrar Workflow y Activity.
4. Crear `POST /workflows/saludos`.
5. Crear `GET /workflows/saludos/{id}`.
6. Mantener el controlador como adaptador: la lógica pertenece al Workflow.

### Ejercicio

Iniciar un Workflow desde un controlador REST y devolver su Workflow ID sin esperar su finalización.

### Criterio de finalización

La API inicia el Workflow, responde rápido y permite consultar su estado posteriormente.

---

## Módulo 4 — Determinismo y replay

### Objetivo

Entender la regla más importante de Temporal: el mismo historial debe producir las mismas decisiones.

### Teoría

- Replay del historial.
- Código determinista.
- Por qué HTTP, base de datos y reloj del sistema no pertenecen al Workflow.
- Uso de APIs seguras ofrecidas por Temporal.
- Separación entre decisiones y efectos externos.

### Práctica

Clasificar fragmentos como seguros o inseguros:

- `repository.save(...)` dentro del Workflow: incorrecto.
- `RestClient` dentro del Workflow: incorrecto.
- Activity para persistir o llamar APIs: correcto.
- `Workflow.sleep(...)` para temporizadores durables: correcto.

### Ejercicio

Refactorizar un servicio Spring que mezcla orquestación, repositorio y llamada HTTP. El Workflow conserva decisiones; las Activities contienen I/O.

### Criterio de finalización

El Workflow supera una prueba de replay y no contiene llamadas externas directas.

---

## Módulo 5 — Resiliencia

### Objetivo

Diseñar procesos que sobrevivan fallos temporales sin duplicar efectos importantes.

### Teoría

- Activity timeouts.
- Retry Policy y backoff.
- Errores reintentables y no reintentables.
- Idempotencia.
- Heartbeats para trabajo prolongado.
- Cancelación.

### Implementación

1. Crear una Activity de cobro simulada.
2. Hacer que falle las primeras ejecuciones.
3. Configurar reintentos y timeout.
4. Registrar un idempotency key usando el Workflow ID.
5. Mostrar los intentos en Temporal UI.

### Ejercicio

Procesar un pago sin cobrar dos veces aunque la Activity falle después de comunicarse con el proveedor.

### Criterio de finalización

El proceso se recupera de un fallo temporal y evita duplicados verificables.

---

## Módulo 6 — Signals, Queries y Updates

### Objetivo

Construir Workflows de larga duración que reciban mensajes y expongan estado.

### Teoría

- Signal: mensaje asíncrono sin respuesta inmediata.
- Query: lectura del estado sin modificarlo.
- Update: cambio validado con resultado para el cliente.
- Timers y esperas durables.
- Child Workflows y Continue-As-New.

### Implementación

1. Crear un Workflow de aprobación de compra.
2. Esperar una decisión sin bloquear un hilo Java.
3. Enviar aprobación o rechazo mediante Signal.
4. Consultar estado mediante Query.
5. Cambiar datos permitidos mediante Update.
6. Agregar vencimiento automático.

### Ejercicio

Una compra espera aprobación hasta 24 horas. Si no recibe respuesta, vence. Su estado siempre puede consultarse.

### Criterio de finalización

Signal, Query, Update y vencimiento tienen pruebas independientes y comportamiento visible en Temporal UI.

---

## Módulo 7 — Testing con JUnit 5

### Objetivo

Probar procesos durables sin esperar horas ni depender de un servidor externo.

### Para qué se usa JUnit 5

- Confirmar resultados automáticamente.
- Verificar reintentos, errores y compensaciones.
- Simular Activities sin llamar servicios reales.
- Detectar cambios que rompen módulos anteriores.
- Integrarse con Maven, Spring Boot y herramientas Java habituales.

### Teoría

- `TestWorkflowEnvironment`.
- Servidor Temporal en memoria.
- Salto automático del tiempo.
- Activities reales versus simuladas.
- Pruebas unitarias, de integración y end-to-end.

### Implementación

1. Agregar `temporal-testing` con la misma versión del Java SDK.
2. Registrar Workflow y Activities de prueba.
3. Simular respuestas y fallos de Activities.
4. Probar un timer de 24 horas en segundos.
5. Integrar casos necesarios con `@SpringBootTest`.

### Ejercicio

Probar que una compra vence después de 24 horas y que una aprobación previa cancela el vencimiento.

### Criterio de finalización

Las pruebas son repetibles, rápidas y no requieren Temporal Server ni servicios externos.

---

## Módulo 8 — Producción

### Objetivo

Preparar Workers y Workflows para cambios, observabilidad y despliegue seguro.

### Teoría

- Compatibilidad con historiales existentes.
- Versionado del código de Workflow.
- Despliegue gradual de Workers.
- Métricas, trazas y logs.
- Namespaces y retención.
- Temporal Cloud versus servidor administrado por el equipo.
- Seguridad, credenciales y cifrado de payloads.

### Implementación

1. Separar API y Worker como procesos desplegables.
2. Agregar Actuator y métricas.
3. Crear imágenes Docker reproducibles.
4. Configurar health checks.
5. Simular una actualización con ejecuciones abiertas.
6. Documentar rollback y recuperación.

### Ejercicio

Cambiar un Workflow mientras existe una ejecución esperando aprobación y demostrar que puede terminar correctamente.

### Criterio de finalización

La versión nueva convive con ejecuciones anteriores, ofrece señales operativas y tiene procedimiento de despliegue y rollback.

---

## Proyecto final — Flujo de pedidos

Construir una API Spring Boot con este proceso:

```text
POST /orders
    ↓
OrderWorkflow
    ├── validar pedido
    ├── reservar stock
    ├── procesar pago
    ├── esperar aprobación si corresponde
    ├── confirmar envío
    └── compensar pasos completados si falla
```

Debe incluir:

- Workflow y Activities separados.
- Inicio y consulta mediante REST.
- Retry Policy y timeouts.
- Idempotencia en pago y reserva.
- Signal de aprobación.
- Query de estado.
- Compensaciones tipo Saga.
- Pruebas con tiempo virtual.
- Historial visible en Temporal UI.
- Ejecución local y mediante Docker.

## Orden de desarrollo de la aplicación web

| Versión | Entrega |
|---|---|
| 1 | Módulo 1, editor, compilador y progreso local |
| 2 | Temporal Server local, Temporal UI y primer Workflow real |
| 3 | Integración Spring Boot y API REST |
| 4 | Laboratorio de determinismo y replay |
| 5 | Simulador de reintentos, timeouts e idempotencia |
| 6 | Laboratorio interactivo de mensajes y timers |
| 7 | Ejercicios JUnit con tiempo virtual |
| 8 | Proyecto final, Docker y guía de producción |

## Referencias oficiales

- Java SDK: <https://docs.temporal.io/develop/java>
- Spring Boot: <https://docs.temporal.io/develop/java/integrations/spring-boot-integration>
- Testing: <https://docs.temporal.io/develop/java/best-practices/testing-suite>
- Temporal CLI: <https://docs.temporal.io/cli>
- Ejemplos Java: <https://github.com/temporalio/samples-java>

