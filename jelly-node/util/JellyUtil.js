'use strict';

const MysqlUtil = require('./MysqlUtil');
const ArrayUtil = require('./ArrayUtil');

function JellyUtil() {

}

module.exports = JellyUtil;

JellyUtil.POINT_NUM_ROW_COL_RATIO = 1000;

JellyUtil.LEVEL_MIN = 1;
JellyUtil.LEVEL_MAX = 10;

JellyUtil.ARRAY_ROW_COUNT = 8;
JellyUtil.ARRAY_COL_COUNT = 8;

JellyUtil.JELLY_NONE = 'N';
JellyUtil.JELLY_NORMAL = 'B';
JellyUtil.JELLY_HORIZONTAL = 'H';
JellyUtil.JELLY_VERTICAL = 'V';
JellyUtil.JELLY_SQUARE = 'S';


JellyUtil.types = [JellyUtil.JELLY_NORMAL, JellyUtil.JELLY_HORIZONTAL, JellyUtil.JELLY_VERTICAL, JellyUtil.JELLY_SQUARE];
JellyUtil.level2InitialArray = new Map();

JellyUtil.init = function () {
    MysqlUtil.query('select level, array from level', [], function (err, rows) {
        if (err) {
            throw err;
        }
        JellyUtil.level2InitialArray.clear();

        rows.forEach(function (row) {
            JellyUtil.level2InitialArray.set(row.level, ArrayUtil.string2Array(row.array));
        })
    })
};

/**
 * 判断指定等级是否有效
 * @param level 指定等级
 * @returns {boolean} 是否有效
 */
JellyUtil.isValidLevel = function (level) {
    return level >= JellyUtil.LEVEL_MIN && level <= JellyUtil.LEVEL_MAX;
};

/**
 * 判断参数指定的区域是否有效
 * @param row0
 * @param col0
 * @param row1
 * @param col1
 * @returns {boolean}
 */
JellyUtil.isValidExplodeArea = function (row0, col0, row1, col1) {
    if (row0 > row1 || col0 > col1) {
        return false;
    }

    if (row0 < 0
        || row1 >= JellyUtil.ARRAY_ROW_COUNT
        || col0 < 0
        || col1 > JellyUtil.ARRAY_COL_COUNT) {
        return false;
    }

    return true;
};

/**
 * 根据等级获得初始布局
 * @param level
 * @returns {Array}
 */
JellyUtil.getInitialArrayByLevel = function (level) {
    let result = [];
    let array = JellyUtil.level2InitialArray.get(level);
    for (let item of array) {
        result.push(item.slice());
    }
    return result;
};

/**
 * 随机一组游戏布局
 * @returns {Array} 布局二维数组
 */
JellyUtil.randomArray = function () {
    let array = [];
    for (let i = 0; i < JellyUtil.ARRAY_ROW_COUNT; i++) {
        let array1 = [];

        for (let j = 0; j < JellyUtil.ARRAY_COL_COUNT; j++) {
            array1.push(JellyUtil.randomJelly());
        }

        array.push(array1);
    }
    return array;
};

/**
 * 随机一个果冻，各种类型果冻概率均等
 * @returns {string} 果冻类型
 */
JellyUtil.randomJelly = function () {
    let index = parseInt(Math.random() * JellyUtil.types.length);
    return JellyUtil.types[index];
};

/**
 * 根据行列计算格子的数值
 * @param row
 * @param col
 * @returns {number}
 */
JellyUtil.calculatePointNum = function (row, col) {
    return row * JellyUtil.POINT_NUM_ROW_COL_RATIO + col;
};

/**
 * 根据格子数值计算所在行
 * @param pointNum
 * @returns {number}
 */
JellyUtil.calculateRowByPointNum = function (pointNum) {
    return parseInt(pointNum / JellyUtil.POINT_NUM_ROW_COL_RATIO);
};

/**
 * 根据格子数值计算所在列
 * @param pointNum
 * @returns {number}
 */
JellyUtil.calculateColByPointNum = function (pointNum) {
    return pointNum % JellyUtil.POINT_NUM_ROW_COL_RATIO;
};

/**
 * 根据指定区域参数对指定布局进行消除，本方法认为所传参数均有效，如无效则可能抛出异常
 * @param array {string[][]}
 * @param startRow {number}
 * @param startCol {number}
 * @param endRow {number}
 * @param endCol {number}
 */
