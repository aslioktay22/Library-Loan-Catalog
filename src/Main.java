import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Book;
import model.Librarian;
import service.LibrarySystem;

public class Main extends Application {

    private LibrarySystem library = new LibrarySystem();
    private Librarian librarian = new Librarian("Admin", library);

    @Override
    public void start(Stage stage) {

        Label title = new Label("Library Catalog");
        title.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #5a4634;"
        );

        // BUTTONS
        Button addBookBtn = new Button("Add Book");
        Button viewBooksBtn = new Button("View Books");
        Button searchBtn = new Button("Search Book");
        Button borrowBtn = new Button("Borrow Book");
        Button returnBtn = new Button("Return Book");
        Button removeBookBtn = new Button("Remove Book");
        Button availableBtn = new Button("View Available Books");
        Button borrowedBtn = new Button("View Borrowed Books");

        addBookBtn.setOnAction(e -> openAddBookWindow());
        viewBooksBtn.setOnAction(e -> showBooksWindow());
        searchBtn.setOnAction(e -> openSearchWindow());
        borrowBtn.setOnAction(e -> openBorrowWindow());
        returnBtn.setOnAction(e -> openReturnWindow());
        removeBookBtn.setOnAction(e -> openRemoveBookWindow());
        availableBtn.setOnAction(e -> showAvailableBooksWindow());
        borrowedBtn.setOnAction(e -> showBorrowedBooksWindow());

        // BUTTON GROUPING + STYLING
        Button[] buttons = {
            addBookBtn, viewBooksBtn, searchBtn,
            borrowBtn, returnBtn, removeBookBtn,
            availableBtn, borrowedBtn
        };

        for (Button b : buttons) {
            b.setPrefWidth(200);
            b.setStyle(
                "-fx-background-color: #a67c52;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 10;"
            );
        }

