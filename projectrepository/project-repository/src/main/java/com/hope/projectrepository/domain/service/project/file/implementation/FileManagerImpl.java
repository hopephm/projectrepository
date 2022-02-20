package com.hope.projectrepository.domain.service.project.file.implementation;

import com.hope.projectrepository.domain.entity.FileInfo;
import com.hope.projectrepository.domain.entity.ProjectContent;
import com.hope.projectrepository.domain.repository.FileInfoRepository;
import com.hope.projectrepository.domain.service.project.file.FileManager;
import com.hope.projectrepository.exception.service.file.CreateFileResponseEntityException;
import com.hope.projectrepository.exception.service.file.FileInfoDoesNotExistException;
import com.hope.projectrepository.exception.service.file.FileTransferException;
import com.hope.projectrepository.exception.service.file.OriginFileDoesNotExistException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public class FileManagerImpl implements FileManager {
    @Autowired
    FileInfoRepository fileInfoRepository;

    @Value("${app.file.save_path}")
    String fileSavePath;

    public ResponseEntity<byte[]> getFile(String fileId) {
        FileInfo fileInfo = getFileInfo(fileId);
        String fileName = fileInfo.getFileName();
        String filePath = fileInfo.getFilePath();

        String format = getFormat(fileName);
        MediaType mediaType = getMediaType(format);

        ResponseEntity<byte[]> entity = constructResponseEntity(mediaType, filePath, fileName);
        return entity;
    }

    private FileInfo getFileInfo(String fileId){
        Optional<FileInfo> optionalFileInfo = fileInfoRepository.findById(Long.parseLong(fileId));

        if(optionalFileInfo.equals(Optional.empty()))
            throw new FileInfoDoesNotExistException();

        FileInfo fileInfo = optionalFileInfo.get();

        return fileInfo;
    }

    private String getFormat(String fileName){
        String formatName = fileName.substring(fileName.lastIndexOf(".")+1);
        return formatName.toUpperCase();
    }

    private MediaType getMediaType(String format){
        if(format.equals("JPG")) return MediaType.IMAGE_JPEG;
        else if (format.equals("GIF")) return MediaType.IMAGE_GIF;
        else if (format.equals("PNG")) return MediaType.IMAGE_PNG;
        else if (format.equals("HTML")) return MediaType.TEXT_HTML;
        else if (format.equals("XML")) return MediaType.TEXT_XML;
        else if (format.equals("PDF")) return MediaType.APPLICATION_PDF;

        return MediaType.APPLICATION_OCTET_STREAM;
    }

    private ResponseEntity<byte[]> constructResponseEntity(MediaType mediaType, String filePath, String fileName){
        ResponseEntity<byte[]> entity = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            if (isUnSupportMediaType(mediaType))
                headers.add("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");

            InputStream fileStream = new FileInputStream(filePath);
            entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(fileStream), headers, HttpStatus.CREATED);
        }catch(Exception e){
            throw new CreateFileResponseEntityException();
        }
        return entity;
    }

    private Boolean isUnSupportMediaType(MediaType mediaType) {
        if (mediaType.equals(MediaType.APPLICATION_OCTET_STREAM))
            return true;
        return false;
    }

    @Transactional
    public void uploadFile(ProjectContent content, MultipartFile file){
        String fileName = constructFileName(content);

        downloadRealFile(fileName, file);
        saveFileInfo(content, fileName, file);
    }

    private String constructFileName(ProjectContent content){
        String root = getFileRootDirectory(content);
        UUID filename = UUID.randomUUID();
        return root + filename;
    }

    private String getFileRootDirectory(ProjectContent content){
        return fileSavePath + content.getProjectOverview().getUser().getUserId().toString() + "/";
    }

    private void downloadRealFile(String fileName, MultipartFile file){
        File target = new File(fileName);
        try{
            InputStream fileStream = file.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream,target);
        }catch (Exception e){
            FileUtils.deleteQuietly(target);
            e.printStackTrace();
        }
    }

    private void saveFileInfo(ProjectContent content, String fileName, MultipartFile file){
        FileInfo fileInfo = FileInfo.builder()
                .projectContent(content)
                .filePath(fileName)
                .fileName(file.getOriginalFilename())
                .build();

        fileInfoRepository.save(fileInfo);
    }
}
