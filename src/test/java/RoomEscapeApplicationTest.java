import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import roomescape.infrastructure.config.WebMvcConfig;

@SpringBootTest
@Import(WebMvcConfig.class)
class RoomEscapeApplicationTest {

    @Test
    void contextLoads() {
    }
}
