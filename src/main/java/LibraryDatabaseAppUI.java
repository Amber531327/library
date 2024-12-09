import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LibraryDatabaseAppUI extends JFrame {

    private JTextArea textArea;
    private JTextField titleField, authorField, priceField, idField;
    private JButton queryButton, insertButton, deleteButton, updateButton, generateButton;
    private BookService bookService;
    private DataGeneratorService dataGeneratorService;

    public LibraryDatabaseAppUI() {
        // 初始化服务类
        bookService = new BookService();
        dataGeneratorService = new DataGeneratorService(bookService);

        setTitle("Library Database App");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建组件
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        titleField = new JTextField(20);
        authorField = new JTextField(20);
        priceField = new JTextField(20);
        idField = new JTextField(20);

        queryButton = new JButton("查询");
        insertButton = new JButton("插入");
        deleteButton = new JButton("删除");
        updateButton = new JButton("更新");
        generateButton = new JButton("生成 100 条数据");

        // 布局管理
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 5, 5));

        panel.add(new JLabel("书名："));
        panel.add(titleField);
        panel.add(new JLabel("作者："));
        panel.add(authorField);
        panel.add(new JLabel("价格："));
        panel.add(priceField);
        panel.add(new JLabel("书籍 ID："));
        panel.add(idField);

        panel.add(queryButton);
        panel.add(insertButton);
        panel.add(deleteButton);
        panel.add(updateButton);
        panel.add(generateButton);

        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // 添加按钮事件监听
        queryButton.addActionListener(e -> queryData());
        insertButton.addActionListener(e -> insertData());
        deleteButton.addActionListener(e -> deleteData());
        updateButton.addActionListener(e -> updateData());
        generateButton.addActionListener(e -> generateFakeData());
    }

    private void queryData() {
        String title = titleField.getText();
        String author = authorField.getText();
        String price = priceField.getText();
        String id = idField.getText();

        String result = bookService.queryBooks(title, author,id,price);
        textArea.setText(result);
    }

    private void insertData() {
        String title = titleField.getText();
        String author = authorField.getText();
        String price = priceField.getText();

        boolean success = bookService.insertBook(title, author, price);
        if (success) {
            JOptionPane.showMessageDialog(this, "插入成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "插入失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteData() {
        String id = idField.getText();

        boolean success = bookService.deleteBook(id);
        if (success) {
            JOptionPane.showMessageDialog(this, "删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "删除失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateData() {
        String id = idField.getText();
        String title = titleField.getText();
        String author = authorField.getText();
        String price = priceField.getText();

        boolean success = bookService.updateBook(id, title, author, price);
        if (success) {
            JOptionPane.showMessageDialog(this, "更新成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "更新失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateFakeData() {
        dataGeneratorService.generateFakeData();
        JOptionPane.showMessageDialog(this, "100 条数据已成功生成并插入！", "成功", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryDatabaseAppUI app = new LibraryDatabaseAppUI();
            app.setVisible(true);
        });
    }
}
