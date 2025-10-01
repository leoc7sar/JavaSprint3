package com.mottu.web;

import com.mottu.dominio.dto.EntregadorDTO;
import com.mottu.servico.EntregadorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/admin/entregadores")
public class EntregadorController {
  private final EntregadorService entregadorService;

  public EntregadorController(EntregadorService entregadorService){ this.entregadorService = entregadorService; }

  @GetMapping
  public String listar(Model m){ m.addAttribute("itens", entregadorService.findAll()); return "entregador/lista"; }

  @GetMapping("/novo")
  public String formNovo(Model m){ m.addAttribute("entregadorDTO", new EntregadorDTO()); return "entregador/form"; }

  @PostMapping
  public String criar(@Valid @ModelAttribute("entregadorDTO") EntregadorDTO entregadorDTO, BindingResult br, RedirectAttributes ra){
    if(br.hasErrors()) return "entregador/form";
    entregadorService.save(entregadorDTO);
    ra.addFlashAttribute("mensagemSucesso", "Entregador cadastrado com sucesso!");
    return "redirect:/admin/entregadores";
  }

  @GetMapping("/{id}/editar")
  public String editar(@PathVariable Long id, Model m){
    EntregadorDTO entregadorDTO = entregadorService.findById(id).orElseThrow(() -> new IllegalArgumentException("Entregador inválido:" + id));
    m.addAttribute("entregadorDTO", entregadorDTO);
    return "entregador/form";
  }

  @PostMapping("/{id}")
  public String atualizar(@PathVariable Long id, @Valid @ModelAttribute("entregadorDTO") EntregadorDTO entregadorDTO, BindingResult br, RedirectAttributes ra){
    if(br.hasErrors()) return "entregador/form";
    entregadorDTO.setId(id); // Garante que o ID correto seja usado para atualização
    entregadorService.save(entregadorDTO);
    ra.addFlashAttribute("mensagemSucesso", "Entregador atualizado com sucesso!");
    return "redirect:/admin/entregadores";
  }

  @PostMapping("/{id}/excluir")
  public String excluir(@PathVariable Long id, RedirectAttributes ra){
    entregadorService.deleteById(id);
    ra.addFlashAttribute("mensagemSucesso", "Entregador excluído com sucesso!");
    return "redirect:/admin/entregadores";
  }
}

