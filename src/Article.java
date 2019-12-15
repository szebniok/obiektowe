import java.util.Collection;
import java.util.Set;

public class Article extends BibTeXEntry {
    static final Set<OrFieldName> requiredFields = new OrFieldName.SetBuilder().withJustFields("author", "title", "journal", "year").build();
    static final Set<OrFieldName> optionalFields = new OrFieldName.SetBuilder().withJustFields("volume", "number", "pages", "month", "note", "key").build();

    public Article(String key, Collection<BibTeXField> fields) {
        super("article", requiredFields, optionalFields, key, fields);
    }
}
