package roomescape.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

  private final AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;
  private final LoginCheckInterceptor loginCheckInterceptor;
  private final AdminAuthorizationInterceptor adminAuthorizationInterceptor;

  public WebMvcConfiguration(
      final AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver,
      final LoginCheckInterceptor loginCheckInterceptor,
      final AdminAuthorizationInterceptor adminAuthorizationInterceptor
  ) {
    this.authenticationPrincipalArgumentResolver = authenticationPrincipalArgumentResolver;
    this.loginCheckInterceptor = loginCheckInterceptor;
    this.adminAuthorizationInterceptor = adminAuthorizationInterceptor;
  }

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    registry.addInterceptor(loginCheckInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns("/", "/login");
    registry.addInterceptor(adminAuthorizationInterceptor)
        .addPathPatterns("/admin/**");
  }

  @Override
  public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(authenticationPrincipalArgumentResolver);
  }
}
