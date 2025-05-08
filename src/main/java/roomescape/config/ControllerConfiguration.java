package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.entity.AccessToken;

@Configuration
public class ControllerConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthorizationArgumentResolver());
    }

    public class AuthorizationArgumentResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameterType().equals(LoginMember.class);
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                      NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
                throws Exception {
            HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
            Cookie[] cookies = request.getCookies();

            Cookie tokenCookie = null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    tokenCookie = cookie;
                    break;
                }
            }

            if (tokenCookie == null) {
                throw new IllegalArgumentException("필수 쿠키가 존재하지 않습니다.");
            }

            AccessToken accessToken = new AccessToken(tokenCookie.getValue());
            long memberId = accessToken.findSubject();
            return new LoginMember(memberId);
        }
    }
}
