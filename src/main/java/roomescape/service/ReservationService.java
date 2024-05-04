package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationAddRequest;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

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

    public List<Reservation> findAllReservation() {
        return reservationRepository.findAll();
    }

    public Reservation saveReservation(ReservationAddRequest request) {
        if (reservationRepository.existByDateAndTimeIdAndThemeId(request.date(), request.timeId(),
                request.themeId())) {
            throw new IllegalArgumentException("중복되는 예약이 존재합니다");
        }

        ReservationTime reservationTime = getReservationTime(request.timeId());
        Theme theme = getTheme(request.themeId());

        Reservation reservation = request.toEntity(reservationTime, theme);
        return reservationRepository.save(reservation);
    }

    private Theme getTheme(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 테마가 존재하지 않습니다 ID: " + themeId));
    }

    private ReservationTime getReservationTime(long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 예약시간이 존재하지 않습니다 ID: " + timeId));
    }

    public void removeReservation(long id) {
        reservationRepository.deleteById(id);
    }
}
