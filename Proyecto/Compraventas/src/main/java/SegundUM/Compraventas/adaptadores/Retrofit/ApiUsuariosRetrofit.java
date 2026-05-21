package SegundUM.Compraventas.adaptadores.Retrofit;

import SegundUM.Compraventas.rest.dto.UsuarioDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiUsuariosRetrofit {
	
	@GET("logged")
	Call<UsuarioDTO> getUsuario(
			@Header("Authorization") String token);
	
	@GET("{id}")
	Call<UsuarioDTO> getVendedor(
			@Path("id") String idUsuario,
			@Header("Authorization") String token);
}
