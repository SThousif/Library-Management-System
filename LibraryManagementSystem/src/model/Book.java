package model;

/**
 * Book - Model class (POJO) representing a book in the library catalog.
 *
 * Fields:
 *   bookId       - Primary key in books table
 *   title        - Book title
 *   author       - Author name
 *   category     - Subject category (e.g., Computer Science)
 *   quantity     - Total copies owned by library
 *   availableQty - Copies currently available to issue
 */
public class Book {

    private int    bookId;
    private String title;
    private String author;
    private String category;
    private int    quantity;
    private int    availableQty;

    // ---- Constructors ----

    public Book() {}

    public Book(int bookId, String title, String author,
                String category, int quantity, int availableQty) {
        this.bookId       = bookId;
        this.title        = title;
        this.author       = author;
        this.category     = category;
        this.quantity     = quantity;
        this.availableQty = availableQty;
    }

    // ---- Getters & Setters ----

    public int    getBookId()       { return bookId; }
    public void   setBookId(int v)  { this.bookId = v; }

    public String getTitle()        { return title; }
    public void   setTitle(String v){ this.title = v; }

    public String getAuthor()        { return author; }
    public void   setAuthor(String v){ this.author = v; }

    public String getCategory()        { return category; }
    public void   setCategory(String v){ this.category = v; }

    public int  getQuantity()     { return quantity; }
    public void setQuantity(int v){ this.quantity = v; }

    public int  getAvailableQty()     { return availableQty; }
    public void setAvailableQty(int v){ this.availableQty = v; }

    @Override
    public String toString() {
        return "Book{id=" + bookId + ", title='" + title +
               "', author='" + author + "', qty=" + quantity +
               ", avail=" + availableQty + "}";
    }
}
