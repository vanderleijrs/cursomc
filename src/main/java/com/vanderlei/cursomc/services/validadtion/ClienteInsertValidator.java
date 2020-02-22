package com.vanderlei.cursomc.services.validadtion;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.vanderlei.cursomc.domain.Cliente;
import com.vanderlei.cursomc.domain.enums.TipoCliente;
import com.vanderlei.cursomc.dto.ClienteNewDTO;
import com.vanderlei.cursomc.repositories.ClienteRepository;
import com.vanderlei.cursomc.resources.exception.FieldMessage;
import com.vanderlei.cursomc.services.validadtion.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO>{

	@Override
	public void initialize(ClienteInsert ann) {
	}
	@Autowired
	private ClienteRepository repo;
	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage>list = new ArrayList<>();
		if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCpfouCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj","CPF inválido"));
		}
		if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfouCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj","CNPJ inválido"));
		}
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if(aux!=null) {
			list.add(new FieldMessage("email", "Email já existente"));
		}
		for(FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		return list.isEmpty();
	}
	
}
