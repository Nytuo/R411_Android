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
    console.log("GETGOOGLEAPI_book : url : " + url);
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
    if (name === "") {
        res.status(400).send("Bad request");
        return;
    }
    if (name.length > 100) {
        res.status(400).send("Bad request");
        return;
    }
    if (name.length < 3) {
        res.status(400).send("Bad request");
        return;
    }
    let ol = await GETOLAPI_search(name);
    let google = await GETGOOGLEAPI_book(name);
    let books = []

    try {
        if (ol.docs.length > 0) {
            for (let i = 0; i < ol.docs.length; i++) {
                let book = ol.docs[i];
                let combinedBook = new Book();
                combinedBook.name = book.title;
                combinedBook.authors = book.author_name;
                if (typeof combinedBook.publisher === "string") {
                    combinedBook.publisher = book.publisher;
                } else {
                    combinedBook.publisher = book.publisher[0];
                }
                combinedBook.date = book.first_publish_year.toString();
                combinedBook.isbn = book.isbn[0];
                combinedBook.imgURLs = [];
                if (book.cover_i !== undefined && book.cover_i !== "" && book.cover_i !== "undefined") {
                    combinedBook.imgURLs.push("https://covers.openlibrary.org/b/id/" + book.cover_i + "-M.jpg");
                } else {
                    combinedBook.imgURLs.push("https://" + req.headers.host + "/img/no_cover.jpg");
                }
                combinedBook.description = book.subtitle;
                combinedBook.price = Math.round(Math.random() * 100);
                books.push(combinedBook);
            }
        }
    } catch (e) {
        console.log("OL API : " + e);
    }
    try {
        if (google.items.length > 0) {
            for (let i = 0; i < google.items.length; i++) {
                let book = google.items[i];
                let combinedBook = new Book();
                combinedBook.name = book.volumeInfo.title;
                combinedBook.authors = book.volumeInfo.authors;
                combinedBook.publisher = book.volumeInfo.publisher;
                combinedBook.date = book.volumeInfo.publishedDate.toString();
                combinedBook.isbn = book.volumeInfo.industryIdentifiers[0].identifier;
                combinedBook.price = Math.round(Math.random() * 100);
                combinedBook.description = book.volumeInfo.description;
                combinedBook.imgURLs = [];
                if (book.volumeInfo.imageLinks !== undefined)
                    combinedBook.imgURLs.push(book.volumeInfo.imageLinks.thumbnail);
                else
                    combinedBook.imgURLs.push("https://" + req.headers.host + "/img/no_cover.jpg");
                books.push(combinedBook);
            }
        }
    } catch (e) {
        console.log("Google API : " + e);
    }
    res.send(books);
});
let librariesNames = [
    "Le Havre du Livre",
    "Le Coin Lecture",
    "L'Annexe Littéraire",
    "L'Étagère à Livres",
    "Le Coffre aux Romans",
    "Le Coin Tranquille",
    "Le Paradis du Lecteur",
    "Le Repaire du Rat de Bibliothèque",
    "L'Oasis du Bibliophile",
    "La Retraite Littéraire",
    "Le Palais de la Prose",
    "L'Emporium du Livre de Poche",
    "L'Encrier",
    "La Forge Fictionnelle",
    "Le Sanctuaire du Conte",
    "La Caverne des Livres",
    "La Bouquinerie",
    "La Galerie Littéraire",
    "La Bibliothèque Imaginaire",
    "La Salle de Lecture",
    "L'Atelier de l'Écrivain",
    "Le Coin des Mots",
    "Le Coin des Écrivains",
    "L'Antre des Mots",
    "Le Livrothèque",
    "Le Coin des Livres Anciens",
    "L'Étage des Écrivains",
    "Le Cabinet de Lecture",
    "La Grotte des Romans",
    "Le Royaume des Livres",
    "La Salle des Auteurs",
    "Le Coin des Essais",
    "Le Palais des Nouvelles",
    "Le Grenier des Livres",
    "La Résidence des Mots"
]
app.get("/api/libraries/positions/:number", limiterDefault, async function (req, res) {
    let number = req.params.number;
    if (number === undefined || number === null || number === "" || isNaN(number) || number === "undefined" || number === "null" || number === "NaN" || number === " " || number === 0) {
        number = 1;
    }
    if (number > librariesNames.length) {
        number = librariesNames.length;
    }
    let positions = [];
    for (let i = 0; i < number; i++) {
        let lat = 46.227638 + Math.random() * 0.5 - 0.25;
        let long = 2.213749 + Math.random() * 0.5 - 0.25;
        positions.push({lat: lat, long: long, name: librariesNames[i]});
    }
    res.send(positions);
});

async function GETNYTAPI_ISBN(NYTAPIKEY, number = 10) {
    let names = "https://api.nytimes.com/svc/books/v3/lists/names.json?api-key=" + NYTAPIKEY;
    let Nresponse = await fetch(names);
    let Ndata = await Nresponse.json();
    let lists = Ndata.results;
    let ISBNs = [];

    while (lists.length > 0) {
        let list = lists[Math.floor(Math.random() * lists.length)];
        let url = "https://api.nytimes.com/svc/books/v3/lists/current/" + list.list_name_encoded + ".json?api-key=" + NYTAPIKEY;
        let response = await fetch(url);
        let data = await response.json();
        for (let i = 0; i < data.results.books.length; i++) {
            ISBNs.push(data.results.books[i].primary_isbn13);
        }
        if (ISBNs.length >= number) {
            return ISBNs;
        }
        lists.splice(lists.indexOf(list), 1);
    }
    return ISBNs;
}

app.get("/api/book/:number", limiterDefault, async function (req, res) {
    let number = req.params.number;
    if (number === undefined || number === null || number === "" || isNaN(number) || number === "undefined" || number === "null" || number === "NaN" || number === " " || number === 0) {
        number = 1;
    }
    let books = [];

    let NYTAPIKEY = "VpSUrkaKLqAqXlo6RCWILRriPyjaGPpC";
    let ISBNs = await GETNYTAPI_ISBN(NYTAPIKEY, number);
    if (ISBNs.length > number) {
        ISBNs = ISBNs.slice(0, number);
    }


    for (let i = 0; i < ISBNs.length; i++) {
        let book = await GETGOOGLEAPI_bookISBN(ISBNs[i]);
        try{


        if (book.items.length > 0) {


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
        } else {
            console.log("Google API : No book found");
        }
    }catch(e){
        console.log("Google API : No book found");
        }
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