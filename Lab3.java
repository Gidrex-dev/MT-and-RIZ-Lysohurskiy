import java.sql.*;
import java.util.Scanner;

// Клас для роботи з базою даних
class Db {
    String dbUrl = "jdbc:mysql://localhost:3306/myGame?useSSL=false";
    String user = "root";
    String password = "Maksym_now";
    Connection con;

    public Db() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.con = DriverManager.getConnection(dbUrl, user, password);
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("Failed to close connection: " + e.getMessage());
        }
    }

    public boolean validateUser(String username, String password) {
        String query = "SELECT count(*) FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 1;  // Повертаємо true, якщо знайдено 1 запис
                }
            }
        } catch (Exception e) {
            System.out.println("Error validating user: " + e.getMessage());
        }
        return false;
    }

    public void createUser(String username, String password) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            System.out.println("Користувача " + username + " створено.");
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }
}

// Клас, що описує гравця
class Player {
    private String name;
    private char symbol;  // 'X' або 'O'

    // Конструктор
    public Player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    // Отримання імені гравця
    public String getName() {
        return name;
    }

    // Отримання символу гравця ('X' або 'O')
    public char getSymbol() {
        return symbol;
    }

    // Метод для введення ходу гравця
    public int[] makeMove() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(name + " (" + symbol + "), введіть номер рядка (1-3): ");
        int row = scanner.nextInt() - 1; // Віднімаємо 1, щоб відповідало індексу масиву
        System.out.println("Введіть номер стовпця (1-3): ");
        int col = scanner.nextInt() - 1; // Віднімаємо 1, щоб відповідало індексу масиву
        return new int[] {row, col};
    }
}

// Клас, що описує ігрове поле
class Board {
    private char[][] board; // Ігрове поле 3x3

    // Конструктор
    public Board() {
        board = new char[3][3];
        // Заповнення поля порожніми символами
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
    }

    // Виведення ігрового поля на екран
    public void printBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Зміна стану ігрового поля (встановлення символу на конкретне місце)
    public boolean makeMove(int row, int col, char symbol) {
        if (row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == '-') {
            board[row][col] = symbol;
            return true;
        }
        System.out.println("Ця клітинка вже зайнята або виходить за межі поля!");
        return false;
    }

    // Перевірка на перемогу
    public boolean checkWin(char symbol) {
        // Перевірка рядків і стовпців
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) ||
                (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol)) {
                return true;
            }
        }

        // Перевірка діагоналей
        if ((board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
            (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol)) {
            return true;
        }

        return false;
    }

    // Перевірка, чи всі клітинки зайняті
    public boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }
}

// Основний клас для гри
public class Lab3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Db db = new Db();

        // Запитуємо логін і пароль для першого гравця
        Player player1 = loginPlayer(db, scanner, 'X');
        
        // Запитуємо логін і пароль для другого гравця
        Player player2 = loginPlayer(db, scanner, 'O');

        Board board = new Board();
        Player currentPlayer = player1;

        boolean gameWon = false;
        while (!board.isBoardFull() && !gameWon) {
            board.printBoard();
            int[] move;
            boolean validMove;

            do {
                move = currentPlayer.makeMove();
                validMove = board.makeMove(move[0], move[1], currentPlayer.getSymbol());
            } while (!validMove);

            gameWon = board.checkWin(currentPlayer.getSymbol());

            if (gameWon) {
                board.printBoard();
                System.out.println("Переміг гравець " + currentPlayer.getName() + "!");
            } else if (board.isBoardFull()) {
                board.printBoard();
                System.out.println("Нічия!");
            } else {
                currentPlayer = (currentPlayer == player1) ? player2 : player1;
            }
        }

        db.close();
    }

    // Метод для логіну гравця
    public static Player loginPlayer(Db db, Scanner scanner, char symbol) {
        String username;
        String password;

        while (true) {
            System.out.println("Введіть логін для гравця (" + symbol + "): ");
            username = scanner.nextLine();
            System.out.println("Введіть пароль для гравця (" + symbol + "): ");
            password = scanner.nextLine();

            if (db.validateUser(username, password)) {
                System.out.println("Логін успішний для гравця " + username);
                return new Player(username, symbol);
            } else {
                System.out.println("Невірний логін або пароль. Спробуйте ще раз.");
            }
        }
    }
}
