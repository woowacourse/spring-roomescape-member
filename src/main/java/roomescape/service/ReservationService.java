package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Person;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.exception.InvalidReservationException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.nowdate.CurrentDateTime;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final CurrentDateTime currentDateTime;

    public ReservationService(ReservationRepository reservationRepository,
        ReservationTimeRepository reservationTimeRepository,
        ThemeRepository themeRepository,
        CurrentDateTime currentDateTime) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.currentDateTime = currentDateTime;
    }

    public List<ReservationResponseDto> getAllReservations() {
        return reservationRepository.findAllReservation().stream()
            .map(ReservationResponseDto::from)
            .toList();
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteReservation(id);
    }

    public ReservationResponseDto saveReservation(ReservationRequestDto reservationRequestDto) {
        Reservation reservation = createReservationFrom(reservationRequestDto);
        reservation.validateDateTime(currentDateTime.get());
        validateAlreadyExistDateTime(reservationRequestDto, reservation.getReservationDate());
        reservationRepository.saveReservation(reservation);
        return ReservationResponseDto.from(reservation);
    }

    private Reservation createReservationFrom(ReservationRequestDto reservationRequestDto) {
        LocalDateTime currentDateTimeInfo = currentDateTime.get();
        Person person = new Person(reservationRequestDto.name());
        ReservationDate date = new ReservationDate(LocalDate.parse(reservationRequestDto.date()));
        date.validateDate(currentDateTimeInfo.toLocalDate());
        ReservationTime reservationTime = reservationTimeRepository.findById(
            reservationRequestDto.timeId());
        Theme theme = themeRepository.findById(reservationRequestDto.themeId());
        return new Reservation(person, date, reservationTime, theme);
    }

    private void validateAlreadyExistDateTime(ReservationRequestDto reservationRequestDto,
        ReservationDate date) {
        if (reservationRepository.hasAnotherReservation(date, reservationRequestDto.timeId())) {
            throw new InvalidReservationException("중복된 날짜와 시간을 예약할 수 없습니다.");
        }
    }
}
