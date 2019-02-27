package com.ipartek.formacion.dgt.api.controller;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ipartek.formacion.dgt.pojos.Agente;
import com.ipartek.formacion.dgt.pojos.Multa;
import com.ipartek.formacion.dgt.service.AgenteService;
import com.ipartek.formacion.dgt.service.impl.AgenteServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/api/agente/")
@Api(tags = { "AGENTE" }, produces = "application/json", description = "Gestión de agente y sus multas")
public class AgenteController {
	// LOG
	private final static Logger LOG = Logger.getLogger(AgenteController.class);

	private static AgenteService agenteService;

	public AgenteController() {
		super();
		agenteService = AgenteServiceImpl.getInstance();

	}

	@ApiResponses({ @ApiResponse(code = 200, message = "Listado"), 
			@ApiResponse(code = 500, message = "Error interno"),
			@ApiResponse(code = 404, message = "Datos no encontrados") })
	@RequestMapping(value = { "{id}/multa" }, method = RequestMethod.GET)
	public ResponseEntity<ArrayList<Multa>> listarMultasAgente(@PathVariable int id) {
		ResponseEntity<ArrayList<Multa>> response = new ResponseEntity<ArrayList<Multa>>(
				HttpStatus.INTERNAL_SERVER_ERROR);
		ArrayList<Multa> multas = new ArrayList<Multa>();
		try {
			multas = (ArrayList<Multa>) agenteService.obtenerMultas(id);
			if (multas.size() != 0) {
				response = new ResponseEntity<ArrayList<Multa>>(multas, HttpStatus.OK);
			} else {
				response = new ResponseEntity<ArrayList<Multa>>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.debug(e);
		}
		return response;
	}

//	{
//		"concepto": "concepto",
//		"importe": 0,
//		"coche": {
//			"id": 1
//		}
//	}
	
	@ApiResponses({ @ApiResponse(code = 201, message = "Creado"), 
			@ApiResponse(code = 500, message = "Error interno"),
			@ApiResponse(code = 409, message = "Conflicto"),
			@ApiResponse(code = 400, message = "Peticion incorrecta") })
	@RequestMapping(value = { "{idAgente}/multa" }, method = RequestMethod.POST)
	public ResponseEntity multar(@PathVariable int idAgente, @RequestBody Multa multa) {
		

		ResponseEntity response = new ResponseEntity(HttpStatus.BAD_REQUEST);
		
		
		try {
			long idVehiculoLong =  multa.getCoche().getId();
			int idVehiculo = (int)idVehiculoLong;
			
			multa = agenteService.multar(idVehiculo, idAgente, multa.getConcepto(), multa.getImporte());
			if (multa != null) {
				response = new ResponseEntity(multa, HttpStatus.CREATED);
			}else { response = new ResponseEntity(HttpStatus.CONFLICT);}

		} catch (Exception e) {
			LOG.debug(e);
			response = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
	
	
	
		@ApiResponses({ @ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 500, message = "Error interno"),
			@ApiResponse(code = 403, message = "Prohibido")})
		@RequestMapping(value = { "login/{placa}/{pass}" }, method = RequestMethod.GET)
		public ResponseEntity<Agente> loginAgente(@PathVariable String placa, @PathVariable String pass) {
			ResponseEntity<Agente> response = new ResponseEntity<Agente>(
					HttpStatus.INTERNAL_SERVER_ERROR);
			Agente a = new Agente();
			
			try {
				a = agenteService.existe(placa, pass);
				if (a != null) {
					response = new ResponseEntity<Agente>(a, HttpStatus.OK);
				} else {
					response = new ResponseEntity<Agente>(HttpStatus.FORBIDDEN);
				}
			} catch (Exception e) {
				LOG.debug(e);
			}
			return response;
		}

}