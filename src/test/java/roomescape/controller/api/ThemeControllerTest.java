package roomescape.controller.api;

import roomescape.controller.BaseControllerTest;

class ThemeControllerTest extends BaseControllerTest {
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
//    @DisplayName("테마를 생성, 조회, 삭제한다.")
//    Stream<DynamicTest> themeControllerTests() {
//        return Stream.of(
//                DynamicTest.dynamicTest("테마를 생성한다.", this::addTheme),
//                DynamicTest.dynamicTest("테마를 모두 조회한다.", this::getAllThemes),
//                DynamicTest.dynamicTest("테마를 삭제한다.", this::deleteThemeById)
//        );
//    }
//
//    @TestFactory
//    @DisplayName("중복된 이름의 테마를 생성하면 실패한다.")
//    Stream<DynamicTest> failWhenDuplicatedTheme() {
//        return Stream.of(
//                DynamicTest.dynamicTest("테마를 생성한다.", this::addTheme),
//                DynamicTest.dynamicTest("이미 존재하는 이름의 테마를 생성한다.", this::addThemeFailWhenDuplicatedTheme)
//        );
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 테마를 삭제하면 실패한다.")
//    void deleteThemeByIdFailWhenNotFoundId() {
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .when().delete("/themes/1")
//                .then().log().all()
//                .extract();
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
//            softly.assertThat(response.body().asString()).contains("해당 id의 테마가 존재하지 않습니다.");
//        });
//    }
//
//    @Test
//    @DisplayName("이미 사용 중인 테마을 삭제하면 실패한다.")
//    void deleteThemeByIdFailWhenUsedTheme() {
//        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 30)));
//        Theme theme = themeRepository.save(new Theme("테마 이름", "테마 설명", "https://example.com"));
//        reservationRepository.save(new Reservation("구름", LocalDate.of(2024, 4, 9), reservationTime, theme));
//
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .when().delete("/themes/1")
//                .then().log().all()
//                .extract();
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//            softly.assertThat(response.body().asString()).contains("해당 테마를 사용하는 예약이 존재합니다.");
//        });
//    }
//
//    @Test
//    @DisplayName("인기있는 테마들을 조회한다.")
//    @Sql("/reservations.sql")
//    void getPopularThemes() {
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .when().get("/themes/popular")
//                .then().log().all()
//                .extract();
//
//        List<ThemeResponse> themeResponses = response.jsonPath()
//                .getList(".", ThemeResponse.class);
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//            softly.assertThat(themeResponses).hasSize(5);
//            softly.assertThat(themeResponses).containsExactly(
//                    new ThemeResponse(5L, "테마5", "테마5 설명", "https://via.placeholder.com/150/56a8c2"),
//                    new ThemeResponse(4L, "테마4", "테마4 설명", "https://via.placeholder.com/150/30f9e7"),
//                    new ThemeResponse(3L, "테마3", "테마3 설명", "https://via.placeholder.com/150/24f355"),
//                    new ThemeResponse(2L, "테마2", "테마2 설명", "https://via.placeholder.com/150/771796"),
//                    new ThemeResponse(1L, "테마1", "테마1 설명", "https://via.placeholder.com/150/92c952")
//            );
//        });
//    }
//
//    private void addTheme() {
//        ThemeRequest request = new ThemeRequest("테마 이름", "테마 설명", "https://example.com/image.jpg");
//
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(request)
//                .when().post("/themes")
//                .then().log().all()
//                .extract();
//
//        ThemeResponse themeResponse = response.as(ThemeResponse.class);
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
//            softly.assertThat(response.header("Location")).isEqualTo("/themes/1");
//            softly.assertThat(themeResponse)
//                    .isEqualTo(new ThemeResponse(1L, "테마 이름", "테마 설명", "https://example.com/image.jpg"));
//        });
//    }
//
//    private void addThemeFailWhenDuplicatedTheme() {
//        ThemeRequest request = new ThemeRequest("테마 이름", "테마 설명-2", "https://example.com/image-2.jpg");
//
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(request)
//                .when().post("/themes")
//                .then().log().all()
//                .extract();
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//            softly.assertThat(response.body().asString()).contains("해당 이름의 테마는 이미 존재합니다.");
//        });
//
//    }
//
//    private void getAllThemes() {
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .when().get("/themes")
//                .then().log().all()
//                .extract();
//
//        List<ThemeResponse> themeResponses = response.jsonPath()
//                .getList(".", ThemeResponse.class);
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//            softly.assertThat(themeResponses).hasSize(1);
//            softly.assertThat(themeResponses)
//                    .containsExactly(new ThemeResponse(1L, "테마 이름", "테마 설명", "https://example.com/image.jpg"));
//        });
//    }
//
//    private void deleteThemeById() {
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .when().delete("/themes/1")
//                .then().log().all()
//                .extract();
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
//        });
//    }
}
