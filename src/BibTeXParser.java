import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.nio.file.Path;
import java.util.*;

public class BibTeXParser {
    final PushbackReader reader;
    int lineCount;
    Map<String, String> variables;

    public BibTeXParser(Path file) {
        lineCount = 1;
        try {
            reader = new PushbackReader(new FileReader(file.toFile()), 2);
            variables = new HashMap<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // bibTeX ::= { bObject }
    public BibTeX getBibTeX() {
        Set<BibTeXEntry> entries = new HashSet<>();
        while (!isEOF()) {
            bObject().ifPresent(entries::add);
            skipWhitespace();
        }
        return new BibTeX(entries);
    }

    private char readChar() {
        try {
            char retval = (char) reader.read();
            if (retval == '\n') lineCount++;
            return retval;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void unreadChar(char c) {
        try {
            reader.unread((int) c);
            if (c == '\n') lineCount--;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isEOF() {
        try {
            int c = reader.read();
            if (c < 0 || c >= 0xFFFF) return true;

            reader.unread(c);
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private char look() {
        char retval = readChar();
        unreadChar(retval);
        return retval;
    }

    private String readUntil(char breakpoint) {
        String retval = "";
        char c = readChar();
        while (c != breakpoint) {
            retval += c;
            c = readChar();
        }
        ;

        unreadChar(breakpoint);
        return retval;
    }

    private boolean isAlpha() {
        char lookup = look();
        return Character.isLetter(lookup) || Character.isDigit(lookup) || Set.of('-', '_').contains(lookup);
    }

    private String readWord() {
        String retval = "";
        while (isAlpha()) {
            retval += readChar();
        }
        return retval;
    }

    private void match(char pattern) {
        if (Character.toUpperCase(look()) != Character.toUpperCase(pattern))
            throw new RuntimeException("Expected: '" + pattern + "' on line " + lineCount);
        readChar();
    }

    private void matchWhole(String pattern) {
        try {
            for (char c : pattern.toCharArray()) {
                match(c);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Expected: '" + pattern + "' on line " + lineCount);
        }
    }

    private void matchCurlyBracketsPair() {
        match('{');

        int nestingLevel = 1;
        while (!isEOF()) {
            switch (readChar()) {
                case '{':
                    nestingLevel++;
                    break;
                case '}':
                    nestingLevel--;
                    break;
            }

            if (nestingLevel == 0) return;
        }

        throw new RuntimeException("Excepted matching '}' on line " + lineCount);
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(look())) {
            readChar();
        }
    }

    // bObject ::= '@' , comment | preamble | bString | entry
    //           | oneLineComment
    private Optional<BibTeXEntry> bObject() {
        skipWhitespace();
        if (look() == '@') {
            match('@');
            char lookup = Character.toLowerCase(look());
            if (lookup == 'c') {
                comment();
                return Optional.empty();
            } else if (lookup == 's') {
                bString();
                return Optional.empty();
            } else if (lookup == 'p') {
                char p = readChar();
                char nextChar = readChar();
                unreadChar(nextChar);
                unreadChar(p);
                if (nextChar == 'r') {
                    preamble();
                    return Optional.empty();
                } else {
                    return Optional.of(entry());
                }
            } else {
                return Optional.of(entry());
            }
        } else {
            oneLineComment();
            return Optional.empty();
        }
    }

    // comment ::= 'comment{', ?whatever until matching brace?, '}'
    private void comment() {
        matchWhole("comment");
        skipWhitespace();
        matchCurlyBracketsPair();
    }

    // preamble ::= 'preamble{', ?whatever until matching brace?, '}'
    private void preamble() {
        matchWhole("preamble");
        skipWhitespace();
        matchCurlyBracketsPair();
    }

    // bString ::= 'string{', tags, '}'
    private void bString() {
        matchWhole("string");
        skipWhitespace();
        match('{');

        for (BibTeXField field : tags()) {
            variables.put(field.name, field.value);
        }

        skipWhitespace();
        match('}');
    }

    // entry ::= entryType, '{', entryKey, tags, '}'
    private BibTeXEntry entry() {
        skipWhitespace();

        String entryType = readWord();
        match('{');
        String key = entryKey();
        skipWhitespace();

        Set<BibTeXField> fields = tags();

        skipWhitespace();
        match('}');

        return BibTeXEntry.constructorFromType(entryType).apply(key, fields);
    }

    // entryKey ::= ?whatever until ,?, ','
    private String entryKey() {
        skipWhitespace();
        String retval = readUntil(',');
        match(',');

        return retval;
    }

    // tags ::= tag, { ',', tag}, {','}
    private Set<BibTeXField> tags() {
        skipWhitespace();

        Set<BibTeXField> retval = new HashSet<>();

        retval.add(tag());
        skipWhitespace();
        while (look() == ',') {
            match(',');
            skipWhitespace();
            if (look() == '}') break;
            retval.add(tag());
        }

        return retval;
    }

    // tag ::= key, '=', value
    private BibTeXField tag() {
        skipWhitespace();
        String key = key();
        skipWhitespace();
        match('=');
        skipWhitespace();
        String value = value();

        return new BibTeXField(key, value);
    }

    // key ::= ?whatever alphabetic word?
    private String key() {
        return readWord();
    }

    // value ::= number | (literalString | variable, { '#', literalString | variable })
    private String value() {
        String retval = "";
        skipWhitespace();

        if (Character.isDigit(look())) {
            retval = number();
        } else {
            if (look() == '"') {
                retval += literalString();
            } else {
                retval += variable();
            }
            skipWhitespace();
            while (look() == '#') {
                match('#');
                skipWhitespace();
                if (look() == '"') {
                    retval += literalString();
                } else {
                    retval += variable();
                }
                skipWhitespace();
            }
        }

        return retval;
    }

    private String literalString() {
        match('"');
        String retval = readUntil('"');
        match('"');

        return retval;
    }

    private String variable() {
        skipWhitespace();
        String variableName = readWord();
        return Optional.ofNullable(variables.get(variableName))
                .orElseThrow(() -> new RuntimeException("no variable with name '" + variableName + "' (referenced on " +
                        "line: " + lineCount + ")"));
    }

    private String number() {
        String retval = "";
        while (Character.isDigit(look())) {
            retval += readChar();
        }

        return retval;
    }

    // oneLineComment ::= '%', ?whatever?, NEWLINE
    private void oneLineComment() {
        match('%');
        readUntil('\n');
        readWord();
    }

}
