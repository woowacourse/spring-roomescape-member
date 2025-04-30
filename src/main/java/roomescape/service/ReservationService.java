package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.exceptions.ReservationDuplicateException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> readReservation() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse postReservation(ReservationRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId());
        Theme theme = themeRepository.findById(request.timeId());
        Reservation reservation = Reservation.createIfDateTimeValid(request.name(), request.date(), time, theme);
        if (isAnyMatchDateTime(reservation)) {
            throw new ReservationDuplicateException("해당 시각의 중복된 예약이 존재합니다.", reservation.date(),
                    reservation.time().startAt());
        }

        Reservation newReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(newReservation);
    }

    @Transactional
    public void deleteReservation(long id) {
        reservationRepository.deleteById(id);
    }

    private boolean isAnyMatchDateTime(Reservation reservation) {
        return reservationRepository.findAll().stream()
                .anyMatch(current -> current.equalDateTime(reservation));
    }
}
