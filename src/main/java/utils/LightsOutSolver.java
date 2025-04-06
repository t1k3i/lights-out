package utils;

import dtos.SolutionStepAddDTO;
import exceptions.not_solvable.NotSolvableException;
import jakarta.ws.rs.BadRequestException;

import java.util.List;

public class LightsOutSolver {
    public LightsOutSolver() {}

    private static void checkIfValidBoard(String board) {
        int length = board.length();
        int sqrt = (int) Math.sqrt(length);
        if(sqrt * sqrt != length)
            throw new BadRequestException("Problem must be a square");
    }

    public static void checkIfSolvable(String board) {
        checkIfValidBoard(board);

        StringBuilder flippedBoard = new StringBuilder();
        for (int i = 0; i < board.length(); i++) {
            char c = board.charAt(i);
            if (c == '1') {
                flippedBoard.append('0');
            } else if (c == '0') {
                flippedBoard.append('1');
            }
        }
        board = flippedBoard.toString();

        int n = (int) Math.sqrt(board.length());
        int size = n * n;
        int[][] allMoves = new int[size][size];
        int[] startingPos = new int[size];

        for (int i = 0; i < size; i++) {
            int x = i % n;
            int y = i / n;
            allMoves[i][i] = 1;
            if (x > 0) allMoves[i][i - 1] = 1;
            if (x < n - 1) allMoves[i][i + 1] = 1;
            if (y > 0) allMoves[i][i - n] = 1;
            if (y < n - 1) allMoves[i][i + n] = 1;
            startingPos[i] = board.charAt(i) - '0';
        }

        checkIfSolvableLinearEquation(allMoves, startingPos);
    }

    private static void checkIfSolvableLinearEquation(int[][] a, int[] b) {
        int size = a.length;
        for (int column = 0; column < size; column++) {
            int pivot = column;
            if (a[column][column] == 0) {
                for (int row = column + 1; row < size; row++) {
                    if (a[row][column] > a[pivot][column]) {
                        pivot = row;
                        break;
                    }
                }
            }

            if (a[pivot][column] == 0) continue;

            int[] tempRow = a[column]; a[column] = a[pivot]; a[pivot] = tempRow;
            int tempVal = b[column]; b[column] = b[pivot]; b[pivot] = tempVal;

            for (int row = column + 1; row < size; row++) {
                if (a[row][column] == 1) {
                    for (int k = column; k < size; k++) {
                        a[row][k] ^= a[column][k];
                    }
                    b[row] ^= b[column];
                }
            }
        }

        for (int i = size - 1; i >= 0; i--)
            if (a[i][i] == 0 && b[i] != 0)
                throw new NotSolvableException("Problem not solvable");
    }

    public static boolean checkIfCorrectSolution(String problemDescription, List<SolutionStepAddDTO> solutionStepAddDTOS) {
        int length = problemDescription.length();
        int n = (int) Math.sqrt(length);
        char[] board = problemDescription.toCharArray();

        for (SolutionStepAddDTO step : solutionStepAddDTOS) {
            int x = step.x();
            int y = step.y();
            press(board, x, y, n);
        }

        for (char c : board)
            if (c != '1') return false;
        return true;
    }

    private static void press(char[] board, int x, int y, int n) {
        toggle(board, x, y, n);
        toggle(board, x - 1, y, n);
        toggle(board, x + 1, y, n);
        toggle(board, x, y - 1, n);
        toggle(board, x, y + 1, n);
    }

    private static void toggle(char[] board, int x, int y, int n) {
        if (x < 0 || y < 0 || x >= n || y >= n) return;
        int move = (n - 1 - y) * n + x;
        board[move] = (board[move] == '1') ? '0' : '1';
    }
}
