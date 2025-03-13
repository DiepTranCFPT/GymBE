package com.gymsystem.cyber.controller;

import com.gymsystem.cyber.iService.ITrainerService;
import com.gymsystem.cyber.entity.Trainer;
import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.model.Request.TrainerRequest;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.TrainerRepository;
import com.gymsystem.cyber.service.TrainerService;
import com.gymsystem.cyber.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/trainers")
@CrossOrigin("*")
public class TrainerController {

    private final ITrainerService trainerService;
    private final UserService userService;

    @Autowired
    public TrainerController(ITrainerService trainerService, UserService userService) {
        this.trainerService = trainerService;
        this.userService = userService;
    }

    @Operation(summary = "Tạo mới huấn luyện viên", description = "Chỉ ADMIN được phép thực hiện.")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public CompletableFuture<ResponseObject> createTrainer(@RequestParam String email, @RequestBody TrainerRequest trainerRequest) {
        return trainerService.creatTrainer(trainerRequest, email);
    }

    @GetMapping("/list-all")
    @Operation(summary = "lay tat ca cac Pt con hoat dong tren he thong", description = "only admin")
    public CompletableFuture<ResponseObject> getAllTrainer() {
        return trainerService.getAllTrains();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "lau Pt voi id", description = "only admin")
    public CompletableFuture<ResponseObject> getTrainerbyId(@PathVariable(value = "id") String id) {
        return trainerService.getTrainerById(id);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "lock Pt voi id", description = "only admin")
    public CompletableFuture<ResponseObject> deletedTrainer(@PathVariable("id") String id) {
        return trainerService.deleteTrainer(id);
    }

}
