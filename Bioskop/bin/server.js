var express = require ('express');
var bodyparser = require('body-parser');
var fs = require('fs');
var mysql = require('mysql');
var app = express();

app.use(bodyparser.json());
const {json} = require('body-parser');
const { connect } = require('http2');

const conn = mysql.createConnection({
    host : 'localhost',
    user : 'root',
    password : '',
    database : 'bioskop'
});

conn.connect(function(err){
    if (err) throw err;
    console.log("MySQL connected...");
});

app.post('/admin', function(req, res) {
    console.log("POST request /admin");
    let username = {user: req.body.username};
    json.getString
    console.log("POST request data ="+JSON.stringify(username.user));
    let password = {pass: req.body.password};
    console.log("POST request data ="+JSON.stringify(password.pass));
    let sql = "SELECT nama FROM admin WHERE nama='"+username.user+"' AND password = '"+password.pass+"'";
    console.log(sql)
    let query = conn.query(sql, (err, result) => {
        console.log(JSON.stringify(
            {"status" : 200, "error" : null, "response" : result}
        ));
        if(result != "") {
            res.send("Login Berhasil")
        }
        else {
            res.send("Login Gagal")}
    });
});

app.post('/register', function(req, res) {
    console.log('POST request /register');
    let username = {user: req.body.username};
    json.getString
    console.log("POST request data ="+JSON.stringify(username.user));

    let password = {pass: req.body.password};
    console.log("POST request data ="+JSON.stringify(password.pass));

    let email = {mail: req.body.email};
    console.log("POST request data ="+JSON.stringify(email.mail));

    let check = "SELECT iduser FROM user WHERE username ='"+username.user+"'";

    let checker = conn.query(check, (err, checkresult)=>{
        console.log(JSON.stringify(
            {
                "status" : 200,
                "error" : null,
                "response" : checkresult
            }
        ));
        console.log(checkresult);
        if (checkresult == ""){
            let sql = "INSERT INTO user (username, password, email) VALUES ('"+username.user+"','"+password.pass+"','"+email.mail+"')";
            let query = conn.query(sql, (err, result) =>{
                console.log(JSON.stringify(
                    {
                        "status" : 200,
                        "error" : null,
                        "response" : result
                    }
                ));
                conn.query(check, (err, checkresult) => {
                    console.log(JSON.stringify(
                        {
                            "status" : 200,
                            "error" : null,
                            "response" : checkresult
                        }
                    ));
                });
                res.send("Pendaftaran Berhasil")
            });
        }
        else {
            res.send("Pendaftaran Gagal")
        }
    })
});

app.post('/login', function(req, res) {
    console.log("POST request /login");
    let username = {user: req.body.username};
    let password = {pass: req.body.password};

    let sql = "SELECT iduser, username FROM user WHERE username='"+username.user+"' AND password = '"+password.pass+"'";
    console.log(sql);

    let query = conn.query(sql, (err, result) => {
    if (err) {
        console.log(err);
        res.status(500).json({ status: 500, error: 'Internal Server Error', response: null });
    } else {
        if (result.length > 0) {
        res.status(200).json({ status: 200, error: null, response: 'Login Berhasil' });
        } else {
        res.status(401).json({ status: 401, error: 'Unauthorized', response: 'Login Gagal' });
        }
    }
    });
});

app.get('/film', function(req, res) {
    console.log('Menerima GET request /film');
    let sql = "SELECT * FROM film";
    let query = conn.query(sql, function(err, result){
        if (err) throw err;
        res.send(JSON.stringify({
            "status" : 200,
            "error" : null,
            "response" : result
        }));
    });
});

app.get('/listbooking', function(req, res) {
    console.log('Menerima GET request /listbooking');
    let sql = "SELECT * FROM bookings";
    let query = conn.query(sql, function(err, result) {
        if (err) throw err;

        console.log(JSON.stringify({
            "status": 200,
            "error": null,
            "response": result
        }));

        res.send(JSON.stringify({
            "status": 200,
            "error": null,
            "response": result
        }));
    });
});

