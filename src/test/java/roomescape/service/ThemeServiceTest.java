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
import roomescape.domain.DuplicateEntityException;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.collection.MemoryReservationTimeRepository;
import roomescape.service.fake.FakeThemeRepository;
import roomescape.web.dto.ThemeRequest;
import roomescape.web.dto.ThemeResponse;
import roomescape.web.dto.ThemeTimesResponse;

class ThemeServiceTest {

    private ThemeRepository themeRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeService themeService;

    static Stream<Arguments> provideReservationStatusScenarios() {
        LocalDate today = LocalDate.now();
        return Stream.of(
                // {조회날짜, 이른 시간 기댓값, 늦은 시간 기댓값}
                Arguments.of(today.minusDays(7), false, false), // 과거: 모두 불가
                Arguments.of(today, false, true),                            // 현재: 시간 비교
                Arguments.of(today.plusDays(7), true, true)        // 미래: 모두 가능
        );
    }

    @BeforeEach
    void setUp() {
        this.themeRepository = new FakeThemeRepository();
        this.reservationTimeRepository = new MemoryReservationTimeRepository();
        this.themeService = new ThemeService(themeRepository, reservationTimeRepository);
    }

    @Test
    void 새로운_테마를_정상적으로_등록한다() {
        // given: 관리자 권한과 등록 정보가 주어짐
        ThemeRequest request = new ThemeRequest("공포테마", "무서운 테마입니다.", "http://image.png");

        // when: 테마 등록 진행
        ThemeResponse response = themeService.register(request);

        // then: 등록된 정보가 입력값과 일치하며 ID가 발급됨
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
        // given: '공포테마'가 이미 등록되어 있음
        themeService.register(new ThemeRequest("공포테마", "설명", "http://image.png"));

        ThemeRequest duplicateRequest = new ThemeRequest("공포테마", "다른 설명", "http://image2.png");

        // when & then: DuplicateEntityException 발생 확인
        assertThatThrownBy(() -> themeService.register(duplicateRequest))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("이미 존재하는 테마입니다. 테마 명: 공포테마");
    }

    @Test
    void 테마_식별자로_테마를_삭제_할_수_있다() {
        // given
        ThemeRequest request = new ThemeRequest("공포테마", "설명", "http://image.png");
        ThemeResponse registerresponse = themeService.register(request);

        // when
        themeService.remove(registerresponse.id());

        // then: 같은 테마명으로 재등록 가능
        assertThatCode(() -> themeService.register(request))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("provideReservationStatusScenarios")
    void 테마별_예약_가능한_시간을_조회_시_시점에_따라_상태를_처리한다(LocalDate date, boolean expectedForEarlyTime,
                                             boolean expectedForLateTime) {
        // given
        for (int hour = 0; hour <= 23; hour++) {
            reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(hour, 0)));
        }
        LocalTime nowTime = LocalTime.now();

        // when
        List<ThemeTimesResponse> response = themeService.getThemeReservationStatus(1L, date);

        // then
        // 1. 현재 시간보다 이른 시간대 검증
        assertThat(response)
                .filteredOn(time -> time.startAt().isBefore(nowTime))
                .allSatisfy(time -> assertThat(time.isReservable()).isEqualTo(expectedForEarlyTime));

        // 2. 현재 시간보다 늦은 시간대 검증
        assertThat(response)
                .filteredOn(time -> time.startAt().isAfter(nowTime))
                .allSatisfy(time -> assertThat(time.isReservable()).isEqualTo(expectedForLateTime));
    }
}
