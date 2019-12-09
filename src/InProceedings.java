import java.util.Collection;
import java.util.Set;

public class InProceedings extends BibTeXEntry {
    static final Set<OrFieldName> requiredFields = new OrFieldName.SetBuilder().withJustFields("author", "title", "publlisher", "year").build();
    static final Set<OrFieldName> optionalFields = new OrFieldName.SetBuilder()
            .withJustFields("editor", "series", "pages", "address", "month", "organization", "publisher", "note", "key")
            .withOrField("volume", "number")
            .build();

    public InProceedings(String key, Collection<BibTeXField> fields) {
        super("inproceedings", requiredFields, optionalFields, key, fields);
    }
}
