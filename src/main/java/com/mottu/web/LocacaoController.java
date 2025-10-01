package com.mottu.web;

import com.mottu.dominio.Pagamento;
import com.mottu.dominio.dto.LocacaoDTO;
import com.mottu.dominio.dto.PagamentoDTO;
import com.mottu.servico.EntregadorService;
import com.mottu.servico.MotoService;
import com.mottu.servico.ServicoLocacao;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller @RequestMapping("/locacoes")
public class LocacaoController {
  private final ServicoLocacao servicoLocacao;
  private final EntregadorService entregadorService;
  private final MotoService motoService;

  public LocacaoController(ServicoLocacao servicoLocacao, EntregadorService entregadorService, MotoService motoService){
    this.servicoLocacao = servicoLocacao;
    this.entregadorService = entregadorService;
    this.motoService = motoService;
  }

  @GetMapping
  public String listar(Model m){ m.addAttribute("itens", servicoLocacao.findAll()); return "locacao/lista"; }

  @GetMapping("/abrir")
  public String formAbrir(Model m){
    m.addAttribute("locacaoDTO", new LocacaoDTO());
    m.addAttribute("entregadores", entregadorService.findAll());
    m.addAttribute("motos", motoService.findAll()); // Buscar todas as motos, o serviço de locação verificará a disponibilidade
    return "locacao/abrir";
  }

  @PostMapping("/abrir")
  public String abrir(@Valid @ModelAttribute("locacaoDTO") LocacaoDTO locacaoDTO, BindingResult br, RedirectAttributes ra){
    if(br.hasErrors()) {
      ra.addFlashAttribute("org.springframework.validation.BindingResult.locacaoDTO", br);
      ra.addFlashAttribute("locacaoDTO", locacaoDTO);
      return "redirect:/locacoes/abrir";
    }
    try {
      servicoLocacao.abrirLocacao(locacaoDTO);
      ra.addFlashAttribute("mensagemSucesso", "Locação aberta com sucesso!");
    } catch (IllegalStateException | IllegalArgumentException e) {
      ra.addFlashAttribute("mensagemErro", e.getMessage());
      return "redirect:/locacoes/abrir";
    }
    return "redirect:/locacoes";
  }

  @GetMapping("/{id}/fechar")
  public String formFechar(@PathVariable Long id, Model m){
    LocacaoDTO locacaoDTO = servicoLocacao.findById(id).orElseThrow(() -> new IllegalArgumentException("Locação inválida:" + id));
    m.addAttribute("locacaoDTO", locacaoDTO);
    m.addAttribute("pagamentoDTO", new PagamentoDTO());
    m.addAttribute("metodosPagamento", Pagamento.Metodo.values());
    return "locacao/fechar";
  }

  @PostMapping("/{id}/fechar")
  public String fechar(@PathVariable Long id,
                       @Valid @ModelAttribute("pagamentoDTO") PagamentoDTO pagamentoDTO, BindingResult br, RedirectAttributes ra){
    if(br.hasErrors()) {
      ra.addFlashAttribute("org.springframework.validation.BindingResult.pagamentoDTO", br);
      ra.addFlashAttribute("pagamentoDTO", pagamentoDTO);
      return "redirect:/locacoes/" + id + "/fechar";
    }
    try {
      // Convertendo o método de pagamento de String para Pagamento.Metodo
      Pagamento.Metodo metodo = Pagamento.Metodo.valueOf(pagamentoDTO.getMetodo());
      servicoLocacao.fecharLocacaoEPagar(id, pagamentoDTO.getDataPagamento(), metodo);
      ra.addFlashAttribute("mensagemSucesso", "Locação fechada e pagamento registrado com sucesso!");
    } catch (IllegalStateException | IllegalArgumentException e) {
      ra.addFlashAttribute("mensagemErro", e.getMessage());
      return "redirect:/locacoes/" + id + "/fechar";
    }
    return "redirect:/pagamentos";
  }
}

