package SegundUM.pasarela.adaptadores.retrofit;

import SegundUM.pasarela.rest.dto.NuevoUsuarioDTO;
import SegundUM.pasarela.rest.dto.UsuarioDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsuariosRestClient {

    @GET("usuarios/sesion/{email}/{clave}")
    Call<UsuarioDTO> verificarCredenciales(
            @Path("email") String email,
            @Path("clave") String clave);

    @GET("usuarios/github/{idGitHub}")
    Call<UsuarioDTO> verificarGitHub(
            @Path("idGitHub") String idGitHub);

    @POST("usuarios/github")
    Call<UsuarioDTO> registrarUsuarioGitHub(@Body NuevoUsuarioDTO nuevoUsuario);
}
