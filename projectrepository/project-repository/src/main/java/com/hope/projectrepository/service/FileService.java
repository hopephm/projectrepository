package com.hope.projectrepository.service;

import com.hope.projectrepository.domain.FileInfo;
import com.hope.projectrepository.repository.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Service
public class FileService {
    @Autowired
    FileInfoRepository fileInfoRepository;

    public void sendFileToClient(HttpServletResponse response, String fileId){
        FileInfo fileInfo = fileInfoRepository.getById(Long.parseLong(fileId));
        String fileName = fileInfo.getFilePath();
        String saveFileName = new String(fileInfo.getFileName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        File file = new File(fileName);

        response.setHeader("Content-Disposition", "attachment; filename=\"" + saveFileName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", "text/plain");
        response.setHeader("Content-Length", "" + file.length());
        response.setHeader("Pragma", "no-cache;");
        response.setHeader("Expires", "-1;");

        try (FileInputStream fis = new FileInputStream(fileName);
             OutputStream out = response.getOutputStream();) {
            int readCount = 0;
            byte[] buffer = new byte[1024];

            while ((readCount = fis.read(buffer)) != -1) {
                out.write(buffer, 0, readCount);
            }
        } catch (Exception ex) {
            throw new RuntimeException("file Load Error");
        }
    }
}
