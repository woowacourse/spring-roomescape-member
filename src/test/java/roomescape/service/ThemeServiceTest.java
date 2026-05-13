package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.fixture.ReservationFixture;
import roomescape.domain.fixture.ReservationTimeFixture;
import roomescape.domain.fixture.ThemeFixture;
import roomescape.global.exception.DuplicateEntityException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.fake.FakeReservationRepository;
import roomescape.repository.fake.FakeReservationTimeRepository;
import roomescape.repository.fake.FakeThemeRepository;
import roomescape.web.dto.theme.ThemeRequest;
import roomescape.web.dto.theme.ThemeResponse;
import roomescape.web.dto.theme.ThemeTimesResponse;

class ThemeServiceTest {

    private ThemeRepository themeRepository;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeService themeService;

    static Stream<Arguments> provideReservationStatusScenarios() {
        LocalDate today = LocalDate.now();
        return Stream.of(
                Arguments.of(today.minusDays(7), false, false),
                Arguments.of(today, false, true),
                Arguments.of(today.plusDays(7), true, true)
        );
    }

    @BeforeEach
    void setUp() {
        this.themeRepository = new FakeThemeRepository();
        this.reservationRepository = new FakeReservationRepository();
        this.reservationTimeRepository = new FakeReservationTimeRepository();
        this.themeService = new ThemeService(themeRepository, reservationRepository, reservationTimeRepository);
    }

    @Test
    void 새로운_테마를_정상적으로_등록한다() {
        // given
        ThemeRequest request = new ThemeRequest("공포테마", "무서운 테마입니다.", "http://image.png");

        // when
        ThemeResponse response = themeService.register(request);

        // then
        assertThat(response)
                .extracting(
                        ThemeResponse::id,
                        ThemeResponse::name,
                        ThemeResponse::description,
                        ThemeResponse::thumbnailImageUrl
                )
                .containsExactly(1L, "공포테마", "무서운 테마입니다.", "http://image.png");
    }

    @Test
    void 이미_존재하는_이름으로_테마_등록을_시도하면_예외가_발생한다() {
        // given
        themeService.register(new ThemeRequest("공포테마", "설명", "http://image.png"));

        ThemeRequest duplicateRequest = new ThemeRequest("공포테마", "다른 설명", "http://image2.png");

        // when & then
        assertThatThrownBy(() -> themeService.register(duplicateRequest))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("이미 존재하는 테마입니다. 테마 명: 공포테마");
    }

    @Test
    void 테마_식별자로_테마를_삭제할_수_있다() {
        // given
        ThemeRequest request = new ThemeRequest("공포테마", "설명", "http://image.png");
        ThemeResponse response = themeService.register(request);

        // when
        themeService.remove(response.id());

        // then
        assertThatCode(() -> themeService.register(request))
                .doesNotThrowAnyException();
    }

    @Test
    void 활성_테마_목록을_페이징_조건에_맞게_조회한다() {
        // given
        ThemeResponse first = themeService.register(new ThemeRequest("첫번째", "첫번째 설명", "http://image1.png"));
        ThemeResponse second = themeService.register(new ThemeRequest("두번째", "두번째 설명", "http://image2.png"));
        ThemeResponse inactive = themeService.register(new ThemeRequest("세번째", "세번째 설명", "http://image3.png"));
        themeService.remove(inactive.id());

        // when
        List<ThemeResponse> responses = themeService.getAllActiveThemesByPaging(0, 10);

        // then
        assertThat(responses)
                .extracting(ThemeResponse::id)
                .containsExactly(first.id(), second.id());
    }

    @Test
    void 인기_테마_목록을_조회_개수만큼_조회한다() {
        // given
        ThemeResponse first = themeService.register(new ThemeRequest("첫번째", "첫번째 설명", "http://image1.png"));
        ThemeResponse second = themeService.register(new ThemeRequest("두번째", "두번째 설명", "http://image2.png"));
        ThemeResponse inactive = themeService.register(new ThemeRequest("세번째", "세번째 설명", "http://image3.png"));
        themeService.remove(inactive.id());

        // when
        List<ThemeResponse> responses = themeService.getPopularThemes(LocalDate.now().minusDays(7), LocalDate.now(), 1);

        // then
        assertThat(responses)
                .extracting(ThemeResponse::id)
                .containsExactly(first.id());
        assertThat(responses)
                .extracting(ThemeResponse::id)
                .doesNotContain(second.id(), inactive.id());
    }

    @ParameterizedTest
    @MethodSource("provideReservationStatusScenarios")
    void 테마별_예약_가능한_시간_조회_시_시점에_따라_상태를_처리한다(LocalDate date, boolean expectedForEarlyTime,
                                                        boolean expectedForLateTime) {
        // given
        Theme theme = themeRepository.save(ThemeFixture.createDefaultTheme());
        for (int hour = 0; hour <= 23; hour++) {
            reservationTimeRepository.save(
                    ReservationTimeFixture.createReservationTimeWithId(null, LocalTime.of(hour, 0)));
        }
        LocalTime nowTime = LocalTime.now();

        // when
        List<ThemeTimesResponse> response = themeService.getThemeReservationStatus(theme.getId(), date);

        // then
        assertThat(response)
                .filteredOn(time -> time.startAt().isBefore(nowTime))
                .allSatisfy(time -> assertThat(time.isReservable()).isEqualTo(expectedForEarlyTime));

        assertThat(response)
                .filteredOn(time -> time.startAt().isAfter(nowTime))
                .allSatisfy(time -> assertThat(time.isReservable()).isEqualTo(expectedForLateTime));
    }

    @Test
    void 테마별_예약_가능한_시간_조회_시_이미_예약된_시간은_예약_불가능하다() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        Theme theme = themeRepository.save(ThemeFixture.createDefaultTheme());
        ReservationTime reservedTime = reservationTimeRepository.save(
                ReservationTimeFixture.createReservationTime(LocalTime.of(10, 0)));
        reservationTimeRepository.save(ReservationTimeFixture.createReservationTime(LocalTime.of(11, 0)));
        reservationRepository.save(ReservationFixture.createDefaultReservationWithNameAndDate("이프", date, theme,
                reservedTime));

        // when
        List<ThemeTimesResponse> response = themeService.getThemeReservationStatus(theme.getId(), date);

        // then
        assertThat(response)
                .filteredOn(time -> time.id().equals(reservedTime.getId()))
                .singleElement()
                .extracting(ThemeTimesResponse::isReservable)
                .isEqualTo(false);
    }
}
