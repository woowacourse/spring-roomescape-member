package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.reservation.ReservationRequestDto;
import roomescape.dto.reservationTime.ReservationTimeRequestDto;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.theme.ThemeRepository;
import roomescape.repository.time.ReservationTimeRepository;

@Service
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

    public Reservation addReservation(ReservationRequestDto requestDto) {
        ReservationTime time = reservationTimeRepository.findById(requestDto.timeId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        Theme theme = themeRepository.findById(requestDto.themeId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        List<ReservationTime> availableTimes = reservationTimeRepository
            .findByDateAndThemeId(requestDto.date(), requestDto.themeId());
        if (!availableTimes.contains(time)) {
            throw new IllegalArgumentException("해당 테마에서 이미 예약된 날짜입니다.");
        }

        Reservation reservation = new Reservation(requestDto.name(), requestDto.date(), time, theme);
        return reservationRepository.createReservation(reservation);
    }

    // TODO: Null 처리
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationTime> getReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime addReservationTime(ReservationTimeRequestDto requestDto) {
        return reservationTimeRepository.createReservationTime(
            new ReservationTime(requestDto.startAt()));
    }

    // TODO: Null 처리
    public void deleteReservationTime(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new IllegalArgumentException("예약이 존재하는 시간은 삭제할 수 없습니다.");
        }

        reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTime> getAvailableTimes(LocalDate date, Long themeId) {
        Theme theme = themeRepository.findById(themeId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        return reservationTimeRepository.findByDateAndThemeId(date, theme.getId());
    }
}
