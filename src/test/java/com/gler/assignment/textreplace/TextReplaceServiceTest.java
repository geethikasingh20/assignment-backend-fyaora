package com.gler.assignment.textreplace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class TextReplaceServiceTest {

    private final TextReplaceService service = new TextReplaceService();

    @Test
    void throwsWhenLengthLessThanTwo() {
        assertThatThrownBy(() -> service.replace(""))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.replace("a"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void returnsEmptyWhenLengthTwo() {
        Optional<String> result = service.replace("ab");
        assertThat(result).isEmpty();
    }

    @Test
    void replacesFirstAndLastCharacter() {
        Optional<String> result = service.replace("abc");
        assertThat(result).contains("*b$");
    }

    @Test
    void replacesMixedString() {
        Optional<String> result = service.replace("abc#20xyz");
        assertThat(result).contains("*bc#20xy$");
    }
}
