package roomescape.reservation.application;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.fixture.MemberFixture.getSavedMember1;
import static roomescape.fixture.MemberFixture.getSavedMember2;
import static roomescape.fixture.ReservationTimeFixture.NOT_SAVED_RESERVATION_TIME_1;
import static roomescape.fixture.ReservationTimeFixture.getSavedReservationTime1;
import static roomescape.fixture.ReservationTimeFixture.getSavedReservationTime2;
import static roomescape.fixture.ThemeFixture.NOT_SAVED_THEME_1;
import static roomescape.fixture.ThemeFixture.getSavedTheme1;
import static roomescape.fixture.ThemeFixture.getSavedTheme2;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.exception.AlreadyExistException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.fixture.ThemeFixture;
import roomescape.member.domain.Member;
import roomescape.member.infrastructure.JdbcMemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.ReservationTimeRepository;
import roomescape.reservation.infrastructure.JdbcReservationRepository;
import roomescape.reservation.infrastructure.JdbcReservationTimeRepository;
import roomescape.reservation.ui.dto.AvailableReservationTimeRequest;
import roomescape.reservation.ui.dto.AvailableReservationTimeResponse;
import roomescape.reservation.ui.dto.CreateReservationRequest;
import roomescape.theme.applcation.ThemeService;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.infrastructure.JdbcThemeRepository;


@JdbcTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void 예약_정보_목록을_조회한다() {
        // given
        final LocalDate date = LocalDate.now().plusDays(1);

        final int beforeCount = reservationService.findAll().size();
        reservationRepository.save(new Reservation(
                date,
                getSavedReservationTime1(reservationTimeRepository),
                getSavedTheme1(themeRepository),
                getSavedMember1(memberRepository)
        ));
        reservationRepository.save(new Reservation(
                date,
                getSavedReservationTime2(reservationTimeRepository),
                getSavedTheme2(themeRepository),
                getSavedMember2(memberRepository)
        ));

        // when
        final int afterCount = reservationService.findAll().size();

        // then
        assertThat(afterCount - beforeCount).isEqualTo(2);
    }

    @Test
    void 예약_정보를_저장한다() {
        // given
        final LocalDate date = LocalDate.now().plusDays(1);
        final Long timeId = reservationTimeRepository.save(NOT_SAVED_RESERVATION_TIME_1);
        final Long themeId = themeRepository.save(NOT_SAVED_THEME_1);
        final Member member = getSavedMember1(memberRepository);
        final CreateReservationRequest request = new CreateReservationRequest(date, timeId, themeId);

        // when & then
        Assertions.assertThatCode(() -> reservationService.create(request, member))
                .doesNotThrowAnyException();
    }

    @Test
    void 예약_정보를_삭제한다() {
        // given
        final LocalDate date = LocalDate.now().plusDays(1);
        final Long reservationId = reservationRepository.save(new Reservation(
                date,
                getSavedReservationTime1(reservationTimeRepository),
                getSavedTheme1(themeRepository),
                getSavedMember1(memberRepository)
        ));

        // when & then
        Assertions.assertThatCode(() -> reservationService.delete(reservationId))
                .doesNotThrowAnyException();
    }

    @Test
    void 특정_날짜에_특정_테마에_대해_이용_가능한_예약_시간_목록을_조회한다() {
        // given
        final LocalDate date = LocalDate.now().plusDays(1);
        final Long themeId = themeRepository.save(ThemeFixture.NOT_SAVED_THEME_1);
        final Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ResourceNotFoundException("테마가 존재하지 않습니다."));
        final ReservationTime reservationTime1 = getSavedReservationTime1(reservationTimeRepository);
        getSavedReservationTime2(reservationTimeRepository);
        final AvailableReservationTimeRequest request = new AvailableReservationTimeRequest(date, themeId);

        final long beforeCount = reservationService.findAvailableReservationTimes(request)
                .stream()
                .filter(AvailableReservationTimeResponse::alreadyBooked)
                .count();
        reservationRepository.save(new Reservation(
                date,
                reservationTime1,
                theme,
                getSavedMember1(memberRepository)
        ));

        // when
        final long afterCount = reservationService.findAvailableReservationTimes(request)
                .stream()
                .filter(AvailableReservationTimeResponse::alreadyBooked)
                .count();

        // then
        assertThat(afterCount - beforeCount).isEqualTo(1);
    }

    @Test
    void 예약_정보를_저장할_때_이미_예약된_시간이면_예외가_발생한다() {
        // given
        final LocalDate date = LocalDate.now().plusDays(1);

        final Long timeId = reservationTimeRepository.save(NOT_SAVED_RESERVATION_TIME_1);
        final ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new ResourceNotFoundException("예약시간이 존재하지 않습니다."));
        final Long themeId = themeRepository.save(NOT_SAVED_THEME_1);
        final Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ResourceNotFoundException("테마가 존재하지 않습니다."));

        reservationRepository.save(
                new Reservation(
                        date,
                        reservationTime,
                        theme,
                        getSavedMember1(memberRepository)
                )
        );

        final CreateReservationRequest request = new CreateReservationRequest(date, timeId, themeId);

        // when & then
        Assertions.assertThatThrownBy(() -> reservationService.create(request, getSavedMember2(memberRepository)))
                .isInstanceOf(AlreadyExistException.class);
    }

    @Test
    void 한_테마의_날짜와_시간이_중복되는_예약을_추가할_수_없다() {
        // given
        final LocalDate date = LocalDate.now().plusDays(1);
        final Long timeId = reservationTimeRepository.save(NOT_SAVED_RESERVATION_TIME_1);
        final ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new ResourceNotFoundException("예약시간이 존재하지 않습니다."));
        final Long themeId = themeRepository.save(NOT_SAVED_THEME_1);
        final Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ResourceNotFoundException("테마가 존재하지 않습니다."));
        final Member member = getSavedMember1(memberRepository);
        final CreateReservationRequest request =
                new CreateReservationRequest(date, timeId, themeId);

        reservationRepository.save(
                new Reservation(date, reservationTime, theme, member)
        );

        // when & then
        Assertions.assertThatThrownBy(() -> reservationService.create(request, getSavedMember2(memberRepository)))
                .isInstanceOf(AlreadyExistException.class);
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ReservationTimeRepository reservationTimeRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new JdbcReservationTimeRepository(jdbcTemplate);
        }

        @Bean
        public ReservationRepository reservationRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new JdbcReservationRepository(jdbcTemplate);
        }

        @Bean
        public ReservationTimeService reservationTimeService(
                final ReservationTimeRepository reservationTimeRepository
        ) {
            return new ReservationTimeService(reservationTimeRepository);
        }

        @Bean
        public ThemeRepository themeRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new JdbcThemeRepository(jdbcTemplate);
        }

        @Bean
        public ThemeService themeService(
                final ThemeRepository themeRepository
        ) {
            return new ThemeService(themeRepository);
        }

        @Bean
        public ReservationService reservationService(
                final ReservationRepository reservationRepository,
                final ReservationTimeRepository reservationTimeRepository,
                final ThemeRepository themeRepository
        ) {
            return new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);
        }

        @Bean
        public MemberRepository memberRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new JdbcMemberRepository(jdbcTemplate);
        }
    }
}
