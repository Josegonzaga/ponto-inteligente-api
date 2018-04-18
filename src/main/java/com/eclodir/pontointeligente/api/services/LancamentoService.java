package com.eclodir.pontointeligente.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.eclodir.pontointeligente.api.entities.Lancamento;

public interface LancamentoService {
	
	/**
	 * Busca um lançamento dado o id de um funcinario
	 * @param funcionarioId
	 * @param pageRequest
	 * @return Page<lancamento>
	 */
	Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest);
	
	
	/**
	 * Busca um lançamento dao o id
	 * @param id
	 * @return Optional<Lancamento>
	 */
	Optional<Lancamento> buscaporId(Long id);
	
	/**
	 * Persiste um lançamento na base de dados.
	 * @param lancamento
	 * @return Lancamento
	 */
	Lancamento persistir(Lancamento lancamento);
	
	/**
	 * Remove um lancamento dado um id
	 * @param id
	 */
	void remover(Long id);
	
}