app.put('/editfilm', function(req, res) {
    let judul = {Judul: req.body.judul};
    let harga = {Harga: req.body.harga};
    let noteater = {NoTeater: req.body.noteater};
    let jumlahkursi = {JumlahKursi: req.body.jumlahkursi};
    
    let sql = "UPDATE film SET NoTeater='"+noteater.NoTeater+"', HargaFilm='"+harga.Harga+"', JumlahKursi='"+jumlahkursi.JumlahKursi+"' WHERE JudulFilm='"+judul.Judul+"'";
    let query = conn.query(sql, (err, result) => {
        console.log(JSON.stringify(
            {
                "status" : 200,
                "error" : null,
                "response" : result
            }
        ));
        console.log(sql)
        if(result.affectedRows == "0") {
            res.send ("Gagal Edit Data")
        }
        else {
            res.send ("Berhasil Mengedit Data")
        }
    })
});

app.post('/tambahfilm', function(req, res) {
    console.log('POST request /tambahfilm');

    let judul = {Judul: req.body.judul};
    let harga = {Harga: req.body.harga};
    let noteater = {NoTeater: req.body.noteater};
    let jumlahkursi = {JumlahKursi: req.body.jumlahkursi};
    console.log(judul);
    console.log(harga);
    console.log(noteater);
    console.log(jumlahkursi);

    let check = "SELECT JudulFilm FROM film WHERE JudulFilm='"+judul.Judul+"'";

    let checker = conn.query(check, (err, checkresult)=>{
        console.log(JSON.stringify(
            {
                "status" : 200,
                "error" : null,
                "response" : checkresult
            }
        ));

        console.log(checkresult);
        if (checkresult == ""){
            let sql = "INSERT INTO film (JudulFilm, HargaFilm, NoTeater, JumlahKursi) VALUES ('"+judul.Judul+"','"+harga.Harga+"','"+noteater.NoTeater+"','"+jumlahkursi.JumlahKursi+"')";
            let query = conn.query(sql, (err, result) =>{
                console.log(JSON.stringify(
                    {
                        "status" : 200,
                        "error" : null,
                        "response" : result
                    }
                ));
                conn.query(check, (err, checkresult) => {
                    console.log(JSON.stringify(
                        {
                            "status" : 200,
                            "error" : null,
                            "response" : checkresult
                        }
                    ));
                });
                res.send("Berhasil Menambah Film")
            });
        }
        else {
            res.send("Gagal Menambah Film")
        }
    })
});

