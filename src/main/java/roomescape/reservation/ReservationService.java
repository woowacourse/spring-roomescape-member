package roomescape.reservation;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.AlreadyInUseException;
import roomescape.exception.InvalidStateException;
import roomescape.exception.NotFoundException;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationsResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public ReservationResponse create(ReservationRequest reservationRequest, LocalDateTime now) {

        ReservationTime reservationTime = reservationTimeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new NotFoundException("예약 시간을 찾을 수 없습니다."));
        Theme theme = themeRepository.findById(reservationRequest.themeId()).stream().findFirst()
                .orElseThrow(() -> new NotFoundException("테마를 찾을 수 없습니다."));

        Reservation reservation = new Reservation(
                reservationRequest.userName(),
                theme,
                reservationRequest.date(),
                reservationTime
        );

        validateDuplicate(reservation);
        validateDateTime(now, reservation);

        Reservation saved = reservationRepository.save(reservation);

        return ReservationResponse.from(saved);
    }

    public ReservationsResponse read(int page, int size) {
        List<ReservationResponse> reservationsResponse = reservationRepository.findAll(page, size).stream()
                .map(ReservationResponse::from)
                .toList();

        return ReservationsResponse.from(reservationsResponse);
    }

    public ReservationsResponse readByUserName(String userName) {
        List<ReservationResponse> reservationsResponse = reservationRepository.findByUserName(userName).stream()
                .map(ReservationResponse::from)
                .toList();

        return ReservationsResponse.from(reservationsResponse);
    }

    @Transactional
    public void delete(Long id, LocalDateTime now) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("예약을 찾을 수 없습니다."));

        validateDateTime(now, reservation);

        reservationRepository.deleteById(id);
    }

    private void validateDuplicate(Reservation reservation) {
        if (reservationRepository.existsByThemeIdAndDateAndTimeId(
                reservation.getTheme().getId(),
                reservation.getDate(),
                reservation.getTime().getId())
        ) {
            throw new AlreadyInUseException("이미 예약된 테마•날짜•시간입니다.");
        }
    }

    private void validateDateTime(LocalDateTime now, Reservation reservation) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservation.getDate(),
                reservation.getTime().getStartAt());
        if (reservationDateTime.isBefore(now)) {
            throw new InvalidStateException("이미 지난 날짜와 시간입니다.");
        }
    }
}
