import com.github.javafaker.Faker;
import java.sql.*;

public class DataGeneratorService {

    private BookService bookService;

    public DataGeneratorService(BookService bookService) {
        this.bookService = bookService;
    }

    public void generateFakeData() {
        Faker faker = new Faker();
        String insertSQL = "INSERT INTO books (title, author, price) VALUES (?, ?, ?)";

        try (Connection conn = bookService.connect(); PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
            for (int i = 0; i < 100; i++) {
                String title = faker.book().title();
                String author = faker.book().author();
                double price = 10 + Math.random() * 490;  // 随机价格

                stmt.setString(1, title);
                stmt.setString(2, author);
                stmt.setDouble(3, price);
                stmt.addBatch();  // 批处理

                if (i % 20 == 0) {
                    stmt.executeBatch();  // 执行批处理
                }
            }
            stmt.executeBatch();  // 执行剩余的批量插入
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
