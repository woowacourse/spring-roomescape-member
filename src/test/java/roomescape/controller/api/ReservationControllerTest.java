package roomescape.controller.api;

import roomescape.controller.BaseControllerTest;

class ReservationControllerTest extends BaseControllerTest {

//    @Autowired
//    private ReservationTimeRepository reservationTimeRepository;
//
//    @Autowired
//    private ThemeRepository themeRepository;
//
//    @BeforeEach
//    void setUp() {
//        reservationTimeRepository.save(new ReservationTime(LocalTime.of(11, 0)));
//        themeRepository.save(new Theme("테마 이름", "테마 설명", "https://example.com"));
//    }
//
//    @TestFactory
//    @DisplayName("예약을 생성, 조회, 삭제한다.")
//    Stream<DynamicTest> reservationControllerTests() {
//        return Stream.of(
//                DynamicTest.dynamicTest("예약을 생성한다.", this::addReservation),
//                DynamicTest.dynamicTest("예약을 모두 조회한다.", this::getAllReservations),
//                DynamicTest.dynamicTest("예약을 삭제한다.", this::deleteReservationById)
//        );
//    }
//
//    @TestFactory
//    @DisplayName("중복된 예약을 생성하면 실패한다.")
//    Stream<DynamicTest> failWhenDuplicatedReservation() {
//        return Stream.of(
//                DynamicTest.dynamicTest("예약을 생성한다.", this::addReservation),
//                DynamicTest.dynamicTest("이미 존재하는 예약을 생성한다.", this::addReservationFailWhenDuplicatedReservation)
//        );
//    }
//
//    @Test
//    @DisplayName("지나간 날짜/시간에 대한 예약은 실패한다.")
//    void failWhenDateTimePassed() {
//        ReservationRequest request = new ReservationRequest("구름", "2024-04-07", 1L, 1L);
//
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(request)
//                .when().post("/reservations")
//                .then().log().all()
//                .extract();
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//            softly.assertThat(response.body().asString()).contains("지나간 날짜/시간에 대한 예약은 불가능합니다.");
//        });
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 예약을 삭제하면 실패한다.")
//    void failWhenNotFoundReservation() {
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .when().delete("/reservations/1")
//                .then().log().all()
//                .extract();
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
//            softly.assertThat(response.body().asString()).contains("해당 id의 예약이 존재하지 않습니다.");
//        });
//    }
//
//
//    private void addReservation() {
//        ReservationRequest request = new ReservationRequest("구름", "2024-04-09", 1L, 1L);
//
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(request)
//                .when().post("/reservations")
//                .then().log().all()
//                .extract();
//
//        ReservationResponse reservationResponse = response.as(ReservationResponse.class);
//        ReservationTimeResponse reservationTimeResponse = new ReservationTimeResponse(1L, LocalTime.of(11, 0));
//        ThemeResponse themeResponse = new ThemeResponse(1L, "테마 이름", "테마 설명", "https://example.com");
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
//            softly.assertThat(response.header("Location")).isEqualTo("/reservations/1");
//            softly.assertThat(reservationResponse).isEqualTo(
//                    new ReservationResponse(1L, "구름", LocalDate.of(2024, 4, 9), reservationTimeResponse,
//                            themeResponse));
//        });
//    }
//
//    private void getAllReservations() {
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .when().get("/reservations")
//                .then().log().all()
//                .extract();
//
//        List<ReservationResponse> reservationResponses = response.jsonPath()
//                .getList(".", ReservationResponse.class);
//        ReservationTimeResponse reservationTimeResponse = new ReservationTimeResponse(1L, LocalTime.of(11, 0));
//        ThemeResponse themeResponse = new ThemeResponse(1L, "테마 이름", "테마 설명", "https://example.com");
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//            softly.assertThat(reservationResponses).hasSize(1);
//            softly.assertThat(reservationResponses)
//                    .containsExactly(
//                            new ReservationResponse(1L, "구름", LocalDate.of(2024, 4, 9), reservationTimeResponse,
//                                    themeResponse));
//        });
//    }
//
//    private void deleteReservationById() {
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .when().delete("/reservations/1")
//                .then().log().all()
//                .extract();
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
//        });
//    }
//
//    private void addReservationFailWhenDuplicatedReservation() {
//        ReservationRequest request = new ReservationRequest("구름", "2024-04-09", 1L, 1L);
//
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(request)
//                .when().post("/reservations")
//                .then().log().all()
//                .extract();
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//            softly.assertThat(response.body().asString()).contains("해당 날짜/시간에 이미 예약이 존재합니다.");
//        });
//    }
}
