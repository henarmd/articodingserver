var mongoose = require('mongoose'),
	Schema = mongoose.Schema;

var boardcellstate = new Schema({
    id: Number,
    x: Number,
    y: Number,
    args: [String]
});

var boardobjectstate = new Schema({
    id: Number,
    x: Number,
    y: Number,
    orientation: Number,
    args: [String]
});

var boardhintstate = new Schema({
    id: Number,
    x: Number,
    y: Number,
    orientation: Number,
    amount: Number,
    args: [String]
});

var boardstate = new Schema({
    rows: Number,
    columns: Number,
    cells: [boardcellstate],
    boardElements: [boardobjectstate],    
    boardHints: [boardhintstate]
});

var Level = new Schema({
    boardstate: {type:boardstate, required: false},
    name: String,
});

module.exports = mongoose.model('Level', Level);