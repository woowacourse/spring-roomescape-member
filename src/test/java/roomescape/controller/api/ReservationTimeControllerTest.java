package roomescape.controller.api;

import roomescape.controller.BaseControllerTest;

class ReservationTimeControllerTest extends BaseControllerTest {
//
//    @Autowired
//    private ReservationTimeRepository reservationTimeRepository;
//
//    @Autowired
//    private ThemeRepository themeRepository;
//
//    @Autowired
//    private ReservationRepository reservationRepository;
//
//    @TestFactory
//    @DisplayName("예약 시간을 생성, 조회, 삭제한다.")
//    Stream<DynamicTest> addReservationTimeAndGetAndDelete() {
//        return Stream.of(
//                DynamicTest.dynamicTest("예약 시간을 생성한다.", this::addReservationTime),
//                DynamicTest.dynamicTest("예약 시간을 모두 조회한다.", this::getAllReservationTimes),
//                DynamicTest.dynamicTest("예약 시간을 삭제한다.", this::deleteReservationTimeById)
//        );
//    }
//
//    @TestFactory
//    @DisplayName("중복된 예약 시간을 생성하면 실패한다.")
//    Stream<DynamicTest> failWhenDuplicatedTime() {
//        return Stream.of(
//                DynamicTest.dynamicTest("예약 시간을 생성한다.", this::addReservationTime),
//                DynamicTest.dynamicTest("이미 존재하는 예약 시간을 생성한다.", this::addReservationTimeFailWhenDuplicatedTime)
//        );
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 예약 시간을 삭제하면 실패한다.")
//    void deleteReservationTimeByIdFailWhenNotFoundId() {
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .when().delete("/times/1")
//                .then().log().all()
//                .extract();
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
//            softly.assertThat(response.body().asString()).contains("해당 id의 시간이 존재하지 않습니다.");
//        });
//    }
//
//    @Test
//    @DisplayName("이미 사용 중인 예약 시간을 삭제하면 실패한다.")
//    void deleteReservationTimeByIdFailWhenUsedTime() {
//        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 30)));
//        Theme theme = themeRepository.save(new Theme("테마 이름", "테마 설명", "https://example.com"));
//        reservationRepository.save(new Reservation("구름", LocalDate.of(2024, 4, 9), reservationTime, theme));
//
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .when().delete("/times/1")
//                .then().log().all()
//                .extract();
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//            softly.assertThat(response.body().asString()).contains("해당 시간을 사용하는 예약이 존재합니다.");
//        });
//    }
//
//    @Test
//    @DisplayName("이용가능한 시간들을 조회한다.")
//    void getAvailableReservationTimes() {
//        ReservationTime notBookedTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
//        ReservationTime alreadyBookedTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(11, 0)));
//        Theme theme = themeRepository.save(new Theme("테마 이름", "테마 설명", "https://example.com"));
//        reservationRepository.save(new Reservation("구름", LocalDate.of(2024, 4, 9), alreadyBookedTime, theme));
//
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .param("date", "2024-04-09")
//                .param("themeId", 1L)
//                .when().get("/times/available")
//                .then().log().all()
//                .extract();
//
//        List<AvailableReservationTimeResponse> availableReservationTimeResponses = response.jsonPath()
//                .getList(".", AvailableReservationTimeResponse.class);
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//            softly.assertThat(availableReservationTimeResponses).hasSize(2);
//            softly.assertThat(availableReservationTimeResponses).containsExactlyInAnyOrder(
//                    new AvailableReservationTimeResponse(
//                            notBookedTime.getId(),
//                            notBookedTime.getStartAt(),
//                            false),
//                    new AvailableReservationTimeResponse(
//                            alreadyBookedTime.getId(),
//                            alreadyBookedTime.getStartAt(),
//                            true)
//            );
//        });
//    }
//
//    private void addReservationTime() {
//        ReservationTimeRequest request = new ReservationTimeRequest("10:30");
//
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(request)
//                .when().post("/times")
//                .then().log().all()
//                .extract();
//
//        ReservationTimeResponse reservationTimeResponse = response.as(ReservationTimeResponse.class);
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
//            softly.assertThat(response.header("Location")).isEqualTo("/times/1");
//            softly.assertThat(reservationTimeResponse).isEqualTo(new ReservationTimeResponse(1L, LocalTime.of(10, 30)));
//        });
//    }
//
//    private void addReservationTimeFailWhenDuplicatedTime() {
//        ReservationTimeRequest request = new ReservationTimeRequest("10:30");
//
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(request)
//                .when().post("/times")
//                .then().log().all()
//                .extract();
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//            softly.assertThat(response.body().asString()).contains("해당 시간은 이미 존재합니다.");
//        });
//    }
//
//    private void getAllReservationTimes() {
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .when().get("/times")
//                .then().log().all()
//                .extract();
//
//        List<ReservationTimeResponse> reservationTimeResponses = response.jsonPath()
//                .getList(".", ReservationTimeResponse.class);
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//            softly.assertThat(reservationTimeResponses).hasSize(1);
//            softly.assertThat(reservationTimeResponses)
//                    .containsExactly(new ReservationTimeResponse(1L, LocalTime.of(10, 30)));
//        });
//    }
//
//    private void deleteReservationTimeById() {
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .when().delete("/times/1")
//                .then().log().all()
//                .extract();
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
//        });
//    }
}
