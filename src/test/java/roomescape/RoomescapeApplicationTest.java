package roomescape;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

// TODO: 왜 webEnvironment가 default(MOCK)이면 테스트가 실패할까?
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomescapeApplicationTest {

        @Test
        void contextLoads() {
        }

}
