const express = require("express");
const swaggerUi = require("swagger-ui-express");
const swaggerFile = require("./swagger_output.json");
const fs = require("fs");
const path = require("path");
const app = express();
let CryptoJS = require("crypto-js");
app.use("", express.static(__dirname + "/public"));
app.use("/js", express.static(__dirname + "/js"));
var RateLimit = require('express-rate-limit');

var limiterDefault = RateLimit({
    windowMs: 1 * 60 * 1000, max: 1000
});
var apiGoogleLimiter = RateLimit({
    windowMs: 1 * 100 * 1000, max: 100
})

app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(swaggerFile));

let CosmicComicsTemp = path.join(path.dirname(__dirname), 'CosmicData');

fs.mkdirSync(CosmicComicsTemp, {recursive: true});

if (!fs.existsSync(CosmicComicsTemp + "/.env")) {
    let envTemplate = "MARVEL_PUBLIC_KEY=\nMARVEL_PRIVATE_KEY=\nGBOOKSAPIKEY=\n"
    fs.writeFileSync(CosmicComicsTemp + "/.env", envTemplate, {encoding: 'utf8'});
}
const dotenv = require('dotenv');
dotenv.config({
    path: CosmicComicsTemp + "/.env"
});


/**
 * Get the name without some special characters
 * @param {string} CommonName
 * @return {string} finalName
 */

const cors = require('cors');
app.use(cors());
app.use(express.json({limit: "50mb"}));
app.use(express.urlencoded({extended: true}));
app.use(limiterDefault)
let host;
const args = process.argv.slice(2);
let port = 6906;
if (args.length > 0) {
    port = args[0];
}
const server = app.listen(port, "0.0.0.0", function () {
    host = this.address().address;
    port = this.address().port;
    console.log("Listening on port %s:%s!", host, port);
});

