import java.util.Collection;
import java.util.Set;

public class Misc extends BibTeXEntry {
    static final Set<OrFieldName> requiredFields = Set.of();

    static final Set<OrFieldName> optionalFields = new OrFieldName.SetBuilder()
            .withJustFields("author", "title", "howpublished", "month", "year", "note", "key")
            .build();

    public Misc(String key, Collection<BibTeXField> fields) {
        super("misc", requiredFields, optionalFields, key, fields);
    }
}
