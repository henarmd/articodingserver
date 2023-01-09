
    var Level = require('./../model/level');
    var mongoose  = require('mongoose');

    //GET ALL
    exports.findAllLevels = function(req, res) {
        console.log('GET ALL');
        Level.find(function(err, levels) {
            if(!err) {
                res.status(200).json(levels);
            } else {
                console.log('ERROR: ' + err);
                res.status(500).json({status:"error"});
            }
        })
    }

    //GET
    exports.findLevelById = function(req, res) {
        console.log('GET ' + req.params.id);
        Level.findById(req.params.id, function(err, level) {
            if(!err) {
                if(level) {
                    res.status(200).json(level);
                } else {
                    res.status(404).json({status:"NO EXISTE EL NIVEL CON ID " + req.params.id});
                }
                
            } else {
                res.status(500).json({status:"ERROR OBTENIENDO EL NIVEL "+ req.params.id});
            }
        })
    }

    //PUT
    exports.updateLevel = function(req, res) {
        console.log('PUT ' + req.params.id);
        Level.findById(req.params.id, function(err, level) {
            if(!err) {
                console.log('PUT ' + req.body.boardstate.rows);
                level.boardstate.rows = req.body.boardstate.rows,
                level.boardstate.columns = req.body.boardstate.columns,
                level.name = req.body.name;
    
                level.save(function(err) {
                    if(!err) {
                        console.log('NIVEL ACTUALIZADO!');
                        res.status(200).json(level);
                    } else {
                        var msg = 'ERROR ACTUALIZANDO'
                        console.log(msg) + err;
                        res.status(500).json(msg);
                    }
                });
            } else {
                var msg = 'ERROR ACTUALIZANDO EL NIVEL ';
                console.log( msg + err);
                res.status(500).json(msg);
            }
            
        });
    }

    //POST
    exports.addLevel = function(req, res) {
        console.log('POST');
        console.log(req.body.name);
        var newLevel = new Level({
            boardstate: {
                rows:req.body.boardstate.rows,
                columns:req.body.boardstate.columns
            },
            name: req.body.name,
            id: req.body.id
        });

        newLevel.save(function(err) {
            if(!err) {
                console.log('NIVEL CREADO!');
                res.status(201).json(newLevel);
            } else {
                var msg = 'ERROR CREANDO NIVEL'
                console.log('ERROR ' + err);
                console.log(msg) + err;
                res.status(500).json(msg);
            }
        });
    }

    //DELETE
    exports.deleteLevel = function(req, res) {
        console.log('DELETE ' + req.params.id);

        Level.deleteOne({ _id: req.params.id }, function(err) {
            if(!err) {
                console.log('NIVEL BORRADO! ');
                res.status(204).json({status:"OK"});
            } else {
                var msg = 'ERROR BORRANDO NIVEL'
                console.log('ERROR ' + err);
                console.log(msg) + err;
                res.status(500).json(msg);
            }
        });

       
    }

