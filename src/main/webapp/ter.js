function ViewModel() {
	var self = this;
	self.usuarios = ko.observableArray([]);
	self.turno = ko.observable(false);
	self.fichas = ko.observableArray([]);
	
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
		}
	}
	/*
	self.ponerEnMesa = function(carta) {
		var msg = {
			type : "carta a la mesa",
			carta : carta
		}
		sws.send(JSON.stringify(msg));
	}*/
	
	self.colocarFicha = function(fichas) {
		// que hacer al pulsar? --> Escribir X o O
		// if User1 : Escribir X
		// if User2: Escribir O
	}
}

var vm = new ViewModel();
ko.applyBindings(vm);