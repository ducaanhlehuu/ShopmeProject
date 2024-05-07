package com.shopme.admin;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {
    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void cleanDir(String cleanDir){
        Path dirPath = Paths.get(cleanDir);
        try{
            if(!Files.exists(dirPath)){
                Files.createDirectories(dirPath);
            }
            Files.list(dirPath).forEach(file->{
                if(!Files.isDirectory(file)){
                    try{
                        System.out.println("Delete file: "+file);
                        Files.delete(file);
                    }
                    catch (IOException e){
                        System.out.println("Could not delete file:" + file);
                    }
                }
            });
        }
        catch (IOException e){
            System.out.println("Could not list dir Path: "+ dirPath);
        }
    }
    public static void removeDir(String dir){
        cleanDir(dir);
        try{
            Files.delete(Paths.get(dir));
        } catch (IOException e) {
            System.out.println("Could not remove dir" + dir);
        }
    }
}
