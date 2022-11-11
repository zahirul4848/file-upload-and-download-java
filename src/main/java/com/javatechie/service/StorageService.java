package com.javatechie.service;


import com.javatechie.entity.FileData;
import com.javatechie.entity.ImageData;
import com.javatechie.repository.FileDataRepository;
import com.javatechie.repository.StorageRepository;
import com.javatechie.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class StorageService {
    private final StorageRepository storageRepository;
    private final FileDataRepository fileDataRepository;
    private final String FOLDER_PATH = "/Users/admin/Desktop/untitled folder 2/";

    @Autowired
    public StorageService(StorageRepository storageRepository, FileDataRepository fileDataRepository) {
        this.storageRepository = storageRepository;
        this.fileDataRepository = fileDataRepository;
    }

    public String uploadImage(MultipartFile file) throws IOException {
        ImageData imageData = storageRepository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes())).build());
        if (imageData != null) {
            return "file uploaded successfully : " + file.getOriginalFilename();
        }
        return null;
    }

    public byte[] downloadImage(String fileName) {
        Optional<ImageData> dbImageData = storageRepository.findByName(fileName);
        byte[] images=ImageUtils.decompressImage(dbImageData.get().getImageData());
        return images;
    }

    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String file_path = FOLDER_PATH + file.getOriginalFilename();
        FileData fileData = fileDataRepository.save(FileData.builder().name(file.getOriginalFilename()).type(file.getContentType()).filePath(file_path).build());
        file.transferTo(new File(file_path));
        if (fileData != null) {
            return "File uploaded successfully : " + file_path;
        }
        return null;
    }

    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        String filePath = fileData.get().getFilePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }
}
