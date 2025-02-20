package com.gymsystem.cyber.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// khi tra ve fe tra vè duoi dạng ResponseObject
public class ResponseObject {
    private String message;
    private HttpStatus httpStatus;
    private Object data;
}