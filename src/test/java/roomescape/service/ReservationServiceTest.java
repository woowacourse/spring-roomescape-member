package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.reservation.CreateReservationRequest;
import roomescape.dto.reservation.ReservationResponses;
import roomescape.exception.DuplicateReservationException;
import roomescape.repository.fake.FakeReservationRepository;
import roomescape.repository.fake.FakeReservationTimeRepository;
import roomescape.repository.fake.FakeThemeRepository;

class ReservationServiceTest {

    private FakeReservationRepository reservationRepository;
    private FakeReservationTimeRepository reservationTimeRepository;
    private FakeThemeRepository themeRepository;
    private ReservationService service;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        themeRepository = new FakeThemeRepository(reservationRepository);
        service = new ReservationService(reservationRepository, themeRepository, reservationTimeRepository);
    }

    @Test
    void createReservation_id가_채워진_도메인을_반환한다() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "https://thumbnail.url"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        CreateReservationRequest request = new CreateReservationRequest(
                "브라운", themeId, LocalDate.of(2026, 5, 6), timeId);

        Reservation created = service.createReservation(request);

        assertThat(created.getId()).isPositive();
        assertThat(created.getName()).isEqualTo("브라운");
        assertThat(created.getDate()).isEqualTo(LocalDate.of(2026, 5, 6));
        assertThat(created.getTheme().getId()).isEqualTo(themeId);
        assertThat(created.getTime().getId()).isEqualTo(timeId);
    }

    @Test
    void createReservation_같은_날짜시간테마_중복이면_예외() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "https://thumbnail.url"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        service.createReservation(new CreateReservationRequest(
                "브라운", themeId, LocalDate.of(2026, 5, 6), timeId));

        assertThatThrownBy(() -> service.createReservation(new CreateReservationRequest(
                "다른사람", themeId, LocalDate.of(2026, 5, 6), timeId)))
                .isInstanceOf(DuplicateReservationException.class)
                .hasMessage("해당 날짜·시간·테마에 이미 예약이 존재합니다.");
    }

    @Test
    void getReservations_다음_페이지가_있으면_hasNext가_true() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        reservationRepository.save(buildReservation("A", themeId, timeId, LocalDate.of(2026, 5, 1)));
        reservationRepository.save(buildReservation("B", themeId, timeId, LocalDate.of(2026, 5, 2)));
        reservationRepository.save(buildReservation("C", themeId, timeId, LocalDate.of(2026, 5, 3)));

        ReservationResponses responses = service.getReservations(0, 2);

        assertThat(responses.reservations()).hasSize(2);
        assertThat(responses.hasNext()).isTrue();
    }

    @Test
    void getReservations_다음_페이지가_없으면_hasNext가_false() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        reservationRepository.save(buildReservation("A", themeId, timeId, LocalDate.of(2026, 5, 1)));
        reservationRepository.save(buildReservation("B", themeId, timeId, LocalDate.of(2026, 5, 2)));

        ReservationResponses responses = service.getReservations(0, 2);

        assertThat(responses.reservations()).hasSize(2);
        assertThat(responses.hasNext()).isFalse();
    }

    @Test
    void getReservation_id로_단건을_조회한다() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        Long reservationId = reservationRepository.save(
                buildReservation("브라운", themeId, timeId, LocalDate.of(2026, 5, 6)));

        Reservation found = service.getReservation(reservationId);

        assertThat(found.getId()).isEqualTo(reservationId);
        assertThat(found.getName()).isEqualTo("브라운");
    }

    @Test
    void deleteReservation_삭제후_조회되지_않는다() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        Long reservationId = reservationRepository.save(
                buildReservation("브라운", themeId, timeId, LocalDate.of(2026, 5, 6)));

        service.deleteReservation(reservationId);

        ReservationResponses responses = service.getReservations(0, 10);
        assertThat(responses.reservations()).extracting("id").doesNotContain(reservationId);
    }

    private Reservation buildReservation(String name, Long themeId, Long timeId, LocalDate date) {
        Theme theme = themeRepository.findById(themeId);
        ReservationTime time = reservationTimeRepository.findById(timeId);
        return new Reservation(null, name, theme, date, time);
    }
}