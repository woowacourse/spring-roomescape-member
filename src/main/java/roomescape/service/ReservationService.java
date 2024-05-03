package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.response.MemberReservationTimeResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.DuplicatedException;
import roomescape.exception.NotFoundException;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.ReservationDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

@Service
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

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAllReservations();
    }

    public Reservation addReservation(ReservationDto reservationDto) {
        ReservationTime reservationTime = reservationTimeRepository.findReservationById(reservationDto.getTimeId());
        Theme theme = themeRepository.findThemeById(reservationDto.getThemeId());

        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDto.getDate(), reservationTime.getStartAt());

        LocalDateTime requestDateTime = reservationDateTime.truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        if (requestDateTime.isBefore(now)) {
            throw new BadRequestException("[ERROR] 현재 이전 예약은 할 수 없습니다.");
        }
        Long countReservation = reservationRepository.countReservationByDateAndTimeId(reservationDto.getDate(), reservationDto.getTimeId());
        if (countReservation == null || countReservation > 0) {
            throw new DuplicatedException("[ERROR] 중복되는 예약은 추가할 수 없습니다.");
        }
        Reservation reservation = new Reservation(reservationDto.getName(), reservationDto.getDate(), reservationTime, theme);
        return reservationRepository.saveReservation(reservation);
    }

    public void deleteReservation(long id) {
        Long count = reservationRepository.countReservationById(id);
        if (count == null || count <= 0) {
            throw new NotFoundException("[ERROR] 존재하지 않는 예약입니다.");
        }
        reservationRepository.deleteReservationById(id);
    }

    public List<MemberReservationTimeResponse> getMemberReservationTimes(LocalDate date, long themeId) {
        List<ReservationTime> allTimes = reservationTimeRepository.findAllReservationTimes();
        List<ReservationTime> bookedTimes = reservationRepository.findReservationTimeByDateAndThemeId(date, themeId);
        List<ReservationTime> notBookedTimes = filterNotBookedTimes(allTimes, bookedTimes);
        List<MemberReservationTimeResponse> bookedResponse = mapToResponse(bookedTimes, true);
        List<MemberReservationTimeResponse> notBookedResponse = mapToResponse(notBookedTimes, false);
        return concat(notBookedResponse, bookedResponse);
    }

    private List<ReservationTime> filterNotBookedTimes(List<ReservationTime> allTimes, List<ReservationTime> bookedTimes) {
        return allTimes.stream()
                .filter(time -> !bookedTimes.contains(time))
                .toList();
    }

    private List<MemberReservationTimeResponse> mapToResponse(List<ReservationTime> times, boolean isBooked) {
        return times.stream()
                .map(time -> new MemberReservationTimeResponse(time.getId(), time.getStartAt(), isBooked))
                .toList();
    }

    private List<MemberReservationTimeResponse> concat(List<MemberReservationTimeResponse> notBookedTimes,
                                                       List<MemberReservationTimeResponse> bookedTimes) {
        return Stream.concat(notBookedTimes.stream(), bookedTimes.stream()).toList();
    }
}
