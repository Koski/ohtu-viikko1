package olutopas;

import com.avaje.ebean.EbeanServer;
import java.util.List;
import java.util.Scanner;
import javax.persistence.OptimisticLockException;
import olutopas.model.Beer;
import olutopas.model.Brewery;
import olutopas.model.Pub;
import olutopas.model.Rating;
import olutopas.model.User;

public class Application {

//    static User logged;
    private EbeanServer server;
    private Scanner scanner = new Scanner(System.in);

    public Application(EbeanServer server) {
        this.server = server;
    }

    public void run(boolean newDatabase) {
        User user = null;
        if (newDatabase) {
            seedDatabase();
        }
        System.out.println("Login (give ? to create a new user)");
        System.out.print("username: ");
        String firstCommand = scanner.nextLine();
        if (firstCommand.equals("?")) {
            createNewUser();
        } else {
            user = login(firstCommand);
        }

        System.out.println("Welcome!");

        while (true) {
            menu();
            System.out.print("> ");
            String command = scanner.nextLine();

            if (command.equals("0")) {
                break;
            } else if (command.equals("1")) {
                findBrewery();
            } else if (command.equals("2")) {
                findAndRateBeer(user);
            } else if (command.equals("3")) {
                addBeer();
            } else if (command.equals("4")) {
                listBreweries();
            } else if (command.equals("5")) {
                deleteBeer();
            } else if (command.equals("6")) {
                listBeers();
            } else if (command.equals("7")) {
                addBrewery();
            } else if (command.equals("8")) {
                deleteBrewery();
            } else if (command.equals("9")) {
                listUsers();
            } else if (command.equals("t")) {
                listRatings(user);
            } else if (command.equals("10")) {
                showBeers();
            } else if (command.equals("11")) {
                listPubsAndBeers();
            } else if (command.equals("12")) {
                removeBeerFromPub();
            } else if (command.equals("13")) {
                addBeerToPub();
            } else if (command.equals("14")) {
                addPub();
            } else {
                System.out.println("unknown command");
            }

            System.out.print("\npress enter to continue");
            scanner.nextLine();
        }

        System.out.println("bye");
    }

    private void menu() {
        System.out.println("");
        System.out.println("1   find brewery");
        System.out.println("2   find and rate beer");
        System.out.println("3   add beer");
        System.out.println("4   list breweries");
        System.out.println("5   delete beer");
        System.out.println("6   list beers");
        System.out.println("7   add brewery");
        System.out.println("8   delete brewery");
        System.out.println("9   list users");
        System.out.println("t   list my ratings");
        System.out.println("10  show beers in a pub");
        System.out.println("11  list pubs and beers in them");
        System.out.println("12  remove a beer from a pub");
        System.out.println("13  add beer to a pub");
        System.out.println("14  add pub to database");
        System.out.println("0   quit");
        System.out.println("");
    }

    // jos kanta on luotu uudelleen, suoritetaan tämä ja laitetaan kantaan hiukan dataa
    private void seedDatabase() throws OptimisticLockException {
        Brewery brewery = new Brewery("Schlenkerla");
        brewery.addBeer(new Beer("Urbock"));
        brewery.addBeer(new Beer("Lager"));
        // tallettaa myös luodut oluet, sillä Brewery:n OneToMany-mappingiin on määritelty
        // CascadeType.all
        server.save(brewery);

        // luodaan olut ilman panimon asettamista
        Beer b = new Beer("Märzen");
        server.save(b);

        // jotta saamme panimon asetettua, tulee olot lukea uudelleen kannasta
        b = server.find(Beer.class, b.getId());
        brewery = server.find(Brewery.class, brewery.getId());
        brewery.addBeer(b);
        server.save(brewery);
        server.save(new Pub("Pikkulintu"));
        server.save(new Brewery("Paulaner"));
    }

    private void addPub() {
        System.out.print("pub to add: ");

        String name = scanner.nextLine();

        Pub exists = server.find(Pub.class).where().like("name", name).findUnique();
        if (exists != null) {
            System.out.println(name + " exists already");
            return;
        }

        server.save(new Pub(name));
    }

    private void findAndRateBeer(User user) {
        System.out.print("beer to find: ");
        String n = scanner.nextLine();
        Beer foundBeer = server.find(Beer.class).where().like("name", n).findUnique();

        if (foundBeer == null) {
            System.out.println(n + " not found");
            return;
        }
        int quantity = 0;
        int ratingsCombined = 0;
        int average = 0;
        List<Rating> ratings = server.find(Rating.class).where().findList();
        for (Rating rating : ratings) {
            if (rating.getBeer().getName().equals(n)) {
                ratingsCombined += rating.getRating();
                quantity++;
            }
        }
        if (quantity > 0) {
            average = ratingsCombined / quantity;
        }

        System.out.println("found: " + foundBeer);
        System.out.println("  number of ratings: " + quantity + " average " + average);
        System.out.print("give rating (leave empty if not): ");
        int ratingValue = scanner.nextInt();

        Rating rating = new Rating(user, foundBeer, ratingValue);
        server.save(rating);

    }

    private void findBrewery() {
        System.out.print("brewery to find: ");
        String n = scanner.nextLine();
        Brewery foundBrewery = server.find(Brewery.class).where().like("name", n).findUnique();

        if (foundBrewery == null) {
            System.out.println(n + " not found");
            return;
        }

        System.out.println(foundBrewery);
        for (Beer bier : foundBrewery.getBeers()) {
            System.out.println("   " + bier.getName());
        }
    }

