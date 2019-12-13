import java.util.Collection;
import java.util.Set;

public class InCollection extends BibTeXEntry {
    static final Set<OrFieldName> requiredFields = new OrFieldName.SetBuilder()
            .withJustFields("author", "title", "booktitle", "publisher", "year")
            .build();

    static final Set<OrFieldName> optionalFields = new OrFieldName.SetBuilder()
            .withJustFields("editor", "series", "type", "chapter", "pages", "address", "edition", "month", "note", "key")
            .withOrField("volume", "number")
            .build();

    public InCollection(String key, Collection<BibTeXField> fields) {
        super("incollection", requiredFields, optionalFields, key, fields);
    }
}
