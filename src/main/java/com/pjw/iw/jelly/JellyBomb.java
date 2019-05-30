package com.pjw.iw.jelly;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class JellyBomb {

    public static final char JELLY_NONE = 'N';
    public static final char JELLY_NORMAL = 'B';
    public static final char JELLY_HORIZONTAL = 'H';
    public static final char JELLY_VERTICAL = 'V';
    public static final char JELLY_SQUARE = 'S';


    private static Random random = new Random();
    private static char[] jellyTypes = new char[]{JELLY_NORMAL, JELLY_HORIZONTAL, JELLY_VERTICAL, JELLY_SQUARE};

    /**
     * 随机一个果冻，各种类型果冻概率均等
     */
    public static char randomJelly() {
        return jellyTypes[random.nextInt(jellyTypes.length)];
    }

    /**
     * 随机生成一组游戏布局
     */
    public static char[][] randomJellyArray() {
        char[][] array = new char[JellyManager.JELLY_ARRAY_ROW_COUNT][JellyManager.JELLY_ARRAY_COL_COUNT];

        for (int i = 0; i < array.length; i++) {
            char[] c = array[i];
            for (int j = 0; j < c.length; j++) {
                array[i][j] = randomJelly();
            }
        }
        return array;
    }

    /**
     * 根据指定区域参数对指定布局进行消除，本方法认为所传参数均有效，如无效则可能抛出异常
     */
    public static void explode(int startRow, int startCol, int endRow, int endCol, char[][] array) {
        //待消除队列，存储需要消除的格子
        Queue<Integer> queue = new ArrayDeque<>();

        //区域内的格子入队
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startCol; j <= endCol; j++) {
                queue.offer(JellyAssistant.calculatePointNum(i, j));
            }
        }

        while (!queue.isEmpty()) {

            //取出1个待消除的格子，计算其行列值
            int pointNum = queue.poll();
            int pointRow = JellyAssistant.calculateRowByPointNum(pointNum);
            int pointCol = JellyAssistant.calculateColByPointNum(pointNum);

            //获取果冻类型
            char jelly = array[pointRow][pointCol];
            if (jelly != JELLY_NONE) {//若该格子无果冻则不处理

                //先把该格子果冻清理掉
                array[pointRow][pointCol] = JELLY_NONE;

                //炸弹果冻特殊处理
                if (jelly != JELLY_NORMAL) {
                    //根据炸弹类型获得与该格子相关联的所有格子
                    int[] relatedNum = null;
                    switch (jelly) {
                        case JELLY_HORIZONTAL: {
                            relatedNum = getRelatedPointByHorizontal(pointNum);
                            break;
                        }
                        case JELLY_VERTICAL: {
                            relatedNum = getRelatedPointByVertical(pointNum);
                            break;
                        }
                        case JELLY_SQUARE: {
                            relatedNum = getRelatedPointBySquare(pointNum);
                            break;
                        }
                    }

                    if (relatedNum != null) {
                        //相关联的格子，如果有未消除的果冻，则进入待消除队列
                        for (int num : relatedNum) {
                            if (getJellyByPointNum(array, num) != JELLY_NONE) {
                                queue.offer(num);
                            }
                        }
                    }
                }
            }
        }

        //所有可消除的果冻已全部处理，按列进行下落操作
        dropByCol(array);
    }

    /**
     * 获得指定布局中指定格子的果冻类型
     */
    private static char getJellyByPointNum(char[][] array, int pointNum) {
        int pointRow = JellyAssistant.calculateRowByPointNum(pointNum);
        int pointCol = JellyAssistant.calculateColByPointNum(pointNum);
        return array[pointRow][pointCol];
    }

    /**
     * 获得指定格子横向关联的所有格子（不包括格子本身）
     */
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

    /**
     * 获得指定格子纵向关联的所有格子（不包括格子本身）
     */
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

    /**
     * 获得指定格子周围八个方向关联的所有格子（不包括格子本身）
     */
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

    /**
     * 按列下落果冻，空位随机补充果冻
     */
    private static void dropByCol(char[][] array) {
        int rowCount = array.length;
        int colCount = array[0].length;

        //逐列下落
        for (int col = 0; col < colCount; col++) {
            int writeIndex = rowCount - 1;
            int readIndex = writeIndex;

            //将未消除的果冻放到最下端
            while (readIndex >= 0) {
                if (array[readIndex][col] != JELLY_NONE) {
                    array[writeIndex--][col] = array[readIndex][col];
                }

                readIndex--;
            }

            //剩余的位置随机补充果冻
            while (writeIndex >= 0) {
                array[writeIndex][col] = randomJelly();

                writeIndex--;
            }
        }
    }
}