        // CARD
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(25));
        card.setMaxWidth(320);

        card.setStyle(
            "-fx-background-color: #fffaf3;" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 18, 0, 0, 6);"
        );

        card.getChildren().addAll(
            title,
            addBookBtn,
            viewBooksBtn,
            searchBtn,
            borrowBtn,
            returnBtn,
            removeBookBtn,
            availableBtn,
            borrowedBtn
        );

        StackPane root = new StackPane(card);
        Scene scene = new Scene(root, 500, 450);
        root.setStyle("-fx-background-color: #f5efe6;");

        stage.setScene(scene);
        stage.setTitle("Library System");
        stage.show();
    }

    // HELPER METHOD
    private Label createTitle(String text) {
        Label title = new Label(text);
        title.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #5a4634;"
        );
        return title;
    }

    private VBox createLayout() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        layout.setStyle(
            "-fx-background-color: #fffaf3;" +
            "-fx-background-radius: 12;"
        );

        return layout;
    }

    private void displayBooks(VBox layout, boolean showAvailable, boolean showBorrowed) {
        boolean found = false;

        for (var b : library.getBooks()) {
            if ((showAvailable && b.isAvailable()) ||
                (showBorrowed && !b.isAvailable()) ||
                (!showAvailable && !showBorrowed)) {

                found = true;
                layout.getChildren().add(
                    new Label(b.getTitle() + " - " + b.getAuthor())
                );
            }
        }

        if (!found) {
            layout.getChildren().add(new Label("No books found."));
        }
    }

    private Scene createScene(VBox layout, int w, int h) {
        return new Scene(layout, w, h);
    }

    // ADD BOOK
    private void openAddBookWindow() {
        Stage window = new Stage();

        TextField titleField = new TextField();
        TextField authorField = new TextField();

        Button saveBtn = new Button("Save");

        saveBtn.setOnAction(e -> {
            librarian.addBook(titleField.getText(), authorField.getText());
            window.close();
        });

        VBox layout = createLayout();
        layout.getChildren().addAll(
            createTitle("Add New Book"),
            titleField,
            authorField,
            saveBtn
        );

        window.setScene(createScene(layout, 300, 200));
        window.setTitle("Add Book");
        window.show();
    }

    // SHOW/VIEW BOOKS
    private void showBooksWindow() {
        Stage window = new Stage();
        VBox layout = createLayout();

        layout.getChildren().add(createTitle("All Books"));

        displayBooks(layout, false, false);

        window.setScene(createScene(layout, 300, 300));
        window.show();
    }

    // SEARCH BOOK
    private void openSearchWindow() {
        Stage window = new Stage();

        TextField field = new TextField();
        Label result = new Label();

        Button detailsBtn = new Button("Details");
        detailsBtn.setVisible(false);

        final Book[] found = new Book[1];

        Button searchBtn = new Button("Search");

        searchBtn.setOnAction(e -> {
            Book book = library.findBook(field.getText());

            if (book != null) {
                found[0] = book;
                result.setText(book.getTitle());
                detailsBtn.setVisible(true);
            } else {
                result.setText("Book not found.");
                detailsBtn.setVisible(false);
            }
        });

        detailsBtn.setOnAction(e -> {
            Book b = found[0];

            VBox layout = createLayout();
            layout.getChildren().addAll(
                new Label("Title: " + b.getTitle()),
                new Label("Author: " + b.getAuthor()),
                new Label("Status: " + (b.isAvailable() ? "Available" : "Borrowed"))
            );

            Stage details = new Stage();
            details.setScene(createScene(layout, 250, 150));
            details.show();
        });

        VBox layout = createLayout();
        layout.getChildren().addAll(
            createTitle("Search Book"),
            field,
            searchBtn,
            result,
            detailsBtn
        );

        window.setScene(createScene(layout, 300, 200));
        window.show();
    }

    // BORROW BOOK
    private void openBorrowWindow() {
        Stage window = new Stage();

        TextField field = new TextField();
        Label result = new Label();

        Button btn = new Button("Borrow");

        btn.setOnAction(e -> {
            result.setText(
                library.borrowBook(field.getText())
                    ? "Book borrowed!"
                    : "Book not available."
            );
        });

        VBox layout = createLayout();
        layout.getChildren().addAll(
            createTitle("Borrow Book"),
            field,
            btn,
            result
        );

        window.setScene(createScene(layout, 300, 200));
        window.show();
    }

    // RETURN BOOK
    private void openReturnWindow() {
        Stage window = new Stage();

        TextField field = new TextField();
        Label result = new Label();

        Button btn = new Button("Return");

        btn.setOnAction(e -> {
            result.setText(
                library.returnBook(field.getText())
                    ? "Book returned!"
                    : "Book was not borrowed."
            );
        });

        VBox layout = createLayout();
        layout.getChildren().addAll(
            createTitle("Return Book"),
            field,
            btn,
            result
        );

        window.setScene(createScene(layout, 300, 200));
        window.show();
    }

    // REMOVE BOOK
    private void openRemoveBookWindow() {
        Stage window = new Stage();

        TextField field = new TextField();
        Label result = new Label();

        Button btn = new Button("Remove");

        btn.setOnAction(e -> {
            boolean removed = librarian.removeBook(field.getText());
            result.setText(removed ? "Book removed!" : "Book not found.");
        });

        VBox layout = createLayout();
        layout.getChildren().addAll(
            createTitle("Remove Book"),
            field,
            btn,
            result
        );

        window.setScene(createScene(layout, 300, 200));
        window.show();
    }

    // VIEW AVAILABLE BOOKS
    private void showAvailableBooksWindow() {
        Stage window = new Stage();
        VBox layout = createLayout();

        layout.getChildren().add(createTitle("Available Books"));
        displayBooks(layout, true, false);

        window.setScene(createScene(layout, 300, 300));
        window.show();
    }

    // VIEW BORROWED BOOKS
    private void showBorrowedBooksWindow() {
        Stage window = new Stage();
        VBox layout = createLayout();

        layout.getChildren().add(createTitle("Borrowed Books"));
        displayBooks(layout, false, true);

        window.setScene(createScene(layout, 300, 300));
        window.show();
    }

    //LAUNCHER/MAIN METHOD
    public static void main(String[] args) {
        launch();
    }
}
