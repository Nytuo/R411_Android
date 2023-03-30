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
var apiAnilistLimiter = RateLimit({
    windowMs: 1 * 60 * 1000, max: 90
});
var apiMarvelLimiter = RateLimit({
    windowMs: 1 * 60 * 1000 * 60 * 24, max: 3000
});
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
let MarvelPublicKey = process.env.MARVEL_PUBLIC_KEY;
let MarvelPrivateKey = process.env.MARVEL_PRIVATE_KEY;
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

async function API_ANILIST_GET(name) {
    let query = `query ($page: Int, $perPage: Int, $search: String) {
  Page(page:$page,perPage:$perPage){
    pageInfo{
      total
    }
    media(type: MANGA,search:$search){
      id
      title{
        romaji
        english
        native
      }
      status
      startDate{
        year
        month
        day
      }
      endDate{
        year
        month
        day
	  }
	  description
	  meanScore
	  genres
	  coverImage{
	  large
	  }
	  bannerImage
	  trending
	  siteUrl
	  volumes
	  chapters
      staff{
        nodes{
          id
          name {
            full
            native
          }
          image {
            medium
          }
          description
          siteUrl
        }
        edges{
        role
        }
      }
      characters{
        nodes{
          id
          name {
            full
            native
          }
          image {
            medium
          }
          description
          siteUrl
        }
        edges{
        role
        }
      }
      relations{
        nodes{
          id
          title{
            romaji
            english
            native
          }
          coverImage{
          large
          }
          type
          format
        }
        edges{
          relationType
        }
      }
    }
  }
}`;
    let variables = {
        search: name, page: 1, perPage: 5
    }
    let url = 'https://graphql.anilist.co', options = {
        method: 'POST', headers: {
            'Content-Type': 'application/json', 'Accept': 'application/json',
        }, body: JSON.stringify({
            query: query, variables: variables
        })
    };
    let results = {};
    await fetch(url, options).then(handleResponse).then(handleData).catch(handleError);

    function handleResponse(response) {
        return response.json().then(function (json) {
            return response.ok ? json : Promise.reject(json);
        });
    }

    // duplicate an object
    function clone(obj) {
        return JSON.parse(JSON.stringify(obj));
    }

    function handleData(data) {
        console.log(data);
        if (data.data.Page.media.length === 0) {
            results = null;
            return;
        }
        let baseObject = clone(data.data.Page.media[0]);
        let staffObject = clone(data.data.Page.media[0].staff.nodes);
        let charactersObject = clone(data.data.Page.media[0].characters.nodes);
        let relationsObjectNodes = clone(data.data.Page.media[0].relations.nodes);
        let relationsObjectEdges = clone(data.data.Page.media[0].relations.edges);
        let relationsObject = [];
        for (let i = 0; i < relationsObjectNodes.length; i++) {
            relationsObject[i] = relationsObjectNodes[i];
            relationsObject[i]["relationType"] = relationsObjectEdges[i].relationType;
        }
        delete baseObject["relations"];
        for (let i = 0; i < baseObject.staff.nodes.length; i++) {
            for (let key in baseObject.staff.nodes[i]) {
                if (key !== "id" && key !== "name") {
                    delete baseObject.staff.nodes[i][key];
                }
            }
            baseObject.staff.nodes[i]["name"] = baseObject.staff.nodes[i]["name"]["full"];
        }
        baseObject.staff = baseObject.staff.nodes;
        for (let i = 0; i < baseObject.characters.nodes.length; i++) {
            for (let key in baseObject.characters.nodes[i]) {
                if (key !== "id" && key !== "name") {
                    delete baseObject.characters.nodes[i][key];
                }
            }
            baseObject.characters.nodes[i]["name"] = baseObject.characters.nodes[i]["name"]["full"];
        }
        baseObject.characters = baseObject.characters.nodes;
        results = {
            "base": baseObject, "staff": staffObject, "characters": charactersObject, "relations": relationsObject
        }
    }

    return results;

    function handleError(error) {
        console.error(error);
    }
}

async function API_ANILIST_GET_SEARCH(name) {
    let query = `query ($page: Int, $perPage: Int, $search: String) {
  Page(page:$page,perPage:$perPage){
    pageInfo{
      total
    }
    media(type: MANGA,search:$search){
      id
      title{
        romaji
        english
        native
      }
	  coverImage{
	  large
	  }
    }
  }
}`;
    let variables = {
        search: name, page: 1, perPage: 20
    }
    let url = 'https://graphql.anilist.co', options = {
        method: 'POST', headers: {
            'Content-Type': 'application/json', 'Accept': 'application/json',
        }, body: JSON.stringify({
            query: query, variables: variables
        })
    };
    let results = {};
    await fetch(url, options).then(handleResponse).then(handleData).catch(handleError);

    function handleResponse(response) {
        return response.json().then(function (json) {
            return response.ok ? json : Promise.reject(json);
        });
    }

    // duplicate an object
    function clone(obj) {
        return JSON.parse(JSON.stringify(obj));
    }

    function handleData(data) {
        console.log(data);
        if (data.data.Page.media.length === 0) {
            results = null;
            return;
        }
        let baseObject = clone(data.data.Page.media);
        results = {
            "base": baseObject,
        }
    }

    return results;

    function handleError(error) {
        console.error(error);
    }
}

