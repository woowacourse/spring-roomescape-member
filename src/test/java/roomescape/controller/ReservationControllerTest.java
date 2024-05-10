package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.controller.request.ReservationRequest;
import roomescape.controller.response.MemberReservationTimeResponse;
import roomescape.controller.response.ReservationResponse;
import roomescape.service.AuthService;
import roomescape.service.dto.AuthDto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationControllerTest {

    private static final int INITIAL_TIME_COUNT = 5;
    private static final int INITIAL_RESERVATION_COUNT = 15;

    private final JdbcTemplate jdbcTemplate;
    private final AuthService authService;
    private final SimpleJdbcInsert themeInsertActor;
    private final SimpleJdbcInsert timeInsertActor;
    private final SimpleJdbcInsert memberInsertActor;
    private final SimpleJdbcInsert reservationInsertActor;

    @Autowired
    public ReservationControllerTest(JdbcTemplate jdbcTemplate, AuthService authService) {
        this.jdbcTemplate = jdbcTemplate;
        this.authService = authService;
        this.themeInsertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
        this.timeInsertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
        this.memberInsertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
        this.reservationInsertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @BeforeEach
    void setUp() {
        initDatabase();
        IntStream.range(1, 6).forEach(i -> insertReservationTime(i + ":00"));
        IntStream.range(0, 20).forEach(i -> insertTheme("n" + i, "d" + i, "t" + i));

        insertMember("에버", "treeboss@gmail.com", "treeboss123!", "USER");
        insertMember("우테코", "wtc@gmail.com", "wtc123!", "ADMIN");

        LocalDate now = LocalDate.now();
        IntStream.range(0, 5).forEach(i -> insertReservation(now.minusDays(i), 1L, 1L, 1L));
        IntStream.range(0, 5).forEach(i -> insertReservation(now.minusDays(i), 2L, 1L, 1L));
        IntStream.range(0, 5).forEach(i -> insertReservation(now.minusDays(i), 3L, 1L, 1L));
    }

    private void initDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE member RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE theme RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE reservation RESTART IDENTITY");
    }

    private void insertTheme(String name, String description, String thumbnail) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", name);
        parameters.put("description", description);
        parameters.put("thumbnail", thumbnail);
        themeInsertActor.execute(parameters);
    }

    private void insertReservationTime(String startAt) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("start_at", startAt);
        timeInsertActor.execute(parameters);
    }

    private void insertMember(String name, String email, String password, String role) {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("name", name);
        parameters.put("email", email);
        parameters.put("password", password);
        parameters.put("role", role);
        memberInsertActor.execute(parameters);
    }

    private void insertReservation(LocalDate date, long timeId, long themeId, long memberId) {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("date", date);
        parameters.put("time_id", timeId);
        parameters.put("theme_id", themeId);
        parameters.put("member_id", memberId);
        reservationInsertActor.execute(parameters);
    }

    @DisplayName("전체 예약을 조회한다.")
    @Test
    void should_get_all_reservations() {
        List<ReservationResponse> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", ReservationResponse.class);

        assertThat(reservations).hasSize(INITIAL_RESERVATION_COUNT);
    }

    @DisplayName("예약을 추가할 수 있다.")
    @Test
    void should_insert_reservation() {
        AuthDto authDto = new AuthDto("treeboss@gmail.com");
        String token = authService.createToken(authDto);
        ReservationRequest request = new ReservationRequest(LocalDate.now().plusDays(1), 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/reservations/" + (INITIAL_RESERVATION_COUNT + 1));

        assertThat(countAllReservations()).isEqualTo(INITIAL_RESERVATION_COUNT + 1);
    }

    @DisplayName("존재하는 예약이라면 예약을 삭제할 수 있다.")
    @Test
    void should_delete_reservation_when_reservation_exist() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        assertThat(countAllReservations()).isEqualTo(INITIAL_RESERVATION_COUNT - 1);
    }

    @DisplayName("특정 날짜와 테마에 따른 모든 시간의 예약 가능 여부를 확인한다.")
    @Test
    void should_get_reservations_with_book_state_by_date_and_theme() {
        String date = LocalDate.now().minusDays(1).toString();
        long themeId = 1;
        List<MemberReservationTimeResponse> times = RestAssured.given().log().all()
                .when().get(String.format("/reservations/times?date=%s&themeId=%d", date, themeId))
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", MemberReservationTimeResponse.class);

        assertThat(times).hasSize(INITIAL_TIME_COUNT);
        assertThat(times.get(0).getIsBooked()).isTrue();
        assertThat(times.get(0).getTimeId()).isEqualTo(1);
        assertThat(times.get(1).getIsBooked()).isTrue();
        assertThat(times.get(1).getTimeId()).isEqualTo(2);
        assertThat(times.get(2).getIsBooked()).isTrue();
        assertThat(times.get(2).getTimeId()).isEqualTo(3);
        assertThat(times.get(3).getIsBooked()).isFalse();
        assertThat(times.get(3).getTimeId()).isEqualTo(4);
        assertThat(times.get(4).getIsBooked()).isFalse();
        assertThat(times.get(4).getTimeId()).isEqualTo(5);
    }

    @DisplayName("날짜, 테마, 사용자 조건으로 예약을 검색한다.")
    @Test
    void should_get_reservations_by_date_and_theme_and_member() {
        List<ReservationResponse> reservations = RestAssured.given().log().all()
                .param("member_id", 1L)
                .param("theme_id", 1L)
                .param("from", "2024-04-30")
                .param("to", "2024-05-10")
                .get("/reservations/filter")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", ReservationResponse.class);
        System.out.println(reservations);
    }

    private Integer countAllReservations() {
        return jdbcTemplate.queryForObject("SELECT count(id) from reservation", Integer.class);
    }
}
