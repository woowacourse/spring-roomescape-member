package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.AvailableBookTimes;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.AddReservationDto;
import roomescape.dto.AvailableTimeRequestDto;
import roomescape.exception.InvalidReservationException;
import roomescape.exception.InvalidReservationTimeException;
import roomescape.exception.InvalidThemeException;
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

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional
    public long addReservation(AddReservationDto newReservation) {
        ReservationTime reservationTime = reservationTimeRepository.findById(newReservation.timeId())
                .orElseThrow(() -> new InvalidReservationTimeException("존재하지 않는 예약 시간 id입니다."));
        Theme theme = themeRepository.findById(newReservation.themeId())
                .orElseThrow(() -> new InvalidThemeException("존재하지 않는 테마 id입니다."));

        Reservation reservation = newReservation.toReservation(reservationTime, theme);

        validateSameReservation(reservation);
        LocalDateTime currentDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        validateAddReservationDateTime(reservation, currentDateTime);
        return reservationRepository.add(reservation);
    }


    private void validateSameReservation(Reservation reservation) {
        if (reservationRepository.existsByDateAndTimeId(reservation)) {
            throw new InvalidReservationException("중복된 예약신청입니다");
        }
    }

    private void validateAddReservationDateTime(Reservation newReservation, LocalDateTime currentDateTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(newReservation.getDate(), newReservation.getStartAt());
        if (reservationDateTime.isBefore(currentDateTime)) {
            throw new InvalidReservationException("과거 시간에 예약할 수 없습니다.");
        }
    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    public AvailableBookTimes availableReservationTimes(AvailableTimeRequestDto availableTimeRequestDto) {
        List<ReservationTime> times = reservationTimeRepository.findAll();

        List<Reservation> alreadyReservedReservations = reservationRepository.findAllByDateAndThemeId(
                availableTimeRequestDto.date(), availableTimeRequestDto.themeId());

        return new AvailableBookTimes(times, alreadyReservedReservations);
    }
}
