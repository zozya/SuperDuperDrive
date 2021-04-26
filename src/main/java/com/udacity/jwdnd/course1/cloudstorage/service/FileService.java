package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import javax.activation.MimeType;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileService {

    private static final Map<String, MimeType> allowedContenttypeList = new HashMap<String, MimeType>();

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    private byte [] convertBlobToFile(Blob blob) throws SQLException {
        byte [] array = blob.getBytes(1, ( int ) blob.length());

        return array;
    }

    public List<File> getFiles(int userid) {
        return fileMapper.getFiles(userid);
    }

    public int createFile(String filename, String contenttype, long filesize, int userid, byte [] filedata) throws SQLException {
        return fileMapper.insert(new File(filename, contenttype, String.valueOf(filesize), userid, filedata));
    }

    public boolean isFilenameExists(String filename, int userid) {
        File file = fileMapper.getFileToName(filename, userid);
        if(file != null) {
            return true;
        } else {
            return false;
        }
    }

    public File getFile(String filename, int userid) {
        return fileMapper.getFileToName(filename, userid);
    }

    public int deleteFile(String filename, int userid) {
        return fileMapper.delete(filename, userid);
    }

}
