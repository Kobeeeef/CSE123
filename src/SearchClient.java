import java.io.*;
import java.util.*;
// Name: Kobe Lei
// Date: 9/1/2025
// CSE 123
// P0: Search Engine
// TA: Sumaya

// This class allows users to find and rate books within BOOK_DIRECTORY
// containing certain terms
public class SearchClient {
    public static final String BOOK_DIRECTORY = "./books";
    private static final Random RAND = new Random();

    // Some class constants you can play around with to give random ratings to the uploaded books!
    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 5;
    public static final int MIN_NUM_RATINGS = 1;
    public static final int MAX_NUM_RATINGS = 100;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        List<Media> media = new ArrayList<>(loadBooks());

        Map<String, Set<Media>> index = createIndex(media);

        System.out.println("Welcome to the CSE 123 Search Engine!");
        String command = "";
        while (!command.equalsIgnoreCase("quit")) {
            System.out.println("What would you like to do? [Search, Rate, Quit]");
            System.out.print("> ");
            command = console.nextLine();

            if (command.equalsIgnoreCase("search")) {
                searchQuery(console, index);
            } else if (command.equalsIgnoreCase("rate")) {
                addRating(console, media);
            } else if (!command.equalsIgnoreCase("quit")) {
                System.out.println("Invalid command, please try again.");
            }
        }
        System.out.println("See you next time!");
    }

    // Creates an inverted index from the given list of media documents.
    //
    // Behavior:
    // The index maps a unique lowercase word (token) from any media content to
    // the set of Media objects that contain that word.
    // It handles null inputs by skipping them.
    //
    // Parameters:
    // docs - The list of Media objects to be indexed.
    //
    // Returns:
    // A Map with the inverted index, where keys are tokens (String)
    // and values are the set of matching Media objects (Set<Media>). Returns
    // an empty Map if docs is null.
    public static Map<String, Set<Media>> createIndex(List<Media> docs) {
        Map<String, Set<Media>> index = new TreeMap<>();
        if (docs != null) {
            for (Media media : docs) {
                if (media != null) {
                    List<String> tokens = media.getContent();
                    if (tokens != null) {
                        for (String token : tokens) {
                            if (token != null) {
                                String key = token.toLowerCase(Locale.ROOT);

                                if (!index.containsKey(key)) {
                                    index.put(key, new HashSet<>());
                                }
                                index.get(key).add(media);
                            }
                        }
                    }
                }
            }
        }
        return index;
    }
    // Searches the given inverted index for all Media documents that contain any of the
    // words in the query string.
    //
    // Parameters:
    // index - The inverted index mapping terms to the Set of media containing those terms.
    // query - The string containing one or more space-separated terms to search for.
    //
    // Returns:
    // A set of Media objects that contain at least one of the query terms. Returns an
    // empty set if index is null, query is null/blank, or no matches are found.
    public static Set<Media> search(Map<String, Set<Media>> index, String query) {
        Set<Media> results = new TreeSet<>();
        if (index == null || query == null || query.isBlank()) return results;

        String[] tokens = query.toLowerCase(Locale.ROOT).split("\\s+");
        for (String token : tokens) {
            if (index.containsKey(token)) {
                results.addAll(index.get(token));
            }
        }
        return results;
    }

    // Allows the user to search a specific query using the provided 'index' to find appropriate
    //  Media entries.
    //
    // Parameters:
    //   console - the Scanner to get user input from. Should be non-null
    //   index - an inverted index mapping terms to the Set of media containing those terms.
    //           Should be non-null
    public static void searchQuery(Scanner console, Map<String, Set<Media>> index) {
        System.out.println("Enter query:");
        System.out.print("> ");
        String query = console.nextLine();

        Set<Media> result = search(index, query);

        if (result.isEmpty()) {
            System.out.println("\tNo results!");
        } else {
            for (Media m : result) {
                System.out.println("\t" + m.toString());
            }
        }
    }

    // Allows the user to add a rating to one of the options wthin 'media'
    //
    // Parameters:
    //   console - the Scanner to get user input from. Should be non-null.
    //   media - list of all media options loaded into the search engine. Should be non-null.
    public static void addRating(Scanner console, List<Media> media) {
        for (int i = 0; i < media.size(); i++) {
            System.out.println("\t" + i + ": " + media.get(i).toString());
        }
        System.out.println("What would you like to rate (enter index)?");
        System.out.print("> ");
        int choice = Integer.parseInt(console.nextLine());
        if (choice < 0 || choice >= media.size()) {
            System.out.println("Invalid choice");
        } else {
            System.out.println("Rating [" + media.get(choice).getTitle() + "]");
            System.out.println("What rating would you give?");
            System.out.print("> ");
            int rating = Integer.parseInt(console.nextLine());
            media.get(choice).addRating(rating);
        }
    }

    // Loads all books from BOOK_DIRECTORY. Assumes that each book starts with two lines -
    //      "Title: " which is followed by the book's title
    //      "Author: " which is followed by the book's author
    //
    // Returns:
    //   A list of all book objects corresponding to the ones located in BOOK_DIRECTORY
    public static List<Media> loadBooks() throws FileNotFoundException {
        List<Media> ret = new ArrayList<>();

        File dir = new File(BOOK_DIRECTORY);
        for (File f : dir.listFiles()) {
            Scanner sc = new Scanner(f, "utf-8");
            String title = sc.nextLine().substring("Title: ".length());
            List<String> author = List.of(sc.nextLine().substring("Author: ".length()));

            Media book = new Book(title, author, sc);

            // Adds random ratings to 'book' based on the class constants. 
            // Feel free to comment this out.
            int minRating = RAND.nextInt(MAX_RATING - MIN_RATING + 1) + MIN_RATING;
            addRatings(minRating, Math.min(MAX_RATING,RAND.nextInt(MAX_RATING - minRating + 1) + minRating),
                    RAND.nextInt(MAX_NUM_RATINGS - MIN_NUM_RATINGS) + MIN_NUM_RATINGS, book);
            ret.add(book);
        }

        return ret;
    }

    // Adds ratings to the provided media numRatings amount of times. Each rating is a random int
    // between minRating and maxRating (inclusive).
    private static void addRatings(int minRating, int maxRating, int numRatings, Media media) {
        for (int i = 0; i < numRatings; i++) {
            media.addRating(RAND.nextInt(maxRating - minRating + 1) + minRating);
        }
    }
}