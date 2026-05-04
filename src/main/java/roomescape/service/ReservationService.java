package roomescape.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.reservation.ReservationRequestDto;
import roomescape.dto.reservation.ReservationResponseDto;
import roomescape.dto.reservationTime.ReservationTimeRequesetDto;
import roomescape.dto.reservationTime.ReservationTimeResponseDto;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;
import roomescape.repository.theme.ThemeRepository;

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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다. timeId: " + requestDto.timeId()));
        Theme theme = themeRepository.findById(requestDto.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다. themeId: " + requestDto.themeId()));

        Reservation reservation = new Reservation(requestDto.name(), requestDto.date(), time, theme);
        return reservationRepository.createReservation(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationTime> getReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime addReservationTime(ReservationTimeRequesetDto requestDto) {
        return reservationTimeRepository.createReservationTime(
            new ReservationTime(requestDto.startAt()));
    }

    public void deleteReservationTime(Long id) {
        reservationTimeRepository.deleteById(id);
    }
}
