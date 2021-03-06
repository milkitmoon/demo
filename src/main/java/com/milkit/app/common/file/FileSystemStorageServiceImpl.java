package com.milkit.app.common.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.milkit.app.common.exception.StorageException;
import com.milkit.app.common.exception.handler.ApiResponseEntityExceptionHandler;
import com.milkit.app.domain.notice.service.NoticeAttachServiceImpl;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import javax.annotation.PostConstruct;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;


@Service
@Slf4j
public class FileSystemStorageServiceImpl {

    private final Path rootLocation;
    
    private FileUploadProperties properties;
	


    @Autowired
    public FileSystemStorageServiceImpl(FileUploadProperties properties) throws Exception {
//        this.rootLocation = Paths.get(properties.getLocation());
    	this.properties = properties;
        this.rootLocation = Paths.get(properties.getLocation()).toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException();
        }
    }

    public String store(MultipartFile file) throws Exception {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        

        if (file.isEmpty()) {
        	throw new StorageException("업로드 파일이 존재하지 않습니다.");
        }
        if (filename.contains("..")) {
        	throw new StorageException("업로드 파일명 형식이 올바르지 않습니다.");
        }
            
		if(isImage(file.getContentType())) {
            Thumbnails.of(file.getInputStream())
            .size(200, 200)
    		.toFile(new File(this.rootLocation.resolve(properties.getThumnailPrefix()+filename).toString()));
		}
            
		Files.copy(file.getInputStream(), this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    public Stream<Path> loadAll() throws Exception {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException();
        }

    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public Resource loadAsResource(String filename) throws Exception {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("파일을 찾을 수 없습니다: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("파일을 읽을 수 없습니다: " + filename);
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
    
    
    private boolean isImage(String filename) {
    	return filename.toLowerCase().matches("image/jpeg|image/png|image/gif") ? true : false;
    }
    
}