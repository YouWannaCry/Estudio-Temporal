const initialCode = `import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

public class PrimerWorkflow {

    @WorkflowInterface
    public interface SaludoWorkflow {
        @WorkflowMethod
        String saludar(String nombre);
    }

    public static class SaludoWorkflowImpl implements SaludoWorkflow {
        @Override
        public String saludar(String nombre) {
            return "Hola " + nombre;
        }
    }
}`;

const editor = document.querySelector("#editor");
const output = document.querySelector("#output");
const status = document.querySelector("#status");
const compileButton = document.querySelector("#compile");
const saved = localStorage.getItem("temporal-lab-code-01");

editor.value = saved ?? initialCode;
editor.addEventListener("input", () => localStorage.setItem("temporal-lab-code-01", editor.value));
editor.addEventListener("keydown", event => {
  if (event.key === "Tab") {
    event.preventDefault();
    const start = editor.selectionStart;
    editor.setRangeText("    ", start, editor.selectionEnd, "end");
  }
  if (event.key === "Enter" && (event.ctrlKey || event.metaKey)) compile();
});

document.querySelector("#reset").addEventListener("click", () => {
  editor.value = initialCode;
  localStorage.setItem("temporal-lab-code-01", initialCode);
  show("LISTO", "Código restaurado.");
});

compileButton.addEventListener("click", compile);

async function compile() {
  compileButton.disabled = true;
  show("COMPILANDO", "Verificando sintaxis y tipos con el compilador de Java…");

  try {
    const response = await fetch("/api/compile", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ code: editor.value })
    });
    if (!response.ok) throw new Error(`Solicitud rechazada (${response.status})`);

    const result = await response.json();
    show(result.success ? "CORRECTO" : "ERROR", result.diagnostics.join("\n"), result.success);
    if (result.success) {
      localStorage.setItem("temporal-lab-complete-01", "true");
      updateProgress();
    }
  } catch (error) {
    show("ERROR", `${error.message}. ¿Está corriendo la aplicación Spring Boot?`, false);
  } finally {
    compileButton.disabled = false;
  }
}

function show(label, message, success) {
  status.textContent = label;
  status.className = success === true ? "success" : success === false ? "error" : "";
  output.textContent = message;
}

function updateProgress() {
  const completed = localStorage.getItem("temporal-lab-complete-01") === "true" ? 1 : 0;
  document.querySelector("#progress-label").textContent = `${completed} / 8 módulos`;
  document.querySelector("#progress-bar").style.width = `${completed * 12.5}%`;
}

updateProgress();
