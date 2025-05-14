package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.auth.domain.Role;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.ReservationCreateRequest;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.ReservationService;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.request.ReservationTimeCreateRequest;
import roomescape.reservationtime.dto.response.AvailableReservationTimeResponse;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;
import roomescape.reservationtime.exception.ReservationTimeAlreadyExistsException;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.repository.FakeReservationTimeRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.FakeThemeRepository;
import roomescape.theme.repository.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {
    private final LocalDate futureDate = LocalDate.now().plusDays(1);
    private final Theme theme = Theme.of(1L, "추리", "셜록 추리 게임 with Danny", "image.png");
    private final Member member = Member.of(1L, "Danny", "danny@example.com", "password", Role.MEMBER);

    private ReservationService reservationService;
    private ReservationTimeService reservationTimeService;

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private MemberRepository memberRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new FakeReservationTimeRepository();
        themeRepository = new FakeThemeRepository(reservationRepository);
        reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
        reservationService = new ReservationService(reservationRepository, memberRepository, reservationTimeRepository,
                themeRepository);
    }

    @Test
    void createReservationTime_shouldThrowException_IfDuplicated() {
        LocalTime time = LocalTime.of(1, 1);
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(time);

        reservationTimeService.create(request);

        assertThatThrownBy(() -> reservationTimeService.create(request))
                .isInstanceOf(ReservationTimeAlreadyExistsException.class);
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
    void deleteReservationTime_shouldThrowException_WhenIdNotFound() {
        assertThatThrownBy(() -> reservationTimeService.delete(999L))
                .isInstanceOf(ReservationTimeNotFoundException.class)
                .hasMessageContaining("요청한 id와 일치하는 예약 시간 정보가 없습니다.");
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
    void deleteReservationTime_shouldThrowException_WhenReservationExists() {
        ReservationTime reservationTime = reservationTimeRepository.save(
                ReservationTime.withUnassignedId(LocalTime.now()));
        Mockito.when(reservationRepository.existsByTimeId(anyLong())).thenReturn(true);

        assertThatThrownBy(() -> reservationTimeService.delete(reservationTime.getId()))
                .hasMessage("해당 시간에 대한 예약이 존재하여 삭제할 수 없습니다.");
    }

    @Test
    void getAvailableReservationTimes_shouldReturnAllAvailableReservationTimes() {
        // given
        reservationTimeService.create(new ReservationTimeCreateRequest(LocalTime.of(10, 0)));
        reservationTimeService.create(new ReservationTimeCreateRequest(LocalTime.of(11, 0)));
        reservationTimeService.create(new ReservationTimeCreateRequest(LocalTime.of(12, 0)));

        themeRepository.save(Theme.of(1L, "추리", "셜록 with Danny", "image.png"));
        Mockito.when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        Reservation reserved = Reservation.of(1L, futureDate, member,
                ReservationTime.of(1L, LocalTime.of(10, 0)), theme);
        Mockito.when(reservationRepository.save(Mockito.any())).thenReturn(reserved);
        Mockito.when(reservationRepository.findByDateAndThemeId(futureDate, 1L))
                .thenReturn(List.of(reserved));

        reservationService.create(1L, new ReservationCreateRequest(futureDate, 1L, 1L));

        // when
        List<AvailableReservationTimeResponse> availableReservationTimes =
                reservationTimeService.getAvailableReservationTimes(futureDate, 1L);

        // then
        long availableCount = availableReservationTimes.stream()
                .filter(time -> !time.alreadyBooked())
                .count();

        assertThat(availableCount).isEqualTo(2);
    }
}
