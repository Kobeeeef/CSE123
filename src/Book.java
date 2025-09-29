import java.util.*;
import java.text.DecimalFormat;

public class Book implements Media, Comparable<Book> {

    private final String title;
    private final List<String> authors;
    private final List<String> contentTokens;

    private long ratingSum;
    private int ratingCount;

    public Book(String title, List<String> authors, Scanner content) {
        if (title == null) {
            throw new IllegalArgumentException("title cannot be null");
        }
        if (authors == null || authors.isEmpty()) {
            throw new IllegalArgumentException("authors cannot be null or empty");
        }
        this.title = title;
        this.authors = Collections.unmodifiableList(new ArrayList<>(authors));
        this.contentTokens = new ArrayList<>();
        if (content != null) {
            while (content.hasNext()) {
                this.contentTokens.add(content.next());
            }
        }
        this.ratingSum = 0;
        this.ratingCount = 0;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public List<String> getArtists() {
        return this.authors;
    }

    @Override
    public void addRating(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("rating score must be non-negative");
        }
        this.ratingSum += score;
        this.ratingCount += 1;
    }

    @Override
    public int getNumRatings() {
        return this.ratingCount;
    }

    @Override
    public double getAverageRating() {
        if (this.ratingCount == 0) {
            return 0.0;
        }
        return (double) this.ratingSum / (double) this.ratingCount;
    }

    @Override
    public List<String> getContent() {
        return Collections.unmodifiableList(this.contentTokens);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.title);
        sb.append(" by ");
        sb.append(this.authors.toString());
        if (this.ratingCount > 0) {
            DecimalFormat df = new DecimalFormat("0.##");
            sb.append(": ");
            sb.append(df.format(getAverageRating()));
            sb.append(" (");
            sb.append(this.ratingCount);
            sb.append(this.ratingCount == 1 ? " rating)" : " ratings)");
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Book other) {
        if (other == null) {
            return -1;
        }
        int cmp;

        double aAvg = this.getAverageRating();
        double bAvg = other.getAverageRating();
        cmp = Double.compare(bAvg, aAvg);
        if (cmp != 0) return cmp;

        cmp = Integer.compare(other.getNumRatings(), this.getNumRatings());
        if (cmp != 0) return cmp;

        cmp = this.title.compareToIgnoreCase(other.title);
        if (cmp != 0) return cmp;

        return Integer.compare(this.authors.size(), other.authors.size());
    }
}
