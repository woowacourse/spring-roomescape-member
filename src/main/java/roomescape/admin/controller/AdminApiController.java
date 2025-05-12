package roomescape.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.domain.Admin;
import roomescape.admin.dto.NameResponse;
import roomescape.auth.infrastructure.argument.AuthorizedAdmin;

@RestController
@RequestMapping("/admins")
public class AdminApiController {

    @GetMapping("/my/name")
    public ResponseEntity<NameResponse> getMyName(@AuthorizedAdmin Admin authorizedAdmin) {
        return ResponseEntity.ok().body(NameResponse.fromAdmin(authorizedAdmin));
    }
}
