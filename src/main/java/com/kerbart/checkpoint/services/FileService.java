package com.kerbart.checkpoint.services;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kerbart.checkpoint.exceptions.CryptoException;
import com.kerbart.checkpoint.helper.CryptoUtils;
import com.kerbart.checkpoint.model.Application;
import com.kerbart.checkpoint.repositories.ApplicationRepository;

@Service
public class FileService {

    @Value("${storage.path}")
    String storagePath;

    @Inject
    ApplicationRepository applicationRepository;

    public String storeFile(String applicationToken, File inputFile) {
        String filePath = storagePath + RandomStringUtils.randomAlphanumeric(32);
        Application app = applicationRepository.findByToken(applicationToken);
        try {
            CryptoUtils.encrypt(app.getSecret(), inputFile, new File(filePath));
        } catch (CryptoException e) {
            return null;
        }
        return filePath;
    }

    public File decryptFile(String applicationToken, File inputFile) {
        try {
            File temp = File.createTempFile("dec", "ord");
            Application app = applicationRepository.findByToken(applicationToken);
            CryptoUtils.decrypt(app.getSecret(), inputFile, temp);
            return temp;
        } catch (IOException e) {
            return null;
        } catch (CryptoException e) {
            return null;
        }
    }

    public File convertToFile(byte[] bytes) {
        File tempFile;
        try {
            tempFile = File.createTempFile(RandomStringUtils.randomAlphanumeric(16), "tmp");
            tempFile.deleteOnExit();
            FileUtils.writeByteArrayToFile(tempFile, bytes);
        } catch (IOException e) {
            return null;
        }
        return tempFile;

    }

}
