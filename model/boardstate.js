var mongoose = require('mongoose'),
    Schema = mongoose.Schema;

var boardcellstate = new Schema({
    id: Number,
    x: Number,
    y: Number,
    args: [String]
});
module.exports = mongoose.model('BoardCellState', boardcellstate);

var boardobjectstate = new Schema({
    id: Number,
    x: Number,
    y: Number,
    orientation: Number,
    args: [String]
});
module.exports = mongoose.model('BoardObjectState', boardobjectstate);

var boardhintstate = new Schema({
    id: Number,
    x: Number,
    y: Number,
    orientation: Number,
    amount: Number,
    args: [String]
});
module.exports = mongoose.model('BoardHintState', boardhintstate);

var boardstate = new Schema({
    rows: Number,
    columns: Number,
    cells: [boardcellstate],
    boardElements: [boardobjectstate],    
    boardHints: [boardhintstate]
});
module.exports = mongoose.model('BoardState', boardstate);