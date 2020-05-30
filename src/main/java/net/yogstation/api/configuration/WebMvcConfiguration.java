package net.yogstation.api.configuration;

import lombok.AllArgsConstructor;
import net.yogstation.api.bean.AuthorizationRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AllArgsConstructor
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private AuthorizationRequestInterceptor authorizationRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationRequestInterceptor);
    }

    @Bean
    public RestOperations restOperations() {
        return new RestTemplate();
    }
}
