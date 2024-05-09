package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.model.Member;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.dao.MemberDao;
import roomescape.repository.dao.ReservationDao;
import roomescape.repository.dao.ReservationTimeDao;
import roomescape.repository.dao.ThemeDao;
import roomescape.repository.dto.ReservationSavedDto;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ReservationRepository {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final MemberDao memberDao;

    public ReservationRepository(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.memberDao = memberDao;
    }

    public List<Reservation> findAllReservations() {
        List<Reservation> result = new ArrayList<>();
        List<ReservationSavedDto> reservations = reservationDao.findAll();
        for (ReservationSavedDto reservation : reservations) {
            ReservationTime time = reservationTimeDao.findById(reservation.getTimeId()).orElseThrow(NoSuchElementException::new);
            Theme theme = themeDao.findById(reservation.getThemeId()).orElseThrow(NoSuchElementException::new);
            Member member = memberDao.findById(reservation.getMemberId()).orElseThrow(NoSuchElementException::new);
            result.add(new Reservation(reservation.getId(), reservation.getDate(), time, theme, member));
        }
        return result;
    }

    public Optional<ReservationTime> findReservationTimeById(long id) {
        return reservationTimeDao.findById(id);
    }

    public Optional<Theme> findThemeById(long id) {
        return themeDao.findById(id);
    }

    public Optional<Member> findMemberById(long id) {
        return memberDao.findById(id);
    }

    public boolean isExistReservationByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId) {
        return reservationDao.isExistByDateAndTimeIdAndThemeId(date, timeId, themeId);
    }

    public Reservation saveReservation(Reservation reservation) {
        ReservationSavedDto reservationSavedDto = new ReservationSavedDto(
                reservation.getId(), reservation.getDate(),
                reservation.getTime().getId(), reservation.getTheme().getId(), reservation.getMember().getId());
        long id = reservationDao.save(reservationSavedDto);
        ReservationSavedDto saved = reservationDao.findById(id).orElseThrow(NoSuchElementException::new);
        ReservationTime time = reservationTimeDao.findById(saved.getTimeId()).orElseThrow(NoSuchElementException::new);
        Theme theme = themeDao.findById(saved.getThemeId()).orElseThrow(NoSuchElementException::new);
        Member member = memberDao.findById(saved.getMemberId()).orElseThrow(NoSuchElementException::new);
        return new Reservation(id, saved.getDate(), time, theme, member);
    }

    public boolean isExistReservationById(long id) {
        return reservationDao.isExistById(id);
    }

    public void deleteReservationById(long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationTime> findReservationTimeBooked(LocalDate date, long themeId) {
        List<ReservationTime> result = new ArrayList<>();
        List<ReservationSavedDto> reservations = reservationDao.findByDateAndThemeId(date, themeId);
        Set<Long> timeIds = reservations.stream()
                .map(ReservationSavedDto::getTimeId)
                .collect(Collectors.toSet());
        for (long timeId : timeIds) {
            ReservationTime time = reservationTimeDao.findById(timeId).orElseThrow(NoSuchElementException::new);
            result.add(time);
        }
        return result;
    }

    public List<ReservationTime> findReservationTimeNotBooked(LocalDate date, long themeId) {
        List<ReservationTime> result = reservationTimeDao.findAll();
        List<ReservationTime> bookedTimes = findReservationTimeBooked(date, themeId);
        result.removeAll(bookedTimes);
        return result;
    }
}
