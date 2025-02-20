package com.gymsystem.cyber.IService;

import com.gymsystem.cyber.entity.Trainer;
import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.model.Request.TrainerRequest;

import java.util.List;

public interface ITrainer {
    // Lấy tất cả Trainer
    List<Trainer> getAllTrainers();

    // Lấy Trainer theo ID
    Trainer getTrainerById(String id);

    // Thêm mới hoặc cập nhật Trainer
    Trainer saveOrUpdateTrainer(TrainerRequest trainerRequest, User user);

    // Xóa Trainer theo ID
    void deleteTrainer(String id);

    // Tìm Trainer theo User
    Trainer findTrainerByUser(User user);
}
