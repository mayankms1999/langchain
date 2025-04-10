package com.DIY.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageParserService {

    private static final Pattern FOLDER_PATTERN = Pattern.compile("<folder>(.*?)</folder>");
    private static final Pattern FILE_PATTERN = Pattern.compile("<action fileName=\"(.*?)\">([\\s\\S]*?)</action>");

    @Value("${output.base-dir:generated-output}")
    private String outputBaseDir;

    private File baseDir;

    @PostConstruct
    public void init() {
        baseDir = new File(outputBaseDir);
        if (!baseDir.exists()) baseDir.mkdirs();
    }

    public void parseAndGenerate(String message) {
        // Extract all folder paths
        List<String> folders = extractAllFolders(message);
        if (folders.isEmpty()) return;

        // Extract app root (first part of first folder path)
        String appRoot = extractAppRoot(folders.get(0));

        File projectDir = new File(baseDir, appRoot);

        createFolders(folders, projectDir);
        createFiles(message, projectDir);
    }

    private List<String> extractAllFolders(String message) {
        List<String> folders = new ArrayList<>();
        Matcher matcher = FOLDER_PATTERN.matcher(message);
        while (matcher.find()) {
            folders.add(matcher.group(1));
        }
        return folders;
    }

    private String extractAppRoot(String fullPath) {
        String[] parts = fullPath.split("/|\\\\");
        return parts.length > 0 ? parts[0] : "default-app";
    }

    private void createFolders(List<String> folders, File projectDir) {
        for (String folderPath : folders) {
            File folder = new File(projectDir, stripAppRoot(folderPath));
            if (!folder.exists()) folder.mkdirs();
        }
    }

    private void createFiles(String message, File projectDir) {
        Matcher matcher = FILE_PATTERN.matcher(message);
        while (matcher.find()) {
            String relativePath = matcher.group(1);
            String content = matcher.group(2);

            // Remove app root from path
            String adjustedPath = stripAppRoot(relativePath);

            File file = new File(projectDir, adjustedPath);
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content.trim());
            } catch (IOException e) {
                throw new RuntimeException("Failed to write file: " + adjustedPath, e);
            }
        }
    }

    private String stripAppRoot(String path) {
        String[] parts = path.split("/|\\\\", 2);
        return parts.length == 2 ? parts[1] : "";
    }
}
