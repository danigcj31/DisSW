var self;

function ViewModel() {
	self = this;
	self.usuarios = ko.observableArray([]);
	self.tableroArray = ko.observableArray([]);
	self.mensaje = ko.observable("");
	self.simbolo = ko.observable("");
	
	var idMatch = sessionStorage.idMatch;
	var started = JSON.parse(sessionStorage.started);

	

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
			self.mensaje(data.jugadorConElTurno + " tiene el turno. ");
			
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
				var ficha = new Ficha(tablero[i], i);
				self.tableroArray.push(ficha);
			}
		
		} else if (data.type == "actualizacionTablero"){	// ACTUALIZA EL
															// TABLERO
			var tablero = data.tablero;
			for (var i = 0; i < tablero.length; i++) {
				var ficha = new Ficha(tablero[i], i);
				self.tableroArray.replace(self.tableroArray()[i],ficha);
			}
			if(!data.ganador == ""){
				self.mensaje("Ha ganado " + data.ganador);
			} else if(data.empate == "T")
				self.mensaje("EMPATE");
			else
				self.mensaje(data.jugadorConElTurno + " tiene el turno.");
				
				
			
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
			type : "movimientoT",
			idMatch : sessionStorage.idMatch,
			ficha: ""+posicion
					
		};
		
		self.sws.send(JSON.stringify(msg));
	}
}
var vm = new ViewModel();
ko.applyBindings(vm);