package com.pjw.iw.jelly;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class JellyBomb {

    public static final char BOMB_NONE = 'N';
    public static final char BOMB_NORMAL = 'B';
    public static final char BOMB_HORIZONTAL = 'H';
    public static final char BOMB_VERTICAL = 'V';
    public static final char BOMB_SQUARE = 'S';


    private static Random random = new Random();
    private static char[] bombTypes = new char[]{BOMB_NORMAL, BOMB_HORIZONTAL, BOMB_VERTICAL, BOMB_SQUARE};

    public static char randomBomb() {
        double ratio = Math.random();
        if (ratio < 0.7) {
            return BOMB_NORMAL;
        } else if (ratio < 0.8) {
            return BOMB_HORIZONTAL;
        } else if (ratio < 0.9) {
            return BOMB_VERTICAL;
        } else {
            return BOMB_SQUARE;
        }

//        return bombTypes[random.nextInt(bombTypes.length)];
    }

    public static char[][] randomJellyArray() {
        char[][] array = new char[JellyManager.JELLY_ARRAY_ROW_COUNT][JellyManager.JELLY_ARRAY_COL_COUNT];

        for (int i = 0; i < array.length; i++) {
            char[] c = array[i];
            for (int j = 0; j < c.length; j++) {
                array[i][j] = randomBomb();
//                array[i][j] = 'B';
            }
        }
        return array;
    }

    public static void printBombArray(char[][] array) {
        for (char[] chars : array) {
            for (char c : chars) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        char[][] c = randomJellyArray();
//        c[0][0]='H';
//        c[0][4]='S';
//        c[2][1]='S';
//        c[3][4]='V';
//        c[4][1]='H';
//        c[5][0]='S';
//        c[5][5]='S';
        printBombArray(c);

        explode(0, 0, 0, 0, c);

//        explode(1, 0, 1, 2, c);
//        System.out.println();
//        printBombArray(c);
//
//        explode(5, 0, 6, 2, c);
        System.out.println();
        printBombArray(c);
    }

    public static void explode(int startRow, int startCol, int endRow, int endCol, char[][] array) {
        Queue<Integer> queue = new ArrayDeque<>();

//        int[] startPoint = JellyAssistant.calculateRowByPointNum(start);
//        int startRow = startPoint[0];
//        int startCol = startPoint[1];
//
//        int[] endPoint = JellyAssistant.calculateRowByPointNum(end);
//        int endRow = endPoint[0];
//        int endCol = endPoint[1];

        for (int i = startRow; i <= endRow; i++) {
            for (int j = startCol; j <= endCol; j++) {
                queue.offer(JellyAssistant.calculatePointNum(i, j));
            }
        }

        while (!queue.isEmpty()) {
            int pointNum = queue.poll();
            int pointRow = JellyAssistant.calculateRowByPointNum(pointNum);
            int pointCol = JellyAssistant.calculateColByPointNum(pointNum);
            char bomb = array[pointRow][pointCol];
            if (bomb != BOMB_NONE) {
                array[pointRow][pointCol] = BOMB_NONE;
                if (bomb != BOMB_NORMAL) {
                    int[] relatedNum = null;
                    switch (bomb) {
                        case BOMB_HORIZONTAL: {
                            relatedNum = getRelatedPointByHorizontal(pointNum);
                            break;
                        }
                        case BOMB_VERTICAL: {
                            relatedNum = getRelatedPointByVertical(pointNum);
                            break;
                        }
                        case BOMB_SQUARE: {
                            relatedNum = getRelatedPointBySquare(pointNum);
                            break;
                        }
                    }

                    if (relatedNum != null) {
                        for (int num : relatedNum) {
                            queue.offer(num);
                        }
                    }
                }
            }

        }
    }

    private static int[] getRelatedPointByHorizontal(int pointNum) {
        int[] result = new int[JellyManager.JELLY_ARRAY_COL_COUNT - 1];
        int pointRow = JellyAssistant.calculateRowByPointNum(pointNum);
        int pointCol = JellyAssistant.calculateColByPointNum(pointNum);

        int index = 0;
        for (int col = 0; col < JellyManager.JELLY_ARRAY_COL_COUNT; col++) {
            if (col != pointCol) {
                result[index++] = JellyAssistant.calculatePointNum(pointRow, col);
            }
        }
        return result;
    }

    private static int[] getRelatedPointByVertical(int pointNum) {
        int[] result = new int[JellyManager.JELLY_ARRAY_ROW_COUNT - 1];

        int pointRow = JellyAssistant.calculateRowByPointNum(pointNum);
        int pointCol = JellyAssistant.calculateColByPointNum(pointNum);

        int index = 0;
        for (int row = 0; row < JellyManager.JELLY_ARRAY_ROW_COUNT; row++) {
            if (row != pointRow) {
                result[index++] = JellyAssistant.calculatePointNum(row, pointCol);
            }
        }
        return result;
    }

    private static int[] getRelatedPointBySquare(int pointNum) {

        int pointRow = JellyAssistant.calculateRowByPointNum(pointNum);
        int pointCol = JellyAssistant.calculateColByPointNum(pointNum);

        int startRow = Math.max(0, pointRow - 1);
        int endRow = Math.min(JellyManager.JELLY_ARRAY_ROW_COUNT - 1, pointRow + 1);
        int startCol = Math.max(0, pointCol - 1);
        int endCol = Math.min(JellyManager.JELLY_ARRAY_COL_COUNT - 1, pointCol + 1);

        int numCount = (endRow - startRow + 1) * (endCol - startCol + 1) - 1;
        int[] result = new int[numCount];

        int index = 0;
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startCol; j <= endCol; j++) {
                if (i != pointRow || j != pointCol) {
                    result[index++] = JellyAssistant.calculatePointNum(i, j);
                }
            }
        }
        return result;
    }
}