function generateMarvelAPIAuth() {
    let ts = new Date().getTime();
    return "&ts=" + ts + "&hash=" + CryptoJS.MD5(ts + MarvelPrivateKey + MarvelPublicKey).toString() + "&apikey=" + MarvelPublicKey;
}

async function API_MARVEL_GET(name = "") {
    console.log("API_MARVEL_GET: " + name);
    if (name === "") {
        console.log("no name provided, aborting GETMARVELAPI");
        return;
    }
    let date = "";
    let dateNb = 0;
    let dateFromName = name.replace(/[^0-9]/g, "#");
    dateFromName.split("#").forEach(function (element) {
        if (dateNb === 0 && element.match(/^[0-9]{4}$/)) {
            dateNb++;
            date = element;
        }
    });
    name = name.replaceAll(/[(].+[)]/g, "");
    name = name.replace(/\s+$/, "");
    let encodedName = encodeURIComponent(name);
    let url;
    if (date !== "") {
        url = "https://gateway.marvel.com:443/v1/public/series?titleStartsWith=" + encodedName + "&startYear=" + date + generateMarvelAPIAuth();
    } else {
        url = "https://gateway.marvel.com:443/v1/public/series?titleStartsWith=" + encodedName + generateMarvelAPIAuth();
    }
    let response = await fetch(url);
    return await response.json();
}

/**
 * Recover the Marvel API data from the server
 * @param {string} what - What to recover (characters, comics, creators, events, series, stories)
 * @param {string} id - The id of the element to recover
 * @param {string} what2 - What to recover (characters, comics, creators, events, series, stories)
 * @param {boolean|string} noVariants - If the comics should be without variants
 * @param {string} orderBy - How to order the results
 * @param {string} type - The type of the element to recover (comic, collection, creator, event, story, series, character)
 */
function recoverMarvelAPILink(what, id, what2, noVariants = true, orderBy = "issueNumber", type = null) {
    if (type != null) {
        return "https://gateway.marvel.com:443/v1/public/" + what + "?" + type + "=" + id + generateMarvelAPIAuth();
    }
    if (what2 === "") {
        return "https://gateway.marvel.com:443/v1/public/" + what + "/" + id + "?noVariants=" + noVariants + "&orderBy=" + orderBy + generateMarvelAPIAuth();
    }
    return "https://gateway.marvel.com:443/v1/public/" + what + "/" + id + "/" + what2 + "?noVariants=" + noVariants + "&orderBy=" + orderBy + generateMarvelAPIAuth();
}

async function GETMARVELAPI_variants(id) {
    let url = recoverMarvelAPILink("series", id, "comics", true, "issueNumber")
    let response = await fetch(url);
    let data = await response.json();
    console.log(data);
    return data;
}

async function GETMARVELAPI_relations(id) {
    let url = recoverMarvelAPILink("series", id, "comics", true, "issueNumber")
    let response = await fetch(url);
    let data = await response.json();
    console.log(data);
    return data;
}

async function GETMARVELAPI_Characters(id, type) {
    let url = recoverMarvelAPILink("characters", id, "comics", true, "issueNumber", type)
    let response = await fetch(url);
    let data = await response.json();
    console.log(data);
    return data;
}

async function GETMARVELAPI_Creators(id, type) {
    let url = recoverMarvelAPILink("creators", id, "comics", true, "issueNumber", type)
    let response = await fetch(url);
    let data = await response.json();
    console.log(data);
    return data;
}

