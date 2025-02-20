package com.gymsystem.cyber.iService;

import com.gymsystem.cyber.model.ResponseObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface IFaceRecodeService {

    CompletableFuture<ResponseObject> regisFaceIDforAccount(String email, MultipartFile file) throws IOException;

}
