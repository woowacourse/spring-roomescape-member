package roomescape.domain.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.reservation.dto.ReservationResponse;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class JdbcTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("Spring JDBC 연결 테스트")
    @Test
    void test1() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("TEST-DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Disabled
    @DisplayName("reservation jdbc 예약 정보 삽입 테스트")
    @Test
    void test2() {
        // given & when
        jdbcTemplate.update("INSERT INTO reservation (name, date_time) VALUES (?, ?)", "브라운", "2023-08-05 15:40");

        // then
        List<ReservationResponse> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponse.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        assertThat(reservations.size()).isEqualTo(count);
    }
}
