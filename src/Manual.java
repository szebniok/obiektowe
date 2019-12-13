import java.util.Collection;
import java.util.Set;

public class Manual extends BibTeXEntry {
    static final Set<OrFieldName> requiredFields = new OrFieldName.SetBuilder()
            .withJustFields("title")
            .build();

    static final Set<OrFieldName> optionalFields = new OrFieldName.SetBuilder()
            .withJustFields("author", "organization", "address", "edition", "month", "year", "note", "key")
            .build();

    public Manual(String key, Collection<BibTeXField> fields) {
        super("manual", requiredFields, optionalFields, key, fields);
    }
}
