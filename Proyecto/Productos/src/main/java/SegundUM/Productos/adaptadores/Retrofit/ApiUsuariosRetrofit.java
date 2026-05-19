package SegundUM.Productos.adaptadores.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiUsuariosRetrofit {

    @GET("email/{email}")
    Call<IdUsuarioDTO> getIdByEmail(@Path("email") String email);
}
