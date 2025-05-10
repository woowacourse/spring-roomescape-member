package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.dto.ReservationAvailableTimeResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.exception.ReservationDuplicateException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.entity.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;

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

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId());
        Theme theme = themeRepository.findById(request.themeId());
        Reservation reservation = Reservation.createIfDateTimeValid(request.name(), request.date(), time, theme);
        if (isDuplicate(reservation)) {
            throw new ReservationDuplicateException("해당 시각의 중복된 예약이 존재합니다.", reservation.getDate(),
                    reservation.getTime().getStartAt(), reservation.getTheme().getName());
        }

        Reservation newReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(newReservation);
    }

    public List<ReservationResponse> readReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public void deleteReservationById(Long id) {
        reservationRepository.deleteById(id);
    }

    private boolean isDuplicate(Reservation reservation) {
        return reservationRepository.findAll().stream()
                .anyMatch(current -> current.isDuplicate(reservation));
    }

    public List<ReservationAvailableTimeResponse> readAvailableReservationTimes(LocalDate date, Long themeId) {
        List<Long> bookedTimeIds = reservationRepository.findBookedTimeIdsByDateAndThemeId(date, themeId);
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        return reservationTimes.stream()
                .map(time -> ReservationAvailableTimeResponse.of(time, bookedTimeIds.contains(time.getId())))
                .toList();
    }
}
