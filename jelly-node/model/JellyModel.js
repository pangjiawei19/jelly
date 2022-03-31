'use strict';

const MysqlUtil = require('../util/MysqlUtil');
const ArrayUtil = require('../util/ArrayUtil');

function JellyModel() {

}

module.exports = JellyModel;

/**
 * 存储布局信息
 * @param array {string[][]} 布局信息（二维数组）
 * @param callback {function} 回调函数
 */
JellyModel.saveArray = function (array, callback) {
    let sql = 'insert into array(array) values(?)';
    let arrayText = ArrayUtil.array2String(array);

    MysqlUtil.query(sql, [arrayText], function (err, result) {
        if (err) {
            console.log('JellyModel.saveArray err: ' + err);
            return callback(err);
        }

        callback(null, result);
    })
};

/**
 * 根据指定sessionId查询游戏布局信息，如果查询不到或发生异常返回null
 * @param sessionId
 * @param callback {function}
 */
JellyModel.findArray = function (sessionId, callback) {
    let sql = 'select array from array where id = ?';

    MysqlUtil.query(sql, [sessionId], function (err, result) {
        if (err) {
            console.log('JellyModel.findArray err: ' + err);
            return callback(err);
        }

        let array;

        if (result.length < 1) {
            array = null;
        } else {
            array = ArrayUtil.string2Array(result[0].array);
        }

        callback(null, array);
    })
};

/**
 * 修改布局信息
 * @param sessionId
 * @param array {string[][]}
 * @param callback {function}
 */
JellyModel.updateArray = function (sessionId, array, callback) {
    let sql = 'update array set array = ? where id = ?';
    let arrayText = ArrayUtil.array2String(array);

    MysqlUtil.query(sql, [arrayText, sessionId], function (err, result) {
        if (err) {
            console.log('JellyModel.updateArray err: ' + err);
            return callback(err);
        }

        callback(null, result);
    })
};