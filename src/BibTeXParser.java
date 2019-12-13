import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BibTeXParser {
    final PushbackReader reader;

    public BibTeXParser(Path file) {
        try {
            reader = new PushbackReader(new FileReader(file.toFile()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BibTeX getBibTeX() {
        Set<BibTeXEntry> entries = new HashSet<>();
        while (!isEOF()) {
            bObject().ifPresent(entries::add);
            skipWhitespace();
        }
        return new BibTeX(entries);
    }

    private char look() {
        try {
            char retval = (char) reader.read();
            reader.unread((int) retval);
            return retval;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private char readChar() {
        try {
            char retval = (char) reader.read();
            return retval;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void unreadChar(char c) {
        try {
            reader.unread((int) c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        return Character.isLetter(look());
    }

    private String readWord() {
        String retval = "";
        while (isAlpha()) {
            retval += readChar();
        }
        return retval;
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

    private void match(char pattern) {
        if (Character.toUpperCase(look()) != Character.toUpperCase(pattern))
            throw new RuntimeException("Expected: '" + pattern + "'");
        readChar();
    }

    private void matchWhole(String pattern) {
        try {
            for (char c : pattern.toCharArray()) {
                match(c);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Expected: '" + pattern + "'");
        }
    }

    private boolean isWhitespace() {
        return Character.isWhitespace(look());
    }

    private void skipWhitespace() {
        while (isWhitespace()) {
            readChar();
        }
    }

    private Optional<BibTeXEntry> bObject() {
        skipWhitespace();
        match('@');

        if (look() == 'c') {
            comment();
            return Optional.empty();
        } else if (look() == 's') {
            bString();
            return Optional.empty();
        } else if (look() == 'p') {
            preamble();
            return Optional.empty();
        } else {
            return Optional.of(entry());
        }
    }

    private void comment() {
        matchWhole("comment");
        skipWhitespace();
        match('{');
        skipWhitespace();
        match('}');
    }

    private void bString() {
        matchWhole("string");
        skipWhitespace();
        match('{');

        tags();

        skipWhitespace();
        match('}');
    }

    private void preamble() {
        matchWhole("preamble");
        skipWhitespace();
        match('{');
        readUntil('}');
        match('}');
    }

    private Set<BibTeXField> tags() {
        skipWhitespace();

        Set<BibTeXField> retval = new HashSet<>();

        retval.add(tag());
        skipWhitespace();
        while (look() == ',') {
            match(',');
            retval.add(tag());
        }

        return retval;
    }

    private BibTeXField tag() {
        skipWhitespace();
        String key = key();
        skipWhitespace();
        match('=');
        skipWhitespace();
        String value = value();

        return new BibTeXField(key, value);
    }

    private BibTeXEntry entry() {
        skipWhitespace();

        String entryType = readWord();
        match('{');
        String key = entryKey();
        skipWhitespace();

        Set<BibTeXField> fields = tags();

        skipWhitespace();
        match('}');

        return BibTeXEntry.fromType(entryType, key, fields);
    }

    private String entryKey() {
        skipWhitespace();
        String retval = readWord();
        match(',');

        return retval;
    }

    private String key() {
        return readWord();
    }

    private String value() {
        match('"');
        String retval = readUntil('"');
        match('"');

        return retval;
    }

}
