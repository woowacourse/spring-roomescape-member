package roomescape.reservation.controller;

import org.springframework.stereotype.Controller;

@Controller("/reservation")
public class ReservationViewController {

    public String getReservationPage() {
        return "reservation";
    }
}
