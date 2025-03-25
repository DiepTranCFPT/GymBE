package com.gymsystem.cyber.model;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
// khi tra ve fe tra vè duoi dạng ResponseObject
public class ResponseObject {
    private String message;
    private HttpStatus httpStatus;
    private Object data;
}