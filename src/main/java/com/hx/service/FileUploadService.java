package com.hx.service;

import com.hx.dao.UserFileDao;
import com.hx.pojo.User;
import com.hx.pojo.UserFile;
import com.hx.utils.FileNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    @Autowired
    UserFileDao userFileDao;
    private final String UPLOAD_PATH = "File/upload/";

    public boolean keepFiles(MultipartFile[] files, User user) throws IOException {
        System.out.println(user);
        for (MultipartFile file : files) {
            // 判断文件是否有内容
            if (file.isEmpty()) {
                System.out.println("该文件无任何内容!!!");
                return false;
            }
            String fileName = FileNameUtils.getFileName(file);
            System.out.println("fileName:" + fileName);
            Path directory = Paths.get(UPLOAD_PATH + user.getUserName());
            // 判断目录是否存在，不存在创建
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            try {
                InputStream inputStream = file.getInputStream();
                Files.copy(inputStream, directory.resolve(fileName));
                userFileDao.saveUserFile(new UserFile(null,user.getUid(),fileName,UPLOAD_PATH + user.getUserName()+'/'+fileName));
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
