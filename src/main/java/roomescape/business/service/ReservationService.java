package roomescape.business.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.business.Reservation;
import roomescape.business.ReservationTime;
import roomescape.business.dto.ReservationRequestDto;
import roomescape.business.dto.ReservationResponseDto;
import roomescape.business.dto.ReservationTimeRequestDto;
import roomescape.business.dto.ReservationTimeResponseDto;
import roomescape.persistence.GeneralRepository;

@Service
public class ReservationService {

    private final GeneralRepository<Reservation> reservationRepository;
    private final GeneralRepository<ReservationTime> reservationTimeRepository;

    @Autowired
    public ReservationService(GeneralRepository<Reservation> reservationRepository,
                              GeneralRepository<ReservationTime> reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponseDto> readReservationAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    public ReservationResponseDto readReservationOne(Long id) {
        Reservation reservation = reservationRepository.findById(id);
        return ReservationResponseDto.from(reservation);
    }

    public Long createReservation(ReservationRequestDto reservationDto) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationDto.timeId());
        return reservationRepository.add(
                new Reservation(reservationDto.name(), reservationDto.date(), reservationTime));
    }

    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }

    public Long createTime(ReservationTimeRequestDto reservationTimeRequestDto) {
        return reservationTimeRepository.add(new ReservationTime(reservationTimeRequestDto.startAt()));
    }

    public List<ReservationTimeResponseDto> readTimeAll() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponseDto::from)
                .toList();
    }

    public ReservationTimeResponseDto readTimeOne(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id);
        return ReservationTimeResponseDto.from(reservationTime);
    }

    public void deleteTime(Long id) {
        reservationTimeRepository.delete(id);
    }
}
