'use strict';

function Util() {

}

module.exports = Util;

Util.RES_ERROR_PARAM = 'INVALID PARAMS';
Util.RES_ERROR_SERVER = 'SERVER ERROR';

Util.sendErrParam = function (res) {
    Util.sendErr(res, Util.RES_ERROR_PARAM);
};

Util.sendErrServer = function (res) {
    Util.sendErr(res, Util.RES_ERROR_SERVER);
};

Util.sendErr = function (res, code) {
    res.send(code);
};

Util.formatArrayOutString = function (array) {
    let result = '';
    for (let row of array) {
        result += '<br>' + row.join('');
    }
    return result;
};

