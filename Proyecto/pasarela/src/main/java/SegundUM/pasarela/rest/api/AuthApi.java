package SegundUM.pasarela.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import SegundUM.pasarela.rest.dto.LoginRequest;
import SegundUM.pasarela.rest.dto.LoginResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Tag(name = "Autenticacion", description = "Operaciones de login y logout mediante credenciales")
public interface AuthApi {

    @Operation(summary = "Login", description = "Autentica al usuario y devuelve un JWT en el cuerpo y en una cookie HttpOnly")
    ResponseEntity<LoginResponse> login(LoginRequest loginRequest, HttpServletResponse response) throws IOException;

    @Operation(summary = "Logout", description = "Invalida la sesion borrando la cookie JWT")
    ResponseEntity<Void> logout(HttpServletResponse response);
}
