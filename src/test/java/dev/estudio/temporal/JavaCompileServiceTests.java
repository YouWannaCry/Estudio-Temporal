package dev.estudio.temporal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class JavaCompileServiceTests {

	@Test
	void distingueCodigoValidoDeCodigoRoto() {
		var compiler = new JavaCompileService();
		var valid = "public class PrimerWorkflow { public String saludo() { return \"hola\"; } }";
		var broken = "public class PrimerWorkflow { public String saludo() { return ; } }";

		assertTrue(compiler.compile(valid).success());
		assertFalse(compiler.compile(broken).success());
	}
}
