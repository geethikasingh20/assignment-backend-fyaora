package com.gler.assignment.textreplace;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gler.assignment.common.exception.GlobalExceptionHandler;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TextReplaceController.class)
@Import(GlobalExceptionHandler.class)
class TextReplaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TextReplaceService textReplaceService;

    @Test
    void returnsEmptyBodyForLengthTwo() throws Exception {
        given(textReplaceService.replace("ab")).willReturn(Optional.empty());

        mockMvc.perform(get("/replace").param("text", "ab"))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }

    @Test
    void returnsReplacedText() throws Exception {
        given(textReplaceService.replace("abc")).willReturn(Optional.of("*b$"));

        mockMvc.perform(get("/replace").param("text", "abc"))
            .andExpect(status().isOk())
            .andExpect(content().string("*b$"));
    }

    @Test
    void returnsBadRequestWhenTextTooShort() throws Exception {
        given(textReplaceService.replace("a")).willThrow(new IllegalArgumentException("Text length must be at least 2"));

        mockMvc.perform(get("/replace").param("text", "a"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.message").value("Text length must be at least 2"))
            .andExpect(jsonPath("$.path").value("/replace"));
    }
}
