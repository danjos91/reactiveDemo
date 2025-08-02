import lombok.Data;

@Entity
@Data
public class Book {
    @Id
    private Integer id;
    private String title;

    public Book() {
    }

    public Book(String title) {
        this.title = title;
    }
}
        
    // геттеры-сеттеры