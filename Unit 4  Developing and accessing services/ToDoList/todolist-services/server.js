/**@author Francisco David Manzanedo */

const mongoose = require('mongoose');
const express = require('express');
const bodyParser = require('body-parser');
const Task = require('./model/task');
const { request, response } = require('express');

mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/tasks');

let app = express();
app.use(express.json());


/**Task Services*/

/**GET  all tasks*/
app.get('/tasks', (request, response)=>{
    Task.find().then(result =>{
        response.send({ok: true, result: result});
    }); 
});


/**GET returns all tasks of the given type */
app.get('/tasks/type/:type', (request, response) =>{
    Task.find({type:request.params.type}).then(result =>{
        if(result.length>0){
            response.send({ok:true, result: result});
        }else
            response.send({ok: false, errorMessage:"Task type must be: 'home', 'work', 'family', 'sport' o 'undefined'"});
    }).catch(error => {
        response.send({ok:false, error:errorMessage});
    });
});


/**GET returns all tasks of the given priority. */
app.get('/tasks/priority/:priority', (request, response) =>{
    Task.find({priority:{$eq:request.params.priority}}).then(result =>{
        if(result.length > 0){
            response.send({ok : true, result : result});
        }else
            response.send({ok: false, errorMessage: "Task Priority must be a value between 1 and 5, inclusive"})
    }).catch(error =>{
        response.send({ok: false, error: error});
    });
});


/**GET returns all tasks done or not */
app.get('/tasks/done/:done', (request, response) =>{
    Task.find({done:request.params.done}).then(result =>{
        if(result.length>0){
            response.send({ok: true, result : result});
        }else
            response.send({ok : false, errorMessage: "Task done must be true or false"});
    }).catch(error => {
        response.send({ok : false, error: error});
    });
});


/**GET returns all tasks of the given minimum difficulty (included), sorted in descending order*/
app.get('/tasks/difficulty/:difficulty', (request, response) =>{
    Task.find({difficulty:{$gte : request.params.difficulty}})
        .sort({difficulty: -1})
        .then(result =>{
            if(result.length > 0){
                response.send({ok: true, result : result});
            }else
                response.send({ok : false, errorMessage: "Task Difficulty must be a value between 0 and 10, inclusive"}); 
    }).catch(error => {
        response.send({ok : false, error: error});
    });
});


/**POST new Task sends a new task to the server, with all the information needed. The service will return in the
result attribute the inserted task with all its information*/
app.post('/tasks', (request, response) =>{
    let newTask = new Task({
        description: request.body.description,
        type : request.body.type,
        priority : request.body.priority,
        done : request.body.done,
        difficulty : request.body.difficulty
    });
    newTask.save().then(result => {
        let data = {ok: true, result : result};
        response.send(data);
    }).catch(error => {
        let data = {ok: false, errorMessage: 'Error adding task'};
        response.send(data);
    });
});


/**PUT updates the contents of the Task matching the id received */
app.put('/tasks/:id', (request, response) =>{
    Task.findByIdAndUpdate(request.params.id , {
        $set:{
            description: request.body.description,
            type : request.body.type,
            priority : request.body.priority,
            done : request.body.done,
            difficulty : request.body.difficulty
        }
    }, {new : true}).then(result => {
        let data = {ok: true, result : result};
        response.send(data);
    }).catch(error => {
        let data = {ok : false, errorMessage: 'Error updating Task'};
        response.send(data);
    });
});

/**DELETE deletes the Task matching the id received */
app.delete('/tasks/:id', (request, response) =>{
    Task.findByIdAndDelete(request.params.id).then(result =>{
        let data = {ok: true, result : result};
        response.send(data);
    }).catch(error => {
        let data = {ok: false, errorMessage: 'Error removing Task'};
        res.send(data);
    });
});


app.listen(8080);