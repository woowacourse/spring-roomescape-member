package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.reservation.ReservationRequestDto;
import roomescape.dto.reservation.ReservationResponseDto;
import roomescape.dto.reservationTime.ReservationTimeRequesetDto;
import roomescape.dto.reservationTime.ReservationTimeResponseDto;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponseDto> getReservations() {
        List<ReservationResponseDto> responseDtos = new ArrayList<>();
        List<Reservation> reservations = reservationRepository.findAll();

        for (Reservation reservation : reservations) {
            Long timeId = reservation.getTime().getId();
            ReservationTime time = reservationTimeRepository.findById(timeId);
            Reservation reservationIncludingTime = new Reservation(reservation, time);

            responseDtos.add(ReservationResponseDto.from(reservationIncludingTime));
        }

        return responseDtos;
    }

    public ReservationResponseDto addReservation(ReservationRequestDto requestDto) {
        ReservationTime time = reservationTimeRepository.findById(requestDto.time_id());
        Long id = reservationRepository.createReservation(new Reservation(null, requestDto.name(), requestDto.date(), time));
        Reservation reservation = reservationRepository.findById(id);

        return ReservationResponseDto.from(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationTimeResponseDto> getReservationTimes() {
        List<ReservationTimeResponseDto> responseDtos = new ArrayList<>();
        List<ReservationTime> times = reservationTimeRepository.findAll();

        for (ReservationTime time : times) {
            responseDtos.add(ReservationTimeResponseDto.from(time));
        }

        return responseDtos;
    }

    public ReservationTimeResponseDto addReservationTime(ReservationTimeRequesetDto requestDto) {
        ReservationTime reservationTime = reservationTimeRepository.createReservationTime(new ReservationTime(null, requestDto.startAt()));

        return ReservationTimeResponseDto.from(reservationTime);
    }

    public void deleteReservationTime(Long id) {
        reservationTimeRepository.deleteById(id);
    }
}
