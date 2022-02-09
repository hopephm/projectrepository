package com.hope.projectrepository.domain.service.project.file.implementation;

import com.hope.projectrepository.domain.entity.FileInfo;
import com.hope.projectrepository.domain.entity.ProjectContent;
import com.hope.projectrepository.domain.repository.FileInfoRepository;
import com.hope.projectrepository.domain.service.project.file.FileManager;
import com.hope.projectrepository.exception.service.file.FileInfoDoesNotExistException;
import com.hope.projectrepository.exception.service.file.FileTransferException;
import com.hope.projectrepository.exception.service.file.OriginFileDoesNotExistException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class FileManagerImpl implements FileManager {
    @Autowired
    FileInfoRepository fileInfoRepository;

    @Value("${app.file.save_path}")
    String fileSavePath;

    public void sendFileToClient(HttpServletResponse response, String fileId){
        FileInfo fileInfo = getFileInfo(fileId);

        String originFileName = getOriginFileName(fileInfo);
        String sendFileName = getSendFileName(fileInfo);
        Long fileLength = getFileLength(originFileName);

        setResponseHeader(response, sendFileName, fileLength);

        sendFile(response, originFileName);
    }

    private FileInfo getFileInfo(String fileId){
        FileInfo fileInfo = fileInfoRepository.getById(Long.parseLong(fileId));
        if(fileInfo == null)
            throw new FileInfoDoesNotExistException();
        return fileInfo;
    }

    private String getOriginFileName(FileInfo fileInfo){
        return fileInfo.getFilePath();
    }

    private String getSendFileName(FileInfo fileInfo){
        return new String(fileInfo.getFileName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
    }

    private Long getFileLength(String originFileName){
        File file = new File(originFileName);
        if(!file.isFile())
            throw new OriginFileDoesNotExistException();
        return file.length();
    }

    private void setResponseHeader(HttpServletResponse response, String sendFileName, Long fileLength){
        response.setHeader("Content-Disposition", "attachment; filename=\"" + sendFileName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", "text/plain");
        response.setHeader("Content-Length", "" + fileLength);
        response.setHeader("Pragma", "no-cache;");
        response.setHeader("Expires", "-1;");
    }

    private void sendFile(HttpServletResponse response, String originFileName){
        try (FileInputStream fis = new FileInputStream(originFileName);
             OutputStream out = response.getOutputStream();) {
            int readCount = 0;
            byte[] buffer = new byte[1024];

            while ((readCount = fis.read(buffer)) != -1) {
                out.write(buffer, 0, readCount);
            }
        } catch (Exception ex) {
            throw new FileTransferException();
        }
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
