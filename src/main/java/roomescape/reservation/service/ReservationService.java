package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.reservation.dto.request.CreateReservationRequest;
import roomescape.reservation.dto.response.CreateReservationResponse;
import roomescape.reservation.dto.response.FindAvailableTimesResponse;
import roomescape.reservation.dto.response.FindReservationResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public CreateReservationResponse createReservation(final CreateReservationRequest createReservationRequest) {
        ReservationTime reservationTime = reservationTimeRepository.findById(createReservationRequest.timeId())
                .orElseThrow(() -> new NoSuchElementException("해당하는 예약 시간이 존재하지 않습니다."));

        Theme theme = themeRepository.findById(createReservationRequest.themeId())
                .orElseThrow(() -> new NoSuchElementException("해당하는 테마가 존재하지 않습니다."));

        validateReservationDateTime(createReservationRequest.date(), reservationTime.getTime());
        Reservation reservation = createReservationRequest.toReservation(reservationTime, theme);
        // TODO: 추가한 예외 처리 테스트 작성하기
        if (reservationRepository.existsByDateAndTime(reservation.getDate(), reservationTime.getId())) {
            throw new IllegalStateException("동일한 시간의 예약이 존재합니다.");
        }

        return CreateReservationResponse.of(reservationRepository.save(reservation));
    }

    // TODO: 추가한 예외 처리 테스트 작성하기
    private void validateReservationDateTime(final LocalDate reservationDate, final LocalTime reservationTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate, reservationTime);
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
        }
    }

    public List<FindReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(FindReservationResponse::of)
                .toList();
    }

    public FindReservationResponse getReservation(final Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 예약이 존재하지 않습니다."));
        return FindReservationResponse.of(reservation);
    }

    public void deleteReservation(final Long id) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 예약이 존재하지 않습니다."));
        reservationRepository.deleteById(id);
    }

    // TODO: stream 리팩토링
    public List<FindAvailableTimesResponse> getAvailableTimes(final LocalDate date, final Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        List<Reservation> reservations = reservationRepository.findAllByDateAndThemeId(date, themeId);
        return reservationTimes.stream()
                .map(reservationTime -> getFindAvailableTimesResponse(reservations, reservationTime))
                .toList();
    }

    // TODO: 함수명 변경
    private static FindAvailableTimesResponse getFindAvailableTimesResponse(final List<Reservation> reservations,
                                                                            final ReservationTime reservationTime) {
        return new FindAvailableTimesResponse(
                reservationTime.getId(),
                reservationTime.getTime(),
                reservations.stream()
                        .anyMatch(reservation -> reservation.isSameTime(reservationTime)));
    }
}
