package SegundUM.Compraventas.adaptadores;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import SegundUM.Compraventas.adaptadores.Retrofit.ApiAuthRetrofit;
import SegundUM.Compraventas.adaptadores.Retrofit.ApiProductosRetrofit;
import SegundUM.Compraventas.adaptadores.Retrofit.ApiUsuariosRetrofit;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Configuración Spring que instancia los clientes HTTP (RestTemplate y Retrofit) usados por los adaptadores de salida. */
@Configuration
public class ClienteConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Value("${api.productos.url}")
    private String productosBaseUrl;
    
    @Bean
    public ApiProductosRetrofit apiProductosRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(productosBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        return retrofit.create(ApiProductosRetrofit.class);
    }
    
    
    @Value("${api.usuarios.url}")
    private String usuariosBaseUrl; 
    @Bean
    public ApiUsuariosRetrofit apiUsuariosRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(usuariosBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        return retrofit.create(ApiUsuariosRetrofit.class);
    }
    
    @Value("${api.usuarios.auth.url}")
    private String usuariosAuthUrl;
    
    @Bean
    public ApiAuthRetrofit apiAuthRetrofit() {
        
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(usuariosAuthUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        return retrofit.create(ApiAuthRetrofit.class);
    }
    
}
