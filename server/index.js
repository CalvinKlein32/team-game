const express = require('express');
const app = express();
//server is an http server that implements features of the express library
const server = require('http').Server(app);
const port = 3000;
//io is a socket io listtening to socket request on tghe server
const io = require('socket.io').listen(server);
//players is an array of all the player entity that connects to the server
var players=[];
//lobbyByLevel is an associative array that stores information about the available lobby's that could be assigned to players. 
//the key is the level and the value is a list where the first element is the number of that particular lobby and the second value 
//is the number of players that has been assigned to lobby number..
var lobbyByLevel = {
    0:[1,1],
    1:[100,1],
    2:[200,1],
    3:[300,1],
    4:[400,1],
    5:[500,1]
}

//displays simple message on the web browser
app.get("/", (req,res) => res.send("helloWorld"));

//the server would listen to request made to the server at the following URL https://arcane-taiga-94757.herokuapp.com/
server.listen(process.env.PORT ||port,() => console.log("We have done it"));

//when a socket connects to the server
io.on('connection', function(socket){
    console.log("Player Connected!");
    socket.emit('socketID', {id: socket.id});
    //when a readyTostart event is received from one client with some data it broadcast the data to all other clients in the server except the client itself.
    socket.on('readyTostart',function(data){
        socket.broadcast.emit('readyTostart',data);
    });
    
    //when a findLobbyByLevel event is received with some data it emits a new player event with data about the lobby number that has been assigned to the client with 
    //the position of the client in the lobby.
    socket.on('findLobbyByLevel',function(data){
        //lobbyNum access a free lobby in the lobbyByLevel associative array based on the level thge client specified obtrained from the data by ehich the client made the request.
        var lobbyNum=lobbyByLevel[data.level][0];
        //numPlayersInLobby access nbumber of players in the lobby prsent in the found lobbyNum
        var numPlayersInLobby = lobbyByLevel[data.level][1];
        var justStarted=true;
        //loops through all the list of players stored in the players list. 
        for (var i=0; i< players.length;i++){
            //check for the player with same id as the client in the playes list
            if (players[i].id == socket.id){
                justStarted=false;
                //updates details of the client in the players list.
                players[i].lobby=lobbyNum;
                players[i].numPlayer=numPlayersInLobby;
                players[i].level=data.level;
            }
        }
        //if the client wasn't found in the loop we create a new player object and add it to the players list.
        if (justStarted){
            players.push(new player(socket.id,0,0,lobbyNum,numPlayersInLobby,data.level));
        }
        
        //increment the number of players in the lobbyByLevel associative array, if there are two players in that lobby number increment the lobby number and reset number of players to 1 
        if (numPlayersInLobby<2){
            lobbyByLevel[data.level][1]++;
        }else{
            lobbyByLevel[data.level][0]++;
            lobbyByLevel[data.level][1]=1;
    
        }
        //emits a newPlayer event with the lobby number and position in the lobby that has been assigned to the client.
        socket.emit('newPlayer', {playersInLobby: numPlayersInLobby, lobby:lobbyNum});
    });
    
    //when a gameEnded event is received from a client with some data is broadcated to all other clients in the server except the client itself.
    socket.on('gameEnded',function(data){
        socket.broadcast.emit('gameEnded',data);
    });
    
    //when a playerCompletedLevel event is received from a client with some data is broadcated to all other clients in the server except the client itself.
    socket.on('playerCompletedLevel',function(data){
        socket.broadcast.emit('playerCompletedLevel',data);
    });

    //when a playerMoved event is received from a client with some data is broadcated to all other clients in the server except the client itself.
    //and it updates the x and y vposition of the client on the players array.
    socket.on('playerMoved', function(data){
        data.id=socket.id
        socket.broadcast.emit('playerMoved', data);
        //loops through the list of players
        for (var i=0; i<players.length;i++){

           // when it founds the player with same ID as the client in the players list it update that player x and y positions.
            if (players[i].id==data.id){
                players[i].x=data.x;
                players[i].y=data.y;
            }
        }
    });

    //when a disconnect event is received from a client another event playerDisconeccted is broadcasted to all other clients in the server except the client itself.
    //it also deletes the players instances stored in the players list and updates lobbyByLevel if the client was the firt player in the latest assigned lobby.
    socket.on('disconnect',function(){
        console.log("Player Disconnected!");
        //socket.broadcast.emit('playerDisconeccted',{id: socket.id});
        var lobbyToEliminate=0;
        var newLobbytoJoin=0;
        //loops through the list of players
        for (var i=0; i< players.length;i++){
            //finds player with same id as the client.
            if (players[i].id == socket.id){
                //find the lobby that was assigned to the client
                lobbyToEliminate=players[i].lobby;
                //find the latest lobby assigned for the level the player was playing
                newLobbytoJoin=lobbyByLevel[players[i].level][0];
                //if the the client was player 1 in the lobby and was in the latest lobby that was assigned increment the free lobby in the lobbyByLevel associative array 
                //so that the next player does.t get added  to this lobby where the first player has disconeected.
                if (players[i].numPlayer==1 && newLobbytoJoin==lobbyToEliminate){
                    lobbyByLevel[players[i].level][0]++;
                    lobbyByLevel[players[i].level][1]=1;
                }
                //deletes the record held for that particular player.
                players.splice(i,1);
            }
        }
        //playerDisconeccted event is broadcasted to all other clients in the server except the client itself
        socket.broadcast.emit('playerDisconeccted',{oldLobby: lobbyToEliminate});

    });

    socket.on('leaveLevel',function(){
        console.log("Player Disconnected!");
        var lobbyToEliminate=0;
        var newLobbytoJoin=0;
        //loops through the list of players
        for (var i=0; i< players.length;i++){
            //finds player with same id as the client.
            if (players[i].id == socket.id){
                //find the lobby that was assigned to the client
                lobbyToEliminate=players[i].lobby;
                 //find the latest lobby assigned for the level the player was playing
                newLobbytoJoin=lobbyByLevel[players[i].level][0];
                //if the the client was player 1 in the lobby and was in the latest lobby that was assigned increment the free lobby in the lobbyByLevel associative array 
                //so that the next player does.t get added  to this lobby where the first player has disconeected.
                if (players[i].numPlayer==1 && newLobbytoJoin==lobbyToEliminate){
                    lobbyByLevel[players[i].level][0]++;
                    lobbyByLevel[players[i].level][1]=1;
                }
                //deletes the record held for that particular player.
                players.splice(i,1);
            }
            
        }
        //playerDisconeccted event is broadcasted to all other clients in the server except the client itself
        socket.broadcast.emit('playerDisconeccted',{oldLobby: lobbyToEliminate});

    });
});



//A player instance that is composed of the plyer id, x and y positions, lobby number, the number of the player in the lobby and the level the player is playing.

function player(id,x,y,lobby,numPlayer,level){
    this.id=id;
    this.x=x;
    this.y=y;
    this.lobby=lobby;
    this.numPlayer=numPlayer;
    this.level=level;
}