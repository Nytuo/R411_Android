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

app.get("/api/libraries/positions/:number", limiterDefault, async function (req, res) {
    let number = req.params.number;
    if (number === undefined || number === null || number === "" || isNaN(number) || number === "undefined" || number === "null" || number === "NaN" || number === " " || number === 0) {
        number = 1;
    }

    let positions = [
        {"latitude": 47.2152, "longitude": 1.5529, "name": "La Bibliothèque de la Comté"},
        {"latitude": 43.6018, "longitude": 1.4391, "name": "La Librairie du Magicien Blanc"},
        {"latitude": 48.8566, "longitude": 2.3522, "name": "La Salle des Cartes de la Terre du Milieu"},
        {"latitude": 46.2276, "longitude": 6.1170, "name": "La Bibliothèque de Fondcombe"},
        {"latitude": 48.8903, "longitude": 2.2370, "name": "La Bibliothèque de Poudlard"},
        {"latitude": 45.7640, "longitude": 4.8357, "name": "Les Sortilèges des Mots"},
        {"latitude": 43.2965, "longitude": 5.3698, "name": "La Librairie des Sorciers"},
        {"latitude": 47.4765, "longitude": -0.5503, "name": "Le Chaudron Littéraire"},
        {"latitude": 43.6043, "longitude": 1.4437, "name": "La Bibliothèque de la Force"},
        {"latitude": 48.5734, "longitude": 7.7521, "name": "La Cantina des Livres"},
        {"latitude": 48.6908, "longitude": 2.3983, "name": "La Librairie du Côté Obscur"},
        {"latitude": 45.7644, "longitude": 4.8351, "name": "Le Sabre des Mots"},
        {"latitude": 44.8378, "longitude": 0.5792, "name": "La Bibliothèque Galactique"},
        {"latitude": 47.2183, "longitude": -1.5533, "name": "La Librairie de l'Espace-Temps"},
        {"latitude": 48.8566, "longitude": 2.3522, "name": "L'Antre des Extraterrestres"},
        {"latitude": 44.8378, "longitude": -0.5792, "name": "La Bibliothèque d'Elfes"},
        {"latitude": 48.8752, "longitude": 2.3356, "name": "Les Chroniques des Dragons"},
        {"latitude": 43.6108, "longitude": 3.8767, "name": "Les Grimoires des Nains"},
        {"latitude": 48.8566, "longitude": 2.3522, "name": "La Bibliothèque du Docteur"},
        {"latitude": 43.6108, "longitude": 3.8767, "name": "La Bibliothèque de Gallifrey"},
        {"latitude": 47.2183, "longitude": -1.5533, "name": "Le Tardis"},
        {"latitude": 48.2118, "longitude": -1.5536, "name": "La bibliothèque de la guerre du Temps"},
        {"latitude": 48.5810, "longitude": 7.7431, "name": "La Bibliothèque de Skaro"},
        {"latitude": 47.4765, "longitude": -0.5503, "name": "La Bibliothèque du Silence"}
    ];
    if (number > positions.length) {
        number = positions.length;
    }
    let randomPositions = [];
    for (let i = 0; i < number; i++) {
        let randomIndex = Math.floor(Math.random() * positions.length);
        randomPositions.push(positions[randomIndex]);
        positions.splice(randomIndex, 1);
    }
    res.send(randomPositions);
});

let Commands = [];

class Command {
    constructor(commandNumber, price, books) {
        this.books = books;
        this.price = price;
        this.commandNumber = commandNumber;
    }
}

app.post("/api/command", limiterDefault, async function (req, res) {
    let commandNumber = req.body.commandNumber;
    let price = req.body.price;
    let books = req.body.books;

    let command = new Command(commandNumber, price, books);
    Commands.push(command);
    res.sendStatus(200);
});

app.get("/api/command/:commandNumber", limiterDefault, async function (req, res) {
    let commandNumber = req.params.commandNumber;
    let command = Commands.find(c => c.commandNumber === commandNumber);
    res.send([command]);
});

async function GETNYTAPI_ISBN(NYTAPIKEY, number = 10) {
    let names = "https://api.nytimes.com/svc/books/v3/lists/names.json?api-key=" + NYTAPIKEY;
    let Nresponse = await fetch(names);
    let Ndata = await Nresponse.json();
    let lists = Ndata.results;
    let ISBNs = [];
    console.log(Ndata,Nresponse);
    try {


        while (lists.length > 0) {
            let list = lists[Math.floor(Math.random() * lists.length)];
            let url = "https://api.nytimes.com/svc/books/v3/lists/current/" + list.list_name_encoded + ".json?api-key=" + NYTAPIKEY;
            let response = await fetch(url);
            let data = await response.json();
            try {
                for (let i = 0; i < data.results.books.length; i++) {
                    ISBNs.push(data.results.books[i].primary_isbn13);
                }
                if (ISBNs.length >= number) {
                    return ISBNs;
                }
                lists.splice(lists.indexOf(list), 1);
            } catch (e) {
                console.log(e);
            }
        }
    } catch (e) {
        console.log(e);
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
        try {


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
        } catch (e) {
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