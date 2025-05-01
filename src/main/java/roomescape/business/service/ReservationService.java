package roomescape.business.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.business.Reservation;
import roomescape.business.ReservationTheme;
import roomescape.business.ReservationTime;
import roomescape.exception.ReservationException;
import roomescape.exception.ReservationThemeException;
import roomescape.exception.ReservationTimeException;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.presentation.dto.ReservationRequestDto;
import roomescape.presentation.dto.ReservationResponseDto;
import roomescape.presentation.dto.ReservationThemeResponseDto;
import roomescape.presentation.dto.ReservationTimeResponseDto;

@Service
public final class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ReservationThemeRepository reservationThemeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationThemeRepository = reservationThemeRepository;
    }

    public List<ReservationResponseDto> readReservationAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(reservation -> new ReservationResponseDto(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getDate(),
                        new ReservationTimeResponseDto(
                                reservation.getTime().getId(),
                                reservation.getTime().getStartAt()
                        ),
                        new ReservationThemeResponseDto(
                                reservation.getTheme().getId(),
                                reservation.getTheme().getName(),
                                reservation.getTheme().getDescription(),
                                reservation.getTheme().getThumbnail()
                        )
                ))
                .toList();
    }

    public ReservationResponseDto readReservationOne(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationException("존재하지 않는 예약입니다."));
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                new ReservationTimeResponseDto(
                        reservation.getTime().getId(),
                        reservation.getTime().getStartAt()
                ),
                new ReservationThemeResponseDto(
                        reservation.getTheme().getId(),
                        reservation.getTheme().getName(),
                        reservation.getTheme().getDescription(),
                        reservation.getTheme().getThumbnail()
                )
        );
    }

    public Long createReservation(ReservationRequestDto reservationDto) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationDto.timeId())
                .orElseThrow(() -> new ReservationTimeException("존재하지 않는 예약 시간입니다."));
        ReservationTheme theme = reservationThemeRepository.findById(reservationDto.themeId())
                .orElseThrow(() -> new ReservationThemeException("존재하지 않는 예약 테마입니다."));
        validatePastDateTime(reservationDto.date(), reservationTime.getStartAt());
        Reservation reservation = new Reservation(reservationDto.name(), reservationDto.date(), reservationTime, theme);
        validateDuplicatedReservation(reservation);
        return reservationRepository.add(reservation);
    }

    private void validatePastDateTime(LocalDate date, LocalTime time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
        if (reservationDateTime.isBefore(now)) {
            throw new ReservationException("과거 일시로 예약을 생성할 수 없습니다.");
        }
    }

    private void validateDuplicatedReservation(Reservation reservation) {
        if (reservationRepository.existsByReservation(reservation)) {
            throw new ReservationException("이미 예약되었습니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
