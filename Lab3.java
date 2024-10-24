import java.util.Scanner;

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

        // Імена гравців за замовчуванням
        Player player1 = new Player("Player1", 'X');
        Player player2 = new Player("Player2", 'O');

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
    }
}
