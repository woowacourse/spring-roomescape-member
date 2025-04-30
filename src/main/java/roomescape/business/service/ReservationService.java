package roomescape.business.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.business.Reservation;
import roomescape.business.ReservationTime;
import roomescape.business.dto.ReservationRequestDto;
import roomescape.business.dto.ReservationResponseDto;
import roomescape.business.dto.ReservationTimeRequestDto;
import roomescape.business.dto.ReservationTimeResponseDto;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
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
                        )
                ))
                .toList();
    }

    public ReservationResponseDto readReservationOne(Long id) {
        Reservation reservation = reservationRepository.findById(id);
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                new ReservationTimeResponseDto(
                        reservation.getTime().getId(),
                        reservation.getTime().getStartAt()
                )
        );
    }

    public Long createReservation(ReservationRequestDto reservationDto) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationDto.timeId());
        validatePastDateTime(reservationDto.date(), reservationTime.getStartAt());
        validateDuplicatedDateTime(reservationDto, reservationTime);
        return reservationRepository.add(
                new Reservation(reservationDto.name(), reservationDto.date(), reservationTime));
    }

    private void validatePastDateTime(LocalDate date, LocalTime time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
        if (reservationDateTime.isBefore(now)) {
            throw new IllegalArgumentException("과거 일시로 예약을 생성할 수 없습니다.");
        }
    }

    private void validateDuplicatedDateTime(ReservationRequestDto reservationDto, ReservationTime reservationTime) {
        if (reservationRepository.existsByDateTime(reservationDto.date(), reservationTime.getStartAt())) {
            throw new IllegalArgumentException("이미 예약된 일시입니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public Long createTime(ReservationTimeRequestDto reservationTimeRequestDto) {
        return reservationTimeRepository.add(new ReservationTime(reservationTimeRequestDto.startAt()));
    }

    public List<ReservationTimeResponseDto> readTimeAll() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return reservationTimes.stream()
                .map(reservationTime -> new ReservationTimeResponseDto(
                        reservationTime.getId(),
                        reservationTime.getStartAt()
                ))
                .toList();
    }

    public ReservationTimeResponseDto readTimeOne(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id);
        return new ReservationTimeResponseDto(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }

    public void deleteTime(Long id) {
        reservationTimeRepository.deleteById(id);
    }
}
