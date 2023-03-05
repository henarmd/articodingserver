var mongoose = require('mongoose'),
	Schema = mongoose.Schema;

var boardcellstate = new Schema({
    id: {type:Number, required: true},
    x: {type:Number, required: true},
    y: {type:Number, required: true},
    args: {type:[String], required: false}
});

var boardobjectstate = new Schema({
    id: {type:Number, required: true},
    x: {type:Number, required: true},
    y: {type:Number, required: true},
    orientation:  {type:Number, required: true},
    args: {type:[String], required: false}
});

var boardhintstate = new Schema({
    id:  {type:Number, required: true},
    x:  {type:Number, required: true},
    y:  {type:Number, required: true},
    orientation:  {type:Number, required: true},
    amount:  {type:Number, required: true},
    args: {type:[String], required: false}
});

var boardstate = new Schema({
    rows:  {type:Number, required: true},
    columns:{type:Number, required: true},
    cells: {type:[boardcellstate], required: true},
    boardElements:  {type:[boardobjectstate], required: true},
    boardHints:  {type:[boardhintstate], required: true}
});
var blockinfoSchema = new Schema({
    blockName: {type:String, required: true},
    maxUses: {type:Number, required: true},
});

var categoryblocksinfoSchema = new Schema({
    activate:{type:Boolean, required: true},
    activeBlocks: [blockinfoSchema]
});

var categorydataSchema = new Schema({
    categoryName:String,
    blocksInfo: [categoryblocksinfoSchema]
});
var activeblocks = new Schema({
    specialBlock:String,
    categories: {type:[categorydataSchema], required: true},
});
var Level = new Schema({
    activeblocks: {type:activeblocks, required: true},
    boardstate: {type:boardstate, required: true},
    name: String
});

module.exports = mongoose.model('Level', Level);