package BrainF;

import static BrainF.TokenType.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private int lineIndex = 0;
    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put(">", RIGHT);
        keywords.put("<", LEFT);
        keywords.put("+", INCREMENT);
        keywords.put("-", DECREMENT);
        keywords.put(".", OUT);
        keywords.put(",", IN);
        keywords.put("[", LEFT_BRACE);
        keywords.put("]", RIGHT_BRACE);
    }

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token (EOF, "", line, 0));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '>':
                addToken(RIGHT);
                break;
            case '<':
                addToken(LEFT);
                break;
            case '+':
                addToken(INCREMENT);
                break;
            case '-':
                addToken(DECREMENT);
                break;
            case '.':
                addToken(OUT);
                break;
            case ',':
                addToken(IN);
                break;
            case '[':
                addToken(LEFT_BRACE);
                break;
            case ']':
                addToken(RIGHT_BRACE);
                break;
            case '\n':
                line++;
                lineIndex = 0;
                break;
        }
    }
    private char advance() {
        lineIndex++;
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, line, lineIndex));
    }
}