app.post('/booking', function(req, res) {
    console.log("POST request /booking");
    let nama = {Nama: req.body.nama};
    let tanggal = {Tanggal: req.body.tanggal};
    let judul = {Judul: req.body.judul};
    let noteater = {NoTeater: req.body.noteater};
    let nokursi = {NoKursi: req.body.nokursi};
    console.log(nama);
    console.log(tanggal);
    console.log(judul);
    console.log(noteater);
    console.log(nokursi);

    let checkAvailabilityQuery = "SELECT JumlahKursi FROM film WHERE JudulFilm = ? AND NoTeater = ?";
    let checkAvailabilityParams = [judul.Judul, noteater.NoTeater];
    conn.query(checkAvailabilityQuery, checkAvailabilityParams, (err, availabilityResult) => {
    if (err) {
        console.error(err);
        res.status(500).json({ status: 500, error: 'Internal Server Error', response: null });
    } else {
        if (availabilityResult.length > 0 && availabilityResult[0].JumlahKursi > 0) {
        let checkDuplicateQuery = "SELECT COUNT(*) AS count FROM bookings WHERE JudulFilm = ? AND NoTeater = ? AND NoKursi = ?";
        let checkDuplicateParams = [judul.Judul, noteater.NoTeater, nokursi.NoKursi];
        conn.query(checkDuplicateQuery, checkDuplicateParams, (err, duplicateResult) => {
            if (err) {
            console.error(err);
            res.status(500).json({ status: 500, error: 'Internal Server Error', response: null });
            } else {
            if (duplicateResult[0].count === 0) {
                let insertBookingQuery = "INSERT INTO bookings (Nama, Tanggal, JudulFilm, NoTeater, NoKursi) VALUES (?, ?, ?, ?, ?)";
                let insertBookingParams = [nama.Nama, tanggal.Tanggal, judul.Judul, noteater.NoTeater, nokursi.NoKursi];
                conn.query(insertBookingQuery, insertBookingParams, (err, bookingResult) => {
                if (err) {
                    console.error(err);
                    res.status(500).json({ status: 500, error: 'Internal Server Error', response: null });
                } else {
                    if (bookingResult.affectedRows > 0) {
                    let updateAvailabilityQuery = "UPDATE film SET JumlahKursi = JumlahKursi - 1 WHERE JudulFilm = ?";
                    let updateAvailabilityParams = [judul.Judul];
                    conn.query(updateAvailabilityQuery, updateAvailabilityParams, (err, updateResult) => {
                        if (err) {
                        console.error(err);
                        res.status(500).json({ status: 500, error: 'Internal Server Error', response: null });
                        } else {
                        console.log("Booking data inserted.");
                        res.status(200).json(bookingResult.insertId);
                        }
                    });
                    } else {
                    res.status(500).json({ status: 500, error: 'Internal Server Error', response: null });
                    }
                }
                });
            } else {
                res.status(400).json({ status: 400, error: 'Bad Request', response: 'NoKursi Already Exists' });
            }
            }
        });
        } else {
        res.status(400).json({ status: 400, error: 'Bad Request', response: 'Kursi Sudah Penuh' });
        }
    }
    });
});

app.post('/batal', function(req, res) {
    console.log('POST request /batal');
    let nama = req.body.nama;
    let noteater = req.body.noteater;
    let nokursi = req.body.nokursi;
    let checkBookingQuery = "SELECT * FROM bookings WHERE Nama = ? AND NoTeater = ? AND NoKursi = ?";
    let checkBookingParams = [nama, noteater, nokursi];

    conn.query(checkBookingQuery, checkBookingParams, (err, bookingResult) => {
        if (err) {
            console.error(err);
            res.status(500).json({ status: 500, error: 'Internal Server Error', response: null });
        } else {
            if (bookingResult.length > 0) {
                let deleteBookingQuery = "DELETE FROM bookings WHERE NoTeater = ? AND Nama = ? AND NoKursi = ?";
                let deleteBookingParams = [noteater, nama, nokursi];
conn.query(deleteBookingQuery, deleteBookingParams, (err, deleteResult) => {
                    if (err) {
                        console.error(err);
                        res.status(500).json({ status: 500, error: 'Internal Server Error', response: null });
                    } else {
                        if (deleteResult.affectedRows > 0) {
                            let updateAvailabilityQuery = "UPDATE film SET JumlahKursi = JumlahKursi + 1 WHERE NoTeater = ?";
                            let updateAvailabilityParams = [bookingResult[0].noteater];
                    
                            conn.query(updateAvailabilityQuery, updateAvailabilityParams, (err, updateResult) => {
                                if (err) {
                                    console.error(err);
                                    res.status(500).json({ status: 500, error: 'Internal Server Error', response: null });
                                } else {
                                    console.log("Booking Dibatalkan. Jumlah Kursi Ditambahkan.");
                                    res.status(200).json({ status: 200, error: null, response: 'Booking Dibatalkan. Jumlah Kursi Ditambahkan.' });
                                }
                            });
                        } else {
                            res.status(500).json({ status: 500, error: 'Internal Server Error', response: null });
                        }
                    }
                });
            } else {
                res.status(404).json({ status: 404, error: 'Not Found', response: 'Booking not found.' });
            }
        }
    });
});

var server = app.listen(3000, function(){
    var host = server.address().address;
    var port = server.address().port;
    console.log("Express app listening at http://%s:%s", host, port);
})