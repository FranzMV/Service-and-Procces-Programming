/**@author Francisco David Manzanedo */

const mongoose = require('mongoose');

let TaskSchema = new mongoose.Schema({

    description:{
        type: String,
        minlength: 1,
        trim: true
    },
    type:{
        type: String,
        required: true,
        enum: ['home', 'work', 'family', 'sport', 'undefined'],
        trim: true
    },
    priority:{
        type: Number,
        min:1,
        max: 5,
        required: true
    },
    done:{
        type: Boolean,
        default: false,
        required: true
    },
    difficulty:{
        type: Number,
        min: 0,
        max: 10,
        required: true
    }, 

});

let Task = mongoose.model('Task', TaskSchema);
module.exports = Task;