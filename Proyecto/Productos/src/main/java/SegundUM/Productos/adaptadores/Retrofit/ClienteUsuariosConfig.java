package SegundUM.Productos.adaptadores.Retrofit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Configuración de Spring que construye el cliente Retrofit apuntando al microservicio Usuarios. */
@Configuration
public class ClienteUsuariosConfig {

    @Value("${api.usuarios.url}")
    private String usuariosBaseUrl;

    @Bean
    public ApiUsuariosRetrofit apiUsuariosRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(usuariosBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiUsuariosRetrofit.class);
    }
}