async function GETGOOGLEAPI_book(name = "") {
    if (name === "") {
        console.log("GETGOOGLEAPI_book : name is empty");
        return;
    }

    name = name.replaceAll(/[(].+[)]/g, "");
    name = name.replaceAll(/[\[].+[\]]/g, "");
    name = name.replaceAll(/[\{].+[\}]/g, "");
    name = name.replaceAll(/[#][0-9]{1,}/g, "");
    name = name.replace(/\s+$/, "");
    console.log("GETGOOGLEAPI_book : name : " + name);
    let url = "https://www.googleapis.com/books/v1/volumes?q=" + encodeURIComponent(name) + "&maxResults=10&key=" + process.env.GBOOKSAPIKEY;
    let response = await fetch(url);
    let data = await response.json();
    return data;
}

async function GETGOOGLEAPI_bookISBN(isbn = "") {
    if (isbn === "") {
        console.log("GETGOOGLEAPI_bookISBN : isbn is empty");
        return;
    }

    console.log("GETGOOGLEAPI_bookISBN : isbn : " + isbn);
    let url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn + "&key=" + process.env.GBOOKSAPIKEY;
    let response = await fetch(url);
    let data = await response.json();
    return data;
}

async function GETOLAPI_search(name = "") {
    if (name === "") {
        console.log("OL API : name is empty");
        return;
    }

    name = name.replaceAll(/[(].+[)]/g, "");
    name = name.replaceAll(/[\[].+[\]]/g, "");
    name = name.replaceAll(/[\{].+[\}]/g, "");
    name = name.replaceAll(/[#][0-9]{1,}/g, "");
    name = name.replace(/\s+$/, "");
    console.log("OL API : name : " + name);
    let url = "http://openlibrary.org/search.json?q=" + encodeURIComponent(name)
    let response = await fetch(url);
    let data = await response.json();
    return data;
}

class Book {

    name;
    authors;
    publisher;
    date;
    isbn;
    imgURLs;
    price;
    description;


    constructor() {

    }


    get name() {
        return this.name;
    }

    set name(value) {
        this.name = value;
    }

    get authors() {
        return this.authors;
    }

    set authors(value) {
        this.authors = value;
    }

    get publisher() {
        return this.publisher;
    }

    set publisher(value) {
        this.publisher = value;
    }

    get date() {
        return this.date;
    }

    set date(value) {
        this.date = value;
    }

    get isbn() {
        return this.isbn;
    }

    set isbn(value) {
        this.isbn = value;
    }

    get imgURLs() {
        return this.imgURLs;
    }

    set imgURLs(value) {
        this.imgURLs = value;
    }

    get price() {
        return this.price;
    }

    set price(value) {
        this.price = value;
    }

    get description() {
        return this.description;
    }

    set description(value) {
        this.description = value;
    }
}

app.get("/api/book/search/:name", limiterDefault, async function (req, res) {
    let name = decodeURIComponent(req.params.name);
    let ol = await GETOLAPI_search(name);
    let google = await GETGOOGLEAPI_book(name);
    let combined = {
        books: [],
    }
    if (ol.docs.length > 0) {
        for (let i = 0; i < ol.docs.length; i++) {
            let book = ol.docs[i];
            let combinedBook = new Book();
            combinedBook.name = book.title;
            combinedBook.authors = book.author_name;
            combinedBook.publisher = book.publisher;
            combinedBook.date = book.first_publish_year;
            combinedBook.isbn = book.isbn;
            combinedBook.imgURLs = [];
            combinedBook.imgURLs.push("https://covers.openlibrary.org/b/id/" + book.cover_i + "-M.jpg");
            combinedBook.description = book.subtitle;
            combinedBook.price = Math.round(Math.random() * 100);
            combined.books.push(combinedBook);
        }
    }
    if (google.items.length > 0) {
        for (let i = 0; i < google.items.length; i++) {
            let book = google.items[i];
            let combinedBook = new Book();
            combinedBook.name = book.volumeInfo.title;
            combinedBook.authors = book.volumeInfo.authors;
            combinedBook.publisher = book.volumeInfo.publisher;
            combinedBook.date = book.volumeInfo.publishedDate;
            combinedBook.isbn = book.volumeInfo.industryIdentifiers[0].identifier;
            combinedBook.price = Math.round(Math.random() * 100);
            combinedBook.description = book.volumeInfo.description;
            combinedBook.imgURLs = [];
            if (book.volumeInfo.imageLinks !== undefined)
                combinedBook.imgURLs.push(book.volumeInfo.imageLinks.thumbnail);
            else
                combinedBook.imgURLs.push("http://" + req.headers.host + "/img/no_cover.jpg");
            combined.books.push(combinedBook);
        }
    }
    res.send(combined);
});

app.get("/api/libraries/positions", limiterDefault, async function (req, res) {
    res.send([
            {
                "longitude": 2.3488,
                "latitude": 48.8534,
                "name": "Bibliothèque nationale de France",
            }
        ]
    );
});

async function GETNYTAPI_ISBN(NYTAPIKEY,number = 10) {
    let names = "https://api.nytimes.com/svc/books/v3/lists/names.json?api-key=" + NYTAPIKEY;
    let Nresponse = await fetch(names);
    let Ndata = await Nresponse.json();
    let lists = Ndata.results;
    let ISBNs = [];

    while (lists.length > 0) {
        let list = lists[0];
        let url = "https://api.nytimes.com/svc/books/v3/lists/current/" + list.list_name_encoded + ".json?api-key=" + NYTAPIKEY;
        let response = await fetch(url);
        let data = await response.json();
        for (let i = 0; i < data.results.books.length; i++) {
            ISBNs.push(data.results.books[i].primary_isbn13);
        }
        if (ISBNs.length >= number) {
            return ISBNs;
        }
        lists.splice(0, 1);
    }
    return ISBNs;
}

app.get("/api/book/:number", limiterDefault, async function (req, res) {

    let books = [];
    let number = req.params.number;
        let NYTAPIKEY = "VpSUrkaKLqAqXlo6RCWILRriPyjaGPpC";
        let ISBNs = await GETNYTAPI_ISBN(NYTAPIKEY,number);
        if (ISBNs.length > number){
            ISBNs = ISBNs.slice(0, number);
        }



        for (let i = 0; i < ISBNs.length; i++) {
            let book = await GETGOOGLEAPI_bookISBN(ISBNs[i]);
            console.log(book);
            let book2 = new Book();
            book2.name = book.items[0].volumeInfo.title;
            book2.authors = book.items[0].volumeInfo.authors;
            book2.publisher = book.items[0].volumeInfo.publisher;
            book2.date = book.items[0].volumeInfo.publishedDate;
            book2.isbn = book.items[0].volumeInfo.industryIdentifiers[0].identifier;
            book2.price = Math.round(Math.random() * 100);
            book2.description = book.items[0].volumeInfo.description;
            book2.imgURLs = [];
            if (book.items[0].volumeInfo.imageLinks !== undefined)
                book2.imgURLs.push(book.items[0].volumeInfo.imageLinks.thumbnail);
            else
                book2.imgURLs.push("http://" + req.headers.host + "/img/no_cover.jpg");
            books.push(book2);
        }


    res.send(books);
});

process.on('SIGINT', () => {
    console.log('SIGINT signal received: closing server');
    server.close(() => {
        console.log('Server closed');
        process.exit(0);
    });
});

//If page not found
app.all('*', (req, res) => {
    res.send("Page not found use routes or go to /api-docs for swagger");
});