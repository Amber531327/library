import java.sql.*;

public class BookService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";  // 请根据实际情况修改
    private static final String USER = "root";  // 请根据实际情况修改
    private static final String PASSWORD = "123456";  // 请根据实际情况修改

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    // 查询书籍
    public String queryBooks(String title, String author, String id, String price) {
        StringBuilder query = new StringBuilder("SELECT * FROM books WHERE 1=1");

        // 根据输入动态添加查询条件
        if (!title.isEmpty()) {
            query.append(" AND title LIKE ?");
        }
        if (!author.isEmpty()) {
            query.append(" AND author LIKE ?");
        }
        if (!id.isEmpty()) {
            query.append(" AND id = ?");
        }
        if (!price.isEmpty()) {
            try {
                Double.parseDouble(price);  // 验证价格是否为有效数字
                query.append(" AND price = ?");
            } catch (NumberFormatException e) {
                return "价格必须是一个有效的数字！";
            }
        }

        StringBuilder result = new StringBuilder();

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            int parameterIndex = 1;

            // 设置查询参数
            if (!title.isEmpty()) {
                stmt.setString(parameterIndex++, "%" + title + "%");
            }
            if (!author.isEmpty()) {
                stmt.setString(parameterIndex++, "%" + author + "%");
            }
            if (!id.isEmpty()) {
                stmt.setInt(parameterIndex++, Integer.parseInt(id));
            }
            if (!price.isEmpty()) {
                stmt.setDouble(parameterIndex++, Double.parseDouble(price));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.append("ID: ").append(rs.getInt("id")).append("\n")
                        .append("书名: ").append(rs.getString("title")).append("\n")
                        .append("作者: ").append(rs.getString("author")).append("\n")
                        .append("价格: ").append(rs.getDouble("price")).append("\n")
                        .append("----------------------------\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "查询失败！";
        }

        return result.length() > 0 ? result.toString() : "没有找到符合条件的书籍。";
    }


    // 插入书籍
    public boolean insertBook(String title, String author, String price) {
        String insertSQL = "INSERT INTO books (title, author, price) VALUES (?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setDouble(3, Double.parseDouble(price));
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // 删除书籍
    public boolean deleteBook(String id) {
        String deleteSQL = "DELETE FROM books WHERE id = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // 更新书籍
    public boolean updateBook(String id, String title, String author, String price) {
        String updateSQL = "UPDATE books SET title = ?, author = ?, price = ? WHERE id = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setDouble(3, Double.parseDouble(price));
            stmt.setInt(4, Integer.parseInt(id));
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
