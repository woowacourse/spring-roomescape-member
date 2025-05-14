package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.domain.Role;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.dto.request.ReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {

    private final LocalDate futureDate = LocalDate.now().plusDays(1);
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ThemeRepository themeRepository;
    private Long memberId;

    @BeforeEach
    void setUp() {
        Member member = Member.withUnassignedId("Danny", "danny@example.com", "password", Role.MEMBER);
        reservationTimeRepository.save(ReservationTime.withUnassignedId(LocalTime.of(10, 0)));
        themeRepository.save(Theme.withUnassignedId("추리", "셜록 추리 게임 with Danny", "image.png"));
        memberId = memberRepository.save(member).getId();
    }

    @Test
    void createReservation_shouldReturnResponseWhenSuccessful() {
        Long timeId = reservationTimeRepository.findAll().getFirst().getId();
        Long themeId = themeRepository.findAll().getFirst().getId();

        ReservationCreateRequest request = new ReservationCreateRequest(futureDate, timeId, themeId);
        ReservationResponse response = reservationService.create(memberId, request);

        assertThat(response.date()).isEqualTo(futureDate);
        assertThat(response.time().startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void deleteReservation_shouldThrowException_WhenIdNotFound() {
        assertThatThrownBy(() -> reservationService.delete(999L))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void deleteReservation_shouldRemoveSuccessfully() {
        Long timeId = reservationTimeRepository.findAll().getFirst().getId();
        Long themeId = themeRepository.findAll().getFirst().getId();

        ReservationCreateRequest request = new ReservationCreateRequest(futureDate, timeId, themeId);
        ReservationResponse response = reservationService.create(memberId, request);

        reservationService.delete(response.id());

        List<ReservationResponse> reservations = reservationService.getReservations();
        assertThat(reservations).isEmpty();
    }
}
