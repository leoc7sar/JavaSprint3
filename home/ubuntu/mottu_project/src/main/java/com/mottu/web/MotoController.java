package com.mottu.web;

import com.mottu.dominio.dto.MotoDTO;
import com.mottu.servico.MotoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/admin/motos")
public class MotoController {
  private final MotoService motoService;

  public MotoController(MotoService motoService){ this.motoService = motoService; }

  @GetMapping
  public String listar(Model m){ m.addAttribute("itens", motoService.findAll()); return "moto/lista"; }

  @GetMapping("/nova")
  public String form(Model m){ 
    MotoDTO motoDTO = new MotoDTO();
    motoDTO.setValorDiaria(0.0); // Valor inicial para evitar NullPointerException no formulário
    m.addAttribute("motoDTO", motoDTO); 
    return "moto/form"; 
  }

  @PostMapping
  public String criar(@Valid @ModelAttribute("motoDTO") MotoDTO motoDTO, BindingResult br, RedirectAttributes ra){
    if(br.hasErrors()) return "moto/form";
    motoService.save(motoDTO);
    ra.addFlashAttribute("mensagemSucesso", "Moto cadastrada com sucesso!");
    return "redirect:/admin/motos";
  }

  @GetMapping("/{id}/editar")
  public String editar(@PathVariable Long id, Model m){
    MotoDTO motoDTO = motoService.findById(id).orElseThrow(() -> new IllegalArgumentException("Moto inválida:" + id));
    m.addAttribute("motoDTO", motoDTO);
    return "moto/form";
  }

  @PostMapping("/{id}")
  public String atualizar(@PathVariable Long id, @Valid @ModelAttribute("motoDTO") MotoDTO motoDTO, BindingResult br, RedirectAttributes ra){
    if(br.hasErrors()) return "moto/form";
    motoDTO.setId(id); // Garante que o ID correto seja usado para atualização
    motoService.save(motoDTO);
    ra.addFlashAttribute("mensagemSucesso", "Moto atualizada com sucesso!");
    return "redirect:/admin/motos";
  }

  @PostMapping("/{id}/excluir")
  public String excluir(@PathVariable Long id, RedirectAttributes ra){
    motoService.deleteById(id);
    ra.addFlashAttribute("mensagemSucesso", "Moto excluída com sucesso!");
    return "redirect:/admin/motos";
  }
}

