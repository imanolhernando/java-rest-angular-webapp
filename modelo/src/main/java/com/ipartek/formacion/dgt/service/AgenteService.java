package com.ipartek.formacion.dgt.service;

import java.util.List;

import com.ipartek.formacion.dgt.pojos.Multa;

public interface AgenteService {
	
	List<Multa> listarMultasAgente(int idAgente);

}
