import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Book extends BibTeXEntry {
    static final Set<OrFieldName> requiredFields = new OrFieldName.SetBuilder()
            .withJustFields("author", "title", "publisher", "year")
            .build();

    static final Set<OrFieldName> optionalFields = new OrFieldName.SetBuilder()
            .withJustFields("volume", "series", "address", "edition", "month", "key")
            .build();

    public Book(String key, Collection<BibTeXField> fields) {
        super("book", requiredFields, optionalFields, key, fields);
    }
}
