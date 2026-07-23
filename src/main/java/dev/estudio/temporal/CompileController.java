package dev.estudio.temporal;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/compile")
class CompileController {

	private final JavaCompileService compiler;

	CompileController(JavaCompileService compiler) {
		this.compiler = compiler;
	}

	@PostMapping
	CompileResponse compile(@RequestBody CompileRequest request) {
		if (request.code() == null || request.code().isBlank() || request.code().length() > 50_000) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El código debe tener entre 1 y 50.000 caracteres");
		}
		return compiler.compile(request.code());
	}

	record CompileRequest(String code) {}

	record CompileResponse(boolean success, List<String> diagnostics) {}
}
