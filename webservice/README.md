# Cosmic Service

Basé sur le serveur de [Cosmic Comics](https://github.com/Nytuo/CosmicComics) de [Nytuo](https://github.com/Nytuo).

Ce webservice permet de communiquer avec les API de Marvel, Anilist et Google Books pour récupérer des informations sur les livres contenus sur ces plateformes.

## Build et lancement

Afin de lancer le service, il faut :

- Avoir NodeJS d'installé (testé avec la LTS 18)

Lancer la commande dans le dossier contenant le fichier package.json
```bash
npm install
```

Génération de la doc Swagger (version basique autogénéré)
```bash
npm run swagger-autogen
```

Une fois terminé lancer le serveur avec
```bash
npm run serv
```

Vous pouvez préciser le port du serveur avec l'option ```-p``` ```--port``` après ```npm run serv```.
Le port par défaut est le ```6906```
La doc swagger ce trouve à l'adresse suivante
```
http://localhost:6906/api-docs
```
