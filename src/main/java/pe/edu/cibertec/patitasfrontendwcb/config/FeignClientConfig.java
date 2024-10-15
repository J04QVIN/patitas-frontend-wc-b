package pe.edu.cibertec.patitasfrontendwcb.config;

import feign.Request;

import org.springframework.context.annotation.Bean;



public class FeignClientConfig {
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(3000, 10000);
    }
}
