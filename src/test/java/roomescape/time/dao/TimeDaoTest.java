package roomescape.time.dao;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.time.entity.TimeEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TimeDaoTest {

    @Autowired
    private TimeDao timeDao;

    @Test
    @DisplayName("시간 추가 확인 테스트")
    void insertTest() {
        timeDao.insert(new TimeEntity(1, LocalTime.of(15, 40)));

        List<TimeEntity> times = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", TimeEntity.class);

        assertThat(times.size()).isEqualTo(timeDao.count());
    }
}
