package roomescape;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

// TODO: 이거 왜 됨?
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class RoomescapeApplicationTest {

    @Test
    void contextLoads() {
    }

}
