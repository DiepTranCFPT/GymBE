package com.gymsystem.cyber.service;


import com.gymsystem.cyber.entity.Trainer;
import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.enums.UserRole;
import com.gymsystem.cyber.iService.ITrainerService;
import com.gymsystem.cyber.model.Request.TrainerRequest;
import com.gymsystem.cyber.model.Response.TrainerReponse;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.AuthenticationRepository;
import com.gymsystem.cyber.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TrainerService implements ITrainerService {


    private final TrainerRepository trainerRepository;
    private final AuthenticationRepository authenticationRepository;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository, AuthenticationRepository authenticationRepository) {
        this.trainerRepository = trainerRepository;
        this.authenticationRepository = authenticationRepository;
    }


    @Override
    @Transactional
    public CompletableFuture<ResponseObject> getAllTrains() {

        return CompletableFuture.supplyAsync(() -> {

            List<TrainerReponse> temp = trainerRepository.findAll().stream()
                    .filter(trainer -> !trainer.getUser().isDeleted())
                    .map(trainer ->
                            TrainerReponse.builder()
                                    .phone(trainer.getUser().getPhone() == null ? "" : trainer.getUser().getPhone())
                                    .experience_year(trainer.getExperience_year())
                                    .name(trainer.getUser().getName())
                                    .id(trainer.getId())
                                    .enable(trainer.isLocked())
                                    .status(trainer.isStatus())
                                    .email(trainer.getUser().getEmail()).build()
                    ).toList();
            return ResponseObject.builder()
                    .data(temp.isEmpty() ? List.of() : temp)
                    .httpStatus(HttpStatus.OK)
                    .message("GET ALL TRAINERS")
                    .build();
        });
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<ResponseObject> getTrainerById(String id) {

        return CompletableFuture.supplyAsync(() -> {

            Trainer trainer = trainerRepository.findById(id).map(trainer1 -> {
                        if (!trainer1.getUser().isDeleted() && trainer1.getUser().isEnable()) {
                            return trainer1;
                        }
                        throw new UsernameNotFoundException("TRAINER NOT FOUND OR DELETED");
                    })
                    .orElseThrow(() -> new UsernameNotFoundException("username not found"));

            TrainerReponse trainerReponse = TrainerReponse.builder().avata(trainer.getUser().getAvata())
                    .phone(trainer.getUser().getPhone())
                    .experience_year(trainer.getExperience_year())
                    .name(trainer.getUser().getName())
                    .id(trainer.getId())
                    .email(trainer.getUser().getEmail())
                    .build();
            return ResponseObject.builder()
                    .message("Trainer: ")
                    .data(trainerReponse)
                    .httpStatus(HttpStatus.OK)
                    .build();
        });
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<ResponseObject> creatTrainer(TrainerRequest trainerRequest, String emailUser) {

        // Tìm user theo email và kiểm tra trạng thái
        User user = authenticationRepository.findByEmail(emailUser)
                .filter(u -> !u.isDeleted() && u.isEnable())
                .orElseThrow(() -> new UsernameNotFoundException("User not exist or deleted, is not enabled"));

        // Kiểm tra xem user đã là trainer chưa
        if (user.getTrainer() != null) {
            throw new UsernameNotFoundException("Trainer already exists!");
        }

        // Cập nhật role
        user.setRole(UserRole.PT);
        authenticationRepository.save(user);

        // Tạo trainer
        Trainer trainer = Trainer.builder()
                .experience_year(Math.max(trainerRequest.getExperienceYear(), 0))
                .user(user)
                .status(true)
                .specialization(trainerRequest.getSpecialization())
                .locked(false)
                .build();

        trainerRepository.save(trainer);

        return CompletableFuture.completedFuture(
                ResponseObject.builder()
                        .httpStatus(HttpStatus.OK)
                        .data(true)
                        .message("Add trainer successfully!")
                        .build()
        );
    }


    @Override
    @Async
    @Transactional
    public CompletableFuture<ResponseObject> deleteTrainer(String id) {

        return CompletableFuture.supplyAsync(() -> {

            Trainer trainer = trainerRepository.findById(id).map(trainer1 ->
                    {
                        if (!trainer1.getUser().isDeleted() && trainer1.getUser().isEnable()) {
                            return trainer1;
                        }
                        throw new UsernameNotFoundException("TRAINER NOT FOUND OR DELETED");
                    })
                    .orElseThrow(
                            () -> new UsernameNotFoundException("Trainer not found or is locked")
                    );


            boolean temp = !trainer.isLocked();
            trainer.setLocked(temp);
            trainer.setStatus(false);
            trainerRepository.saveAndFlush(trainer);
            return ResponseObject.builder()
                    .httpStatus(HttpStatus.OK)
                    .data(temp ? "lock successfully" : "unlock successfully")
                    .message("Update lock trainer successfully!")
                    .build();
        });
    }


}
