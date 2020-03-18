var self;

function ViewModel() {
	self = this;
	self.usuarios = ko.observableArray([]);
	self.tableroArray = ko.observableArray([]);
	self.turno = ko.observable(false);
	self.fichaX = ko.observable("");
	self.fichaO = ko.observable("");
	self.jugadorTurno = ko.observable("");

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
			self.jugadorTurno(data.jugadorConElTurno + " tiene el turno. ");
			
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
			self.jugadorTurno(data.jugadorConElTurno + " tiene el turno.");
			var tablero = data.tablero;
			for (var i = 0; i < tablero.length; i++) {
				//self.tableroArray.push(tablero[i]);
				var ficha = new Ficha(tablero[i], i);
				self.tableroArray.replace(self.tableroArray()[i],ficha);
			}
			if(!data.ganador == ("")){
				self.mensajeGanador("Ha ganado " + data.ganador);
			}
			//replace cada posicion de cada valor que llega del JSONArray 
		} 
	}
	
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