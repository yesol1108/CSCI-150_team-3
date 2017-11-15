var express = require('express');
var router = express.Router();
var md5 = require('md5');
/**
 * user login
 */
router.post('/login', function(req, res, next) {
    // validation
    if(! req.body.email) return res.json({"result": false, "message": "Email required"});
    if(! req.body.pswd) return res.json({"result": false, "message": "Password required"});
    
    // user check query
    var schema = [req.body.email, md5(req.body.pswd)];
    var sql = 'SELECT * FROM user_db WHERE email = ? AND pswd = ?';
    pool.query(sql, schema, function(err, result) {
        if(err) {
            next(err);
        } else {
            // if exists user
            if(result[0]) {
                return res.json({
                    "result": true,
                    "message": "Login success",
                    "data": {
                        "userNo": result[0]['userNo'],
                        "name": result[0]['name'],
                        "email": result[0]['email'],
                        "phone": result[0]['phone']
                    }
                });
            }

            // if not exists user
            res.json({
                "result": false,
                "message": "Can not find user"
            });
        }
    });
});


/**
 * user register
 */
router.post('/register', function(req, res, next) {
    // validation
    if(! req.body.email) return res.json({"result": false, "message": "Email required"});
    if(! req.body.pswd) return res.json({"result": false, "message": "Password required"});
    if(! req.body.name) return res.json({"result": false, "message": "Name required"});
    if(! req.body.phone) return res.json({"result": false, "message": "Phone required"});

    // already register user check by email
    pool.query('SELECT COUNT(*) AS cnt FROM user_db WHERE email = ?', [req.body.email], function(err, result) {
        if(err) {
            next(err);
        } else {
            if(result[0]['cnt'] > 0) {
                res.json({"result": false, "message": "Already registered email"});
            } else {
                // new user register
                var schema = [req.body.name, req.body.email, md5(req.body.pswd), req.body.phone];
                var sql = 'INSERT INTO user_db SET name = ?, email = ?, pswd = ?, phone = ?';
                pool.query(sql, schema, function(err, result) {
                    if(err) {
                        next(err);
                    } else {
                        res.json({"result": true, "message": "Register success"})
                    }
                });
            }
        }
    });
});

module.exports = router;
