package com.gymsystem.cyber.service;


import com.gymsystem.cyber.entity.Trainer;
import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.iService.ITrainerService;
import com.gymsystem.cyber.model.Request.TrainerRequest;
import com.gymsystem.cyber.model.Response.TrainerReponse;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.AuthenticationRepository;
import com.gymsystem.cyber.repository.TrainerRepository;
import io.grpc.Grpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    @Async
    public CompletableFuture<ResponseObject> getAllTrains() {

        return CompletableFuture.supplyAsync(() -> {

            List<TrainerReponse> temp = trainerRepository.findAll().stream()
                    .filter(trainer -> !trainer.getUser().isDeleted() && trainer.getUser().isEnable())
                    .map(trainer ->
                            TrainerReponse.builder().avata(trainer.getUser().getAvata())
                                    .phone(trainer.getUser().getPhone())
                                    .experience_year(trainer.getExperience_year())
                                    .name(trainer.getUser().getName())
                                    .id(trainer.getId())
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

        return CompletableFuture.supplyAsync(() -> {
            User user = authenticationRepository.findByEmail(emailUser).map(user1 -> {
                        if (!user1.isDeleted() && user1.isEnable()) {
                            return user1;
                        }
                        throw new UsernameNotFoundException("USER NOT FOUND OR DELETED");
                    })
                    .orElseThrow(() -> new UsernameNotFoundException("User not exist or deleted, is not enabled"));

            if (trainerRepository.existsByUser(user))
                throw new UsernameNotFoundException("Trainer exists!");

            Trainer trainer = Trainer.builder()
                    .experience_year(Math.max(trainerRequest.getExperienceYear(), 0))
                    .user(user)
                    .specialization(trainerRequest.getSpecialization())
                    .locked(false).build();

            trainerRepository.saveAndFlush(trainer);

            return ResponseObject.builder()
                    .httpStatus(HttpStatus.OK)
                    .data(true)
                    .message("add trainer successfully!")
                    .build();

        });

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
            trainerRepository.saveAndFlush(trainer);
            return ResponseObject.builder()
                    .httpStatus(HttpStatus.OK)
                    .data(temp ? "lock successfully" : "unlock successfully")
                    .message("Update lock trainer successfully!")
                    .build();
        });
    }

}
