import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        var scanner = new Scanner(System.in);
        System.out.println("Input a query for which you want to download pictures:");
        var query = scanner.nextLine();
        System.out.println("Input a number of pictures you need:");
        int pagesNeeded = Integer.parseInt(scanner.nextLine());

        var loader = new ImageLoader(query, pagesNeeded);
        loader.parseAndLoad();

        System.out.println("Images are downloaded!");
    }
}