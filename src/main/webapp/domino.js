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
	self.fichaSeleccionada = ko.observable();
	self.fichaDeMesa = ko.observable();
	self.botonesVisibles = ko.observable(false);
	
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
			var fichasJugador = data.startData.data;
			// Dibuja las 7 fichas iniciales
			for (var i = 0; i < fichasJugador.length; i++) {
				var ficha = fichasJugador[i];
				self.fichasJugador.push(new Ficha(ficha.numberL, ficha.numberR));
			}
			// Fichas del rival
			for (var i = 0; i < fichasJugador.length; i++) {
				var ficha = fichasJugador[i];
				self.fichasRival.push(" ");
			}
			self.taco("Robar");
            self.pasarTurno("Pasar turno")
		}else if (data.type == "actualizacionTablero"){	// ACTUALIZA EL
															// TABLERO
			var tablero = data.tablero;
			for (var i = 0; i < tablero.length; i++) {
				var ficha = new Ficha(tablero[i].numberLeft, tablero[i].numberRight);
				self.mesa.push(ficha);
				ficha.enMesa = true;
				for (var i=0; i<self.fichasJugador().length; i++) {
					if (self.fichasJugador()[i]==ficha) {
						self.fichasJugador.splice(i, 1);
						break;
					}
				}
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
	constructor(numberLeft, numberRight){
		this.numberLeft = numberLeft;
		this.numberRight = numberRight;
		this.enMesa = false;
	}
	
	seleccionarFicha() {
		if (self.mesa().length==0) {
				var p = {
						idMatch : sessionStorage.idMatch,
						type : "movimientoD",
						pongo : this,
						juntoA : this
					};
					self.sws.send(JSON.stringify(p));
							
		} else if (this.enMesa) {
			var p = {
					idMatch : sessionStorage.idMatch,
					type : "movimientoD",
					pongo : self.fichaSeleccionada(),
					juntoA : this
				};
				self.sws.send(JSON.stringify(p));
				
			for (var i=0; i<self.fichasJugador().length; i++) {
				if (self.fichasJugador()[i]==self.fichaSeleccionada()) {
					self.fichasJugador.splice(i, 1);
					break;
				}
				
			}
			self.mesa.push(self.fichaSeleccionada());
			self.fichaSeleccionada().enMesa = true;
			
		} else {
			self.fichaSeleccionada(this);
		}
	}
}

var vm = new ViewModel();
ko.applyBindings(vm);