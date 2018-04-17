package com.eclodir.pontointeligente.api.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eclodir.pontointeligente.api.entities.Empresa;
import com.eclodir.pontointeligente.api.repositories.EmpresaRepository;
import com.eclodir.pontointeligente.api.services.EmpresaService;

@Service
public class EmpresaServiceImpl implements EmpresaService{

	public static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);
	
	@Autowired
	EmpresaRepository empresaRepository;
	
	
	@Override
	public Optional<Empresa> buscaPorCnpj(String cnpj) {
		log.info("Busando uma empresa por CNPJ {}",cnpj);
		return Optional.ofNullable(empresaRepository.findByCnpj(cnpj));
	}

	@Override
	public Empresa persistir(Empresa empresa) {
		log.info("Persistindo emporesa {}, empresa");
		return this.empresaRepository.save(empresa);
	}

}
