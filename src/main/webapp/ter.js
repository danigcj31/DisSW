function ViewModel() {
	var self = this;
	self.usuarios = ko.observableArray([]);
	self.tablero = ko.observableArray([]);
	self.turno = ko.observable(false);
	
	var idMatch = sessionStorage.idMatch;
	var started = JSON.parse(sessionStorage.started);
	
	self.mensaje = ko.observable("");
	
	if (started) {
		self.mensaje("La partida " + idMatch + " ha comenzado");
	} else {
		self.mensaje("Esperando oponente para la partida " + idMatch);
	}
	
	var url = "ws://localhost:8800/juegos";
	var sws = new WebSocket(url);	//NO RECONOCE LA CLASE SpringWebSocket. Preguntar a Macario

	sws.onopen = function(event) {
		var msg = {
			type : "ready",
			idMatch : sessionStorage.idMatch
		};
		sws.send(JSON.stringify(msg));
	}

	sws.onmessage = function(event) {
		var data = event.data;
		data = JSON.parse(data);
		
		if (data.type == "matchStarted") {
			self.mensaje("La partida ha empezado");
			var players = data.players;
			// Mete a los usuarios en la partida
			for (var i=0; i<players.length; i++) {
				var player = players[i];
				self.usuarios.push(player.userName);
			}
			
			// Dibuja el tablero
			var tablero = data.startData.table;
			for (var i=0; i<tablero.length; i++) {
				var ficha = tablero[i];
				console.log(data)
				self.tablero.push(ficha.simbolo);
			}
		}
	}
/*	
	self.funcion = function() {
		var players = data.players;
		for (var i=0; i<players.length; i++) {
			var player1 = players[0];
			var player2 = players[1];
		}
		sortearInicio(1,2); //return player N que empieza
		Syso("Empieza Player N");
	}
	*/
	
	self.sortearJugador = function () {
		var players = data.players;
		
	}
	
	self.colocarFicha = function() {
			self.tablero('X');
	}
	self.colocarFicha2 = function() {
		self.ficha2('X');
	}
	self.colocarFicha3 = function() {
		self.ficha3('O');
	}
	self.colocarFicha4 = function() {
		self.ficha4('X');
	}	
	self.colocarFicha5 = function() {
		self.ficha5('X');
	}
	self.colocarFicha6 = function() {
		self.ficha6('X');
	}
	self.colocarFicha7 = function() {
		self.ficha7('X');
	}	
	self.colocarFicha8 = function() {
		self.ficha8('X');
	}
	self.colocarFicha9 = function() {
		self.ficha9('X');
	}
		
}

var vm = new ViewModel();
ko.applyBindings(vm);