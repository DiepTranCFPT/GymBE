//package com.gymsystem.cyber.controller;
//
//
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import org.springframework.cglib.core.Local;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//
//@RestController
//@SecurityRequirement(name = "bearerAuth")
//@CrossOrigin("*")
//@RequestMapping("api/test")
//public class testAPI {
//
//    @GetMapping("/public-api")
//    public String publicAPI() {
//        return "This is a public API";
//    }
//
//
//    @GetMapping("/admin-api")
//    public String adminAPI() {
//        int localDateTime = LocalDateTime.now().getDayOfYear();
//        return localDateTime + "This is an admin API";
//    }
//}
