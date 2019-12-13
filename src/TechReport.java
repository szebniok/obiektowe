import java.util.Collection;
import java.util.Set;

public class TechReport extends BibTeXEntry {
    static final Set<OrFieldName> requiredFields = new OrFieldName.SetBuilder()
            .withJustFields("author", "title", "institution", "year")
            .build();

    static final Set<OrFieldName> optionalFields = new OrFieldName.SetBuilder()
            .withJustFields("editor", "series", "address", "month", "organization",
                    "publisher", "note", "key")
            .withOrField("volume", "number")
            .build();

    public TechReport(String key, Collection<BibTeXField> fields) {
        super("techreport", requiredFields, optionalFields, key, fields);
    }
}
