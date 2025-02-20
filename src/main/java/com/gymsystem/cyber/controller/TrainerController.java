package com.gymsystem.cyber.controller;

import com.gymsystem.cyber.entity.Trainer;
import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.model.Request.TrainerRequest;
import com.gymsystem.cyber.repository.TrainerRepository;
import com.gymsystem.cyber.service.TrainerService;
import com.gymsystem.cyber.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@CrossOrigin("*")
public class TrainerController {

    private final TrainerService trainerService;
    private final UserService userService;
    @Autowired
    public TrainerController(TrainerService trainerService, UserService userService) {
        this.trainerService = trainerService;
        this.userService = userService;
    }

    @Operation(summary = "Lấy danh sách tất cả huấn luyện viên", description = "Chỉ ADMIN mới được phép thực hiện.")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public List<Trainer> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    @Operation(summary = "Lấy thông tin huấn luyện viên theo ID", description = "ADMIN và USER đều có thể truy cập.")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{id}")
    public Trainer getTrainerById(@PathVariable String id) {
        return trainerService.getTrainerById(id);
    }

    @Operation(summary = "Tạo mới huấn luyện viên", description = "Chỉ ADMIN được phép thực hiện.")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public Trainer createTrainer(@RequestBody TrainerRequest trainerRequest) {
        User user = userService.getUserById(trainerRequest.getUserId());// Lấy User từ userId
        return trainerService.saveOrUpdateTrainer(trainerRequest, user);
    }

    @Operation(summary = "Cập nhật thông tin huấn luyện viên", description = "Chỉ ADMIN được phép thực hiện.")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public Trainer updateTrainer(@PathVariable String id, @RequestBody TrainerRequest trainerRequest) {
        User user = userService.getUserById(trainerRequest.getUserId());
        Trainer trainer = trainerService.getTrainerById(id);
        trainer.setSpecialization(trainerRequest.getSpecialization());
        trainer.setExperience_year(trainerRequest.getExperienceYear());
        trainer.setAvailability(trainerRequest.isAvailability());
        trainer.setUser(user);
        return trainerService.saveOrUpdateTrainer(trainerRequest, user);
    }

    @Operation(summary = "Xóa huấn luyện viên", description = "Chỉ ADMIN được phép thực hiện.")
   // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteTrainer(@PathVariable String id) {
        trainerService.deleteTrainer(id);
    }
}
