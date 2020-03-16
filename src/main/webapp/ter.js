function ViewModel() {
	var self = this;
	self.usuarios = ko.observableArray([]);
	self.tableroArray = ko.observableArray([]);
	self.turno = ko.observable(false);
	self.fichaX = ko.observable("");
	self.fichaO = ko.observable("");

	var idMatch = sessionStorage.idMatch;
	var started = JSON.parse(sessionStorage.started);

	self.mensaje = ko.observable("");
	self.simbolo = ko.observable("");

	if (started) {
		self.mensaje("La partida " + idMatch + " ha comenzado");
	} else {
		self.mensaje("Esperando oponente para la partida " + idMatch);
	}

	var url = "ws://"+window.location.host+"/juegos";
	var sws = new WebSocket(url); // NO RECONOCE LA CLASE SpringWebSocket.
	// Preguntar a Macario

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
			for (var i = 0; i < players.length; i++) {
				var player = players[i];
				self.usuarios.push(player.userName);
			}
			console.log(data);

			// Dibuja el tablero
			var tablero = data.startData.tablero;

			for (var i = 0; i < tablero.length; i++) {
				self.tableroArray.push(tablero[i]);
			}
	

		}

	}
	self.colocarFicha = function(ficha) {
		var posicion = self.tableroArray.indexOf(ficha);	
		var msg = {
			type : "movimiento",
			idMatch : sessionStorage.idMatch,
			ficha: ""+posicion
					
		};
		sws.send(JSON.stringify(msg));


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

var vm = new ViewModel();
ko.applyBindings(vm);