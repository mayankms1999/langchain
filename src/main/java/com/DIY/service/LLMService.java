package com.DIY.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class LLMService {

    private final ChatMemory chatMemory;
    private final ChatLanguageModel gemini;

    public LLMService(@Value("${google.api.key}") String apiKey) {
        this.chatMemory = TokenWindowChatMemory.withMaxTokens(1000, new Tokenizer() {
            @Override
            public int estimateTokenCountInText(String s) {
                return 0;
            }

            @Override
            public int estimateTokenCountInMessage(ChatMessage chatMessage) {
                return 0;
            }

            @Override
            public int estimateTokenCountInMessages(Iterable<ChatMessage> iterable) {
                return 0;
            }
        });

        this.gemini = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.0-flash")
                .build();
    }

    public String chatWithMemory(String prompt) {
        chatMemory.add(new UserMessage(prompt));

        List<ChatMessage> messages = chatMemory.messages();
        ChatResponse response = gemini.chat(messages);
        AiMessage aiMessage = response.aiMessage(); // Get actual AI message

        chatMemory.add(aiMessage);
        return aiMessage.text(); // Return only the message text
    }


    public String chatOnce(String prompt) {
        ChatResponse response = gemini.chat(List.of(new UserMessage(prompt)));
        AiMessage aiMessage = response.aiMessage();
        return aiMessage.text();
    }


    public void parseAndZipToResponse(String code, HttpServletResponse response) {
        try {
            Path tempDir = Files.createTempDirectory("parsed-code");

            // Split code into files
            String[] blocks = code.split("(?=\\b[a-zA-Z0-9_\\-/]+\\.[a-z]+\\b)");

            for (String block : blocks) {
                try {
                    String[] lines = block.strip().split("\n", 2);
                    if (lines.length < 2) continue;

                    String pathLine = lines[0].trim().replaceAll("[^a-zA-Z0-9._/\\\\-]", "_");
                    String fileContent = lines[1];

                    Path fullPath = tempDir.resolve(pathLine).normalize();

                    if (!fullPath.startsWith(tempDir)) {
                        System.out.println("Skipped invalid path: " + pathLine);
                        continue;
                    }

                    Files.createDirectories(fullPath.getParent());
                    Files.writeString(fullPath, fileContent);
                } catch (Exception e) {
                    System.out.println("Failed block: " + block);
                    e.printStackTrace();
                }
            }


            File zipFile = Files.createTempFile("generated-", ".zip").toFile();
            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
                Files.walk(tempDir).filter(Files::isRegularFile).forEach(file -> {
                    try {
                        ZipEntry zipEntry = new ZipEntry(tempDir.relativize(file).toString());
                        zos.putNextEntry(zipEntry);
                        Files.copy(file, zos);
                        zos.closeEntry();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
            }

            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"generated-project.zip\"");
            try (InputStream is = new FileInputStream(zipFile)) {
                is.transferTo(response.getOutputStream());
                response.flushBuffer();
            }

            Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            zipFile.delete();

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse and zip code", e);
        }
    }
}
