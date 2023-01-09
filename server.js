
var express     = require("express"),
app             = express(),
bodyParser      = require("body-parser"),
methodOverride  = require("method-override"),
mongoose        = require('mongoose');

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(methodOverride());


//Inicio Conexión BBDD
mongoose.set('strictQuery', false);
const mongoDB = "mongodb://0.0.0.0:27017/";
mongoose.connect(mongoDB, { useNewUrlParser: true, useUnifiedTopology: true });
const db = mongoose.connection;
db.on("error", err => {
    console.log("ERROR: " + err);
  });
db.on('open', err => {
    console.log("BBDD de niveles conectada!");
  });
//Fin Conexión BBDD

var levelsAPI = express.Router();

levelsAPI.get("/", function (req, res) {
    res.send("Hello World!");
  });

var LevelCrtl = require('./controller/levelsController');

//Link routes and functions
levelsAPI.get('/levels', LevelCrtl.findAllLevels);
levelsAPI.get('/levels/:id', LevelCrtl.findLevelById);
levelsAPI.post('/levels', LevelCrtl.addLevel);
levelsAPI.put('/levels/:id', LevelCrtl.updateLevel);
levelsAPI.delete('/levels/:id', LevelCrtl.deleteLevel);

app.use(levelsAPI);
const port = 5000
app.listen(port, () => {
  console.log(`Articoding\'s Server está a tope en el ${port}!`)
})