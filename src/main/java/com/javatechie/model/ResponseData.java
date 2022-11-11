package com.javatechie.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseData {

    private String fileName;
    private String fileType;
    private String downloadUrl;
    private Long fileSize;

}
