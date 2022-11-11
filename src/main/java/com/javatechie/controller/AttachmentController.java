package com.javatechie.controller;


import com.javatechie.entity.Attachment;
import com.javatechie.model.ResponseData;
import com.javatechie.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping(path = "/file")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @Autowired
    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        Attachment attachment = new Attachment();
        String downloadUrl = "";
        attachment = attachmentService.saveAttachment(file);

        downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/file/download/").path(attachment.getId()).toUriString();
        return new ResponseData(attachment.getFileName(), downloadUrl, file.getContentType(), file.getSize());
    }

    @GetMapping(path = "/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Attachment attachment = new Attachment();
        attachment = attachmentService.getAttachment(fileId);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(attachment.getFileType())).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"").body(new ByteArrayResource(attachment.getData()));
    }

}
