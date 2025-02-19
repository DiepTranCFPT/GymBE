package com.gymsystem.cyber.service;

import com.gymsystem.cyber.IService.ITrainer;
import com.gymsystem.cyber.entity.Trainer;
import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.model.Request.TrainerRequest;
import com.gymsystem.cyber.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TrainerService implements ITrainer {


    private final TrainerRepository trainerRepository;
    @Autowired
    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    @Override
    public Trainer getTrainerById(String id) {
        Optional<Trainer> trainer = trainerRepository.findById(id);
        return trainer.orElseThrow(() -> new RuntimeException("Trainer not found with id: " + id));
    }

    @Override
    public Trainer saveOrUpdateTrainer(TrainerRequest trainerRequest, User user) {
        Trainer trainer = new Trainer();
        trainer.setId(UUID.randomUUID().toString());
        trainer.setSpecialization(trainerRequest.getSpecialization());
        trainer.setExperience_year(trainerRequest.getExperienceYear());
        trainer.setAvailability(trainerRequest.isAvailability());
        trainer.setUser(user); // Thiết lập User từ UserService
        System.out.println(trainer);
        return trainerRepository.save(trainer);
    }


    @Override
    public void deleteTrainer(String id) {
        trainerRepository.deleteById(id);
    }

    @Override
    public Trainer findTrainerByUser(User user) {
        return trainerRepository.findByUser(user);
    }

}
