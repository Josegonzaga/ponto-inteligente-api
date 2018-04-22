package com.eclodir.pontointeligente.api.conrollers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eclodir.pontointeligente.api.dtos.CadastroPFDto;
import com.eclodir.pontointeligente.api.entities.Empresa;
import com.eclodir.pontointeligente.api.entities.Funcionario;
import com.eclodir.pontointeligente.api.enums.PerfilEnum;
import com.eclodir.pontointeligente.api.response.Response;
import com.eclodir.pontointeligente.api.services.EmpresaService;
import com.eclodir.pontointeligente.api.services.FuncionarioService;
import com.eclodir.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class CadastroPFController {
	
	private static Logger log = LoggerFactory.getLogger(CadastroPFController.class);
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private FuncionarioService funcionarioService;

	public CadastroPFController() {}
	
	public ResponseEntity<Response<CadastroPFDto>> cadastrar (CadastroPFDto cadastroPFDto, BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Cadastro de PF: {}" , cadastroPFDto.toString());
		
		Response<CadastroPFDto> response = new Response<CadastroPFDto>();
		
		validarDadosExistentes (cadastroPFDto, result);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPFDto, result);
		
		if (result.hasErrors()) {
			log.error("Erro validando dados de cadastro PF: {}", result.getAllErrors());
			return ResponseEntity.badRequest().body(response);
		}
		
		Optional<Empresa> empresa = this.empresaService.buscaPorCnpj(cadastroPFDto.getCnpj());
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		this.funcionarioService.persistir(funcionario);
		response.setData(this.converterCadastroPFDto(funcionario));
		return ResponseEntity.ok(response);
	}
	
	
	private void validarDadosExistentes(CadastroPFDto cadastroPFDto, BindingResult result) {
		Optional<Empresa> empresa = this.empresaService.buscaPorCnpj(cadastroPFDto.getCnpj());
		if (!empresa.isPresent()) {
			result.addError(new ObjectError("empresa", "empresa não encontrada"));
		}
		
		this.funcionarioService.buscarPorCpf(cadastroPFDto.getCpf()).ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente")));
		this.funcionarioService.buscarPorEmail(cadastroPFDto.getEmail()).ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente")));
		
	}
	
	private Funcionario converterDtoParaFuncionario(CadastroPFDto cadastroPFDto, BindingResult result) throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPFDto.getNome());
		funcionario.setEmail(cadastroPFDto.getEmail());
		funcionario.setCpf(cadastroPFDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPFDto.getSenha()));
		
		cadastroPFDto.getQtdHorasAlmoco().ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		cadastroPFDto.getQtdHorasTrabalhoDia().ifPresent(qtdHorasTrabalhoDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhoDia)));
		cadastroPFDto.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));
		return funcionario;
	}
	
	private CadastroPFDto converterCadastroPFDto(Funcionario funcionario) {
		CadastroPFDto cadastroPFDto = new CadastroPFDto();
		cadastroPFDto.setNome(funcionario.getNome());
		cadastroPFDto.setEmail(funcionario.getEmail());
		cadastroPFDto.setId(funcionario.getId());
		cadastroPFDto.setCpf(funcionario.getCpf());
		cadastroPFDto.setCnpj(funcionario.getEmpresa().getCnpj());
		funcionario.getValorHoraOpt().ifPresent(valorHora -> cadastroPFDto.setValorHora(Optional.of(valorHora.toString())));
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(qtdHorasTrabalhoDia -> cadastroPFDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabalhoDia))));
		funcionario.getQtdHorasAlmocoOpt().ifPresent(qtdHorasAlmoco -> cadastroPFDto.setQtdHorasAlmoco(Optional.of(qtdHorasAlmoco.toString())));
		
		return cadastroPFDto;
	}
	
}
