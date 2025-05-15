package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.domain.Admin;
import roomescape.auth.infrastructure.argument.AuthorizedAdmin;
import roomescape.auth.infrastructure.argument.AuthorizedMember;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.dto.ReservationFilterCondition;
import roomescape.reservation.domain.dto.ReservationReqDto;
import roomescape.reservation.domain.dto.ReservationResDto;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationApiController {

    private final ReservationService service;

    public ReservationApiController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResDto>> findAllByCondition(
        @AuthorizedAdmin Admin admin,
        @ModelAttribute ReservationFilterCondition filterCondition
    ) {
        List<ReservationResDto> resDtos = service.findAll(filterCondition);
        return ResponseEntity.ok(resDtos);
    }

    @PostMapping
    public ResponseEntity<ReservationResDto> add(
        @AuthorizedMember Member authorizedMember,
        @RequestBody @Valid ReservationReqDto reqDto
    ) {
        ReservationResDto resDto = service.add(authorizedMember, reqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resDto);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> delete(@PathVariable("reservationId") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
