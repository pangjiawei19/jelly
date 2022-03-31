'use strict';

function ArrayUtil() {

}

module.exports = ArrayUtil;

/**
 * 二维数组转字符串，如果数组为null或空，返回字符串"null"
 * @param array
 * @returns {string}
 */
ArrayUtil.array2String = function (array) {
    if (!array || array.length < 1) {
        return 'null';
    }

    let result = String(array.length) + ",";
    for (let row of array) {
        result += row.length + ",";
        for (let col of row) {
            result += col + ",";
        }
    }

    return result.substring(0, result.length - 1);
};

/**
 * 字符串转二维数组，如果文本不符合格式则返回null
 * @param text
 * @returns {null|Array}
 */
ArrayUtil.string2Array = function (text) {
    if (!text || text === 'null') {
        return null;
    }

    let array = [];
    let index = 0;
    const dataArray = text.split(',');
    let rowCount = parseInt(dataArray[index++]);

    for (let i = 0; i < rowCount; i++) {
        let colCount = parseInt(dataArray[index++]);
        let array1 = [];
        for (let j = 0; j < colCount; j++) {
            array1.push(dataArray[index++]);
        }

        array.push(array1);
    }

    return array;
};