package com.vanderlei.cursomc.services.validadtion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.vanderlei.cursomc.domain.Cliente;
import com.vanderlei.cursomc.dto.ClienteDTO;
import com.vanderlei.cursomc.repositories.ClienteRepository;
import com.vanderlei.cursomc.resources.exception.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO>{
	
	@Autowired
	private HttpServletRequest request;
	@Override
	public void initialize(ClienteUpdate ann) {
	}
	@Autowired
	private ClienteRepository repo;
	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		@SuppressWarnings("unchecked")
		Map<String,String>map = (Map<String,String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));
		
		List<FieldMessage>list = new ArrayList<>();
		//Valida se o email já existe em outro cliente
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if(aux!=null && !aux.getId().equals(uriId)){
			list.add(new FieldMessage("email", "Email já existente"));
		}
		for(FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		return list.isEmpty();
	}
	
}
