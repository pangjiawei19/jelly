'use strict';

const path = require('path');
const mysql = require('mysql');

// process.env.NODE_CONFIG_DIR = path.join(__dirname, '../', 'config');

const config = require('config');

const MysqlUtil = {

};

module.exports = MysqlUtil;

const dbConfig = config.get('mysql');
const pool = mysql.createPool({
    connectionLimit: 10,
    host: dbConfig.host,
    user: dbConfig.user,
    password: dbConfig.password,
    database: dbConfig.database
});

MysqlUtil.query = function (sql, params, cb) {
    params = params || [];
    pool.query(sql, params, cb);
};


MysqlUtil.createConnection = function () {
    return mysql.createConnection({
        host: dbConfig.host,
        user: dbConfig.user,
        password: dbConfig.password,
        database: dbConfig.database
    });
};

// let connection = MysqlUtil.createConnection();
// connection.connect();


// connection.query('insert into array values(? , ?)', [String(new Date().getTime()), 'ddd'], function (err, rows) {
//     if (err) {
//         throw err;
//     }
//     console.log(rows);
// });

// connection.query('select * from array', function (err, rows, fields) {
//     if (err) {
//         throw err;
//     }
//
//     console.log(fields);
//
//     for (let row of rows) {
//         console.log(row.id + ":" + row.array);
//
//     }
// });

// connection.end();

