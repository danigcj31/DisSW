var self;

function ViewModel() {
	self = this;
	self.usuarios = ko.observableArray([]);
	self.tableroArray = ko.observableArray([]);
	self.turno = ko.observable(false);
	self.fichaX = ko.observable("");
	self.fichaO = ko.observable("");

	var idMatch = sessionStorage.idMatch;
	var started = JSON.parse(sessionStorage.started);

	self.mensaje = ko.observable("");
	self.mensajeGanador = ko.observable("");
	self.mensajePerdedor = ko.observable("");
	self.simbolo = ko.observable("");

	if (started) {
		self.mensaje("La partida " + idMatch + " ha comenzado");
	} else {
		self.mensaje("Esperando oponente para la partida " + idMatch);
	}

	var url = "ws://"+window.location.host+"/juegos";
	self.sws = new WebSocket(url); 

	self.sws.onopen = function(event) {
		var msg = {
			type : "ready",
			idMatch : sessionStorage.idMatch
		};
		self.sws.send(JSON.stringify(msg));
	}

	self.sws.onmessage = function(event) {
		var data = event.data;
		data = JSON.parse(data);

		if (data.type == "matchStarted") {
			self.mensaje("La partida ha empezado");
			var players = data.players;
			// Mete a los usuarios en la partida
			for (var i = 0; i < players.length; i++) {
				var player = players[i];
				self.usuarios.push(player.userName);
			}
			console.log(data);
			// Dibuja el tablero
			var tablero = data.startData.tablero;
			for (var i = 0; i < tablero.length; i++) {
				//self.tableroArray.push(tablero[i]);
				var ficha = new Ficha(tablero[i], i);
				self.tableroArray.push(ficha);
			}
		
		} else if (data.type == "actualizacionTablero"){
			
			//replace cada posicion de cada valor que llega del JSONArray 
		}
	}
	
	//compr en servidor
	self.getWinnerFilas = function () {
		//Filas gana X
		//Fila 1
		if (self.tableroArray()[0].simbolo == "X" && self.tableroArray()[1].simbolo == "X" && self.tableroArray()[2].simbolo == "X") {
			document.write("Ha ganado el jugador " + self.usuarios()[0]);
		}
		//Fila 2
		if (self.tableroArray()[3] == "X" && self.tableroArray()[4] == "X" && self.tableroArray()[5] == "X") {
			
		}
		//Fila 3
		if (self.tableroArray()[6] == "X" && self.tableroArray()[7] == "X" && self.tableroArray()[8] == "X") {
			
		}
		//Filas gana O
		//Fila 1
		if (self.tableroArray()[0] == "O" && self.tableroArray()[1] == "O" && self.tableroArray()[2] == "O") {
			
		}
		//Fila 2
		if (self.tableroArray()[3] == "O" && self.tableroArray()[4] == "O" && self.tableroArray()[5] == "O") {
			
		}
		//Fila 4
		if (self.tableroArray()[6] == "O" && self.tableroArray()[7] == "O" && self.tableroArray()[8] == "O") {
			
		}
	}
	
	self.getWinnerColumnas = function () {
		//Columnas gana X
		//Columna 1
		if (self.tableroArray()[0] == "X" && self.tableroArray()[3] == "X" && self.tableroArray()[6] == "X") {
			
		}
		//Columna 2
		if (self.tableroArray()[1] == "X" && self.tableroArray()[4] == "X" && self.tableroArray()[7] == "X") {
			
		}
		//Columna 3
		if (self.tableroArray()[2] == "X" && self.tableroArray()[5] == "X" && self.tableroArray()[8] == "X") {
			
		}
		//Columna gana 0
		//Columna 1
		if (self.tableroArray()[0] == "O" && self.tableroArray()[3] == "O" && self.tableroArray()[6] == "O") {
			
		}
		//Columna 2
		if (self.tableroArray()[1] == "O" && self.tableroArray()[4] == "O" && self.tableroArray()[7] == "O") {
			
		}
		//Columna 3
		if (self.tableroArray()[2] == "O" && self.tableroArray()[5] == "O" && self.tableroArray()[8] == "O") {
			
		}
	}
	
	self.getWinnerDiagonales = function () {
		//Diagonales gana X
		//Diagonal hacia abajo
		if (self.tableroArray()[0] == "X" && self.tableroArray()[4] == "X" && self.tableroArray()[8] == "X") {
			
		}
		//Diagonal hacia arriba
		if (self.tableroArray()[6] == "X" && self.tableroArray()[4] == "X" && self.tableroArray()[2] == "X") {
			
		}
		//Diagonales gana 0
		//Diagonal hacia abajo
		if (self.tableroArray()[0] == "O" && self.tableroArray()[4] == "O" && self.tableroArray()[8] == "O") {
			
		}
		//Diagonal hacia arriba
		if (self.tableroArray()[6] == "O" && self.tableroArray()[4] == "O" && self.tableroArray()[2] == "O") {
			
		}
	}


	
	
	
	
	
	/*
	 * self.funcion = function() { var players = data.players; for (var i=0; i<players.length;
	 * i++) { var player1 = players[0]; var player2 = players[1]; }
	 * sortearInicio(1,2); //return player N que empieza Syso("Empieza Player
	 * N"); }
	 */

	/*
	 * self.sortearJugador = function () { var players = data.players; }
	 * 
	 */
}

class Ficha {
	constructor (simbolo, index) {
		this.simbolo = simbolo;
		this.index = index;
	}
	colocarFicha () {
		var posicion = this.index;	
		var msg = {
			type : "movimiento",
			idMatch : sessionStorage.idMatch,
			ficha: ""+posicion
					
		};
		
		self.sws.send(JSON.stringify(msg));
	}
}
var vm = new ViewModel();
ko.applyBindings(vm);