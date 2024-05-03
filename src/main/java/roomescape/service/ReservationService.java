package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.reservation.dto.ReservationRequest;
import roomescape.controller.reservation.dto.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.exception.DuplicateReservation;
import roomescape.service.exception.PreviousTimeException;
import roomescape.service.exception.ThemeNotFoundException;
import roomescape.service.exception.TimeNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    // TODO 리팩터링
    public ReservationResponse addReservation(final ReservationRequest reservationRequest) {
        final ReservationTime time = reservationTimeRepository
                .findById(reservationRequest.timeId())
                .orElseThrow(() -> new TimeNotFoundException("존재하지 않은 시간입니다."));
        final Theme theme = themeRepository
                .findById(reservationRequest.themeId())
                .orElseThrow(() -> new ThemeNotFoundException("존재하지 않는 테마입니다."));

        final Reservation parsedReservation = reservationRequest.toDomain(time, theme);
        //TODO 이때 테마도 같이 확인 해야함.
        final boolean isExistsReservation = reservationRepository.existsByDateAndTimeId(time.getId(), parsedReservation.getDate());
        if (isExistsReservation) {
            throw new DuplicateReservation("중복된 시간으로 예약이 불가합니다.");
        }

        final LocalDateTime reservationDateTime = parsedReservation.getDate().atTime(time.getStartAt());

        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new PreviousTimeException("지난 시간으로 예약할 수 없습니다.");
        }

        final Reservation savedReservation = reservationRepository.save(parsedReservation);
        return ReservationResponse.from(savedReservation);
    }

    public int deleteReservation(final Long id) {
        return reservationRepository.deleteById(id);
    }
}
