package org.lib.rms_jobs.controllers.admin;

import org.lib.rms_jobs.constant.RequestPath;
import org.lib.rms_jobs.dto.response.ApiResponse;
import org.lib.rms_jobs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RequestPath.API_V1_USER)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getUserDetails(@RequestParam Long id){
        ApiResponse<Object> res = ApiResponse.success(userService.getUserDetails(id));
        return ResponseEntity.ok(res);
    }
}
