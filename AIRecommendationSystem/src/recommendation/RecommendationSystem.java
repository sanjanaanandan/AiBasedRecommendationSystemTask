package recommendation;

import java.io.*;
import java.util.*;

public class RecommendationSystem {

    // Rating class
    static class Rating {
        String user;
        String movie;
        int rating;

        Rating(String user, String movie, int rating) {
            this.user = user;
            this.movie = movie;
            this.rating = rating;
        }
    }

    public static void main(String[] args) {

        List<Rating> ratings = new ArrayList<>();

        // Read CSV file
        try {
            BufferedReader br = new BufferedReader(new FileReader("movies.csv"));

            String line;

            // Skip header
            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                String user = data[0];
                String movie = data[1];
                int rating = Integer.parseInt(data[2]);

                ratings.add(new Rating(user, movie, rating));
            }

            br.close();

        } catch (Exception e) {

            System.out.println("Error reading file!");
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter User Name:");
        String targetUser = sc.nextLine();

        recommendMovies(ratings, targetUser);

        sc.close();
    }

    // Recommendation Logic
    public static void recommendMovies(List<Rating> ratings, String targetUser) {

        Map<String, Integer> movieScores = new HashMap<>();

        Set<String> watchedMovies = new HashSet<>();

        // Find movies already watched by target user
        for (Rating r : ratings) {

            if (r.user.equalsIgnoreCase(targetUser)) {

                watchedMovies.add(r.movie);
            }
        }

        // Recommend movies from other users
        for (Rating r : ratings) {

            // Recommend only highly rated movies
            if (!r.user.equalsIgnoreCase(targetUser) && r.rating >= 4) {

                // Avoid recommending already watched movies
                if (!watchedMovies.contains(r.movie)) {

                    movieScores.put(
                        r.movie,
                        movieScores.getOrDefault(r.movie, 0) + r.rating
                    );
                }
            }
        }

        // Display recommendations
        System.out.println("\nRecommended Movies for " + targetUser + ":");

        if (movieScores.isEmpty()) {

            System.out.println("No recommendations available.");

        } else {

            for (Map.Entry<String, Integer> entry : movieScores.entrySet()) {

                System.out.println(
                    entry.getKey() + " (Score: " + entry.getValue() + ")"
                );
            }
        }
    }
}