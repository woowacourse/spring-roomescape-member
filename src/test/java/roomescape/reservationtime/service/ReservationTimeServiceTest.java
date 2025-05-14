package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import roomescape.config.TestConfig;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.fixture.TestFixture;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.ReservationService;
import roomescape.reservationtime.dto.request.ReservationTimeCreateRequest;
import roomescape.reservationtime.dto.response.AvailableReservationTimeResponse;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;
import roomescape.reservationtime.exception.ReservationTimeAlreadyExistsException;
import roomescape.reservationtime.exception.ReservationTimeInUseException;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.repository.ThemeRepository;

@JdbcTest
@Import(TestConfig.class)
@TestPropertySource(properties = {
        "spring.sql.init.schema-locations=classpath:schema.sql",
        "spring.sql.init.data-locations="
})
class ReservationTimeServiceTest {

    private final LocalDate futureDate = TestFixture.makeFutureDate();
    private final LocalDateTime afterOneHour = TestFixture.makeTimeAfterOneHour();

    private Long themeId;
    private Long memberId;

    private ReservationService reservationService;
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        themeId = themeRepository.save(TestFixture.makeTheme(1L)).getId();
        reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
        memberId = memberRepository.save(TestFixture.makeMember()).getId();
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository,
                memberRepository);
    }

    @Test
    void createReservationTime_shouldThrowException_IfDuplicated() {
        LocalTime time = LocalTime.of(1, 1);
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(time);

        reservationTimeService.create(request);

        assertThatThrownBy(() -> reservationTimeService.create(request))
                .isInstanceOf(ReservationTimeAlreadyExistsException.class)
                .hasMessageContaining("중복된 예약 시간을 생성할 수 없습니다.");
    }

    @Test
    void createReservationTime_shouldReturnResponse_WhenSuccessful() {
        LocalTime time = LocalTime.of(9, 0);
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(time);

        ReservationTimeResponse response = reservationTimeService.create(request);

        assertThat(response.startAt()).isEqualTo(time);
    }

    @Test
    void getReservationTimes_shouldReturnAllCreatedTimes() {
        reservationTimeService.create(new ReservationTimeCreateRequest(LocalTime.of(10, 0)));
        reservationTimeService.create(new ReservationTimeCreateRequest(LocalTime.of(11, 0)));

        List<ReservationTimeResponse> result = reservationTimeService.getReservationTimes();

        assertThat(result).hasSize(2);
    }

    @Test
    void deleteReservationTime_shouldRemoveSuccessfully() {
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(LocalTime.of(13, 30));
        ReservationTimeResponse response = reservationTimeService.create(request);

        reservationTimeService.delete(response.id());

        List<ReservationTimeResponse> result = reservationTimeService.getReservationTimes();
        assertThat(result).isEmpty();
    }

    @Test
    void deleteReservationTime_shouldThrowException_WhenIdNotFound() {
        assertThatThrownBy(() -> reservationTimeService.delete(999L))
                .isInstanceOf(ReservationTimeNotFoundException.class)
                .hasMessageContaining("요청한 id와 일치하는 예약 시간 정보가 없습니다.");
    }

    @Test
    void deleteReservationTime_shouldThrowException_WhenReservationExists() {
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.create(
                new ReservationTimeCreateRequest(LocalTime.now()));
        reservationService.create(futureDate, reservationTimeResponse.id(), themeId, memberId, afterOneHour);
        assertThatThrownBy(() -> reservationTimeService.delete(reservationTimeResponse.id()))
                .isInstanceOf(ReservationTimeInUseException.class)
                .hasMessageContaining("해당 시간에 대한 예약이 존재하여 삭제할 수 없습니다.");
    }

    @Test
    void getAvailableReservationTimes_shouldReturnAllAvailableReservationTimes() {
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.create(
                new ReservationTimeCreateRequest(LocalTime.of(10, 0)));
        reservationTimeService.create(new ReservationTimeCreateRequest(LocalTime.of(11, 0)));
        reservationTimeService.create(new ReservationTimeCreateRequest(LocalTime.of(12, 0)));

        reservationService.create(futureDate, reservationTimeResponse.id(), themeId, memberId, afterOneHour);
        List<AvailableReservationTimeResponse> availableReservationTimes = reservationTimeService.getAvailableReservationTimes(
                futureDate, themeId);

        assertThat(
                availableReservationTimes.stream()
                        .filter(AvailableReservationTimeResponse::alreadyBooked)
                        .count())
                .isEqualTo(1L);
    }
}
