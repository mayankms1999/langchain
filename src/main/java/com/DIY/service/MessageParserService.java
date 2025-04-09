package com.DIY.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public void parseAndGenerate(String message) {
        createFolders(message);
        createFiles(message);
    }

    private void createFolders(String message) {
        Matcher matcher = FOLDER_PATTERN.matcher(message);
        while (matcher.find()) {
            File folder = new File(baseDir, matcher.group(1));
            if (!folder.exists()) folder.mkdirs();
        }
    }

    private void createFiles(String message) {
        Matcher matcher = FILE_PATTERN.matcher(message);
        while (matcher.find()) {
            String path = matcher.group(1);
            String content = matcher.group(2);

            File file = new File(baseDir, path);
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content.trim());
            } catch (IOException e) {
                throw new RuntimeException("Failed to write file: " + path, e);
            }
        }
    }
}
