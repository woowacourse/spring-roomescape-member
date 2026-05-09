package roomescape.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.reservation.ReservationRequestDto;
import roomescape.dto.reservationTime.ReservationTimeRequestDto;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.theme.ThemeRepository;
import roomescape.repository.time.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
        ReservationRepository reservationRepository,
        ReservationTimeRepository reservationTimeRepository,
        ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation addReservation(ReservationRequestDto requestDto) {
        ReservationTime time = reservationTimeRepository.findById(requestDto.timeId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        Theme theme = themeRepository.findById(requestDto.themeId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        List<ReservationTime> availableTimes = reservationTimeRepository
            .findTimesByDateAndThemeId(requestDto.date(), requestDto.themeId());
        if (!availableTimes.contains(time)) {
            throw new IllegalArgumentException("해당 테마에서 이미 예약된 날짜입니다.");
        }

        Reservation reservation = new Reservation(requestDto.name(), requestDto.date(), time, theme);
        return reservationRepository.createReservation(reservation);
    }

    @Transactional
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationTime> getReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    @Transactional
    public ReservationTime addReservationTime(ReservationTimeRequestDto requestDto) {
        return reservationTimeRepository.createReservationTime(
            new ReservationTime(requestDto.startAt()));
    }

    @Transactional
    public void deleteReservationTime(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new IllegalArgumentException("예약이 존재하는 시간은 삭제할 수 없습니다.");
        }

        reservationTimeRepository.deleteById(id);
    }

    public Map<ReservationTime, Boolean> getTimesWithAvailability(LocalDate date, Long themeId) {
        Map<ReservationTime, Boolean> timesWithAvailability =  new HashMap<>();

        Theme theme = themeRepository.findById(themeId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        List<ReservationTime> availableTimes = reservationTimeRepository.findTimesByDateAndThemeId(date, theme.getId());

        for (ReservationTime time : reservationTimeRepository.findAll()) {
            if (availableTimes.contains(time)) {
                timesWithAvailability.put(time, true);
                continue;
            }

            timesWithAvailability.put(time, false);
        }

        return timesWithAvailability;
    }
}
