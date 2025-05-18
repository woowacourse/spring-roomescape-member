package roomescape.admin.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.domain.Admin;
import roomescape.admin.dto.MemberResponse;
import roomescape.admin.dto.NameResponse;
import roomescape.admin.service.AdminService;
import roomescape.auth.infrastructure.argument.AuthorizedAdmin;

@RestController
public class AdminApiController {

    private final AdminService adminService;

    public AdminApiController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admins/my/name")
    public ResponseEntity<NameResponse> getMyName(@AuthorizedAdmin Admin authorizedAdmin) {
        return ResponseEntity.ok().body(NameResponse.fromAdmin(authorizedAdmin));
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> getMembers(@AuthorizedAdmin Admin authorizedAdmin) {
        return ResponseEntity.ok().body(adminService.getMembers());
    }
}
