package roomescape.reservation.application;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.fixture.domain.MemberFixture.NOT_SAVED_MEMBER_1;
import static roomescape.fixture.domain.MemberFixture.NOT_SAVED_MEMBER_2;
import static roomescape.fixture.domain.ReservationTimeFixture.NOT_SAVED_RESERVATION_TIME_1;
import static roomescape.fixture.domain.ReservationTimeFixture.NOT_SAVED_RESERVATION_TIME_2;
import static roomescape.fixture.domain.ThemeFixture.NOT_SAVED_THEME_1;
import static roomescape.fixture.domain.ThemeFixture.NOT_SAVED_THEME_2;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.exception.auth.AuthorizationException;
import roomescape.exception.resource.AlreadyExistException;
import roomescape.exception.resource.ResourceNotFoundException;
import roomescape.fixture.domain.ThemeFixture;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberCommandRepository;
import roomescape.member.domain.MemberQueryRepository;
import roomescape.member.infrastructure.JdbcMemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.ReservationTimeRepository;
import roomescape.reservation.infrastructure.JdbcReservationRepository;
import roomescape.reservation.infrastructure.JdbcReservationTimeRepository;
import roomescape.reservation.ui.dto.request.AvailableReservationTimeRequest;
import roomescape.reservation.ui.dto.request.CreateReservationRequest;
import roomescape.reservation.ui.dto.response.AvailableReservationTimeResponse;
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
    private MemberCommandRepository memberCommandRepository;

    @Autowired
    private MemberQueryRepository memberQueryRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void 예약을_추가한다() {
        // given
        final LocalDate date = LocalDate.now().plusDays(1);
        final Long timeId = reservationTimeRepository.save(NOT_SAVED_RESERVATION_TIME_1);
        final Long themeId = themeRepository.save(NOT_SAVED_THEME_1);
        final Member member = memberQueryRepository.findById(memberCommandRepository.save(NOT_SAVED_MEMBER_1))
                .orElseThrow(() -> new ResourceNotFoundException("회원이 존재하지 않습니다."));
        final CreateReservationRequest request = new CreateReservationRequest(date, timeId, themeId);

        // when & then
        Assertions.assertThatCode(() -> reservationService.create(request, member))
                .doesNotThrowAnyException();
    }

    @Test
    void 예약을_삭제한다() {
        // given
        final LocalDate date = LocalDate.now().plusDays(1);
        final Long timeId1 = reservationTimeRepository.save(NOT_SAVED_RESERVATION_TIME_1);
        final ReservationTime reservationTime1 = reservationTimeRepository.findById(timeId1)
                .orElseThrow(() -> new ResourceNotFoundException("예약시간이 존재하지 않습니다."));
        final Long themeId1 = themeRepository.save(NOT_SAVED_THEME_1);
        final Theme theme1 = themeRepository.findById(themeId1)
                .orElseThrow(() -> new ResourceNotFoundException("테마가 존재하지 않습니다."));
        final Long memberId1 = memberCommandRepository.save(NOT_SAVED_MEMBER_1);
        final Member member1 = memberQueryRepository.findById(memberId1)
                .orElseThrow(() -> new ResourceNotFoundException("회원이 존재하지 않습니다."));

        final Reservation reservation = new Reservation(date, reservationTime1, theme1, member1);
        final Long reservationId = reservationRepository.save(reservation);

        // when & then
        Assertions.assertThatCode(() -> reservationService.deleteIfOwner(reservationId, member1))
                .doesNotThrowAnyException();
    }

    @Test
    void 본인의_예약이_아니면_삭제할_수_없다() {
        // given
        final LocalDate date = LocalDate.now().plusDays(1);
        final Long timeId1 = reservationTimeRepository.save(NOT_SAVED_RESERVATION_TIME_1);
        final ReservationTime reservationTime1 = reservationTimeRepository.findById(timeId1)
                .orElseThrow(() -> new ResourceNotFoundException("예약시간이 존재하지 않습니다."));
        final Long themeId1 = themeRepository.save(NOT_SAVED_THEME_1);
        final Theme theme1 = themeRepository.findById(themeId1)
                .orElseThrow(() -> new ResourceNotFoundException("테마가 존재하지 않습니다."));
        final Long memberId1 = memberCommandRepository.save(NOT_SAVED_MEMBER_1);
        final Long memberId2 = memberCommandRepository.save(NOT_SAVED_MEMBER_2);
        final Member member1 = memberQueryRepository.findById(memberId1)
                .orElseThrow(() -> new ResourceNotFoundException("회원이 존재하지 않습니다."));
        final Member member2 = memberQueryRepository.findById(memberId2)
                .orElseThrow(() -> new ResourceNotFoundException("회원이 존재하지 않습니다."));

        final Reservation reservation = new Reservation(date, reservationTime1, theme1, member1);
        final Long reservationId = reservationRepository.save(reservation);

        // when & then
        Assertions.assertThatThrownBy(() -> reservationService.deleteIfOwner(reservationId, member2))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    void 예약_목록_전체를_조회한다() {
        // given

        final int beforeCount = reservationService.findAll().size();
        reservationRepository.save(createNotSavedReservation1());
        reservationRepository.save(createNotSavedReservation2());

        // when
        final int afterCount = reservationService.findAll().size();

        // then
        assertThat(afterCount - beforeCount).isEqualTo(2);
    }

    @Test
    void 특정_날짜에_특정_테마에_대해_이용_가능한_예약_시간_목록을_조회한다() {
        // given
        final LocalDate date = LocalDate.now().plusDays(1);
        final Long themeId = themeRepository.save(ThemeFixture.NOT_SAVED_THEME_1);
        final Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ResourceNotFoundException("테마가 존재하지 않습니다."));
        final Long timeId1 = reservationTimeRepository.save(NOT_SAVED_RESERVATION_TIME_1);
        final ReservationTime reservationTime1 = reservationTimeRepository.findById(timeId1)
                .orElseThrow(() -> new ResourceNotFoundException("예약시간이 존재하지 않습니���."));
        reservationTimeRepository.save(NOT_SAVED_RESERVATION_TIME_2);

        final AvailableReservationTimeRequest request = new AvailableReservationTimeRequest(date, themeId);
        final Member member = memberQueryRepository.findById(memberCommandRepository.save(NOT_SAVED_MEMBER_1))
                .orElseThrow(() -> new ResourceNotFoundException("회원이 존재하지 않습니다."));

        final long beforeCount = reservationService.findAvailableReservationTimes(request)
                .stream()
                .filter(AvailableReservationTimeResponse::alreadyBooked)
                .count();
        reservationRepository.save(new Reservation(date, reservationTime1, theme, member));

        // when
        final long afterCount = reservationService.findAvailableReservationTimes(request)
                .stream()
                .filter(AvailableReservationTimeResponse::alreadyBooked)
                .count();

        // then
        assertThat(afterCount - beforeCount).isEqualTo(1);
    }

    @Test
    void 예약을_추가할_때_이미_예약된_시간이면_예외가_발생한다() {
        // given
        final LocalDate date = LocalDate.now().plusDays(1);

        final Long timeId = reservationTimeRepository.save(NOT_SAVED_RESERVATION_TIME_1);
        final ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new ResourceNotFoundException("예약시간이 존재하지 않습니다."));
        final Long themeId = themeRepository.save(NOT_SAVED_THEME_1);
        final Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ResourceNotFoundException("테마가 존재하지 않습니다."));
        final Member member = memberQueryRepository.findById(memberCommandRepository.save(NOT_SAVED_MEMBER_1))
                .orElseThrow(() -> new ResourceNotFoundException("회원이 존재하지 않습니다."));

        reservationRepository.save(new Reservation(date, reservationTime, theme, member));

        final CreateReservationRequest request = new CreateReservationRequest(date, timeId, themeId);

        // when & then
        Assertions.assertThatThrownBy(() -> reservationService.create(request, member))
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
        final Member member = memberQueryRepository.findById(memberCommandRepository.save(NOT_SAVED_MEMBER_1))
                .orElseThrow(() -> new ResourceNotFoundException("회원이 존재하지 않습니다."));

        final CreateReservationRequest request = new CreateReservationRequest(date, timeId, themeId);

        reservationRepository.save(new Reservation(date, reservationTime, theme, member));

        // when & then
        Assertions.assertThatThrownBy(() -> reservationService.create(request, member))
                .isInstanceOf(AlreadyExistException.class);
    }

    private Reservation createNotSavedReservation1() {
        final LocalDate date = LocalDate.now().plusDays(1);
        final Long timeId1 = reservationTimeRepository.save(NOT_SAVED_RESERVATION_TIME_1);
        final ReservationTime reservationTime1 = reservationTimeRepository.findById(timeId1)
                .orElseThrow(() -> new ResourceNotFoundException("예약시간이 존재하지 않습니다."));
        final Long themeId1 = themeRepository.save(NOT_SAVED_THEME_1);
        final Theme theme1 = themeRepository.findById(themeId1)
                .orElseThrow(() -> new ResourceNotFoundException("테마가 존재하지 않습니다."));
        final Long memberId1 = memberCommandRepository.save(NOT_SAVED_MEMBER_1);
        final Member member1 = memberQueryRepository.findById(memberId1)
                .orElseThrow(() -> new ResourceNotFoundException("회원이 존재하지 않습니다."));

        return new Reservation(date, reservationTime1, theme1, member1);
    }

    private Reservation createNotSavedReservation2() {
        final LocalDate date = LocalDate.now().plusDays(2);
        final Long timeId2 = reservationTimeRepository.save(NOT_SAVED_RESERVATION_TIME_2);
        final ReservationTime reservationTime2 = reservationTimeRepository.findById(timeId2)
                .orElseThrow(() -> new ResourceNotFoundException("예약시간이 존재하지 않습니다."));
        final Long themeId2 = themeRepository.save(NOT_SAVED_THEME_2);
        final Theme theme2 = themeRepository.findById(themeId2)
                .orElseThrow(() -> new ResourceNotFoundException("테마가 존재하지 않습니다."));
        final Long memberId2 = memberCommandRepository.save(NOT_SAVED_MEMBER_2);
        final Member member2 = memberQueryRepository.findById(memberId2)
                .orElseThrow(() -> new ResourceNotFoundException("회원이 존재하지 않습니다."));

        return new Reservation(date, reservationTime2, theme2, member2);
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
        public MemberCommandRepository memberCommandRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new JdbcMemberRepository(jdbcTemplate);
        }

        @Bean
        public MemberQueryRepository memberQueryRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new JdbcMemberRepository(jdbcTemplate);
        }
    }
}
