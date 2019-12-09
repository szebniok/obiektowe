import java.util.Collection;
import java.util.Set;

public class Booklet extends BibTeXEntry {
    static final Set<OrFieldName> requiredFields = new OrFieldName.SetBuilder().withJustFields("title").build();
    static final Set<OrFieldName> optionalFields = new OrFieldName.SetBuilder()
            .withJustFields("author", "howpublished", "address", "month", "year", "note", "key")
            .build();

    public Booklet(String key, Collection<BibTeXField> fields) {
        super("booklet", requiredFields, optionalFields, key, fields);
    }
}
