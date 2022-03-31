var express = require('express');
var router = express.Router();

const JellyService = require('../service/JellyService');

/* GET home page. */
router.get('/', function (req, res, next) {
    res.render('index', {title: 'Express'});
});

router.get('/start-level', function (req, res) {
    JellyService.startLevel(req, res);
});

router.get('/move', function (req, res) {
    JellyService.move(req, res);
});


module.exports = router;
