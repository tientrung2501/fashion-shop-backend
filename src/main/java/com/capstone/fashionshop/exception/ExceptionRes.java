package com.capstone.fashionshop.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionRes {
    private int status;
    private Object message;
}
