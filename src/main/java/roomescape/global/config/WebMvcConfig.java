package roomescape.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.global.auth.AuthorizationHeaderInterceptor;
import roomescape.global.exception.BusinessExceptionMappingJackson2HttpMessageConverter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;
    private final AuthorizationHeaderInterceptor interceptor;

    public WebMvcConfig(ObjectMapper objectMapper, AuthorizationHeaderInterceptor interceptor) {
        this.objectMapper = objectMapper;
        this.interceptor = interceptor;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.addFirst( new BusinessExceptionMappingJackson2HttpMessageConverter(objectMapper));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
