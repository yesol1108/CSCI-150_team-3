var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var mysql = require('mysql');

var app = express();

// load enviroment variabled
require('dotenv').config({path: './config/'+(process.env.NODE_ENV || 'development')+'.env'});

// db connection
var connectionInfo = {
    host : "csci150se.c9g7nukf2ffx.us-west-1.rds.amazonaws.com",
    user : "intrSE_3",
    password : "tkfkdgo11",
    database : "SPLITTR",
    // connectionLimit : process.env.CSCI_DB_CONNECTION_LIMIT,
    // charset: process.env.CSCI_DB_CHARSET
}
var connection = mysql.createPool(connectionInfo);
global.pool = connection;
connection.on('err', function(err) {
    next(err);
});

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(logger('dev'));

// load router module
var users = require('./routes/users');

// url mapping to router
app.use('/users', users);

// catch 404
app.use(function(req, res, next) {
  res.status(404).json({"result": false, "message": "Page not found"});
});

// error handler
app.use(function(err, req, res, next) {
  // render the error page
  res.status(err.status || 500).json({"result": false, "message":err});

});

module.exports = app;
