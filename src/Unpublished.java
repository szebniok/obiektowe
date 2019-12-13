import java.util.Collection;
import java.util.Set;

public class Unpublished extends BibTeXEntry {
    static final Set<OrFieldName> requiredFields = new OrFieldName.SetBuilder()
            .withJustFields("author", "title", "note")
            .build();

    static final Set<OrFieldName> optionalFields = new OrFieldName.SetBuilder()
            .withJustFields("month", "year", "key")
            .build();

    public Unpublished(String key, Collection<BibTeXField> fields) {
        super("unpublished", requiredFields, optionalFields, key, fields);
    }
}
