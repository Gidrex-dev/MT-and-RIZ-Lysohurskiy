public class Lab2 {

    // Метод для виведення симетричної ялинки з символів ''
    public void printTree(int levels) {
        for (int i = 1; i <= levels; i++) {
            // Виводимо пробіли для вирівнювання зірочок по центру
            for (int j = 0; j < levels - i; j++) {
                System.out.print(" ");
            }
            // Виводимо зірочки
            for (int k = 0; k < (2*i - 1); k++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }

    // Метод для створення та виведення двовимірного масиву, кожен елемент якого на 3 більший за попередній
    public void createAndPrintArray(int rows, int cols) {
        int[][] array = new int[rows][cols];
        int value = 1;  // Початкове значення

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                array[i][j] = value;
                value += 3;  // Кожен наступний елемент на 3 більше
            }
        }

        // Виведення масиву
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(array[i][j] + "\t");
            }
            System.out.println();
        }
    }

    // Основний метод для тестування
    public static void main(String[] args) {
        Lab2 lab = new Lab2();

        // Задаємо кількість рівнів ялинки у змінній
        int treeLevels = 15;

        // Виведення симетричної ялинки
        lab.printTree(treeLevels);

        // Створення і виведення двовимірного масиву
        lab.createAndPrintArray(3, 4);
    }
}