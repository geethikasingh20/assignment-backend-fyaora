package com.gler.assignment.textreplace;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TextReplaceService {

    public Optional<String> replace(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text must be provided");
        }
        int length = text.length();
        if (length < 2) {
            throw new IllegalArgumentException("Text length must be at least 2");
        }
        if (length == 2) {
            return Optional.empty();
        }
        String replaced = "*" + text.substring(1, length - 1) + "$";
        return Optional.of(replaced);
    }
}
