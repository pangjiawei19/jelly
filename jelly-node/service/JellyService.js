'use strict';

const async = require('async');
const JellyUtil = require('../util/JellyUtil');
const Util = require('../util/Util');

const JellyModel = require('../model/JellyModel');

function JellyService() {

}

module.exports = JellyService;

JellyService.startLevel = function (req, res) {
    const level = parseInt(req.query.level);
    if (isNaN(level) || !JellyUtil.isValidLevel(level)) {
        return Util.sendErrParam(res);
    }

    const array = JellyUtil.getInitialArrayByLevel(level);

    async.waterfall([
        function (callback) {
            JellyModel.saveArray(array, callback);
        },

    ], function (err, result) {
        if (err) {
            return Util.sendErr(res, Util.RES_ERROR_SERVER);
        }

        res.send(result.insertId + Util.formatArrayOutString(array));
    });
};


JellyService.move = function (req, res) {
    const row0 = parseInt(req.query.row0);
    const col0 = parseInt(req.query.col0);
    const row1 = parseInt(req.query.row1);
    const col1 = parseInt(req.query.col1);
    const sessionId = req.query.sessionId;

    if (isNaN(row0) || isNaN(col0) || isNaN(row1) || isNaN(col1) || !sessionId || !JellyUtil.isValidExplodeArea(row0, col0, row0, col1)) {
        return Util.sendErrParam(res);
    }

    async.waterfall([
        function (callback) {
            JellyModel.findArray(sessionId, function (err, array) {
                if (err || !array) {
                    return callback(Util.RES_ERROR_PARAM);
                }

                callback(null, array);
            });


        },
        /**
         *
         * @param array {string[][]}
         * @param callback {function}
         */
        function (array, callback) {
            //爆炸
            JellyUtil.explode(array, row0, col0, row1, col1);

            //更新布局信息
            JellyModel.updateArray(sessionId, array, function (err) {
                if (err) {
                    return callback(Util.RES_ERROR_SERVER);
                }

                callback(null, array);
            })
        }
    ], function (err, array) {
        if (err) {
            return Util.sendErr(res, err);
        }

        res.send(sessionId + Util.formatArrayOutString(array));
    })
};