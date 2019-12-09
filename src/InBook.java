import java.util.Collection;
import java.util.Set;

public class InBook extends BibTeXEntry {
    static final Set<OrFieldName> requiredFields = new OrFieldName.SetBuilder()
            .withJustFields("title", "chapter", "publisher", "year")
            .withOrField("author", "editor")
            .withOrField("chapter", "pages")
            .build();

    static final Set<OrFieldName> optionalFields = new OrFieldName.SetBuilder()
            .withJustFields("series", "type", "address", "edition", "month", "note", "key")
            .withOrField("volume", "number")
            .build();

    public InBook(String key, Collection<BibTeXField> fields) {
        super("inbook", requiredFields, optionalFields, key, fields);
    }
}
