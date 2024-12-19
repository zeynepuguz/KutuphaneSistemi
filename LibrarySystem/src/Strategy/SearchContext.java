package Strategy;

import java.util.List;

public class SearchContext {
    private SearchStrategy searchStrategy;

    public void setSearchStrategy(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public List<String> search(String query) {
        if (searchStrategy == null) {
            throw new IllegalStateException("Arama stratejisi belirlenmedi!");
        }
        return searchStrategy.search(query);
    }
}