    private void listBreweries() {
        List<Brewery> breweries = server.find(Brewery.class).findList();
        for (Brewery brewery : breweries) {
            System.out.println(brewery);
        }
    }

    private void addBrewery() {
        System.out.println("brewery name: ");
        String name = scanner.nextLine();

        Brewery brewery = server.find(Brewery.class).where().like("name", name).findUnique();

        if (brewery != null) {
            System.out.println(name + " already exists");
        } else {
            brewery = new Brewery(name);
            server.save(brewery);
            System.out.println("new brewery: " + name + " created");
        }

    }

    private void addBeer() {
        System.out.print("to which brewery: ");
        String name = scanner.nextLine();
        Brewery brewery = server.find(Brewery.class).where().like("name", name).findUnique();

        if (brewery == null) {
            System.out.println(name + " does not exist");
            return;
        }

        System.out.print("beer to add: ");

        name = scanner.nextLine();

        Beer exists = server.find(Beer.class).where().like("name", name).findUnique();
        if (exists != null) {
            System.out.println(name + " exists already");
            return;
        }

        brewery.addBeer(new Beer(name));
        server.save(brewery);
        System.out.println(name + " added to " + brewery.getName());
    }

    private void deleteBeer() {
        System.out.print("beer to delete: ");
        String n = scanner.nextLine();
        Beer beerToDelete = server.find(Beer.class).where().like("name", n).findUnique();

        if (beerToDelete == null) {
            System.out.println(n + " not found");
            return;
        }

        server.delete(beerToDelete);
        System.out.println("deleted: " + beerToDelete);

    }

    private void listBeers() {
        List<Brewery> breweries = server.find(Brewery.class).findList();

        for (Brewery brewery : breweries) {
            List<Beer> beers = brewery.getBeers();
            for (Beer beer : beers) {
                System.out.println(beer.getName());
            }
        }
    }

    private void deleteBrewery() {
        System.out.print("brewery to delete: ");
        String name = scanner.nextLine();
        Brewery foundBrewery = server.find(Brewery.class).where().like("name", name).findUnique();

        if (foundBrewery == null) {
            System.out.println(name + " not found");
        } else {
            server.delete(foundBrewery);
            System.out.println("brewery: " + name + " has been deleted");
        }
    }

    private void createNewUser() {
        System.out.print("give username: ");
        String username = scanner.nextLine();

        User user = new User(username);
        server.save(user);


        System.out.println("user created");
    }

    private User login(String firstCommand) {
        User foundUser = server.find(User.class).where().like("username", firstCommand).findUnique();

        if (foundUser == null) {
            System.out.println("username " + firstCommand + " does not exist");
            return null;
        } else {
            System.out.println("Welcome to ratebeer " + firstCommand + " !");
            return foundUser;
        }
    }

    private void listUsers() {
        List<User> users = server.find(User.class).findList();
        for (User user : users) {
            System.out.println(user.getUsername());
        }
    }

    private void listRatings(User logged) {
        List<Rating> ratings = server.find(Rating.class).findList();
        for (Rating rating : ratings) {
            if (rating.getUser().getUsername().equals(logged.getUsername())) {
                System.out.println(rating.getBeer().getName() + " (" + rating.getBeer().getBrewery().getName() + ") "
                        + rating.getRating() + " points");
            }
        }
    }

    private void showBeers() {
        System.out.print("which pub: ");
        String pub = scanner.nextLine();

        Pub foundPub = server.find(Pub.class).where().like("name", pub).findUnique();
        if (foundPub == null) {
            System.out.println("pub doesn't exist");
            return;
        }
        List<Beer> beers = foundPub.getBeers();
        for (Beer beer : beers) {
            System.out.println(beer.getName());
        }
    }

    private void listPubsAndBeers() {
        List<Pub> pubs = server.find(Pub.class).findList();

        for (Pub pub : pubs) {
            System.out.println(pub.getName());
            for (Beer beer : pub.getBeers()) {
                System.out.println("  -" + beer.getName());
            }
        }
    }

    private void removeBeerFromPub() {
        System.out.print("which pub: ");
        String pub = scanner.nextLine();
        Pub foundPub = server.find(Pub.class).where().like("name", pub).findUnique();
        if (foundPub == null) {
            System.out.println("pub doesn't exist");
            return;
        }

        System.out.print("which beer:");
        String beer = scanner.nextLine();
        Beer foundBeer = server.find(Beer.class).where().like("name", beer).findUnique();
        if (foundBeer == null) {
            System.out.println("Beer doesn't exist");
            return;
        }
        foundPub.removeBeer(foundBeer);
        server.save(foundPub);
    }

    private void addBeerToPub() {
        System.out.print("beer: ");
        String name = scanner.nextLine();
        Beer beer = server.find(Beer.class).where().like("name", name).findUnique();

        if (beer == null) {
            System.out.println("does not exist");
            return;
        }

        System.out.print("pub: ");
        name = scanner.nextLine();
        Pub pub = server.find(Pub.class).where().like("name", name).findUnique();

        if (pub == null) {
            System.out.println("does not exist");
            return;
        }

        pub.addBeer(beer);
        server.save(pub);
    }
}
