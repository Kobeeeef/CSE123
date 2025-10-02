import java.util.*;

// Name: Kobe Lei
// Date: 9/1/2025
// CSE 123
// Assignment: P0: Search Engine
// TA: Sumaya
// This class represents a specific media type, a Book, storing its metadata,
// content, and ratings, and allowing comparison for sorting.
public class Book implements Media, Comparable<Book> {

    private final String title;
    private final List<String> authors;
    private final List<String> contentTokens;

    private long ratingSum;
    private int ratingCount;

    // Initializes a new Book object, setting its variables and tokenizing its content
    // from the Scanner.
    //
    // Parameters:
    // title - The title of the book.
    // authors - A list of authors for the book.
    // content - A Scanner at the start of the book's content.
    //
    // Exceptions:
    // IllegalArgumentException is thrown if the given title is null.
    // IllegalArgumentException is thrown if the given authors list is null or empty.
    public Book(String title, List<String> authors, Scanner content) {
        if (title == null) {
            throw new IllegalArgumentException("title cannot be null");
        }
        if (authors == null || authors.isEmpty()) {
            throw new IllegalArgumentException("authors cannot be null or empty");
        }
        this.title = title;
        this.authors = new ArrayList<>(authors);
        this.contentTokens = new ArrayList<>();
        if (content != null) {
            while (content.hasNext()) {
                this.contentTokens.add(content.next());
            }
        }
        this.ratingSum = 0;
        this.ratingCount = 0;
    }

    // Gets the title of this book.
    //
    // Returns:
    // The title of this book.
    @Override
    public String getTitle() {
        return this.title;
    }

    // Gets all authors associated with this book.
    //
    // Returns:
    // A list of authors for this book.
    @Override
    public List<String> getArtists() {
        return this.authors;
    }

    // Adds a rating score to this book.
    //
    // Parameters:
    // score - The score for the new rating. Must be non-negative.
    //
    // Exceptions:
    // IllegalArgumentException is thrown if the given score is negative.
    @Override
    public void addRating(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("rating score must be non-negative");
        }
        this.ratingSum += score;
        this.ratingCount += 1;
    }

    // Gets the number of times this book has been rated.
    //
    // Returns:
    // The number of ratings for this book.
    @Override
    public int getNumRatings() {
        return this.ratingCount;
    }

    // Gets the average (mean) of all ratings for this book.
    //
    // Returns:
    // The average (mean) of all ratings for this book. Returns 0.0 if no ratings exist.
    @Override
    public double getAverageRating() {
        if (this.ratingCount == 0) {
            return 0.0;
        }
        return (double) this.ratingSum / (double) this.ratingCount;
    }

    // Gets all of the content tokens (words) contained in this book.
    //
    // Returns:
    // The content stored as a List of strings (tokens).
    @Override
    public List<String> getContent() {
        return this.contentTokens;
    }

    // Produces a string representation of this book.
    //
    // Behavior:
    // If the book has zero ratings, the format is "<title> by [<artists>]".
    // If the book has one or more ratings, the format is
    // "<title> by [<artists>]: <average rating> (<num ratings> ratings)".
    // The average rating is rounded to exactly two decimal places.
    //
    // Returns:
    // The appropriately formatted string representation.
    @Override
    public String toString() {
        String result = this.title + " by " + this.authors.toString();

        if (this.ratingCount > 0) {
            double avg = getAverageRating();

            String avgString = String.format("%.2f", avg);

            result += ": " + avgString + " (";
            result += this.ratingCount + " ratings)";
        }
        return result;
    }

    // Compares this Book to another Book for sorting purposes.
    // This method defines the book's order, prioritizing books with the highest
    // ratings and most reviews.
    //
    // Parameters:
    // other - The other Book to compare against.
    //
    // Returns:
    // A negative integer, zero, or a positive integer as this Book is less than,
    // equal to, or greater than the specified Book.
    //
    // Sorting Criteria (in order of priority):
    // 1. Average Rating (higher rating comes first).
    // 2. Number of Ratings (more ratings comes first).
    // 3. Number of Authors (fewer authors comes first).
    @Override
    public int compareTo(Book other) {
        if (other == null) {
            return -1;
        }
        int comparision;

        double aAvgRating = this.getAverageRating();
        double bAvgRating = other.getAverageRating();

        // 1. Average Rating (Descending)
        if (bAvgRating > aAvgRating) {
            comparision = 1;
        } else if (bAvgRating < aAvgRating) {
            comparision = -1;
        } else {
            comparision = 0;
        }
        if (comparision != 0) return comparision;

        // 2. Number of Ratings (Descending: other - this)
        comparision = other.getNumRatings() - this.getNumRatings();
        if (comparision != 0) return comparision;

        // 3. Number of Authors (Ascending: this - other)
        return this.authors.size() - other.authors.size();
    }
}