package dev.estudio.temporal;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import org.springframework.stereotype.Service;

import dev.estudio.temporal.CompileController.CompileResponse;

@Service
class JavaCompileService {

	CompileResponse compile(String code) {
		var compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			return new CompileResponse(false, List.of("Hace falta ejecutar la aplicación con un JDK, no solo un JRE."));
		}

		var diagnostics = new DiagnosticCollector<JavaFileObject>();
		var source = new Source(code);
		var options = List.of("-classpath", System.getProperty("java.class.path"), "-proc:none");
		var success = compiler.getTask(null, null, diagnostics, options, null, List.of(source)).call();
		var messages = diagnostics.getDiagnostics().stream()
				.map(d -> "Línea " + d.getLineNumber() + ": " + d.getMessage(Locale.forLanguageTag("es")))
				.toList();

		return new CompileResponse(success, success ? List.of("Compilación correcta. Primer paso superado.") : messages);
	}

	private static final class Source extends SimpleJavaFileObject {
		private final String code;

		Source(String code) {
			super(URI.create("string:///PrimerWorkflow.java"), Kind.SOURCE);
			this.code = code;
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return code;
		}
	}
}
