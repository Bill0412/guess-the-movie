import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GuessMovie {
    public static void main(String[] args) {

        try{

            int lines = 25;
            // load all the movie names
            String[] movies = loadMovies("movies.txt", lines);

            // randomly choose one
            String movie = randomMovie(movies, lines);
            // System.out.println(movie);

            // play the game with the chosen movie
            Game game = new Game(movie);
            game.playGame();
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("The movies.txt file not found.");
        }


    }
    private static Scanner openFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        return scanner;
    }
    private static String[] loadMovies(String fileName, int lines) throws FileNotFoundException {
        try {
            Scanner scanner  = openFile(fileName);
            String[] movies = new String[lines];
            for(int i = 0; i < lines; i++)
            {
                movies[i] = scanner.nextLine();
            }

            return movies;
        } catch (FileNotFoundException e) {
            throw e;
        }
    }

    private static String randomMovie(String[] movies, int lines) {
        return movies[(int)(Math.random() * lines)];
    }
}

class Game {
    private String movie;
    private String movieToDisplay;
    private int cWrongLetters;
    private int cWrongGuesses;
    private int maxWrongGuesses;
    boolean[] wrongLetterList;
    private boolean isWin;
    private boolean isLost;
    // constructor
    Game(String movie) {
        this.movie = movie;
        cWrongLetters = 0;
        cWrongGuesses = 0;
        maxWrongGuesses = 10;
        isWin = false;
        isLost = false;
        char[] temp = movie.toCharArray();
        for(int i = 0; i < temp.length; i++) {
            if(temp[i] >= 'a' && temp[i] <= 'z') {
                temp[i] = '_';
            }
        }
        movieToDisplay = new String(temp);

        wrongLetterList = new boolean[26];
        for(int i= 0; i < 26; i++) {
            wrongLetterList[i] = false;
        }

    }

    public void playGame() {
        while(!isWin && !isLost) {
            displayOneGuess();
            // gets the lower case letter
            char guessLetter = getUserLetter();
            // if it is not a lowercase letter
            if(!(guessLetter <= 'z' && guessLetter >= 'a'))
            {
                increWrongGuesses();
                continue;
            }
           //  System.out.println(guessLetter);
            if(movie.toLowerCase().indexOf(guessLetter) >= 0) {
                uncoverLetter(guessLetter);
            }
            else {  // if the letter does not exist in the word
                increWrongGuesses();
                pushWrongLetter(guessLetter);
            }
        }

        // if the player wins
        if(isWin) {
            displayWinMessage();
        }

        if(isLost) {
            displayLostMessage();
        }
    }

    private void displayOneGuess() {
        System.out.println("You are guessing:" + movieToDisplay);
        System.out.println("You have guessed (" + cWrongLetters +
                ") wrong letters: " + getWrongLetterString());
        System.out.print("Guess a letter:");
    }

    private void pushWrongLetter(char c) {
        if(wrongLetterList[c - 'a'] == false) {
            wrongLetterList[c - 'a'] = true;
            cWrongLetters++;
        }
    }

    private String getWrongLetterString() {
        char[] temp = new char[cWrongLetters * 2];
        for(char i = 0, j = 0; i < 26; i++) {
            if(wrongLetterList[i]) {
                temp[2 * j] = (char)((int)'a' + i);
                temp[2 * j  + 1] = ' ';
                j++;
            }
        }
        String str = new String(temp);
        return str;
    }

    private char getUserLetter() {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        char[] strArray = str.toLowerCase().toCharArray();
        while(strArray.length == 0) {
            str = scanner.nextLine();
            strArray = str.toLowerCase().toCharArray();
        }
        char c = strArray[0];
        return c;
    }

    private void uncoverLetter(char letter) {
        boolean likelyToWin = true;
        char[] movieArray = movie.toCharArray();
        char[] lowerMovie = movie.toLowerCase().toCharArray();
        char[] temp = movieToDisplay.toCharArray();
        for(int i = 0; i < movie.length(); i++) {
            if(lowerMovie[i] == letter) {
                temp[i] = movieArray[i];
            }
            if(temp[i] == '_') {
                likelyToWin = false;
            }
        }
        movieToDisplay = new String(temp);

        if(likelyToWin) {
            isWin = true;
            System.out.println("isWin");
        }
    }

    private void displayWinMessage() {
        System.out.println("You Win!");
        System.out.println("You have guessed \"" + movie + "\" correctly.");
    }

    private void displayLostMessage() {
        System.out.println("You Lost!");
        System.out.println("The movie name is: " + movie);
    }

    private void increWrongGuesses() {
        cWrongGuesses++;
        if(cWrongGuesses > maxWrongGuesses) {
            isLost = true;
        }
    }

}