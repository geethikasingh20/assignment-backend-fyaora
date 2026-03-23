package com.gler.assignment.textreplace;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TextReplaceController {

    private final TextReplaceService textReplaceService;

    public TextReplaceController(TextReplaceService textReplaceService) {
        this.textReplaceService = textReplaceService;
    }

    @GetMapping("/replace")
    public ResponseEntity<String> replace(@RequestParam("text") String text) {
        Optional<String> replaced = textReplaceService.replace(text);
        if (replaced.isEmpty()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(replaced.get());
    }
}
