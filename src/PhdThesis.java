import java.util.Collection;
import java.util.Set;

public class PhdThesis extends BibTeXEntry {
    static final Set<OrFieldName> requiredFields = new OrFieldName.SetBuilder()
            .withJustFields("author", "title", "school", "year")
            .build();

    static final Set<OrFieldName> optionalFields = new OrFieldName.SetBuilder()
            .withJustFields("type", "address", "month", "note", "key")
            .build();

    public PhdThesis(String key, Collection<BibTeXField> fields) {
        super("phdthesis", requiredFields, optionalFields, key, fields);
    }
}
