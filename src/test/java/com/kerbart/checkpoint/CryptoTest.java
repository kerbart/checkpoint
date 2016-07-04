package com.kerbart.checkpoint;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.kerbart.checkpoint.exceptions.CryptoException;
import com.kerbart.checkpoint.helper.CryptoUtils;
import com.kerbart.checkpoint.spring.AppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/application-context-test.xml")
@SpringApplicationConfiguration(classes = AppConfiguration.class)
@WebAppConfiguration
public class CryptoTest {

    @Test
    public void shouldEncryptAndDecryptDocument() {
        String originalContent = "my content is good ok !!!! \n yes !";
        String key = RandomStringUtils.randomAlphanumeric(16);
        File inputFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "document.txt");
        try {
            IOUtils.write(originalContent, new FileOutputStream(inputFile));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File encryptedFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "document.encrypted");
        File decryptedFile = new File(System.getProperty("java.io.tmpdir") + File.separator +"document.decrypted");

        try {
            CryptoUtils.encrypt(key, inputFile, encryptedFile);
            CryptoUtils.decrypt(key, encryptedFile, decryptedFile);

            String descrytedContent = FileUtils.readFileToString(decryptedFile);
            System.out.println(descrytedContent);
            assertEquals(originalContent, descrytedContent);
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
