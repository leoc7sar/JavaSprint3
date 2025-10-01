package com.mottu.web;

import com.mottu.servico.PagamentoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequestMapping("/pagamentos")
public class PagamentoController {
  private final PagamentoService pagamentoService;

  public PagamentoController(PagamentoService pagamentoService){ this.pagamentoService = pagamentoService; }

  @GetMapping
  public String listar(Model m){ m.addAttribute("itens", pagamentoService.findAll()); return "pagamento/lista"; }
}