JellyUtil.explode = function (array, startRow, startCol, endRow, endCol) {
    //待消除队列，存储需要消除的格子
    let wait = [];
    for (let i = startRow; i <= endRow; i++) {
        for (let j = startCol; j <= endCol; j++) {
            wait.push(JellyUtil.calculatePointNum(i, j));
        }
    }

    while (wait.length > 0) {
        //取出1个待消除的格子，计算其行列值
        let pointNum = wait.pop();
        let pointRow = JellyUtil.calculateRowByPointNum(pointNum);
        let pointCol = JellyUtil.calculateColByPointNum(pointNum);

        let jelly = array[pointRow][pointCol];
        //若该格子无果冻则不处理
        if (jelly !== JellyUtil.JELLY_NONE) {

            //先把该格子果冻清理掉
            array[pointRow][pointCol] = JellyUtil.JELLY_NONE;

            //炸弹果冻特殊处理
            if (jelly !== JellyUtil.JELLY_NORMAL) {
                //根据炸弹类型获得与该格子相关联的所有格子

                let relatedNums;

                switch (jelly) {
                    case JellyUtil.JELLY_HORIZONTAL: {
                        relatedNums = JellyUtil.getRelatedPointByHorizontal(pointNum);
                        break;
                    }
                    case JellyUtil.JELLY_VERTICAL: {
                        relatedNums = JellyUtil.getRelatedPointByVertical(pointNum);
                        break;
                    }
                    case JellyUtil.JELLY_SQUARE: {
                        relatedNums = JellyUtil.getRelatedPointBySquare(pointNum);
                        break;
                    }
                }

                if (relatedNums) {
                    relatedNums
                        .filter(num => JellyUtil.getJellyByPointNum(array, num) !== JellyUtil.JELLY_NONE)
                        .forEach(num => wait.push(num));

                }
            }
        }
    }

    //所有可消除的果冻已全部处理，按列进行下落操作
    JellyUtil.dropByCol(array);
};

/**
 * 获得指定布局中指定格子的果冻类型
 * @param array
 * @param pointNum
 * @returns {string}
 */
JellyUtil.getJellyByPointNum = function (array, pointNum) {
    let pointRow = JellyUtil.calculateRowByPointNum(pointNum);
    let pointCol = JellyUtil.calculateColByPointNum(pointNum);
    return array[pointRow][pointCol];
};

/**
 * 获得指定格子横向关联的所有格子（不包括格子本身）
 * @param pointNum {number}
 * @returns {Array}
 */
JellyUtil.getRelatedPointByHorizontal = function (pointNum) {
    let result = [];

    let pointRow = JellyUtil.calculateRowByPointNum(pointNum);
    let pointCol = JellyUtil.calculateColByPointNum(pointNum);

    for (let col = 0; col < JellyUtil.ARRAY_COL_COUNT; col++) {
        if (col !== pointCol) {
            result.push(JellyUtil.calculatePointNum(pointRow, col));
        }
    }

    return result;
};

/**
 * 获得指定格子纵向关联的所有格子（不包括格子本身）
 * @param pointNum {number}
 * @returns {Array}
 */
JellyUtil.getRelatedPointByVertical = function (pointNum) {
    let result = [];

    let pointRow = JellyUtil.calculateRowByPointNum(pointNum);
    let pointCol = JellyUtil.calculateColByPointNum(pointNum);

    for (let row = 0; row < JellyUtil.ARRAY_ROW_COUNT; row++) {
        if (row !== pointRow) {
            result.push(JellyUtil.calculatePointNum(row, pointCol));
        }
    }

    return result;
};

/**
 * 获得指定格子周围八个方向关联的所有格子（不包括格子本身）
 * @param pointNum {number}
 * @returns {Array}
 */
JellyUtil.getRelatedPointBySquare = function (pointNum) {
    let result = [];

    let pointRow = JellyUtil.calculateRowByPointNum(pointNum);
    let pointCol = JellyUtil.calculateColByPointNum(pointNum);

    let startRow = Math.max(0, pointRow - 1);
    let endRow = Math.min(JellyUtil.ARRAY_ROW_COUNT - 1, pointRow + 1);
    let startCol = Math.max(0, pointCol - 1);
    let endCol = Math.min(JellyUtil.ARRAY_COL_COUNT - 1, pointCol + 1);

    for (let i = startRow; i <= endRow; i++) {
        for (let j = startCol; j <= endCol; j++) {
            if (i !== pointRow && j !== pointCol) {
                result.push(JellyUtil.calculatePointNum(i, j));
            }
        }
    }

    return result;
};


/**
 * 按列下落果冻，空位随机补充果冻
 * @param array {string[][]}
 */
JellyUtil.dropByCol = function (array) {
    let rowCount = array.length;
    let colCount = array[0].length;

    for (let col = 0; col < colCount; col++) {
        let writeIndex = rowCount - 1;
        let readIndex = writeIndex;

        //将未消除的果冻放到最下端
        while (readIndex >= 0) {
            if (array[readIndex][col] !== JellyUtil.JELLY_NONE) {
                array[writeIndex--][col] = array[readIndex][col];
            }

            readIndex--;
        }

        //剩余的位置随机补充果冻
        while (writeIndex >= 0) {
            array[writeIndex][col] = JellyUtil.randomJelly();

            writeIndex--;
        }
    }
};


