package roomescape.globar.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

  private final AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;
  private final AdminAuthorizationInterceptor adminAuthorizationInterceptor;

  public WebMvcConfiguration(
      final AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver,
      final AdminAuthorizationInterceptor adminAuthorizationInterceptor
  ) {
    this.authenticationPrincipalArgumentResolver = authenticationPrincipalArgumentResolver;
    this.adminAuthorizationInterceptor = adminAuthorizationInterceptor;
  }

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    registry.addInterceptor(adminAuthorizationInterceptor)
        .addPathPatterns("/admin/**");
  }

  @Override
  public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(authenticationPrincipalArgumentResolver);
  }
}
