package roomescape.domain.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.domain.auth.service.JwtManager;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final JwtManager jwtManager;

    @Autowired
    public AuthConfig(final JwtManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminLoginInterceptor(jwtManager))
                .addPathPatterns("/admin", "/admin/**");
    }
}
