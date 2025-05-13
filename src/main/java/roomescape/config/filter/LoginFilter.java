package roomescape.config.filter;


import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import roomescape.jwt.JwtProvider;
import roomescape.util.CookieParser;

public class LoginFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDE_URL_PATH = List.of("/", "/login", "/logout",
            "/themes/popular", "/favicon.ico",
            "/image/**", "/css/**", "/js/**");

    private final JwtProvider jwtProvider;
    private final AntPathMatcher pathMatcher;

    public LoginFilter(final JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return EXCLUDE_URL_PATH.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        Optional<String> cookieOptional = CookieParser.getCookie(request, "token");
        if (cookieOptional.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증에 실패했습니다");
            return;
        }

        try {
            String token = cookieOptional.get();
            jwtProvider.verifyToken(token);
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }
}
