package com.projetos.cobranca.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projetos.cobranca.model.StatusTitulo;
import com.projetos.cobranca.model.Titulo;
import com.projetos.cobranca.repository.Titulos;
import com.projetos.cobranca.repository.filter.TituloFilter;
import com.projetos.cobranca.services.CadastroTituloService;

import javassist.expr.NewArray;

@Controller
@RequestMapping("/titulos")
public class TituloController {
	
	@Autowired // para conseguir salvar e mandar para dentro deste objeto o titulo, tem de usar este comando
	private Titulos titulosRepos;
	
	@Autowired
	private CadastroTituloService cadastroTituloService;
	
	private static final String CADASTRO_VIEW = "cadastrotitulo";
	
	@RequestMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		
		mv.addObject(new Titulo());
		return mv;
	}

	
	@RequestMapping
	public ModelAndView Pesquisar(@ModelAttribute("filtro") TituloFilter filtro) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		List<Titulo> todosTitulos = titulosRepos.findByDescricaoContaining(descricao);
		ModelAndView mv = new ModelAndView("PesquisaTitulos");
		mv.addObject("titulos", todosTitulos);
		mv.addObject("filtro", descricao);
		return mv;
	}
	
	@RequestMapping("/{codigo}")
	public ModelAndView edicao(@PathVariable long codigo) {
		Titulo titulosEdit = titulosRepos.findById(codigo).get();
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(titulosEdit);
		return mv;
	}
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes) {
		//salvar no banco de dados
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW); // ta no nome, eu consigo mandar objetos ao mesmo tempo
		if(errors.hasErrors()) {
			return mv;
		}
		try {
			cadastroTituloService.salvar(titulo);
			ModelAndView mv2 = new ModelAndView("redirect:/titulos/novo");
			attributes.addFlashAttribute("mensagem", "Titulo salvo com sucesso");
			return mv2;
		} catch (IllegalArgumentException e) {
			errors.rejectValue("dataVencimento", null, e.getMessage());
			ModelAndView mv2 = new ModelAndView(CADASTRO_VIEW);
			return mv2;
		}
	}
	
	@RequestMapping(value = "/{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String Receber(@PathVariable Long codigo) {
		return cadastroTituloService.receber(codigo);
	}
	
	@RequestMapping("/excluir/{codigo}")
	public String excluir(@PathVariable long codigo) {
		cadastroTituloService.excluir(codigo);
		
		return "redirect:/titulos";
	}
	
	
	@ModelAttribute("todosStatusTitulo") //deixar um atributo disponivel para todas as paginas
	public List<StatusTitulo> todosStatusTitulo() {
		return Arrays.asList(StatusTitulo.values());
	}
}