async function GETMARVELAPI_Comics(name = "", seriesStartDate = "") {
    if (name === "") {
        console.log("GETMARVELAPI_Comics : name is empty");
        return;
    }
    if (seriesStartDate === "") {
        console.log("GETMARVELAPI_Comics : seriesStartDate is empty");
        return;
    }
    let issueNumber = "";
    let inbFromName = name.replace(/[^#0-9]/g, "&");
    console.log("inbFromName : " + inbFromName);
    inbFromName.split("&").forEach(function (element) {
        if (element.match(/^[#][0-9]{1,}$/)) {
            issueNumber = element.replaceAll("#", "");
        }
    });
    name = name.replaceAll(/[(].+[)]/g, "");
    name = name.replaceAll(/[\[].+[\]]/g, "");
    name = name.replaceAll(/[\{].+[\}]/g, "");
    name = name.replaceAll(/[#][0-9]{1,}/g, "");
    name = name.replace(/\s+$/, "");
    console.log("GETMARVELAPI_Comics : name : " + name);
    console.log("GETMARVELAPI_Comics : issueNumber : " + issueNumber);
    console.log("GETMARVELAPI_Comics : seriesStartDate : " + seriesStartDate);
    let url;
    if (seriesStartDate !== "" && issueNumber !== "") {
        url = "https://gateway.marvel.com:443/v1/public/comics?titleStartsWith=" + encodeURIComponent(name) + "&startYear=" + seriesStartDate + "&issueNumber=" + issueNumber + "&noVariants=true" + generateMarvelAPIAuth();
    } else {
        url = "https://gateway.marvel.com:443/v1/public/comics?titleStartsWith=" + encodeURIComponent(name) + "&noVariants=true" + generateMarvelAPIAuth();
    }
    let response = await fetch(url);
    let data = await response.json();
    console.log(data);
    return data;
}

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
    let url = "https://www.googleapis.com/books/v1/volumes?q=" + encodeURIComponent(name) + "&maxResults=1&key=" + process.env.GBOOKSAPIKEY;
    let response = await fetch(url);
    let data = await response.json();
    console.log(data);
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

async function GETMARVELAPI_SEARCH(name = "", date = "") {
    if (name === "") {
        console.log("no name provided, aborting GETMARVELAPI");
        return;
    }
    name = name.replaceAll(/[(].+[)]/g, "");
    name = name.replace(/\s+$/, "");
    let encodedName = encodeURIComponent(name);
    let url;
    if (date !== "") {
        url = "https://gateway.marvel.com:443/v1/public/series?titleStartsWith=" + encodedName + "&startYear=" + date + generateMarvelAPIAuth();
    } else {
        url = "https://gateway.marvel.com:443/v1/public/series?titleStartsWith=" + encodedName + generateMarvelAPIAuth();
    }
    let response = await fetch(url);
    return await response.json();
}

app.get("/api/marvel/search/:name/", apiMarvelLimiter, async (req, res) => {
    GETMARVELAPI_SEARCH(req.params.name).then(function (data) {
        res.send(data);
    })
})
app.get("/api/marvel/search/:name/:date", apiMarvelLimiter, async (req, res) => {
    GETMARVELAPI_SEARCH(req.params.name, req.params.date).then(function (data) {
        res.send(data);
    })
})


app.get("/api/marvel/book/:name", apiMarvelLimiter, (req, res) => {

    let name = req.params.name;
    API_MARVEL_GET(name).then(async function (data) {
        console.log(data);
        let book = data
        let creators = await GETMARVELAPI_Creators(data["data"]["results"][0]["id"], "series");
        let characters = await GETMARVELAPI_Characters(data["data"]["results"][0]["id"], "series");
        let variants = await GETMARVELAPI_variants(data["data"]["results"][0]["id"]);
        let relations = await GETMARVELAPI_relations(data["data"]["results"][0]["id"]);
        let combined = {
            "book": book,
            "creators": creators,
            "characters": characters,
            "variants": variants,
            "relations": relations
        }
        res.send(combined);
    }).catch((err) => {
        res.sendStatus(500)
    })
})

app.get("/api/anilist/book/:name", apiAnilistLimiter, async (req, res) => {
    let name = req.params.name;
    await API_ANILIST_GET(name).then(async function (thedata) {
        res.send(thedata)
    })
});

app.get("/api/anilist/search/:name", apiAnilistLimiter, (req, res) => {
    let name = req.params.name;
    API_ANILIST_GET_SEARCH(name).then(async function (dataa) {
        res.send(dataa);
    })
});
app.get("/api/marvel/book/:name/:date", apiMarvelLimiter, async function (req, res) {
    let name = decodeURIComponent(req.params.name);
    let date = decodeURIComponent(req.params.date);
    GETMARVELAPI_Comics(req.params.name, req.params.date).then(function (data) {
        res.send(data);
    })
})
app.get("/api/ol/book/:name", limiterDefault, async function (req, res) {
    let name = decodeURIComponent(req.params.name);
    GETOLAPI_search(name).then(function (data) {
        res.send(data);
    })
})
app.get("/api/google/book/:name", apiGoogleLimiter, async function (req, res) {
    let name = decodeURIComponent(req.params.name);
    GETGOOGLEAPI_book(name).then(function (data) {
        res.send(data);
    })
})
process.on('SIGINT', () => {
    console.log('SIGINT signal received: closing server');
    server.close(() => {
        console.log('Server closed');
        process.exit(0);
    });
});

//If page not found
app.all('*', (req, res) => {
    res.sendFile(__dirname + '/404.html');
});