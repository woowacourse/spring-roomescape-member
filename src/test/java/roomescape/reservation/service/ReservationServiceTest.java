package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import roomescape.config.TestConfig;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.exception.ReservationAlreadyExistsException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.fixture.TestFixture;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@JdbcTest
@Import(TestConfig.class)
@TestPropertySource(properties = {
        "spring.sql.init.schema-locations=classpath:schema.sql",
        "spring.sql.init.data-locations="
})
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ReservationServiceTest {

    private static final LocalDate futureDate = TestFixture.makeFutureDate();
    private static final LocalDateTime afterOneHour = TestFixture.makeTimeAfterOneHour();

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository,
                memberRepository);
    }

    @Test
    void createReservation_shouldReturnResponseWhenSuccessful() {
        ReservationTime time1 = reservationTimeRepository.save(ReservationTime.of(1L, LocalTime.of(10, 0)));
        Theme savedTheme = themeRepository.save(TestFixture.makeTheme(1L));
        Member savedMember = memberRepository.save(TestFixture.makeMember());

        ReservationResponse response = reservationService.create(futureDate, time1.getId(), savedTheme.getId(),
                savedMember.getId(), afterOneHour);

        Assertions.assertAll(
                () -> assertThat(response.member().name()).isEqualTo("Mint"),
                () -> assertThat(response.date()).isEqualTo(futureDate),
                () -> assertThat(response.time().startAt()).isEqualTo(LocalTime.of(10, 0))
        );
    }

    @Test
    void getReservations_shouldReturnAllCreatedReservations() {
        ReservationTime time1 = reservationTimeRepository.save(ReservationTime.of(1L, LocalTime.of(10, 0)));
        ReservationTime time2 = reservationTimeRepository.save(ReservationTime.of(2L, LocalTime.of(11, 0)));
        Theme savedTheme = themeRepository.save(TestFixture.makeTheme(1L));
        Member savedMember = memberRepository.save(TestFixture.makeMember());

        reservationTimeRepository.save(time1);
        reservationService.create(futureDate, time1.getId(), savedTheme.getId(), savedMember.getId(), afterOneHour);
        reservationService.create(futureDate, time2.getId(), savedTheme.getId(), savedMember.getId(), afterOneHour);

        List<ReservationResponse> result = reservationService.findReservations(null, null, null, null);
        assertThat(result).hasSize(2);
    }

    @Test
    void deleteReservation_shouldThrowException_WhenIdNotFound() {
        assertThatThrownBy(() -> reservationService.delete(999L))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessageContaining("요청한 id와 일치하는 예약 정보가 없습니다.");
    }

    @Test
    void deleteReservation_shouldRemoveSuccessfully() {
        ReservationTime time1 = reservationTimeRepository.save(ReservationTime.of(1L, LocalTime.of(10, 0)));
        Theme savedTheme = themeRepository.save(TestFixture.makeTheme(1L));
        Member savedMember = memberRepository.save(TestFixture.makeMember());

        ReservationResponse response = reservationService.create(futureDate, time1.getId(), savedTheme.getId(),
                savedMember.getId(), afterOneHour);
        reservationService.delete(response.id());

        List<ReservationResponse> result = reservationService.findReservations(savedTheme.getId(), savedMember.getId(),
                futureDate,
                futureDate.plusDays(1));
        assertThat(result).isEmpty();
    }

    @Test
    void createReservation_shouldThrowException_WhenDuplicated() {
        ReservationTime time1 = reservationTimeRepository.save(ReservationTime.of(1L, LocalTime.of(10, 0)));
        Theme savedTheme = themeRepository.save(TestFixture.makeTheme(1L));
        Member savedMember = memberRepository.save(TestFixture.makeMember());

        reservationService.create(futureDate, time1.getId(),
                savedTheme.getId(), savedMember.getId(), afterOneHour);

        assertThatThrownBy(
                () -> reservationService.create(futureDate, time1.getId(), savedTheme.getId(), savedMember.getId(),
                        afterOneHour))
                .isInstanceOf(ReservationAlreadyExistsException.class)
                .hasMessageContaining("해당 시간에 이미 예약이 존재합니다.");
    }

    @Test
    void createReservation_shouldThrowException_WhenTimeIdNotFound() {
        Theme savedTheme = themeRepository.save(TestFixture.makeTheme(1L));
        Member savedMember = memberRepository.save(TestFixture.makeMember());

        assertThatThrownBy(() -> reservationService.create(futureDate, 999L, savedTheme.getId(), savedMember.getId(),
                afterOneHour))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessageContaining("요청한 id와 일치하는 예약 시간 정보가 없습니다.");
    }
}
