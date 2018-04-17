package com.eclodir.pontointeligente.api.services;

import java.util.Optional;

import com.eclodir.pontointeligente.api.entities.Funcionario;

public interface FuncionarioService {
	
	/**
	 * Persistir um funcionário na base de dados.
	 * @param funcionario
	 * @return Optional<Funcionario>
	 */
	Funcionario persistir(Funcionario funcionario);
	
	/**
	 * Busca e retorna um funcinário dado um CPF.
	 * @param cpf
	 * @return Optional<Funcionario>
	 */
	Optional <Funcionario> buscarPorCpf(String cpf);
	
	
	/**
	 * Busca e retorna um funcionario dado um email.
	 * @param email
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorEmail(String email);
	
	
	/**
	 * Busca e retorna um funcionario dado um Id.
	 * @param id
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorId(Long id);
	

}
