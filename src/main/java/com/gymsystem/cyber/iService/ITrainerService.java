package com.gymsystem.cyber.iService;

import com.gymsystem.cyber.model.Request.TrainerRequest;
import com.gymsystem.cyber.model.ResponseObject;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ITrainerService {
    /**
     * lay ta ca cac trainer tren db voi role admin
     *
     * @return @{@link CompletableFuture<ResponseObject> } {@link ResponseObject} => {@link List<com.gymsystem.cyber.model.Response.TrainerReponse>}
     */
    CompletableFuture<ResponseObject> getAllTrains();
    // Lấy Trainer theo ID

    /**
     * @param @{@link String} id Lấy Trainer theo ID
     * @return @{@link CompletableFuture<ResponseObject> } {@link ResponseObject} => {@link com.gymsystem.cyber.model.Response.TrainerReponse}
     */
    CompletableFuture<ResponseObject> getTrainerById(String id);

    //

    /**
     * Thêm mới hoặc cập nhật Trainer
     * @param @{@link TrainerRequest} trainerRequest
     * @param @{@link String} emailUser
     * @return @{@link CompletableFuture<ResponseObject>} true f
     */
    CompletableFuture<ResponseObject> creatTrainer(TrainerRequest trainerRequest, String emailUser);

    //

    /**
     * Xóa Trainer theo ID
     * @param {{@link String}} id
     * @return {@link CompletableFuture<ResponseObject>}
     */
    CompletableFuture<ResponseObject> deleteTrainer(String id);

//    // Tìm Trainer theo User
//    Trainer findTrainerByUser(User user);
}
