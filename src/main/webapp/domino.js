var self;

function ViewModel() {
	self = this;
	self.usuarios = ko.observableArray([]);
	self.mensaje = ko.observable("");
	self.fichasJugador = ko.observableArray([]);
	self.fichasRival = ko.observableArray([]);
	self.mesa = ko.observableArray([]);
	self.taco = ko.observable("");
	self.pasarTurno = ko.observable("");
	
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
			var fichaJugador = data.startData.data;
			// Dibuja las 7 fichas iniciales
			for (var i = 0; i < fichaJugador.length; i++) {
				var ficha = fichaJugador[i];
				self.fichasJugador.push(ficha);
			}
			// Fichas del rival
			for (var i = 0; i < fichaJugador.length; i++) {
				var ficha = fichaJugador[i];
				self.fichasRival.push(" ");
			}
			self.taco("Robar");
			self.pasarTurno("Pasar turno");
		}
	}
}

var vm = new ViewModel();
ko.applyBindings(vm);