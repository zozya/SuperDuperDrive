package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentials(int userid) {
        List<Credential> credentials = credentialMapper.getCredentials(userid);
        for(Credential credential : credentials) {
            String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());
            credential.setDecryptedPassword(decryptedPassword);
        }
        return credentials;
    }

    public int saveCredentialOrEdit(String url, String username, String password, String credentialid, int userid) {

        if(credentialid == null || credentialid.isBlank()) {
            return saveCredential(url, username, password, userid);
        } else {
            return editCredential(url, username, password, Integer.valueOf(credentialid), userid);
        }
    }

    private int saveCredential(String url, String username, String password, int userid) {
        String encodedKey = generateKey();
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
        return credentialMapper.insert(new Credential(url, username, encodedKey, encryptedPassword, userid));
    }

    private int editCredential(String url, String username, String password, int credentialid, int userid) {
        Credential credential = credentialMapper.getCredential(credentialid, userid);
        String encryptedPassword = encryptionService.encryptValue(password, credential.getKey());
        return credentialMapper.update(url, username, encryptedPassword, credentialid, userid);
    }

    public int deleteCredential(int credentialid, int userid) {
        return credentialMapper.delete(credentialid, userid);
    }

    private String generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

}
