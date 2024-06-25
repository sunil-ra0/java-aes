package org.example.encryptionDemo;

import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DataController {
    private final EncryptionService encryptionService;

    public DataController() throws Exception {
        this.encryptionService = new EncryptionService();
    }

    @GetMapping("/encrypt")
    public Map<String, String> encryptData() throws Exception {
        String data = "Sensitive Data Example";
        String encryptedData = encryptionService.encryptData(data);
        Files.write(Paths.get("data.txt"), encryptedData.getBytes());

        Map<String, String> response = new HashMap<>();
        response.put("encryptedData", encryptedData);
        response.put("aesKey", Base64.getEncoder().encodeToString(encryptionService.getAesKey().getEncoded()));

        return response;
    }

    @PostMapping("/decrypt")
    public String decryptData(@RequestBody Map<String, String> request) throws Exception {
        String encryptedData = request.get("encryptedData");
        return encryptionService.decryptData(encryptedData);
    }
}

