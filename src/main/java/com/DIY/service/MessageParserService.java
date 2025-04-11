package com.DIY.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageParserService {

    private static final Pattern FOLDER_PATTERN = Pattern.compile("<folder>(.*?)</folder>");
    private static final Pattern FILE_PATTERN = Pattern.compile("<action fileName=\"(.*?)\">([\\s\\S]*?)</action>");

    private File baseDir;

    @PostConstruct
    public void init() {
        baseDir = new File("generated-output");
        if (!baseDir.exists()) baseDir.mkdirs();
    }

    public String parseAndGenerate(String message, String appName) {
        String uniqueId = UUID.randomUUID().toString();
        File appRoot = new File(baseDir, uniqueId + File.separator + appName);
        appRoot.mkdirs();

        createFolders(message, appRoot);
        createFiles(message, appRoot);

        // Return the generated folder name (UUID) for reference
        return uniqueId;
    }

    private void createFolders(String message, File appRoot) {
        Matcher matcher = FOLDER_PATTERN.matcher(message);
        while (matcher.find()) {
            String folderPath = matcher.group(1).trim();
            File folder = new File(appRoot, folderPath);
            if (!folder.exists()) folder.mkdirs();
        }
    }

    private void createFiles(String message, File appRoot) {
        Matcher matcher = FILE_PATTERN.matcher(message);
        while (matcher.find()) {
            String filePath = matcher.group(1).trim();
            String content = matcher.group(2).trim();

            File file = new File(appRoot, filePath);
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write file: " + filePath, e);
            }
        }
    }
}
