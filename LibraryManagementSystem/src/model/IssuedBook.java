package model;

/**
 * IssuedBook - Model class (POJO) representing a book issue/return transaction.
 *
 * Fields:
 *   issueId     - Primary key in issued_books table
 *   studentId   - Student's roll number / enrollment ID
 *   studentName - Student's full name
 *   bookId      - FK to books table
 *   bookTitle   - Title fetched via JOIN (not a DB column, for display only)
 *   issueDate   - Date the book was issued (String for easy JSP display)
 *   returnDate  - Date the book was returned (null if not yet returned)
 *   status      - "ISSUED" or "RETURNED"
 */
public class IssuedBook {

    private int    issueId;
    private String studentId;
    private String studentName;
    private int    bookId;
    private String bookTitle;    // populated via JOIN query
    private String issueDate;
    private String returnDate;
    private String status;

    // ---- Constructors ----

    public IssuedBook() {}

    public IssuedBook(int issueId, String studentId, String studentName,
                      int bookId, String issueDate, String returnDate, String status) {
        this.issueId     = issueId;
        this.studentId   = studentId;
        this.studentName = studentName;
        this.bookId      = bookId;
        this.issueDate   = issueDate;
        this.returnDate  = returnDate;
        this.status      = status;
    }

    // ---- Getters & Setters ----

    public int    getIssueId()        { return issueId; }
    public void   setIssueId(int v)   { this.issueId = v; }

    public String getStudentId()        { return studentId; }
    public void   setStudentId(String v){ this.studentId = v; }

    public String getStudentName()        { return studentName; }
    public void   setStudentName(String v){ this.studentName = v; }

    public int  getBookId()     { return bookId; }
    public void setBookId(int v){ this.bookId = v; }

    public String getBookTitle()        { return bookTitle; }
    public void   setBookTitle(String v){ this.bookTitle = v; }

    public String getIssueDate()        { return issueDate; }
    public void   setIssueDate(String v){ this.issueDate = v; }

    public String getReturnDate()        { return returnDate; }
    public void   setReturnDate(String v){ this.returnDate = v; }

    public String getStatus()        { return status; }
    public void   setStatus(String v){ this.status = v; }

    @Override
    public String toString() {
        return "IssuedBook{issueId=" + issueId +
               ", student=" + studentName + " (" + studentId + ")" +
               ", bookId=" + bookId + ", status=" + status + "}";
    }
}
