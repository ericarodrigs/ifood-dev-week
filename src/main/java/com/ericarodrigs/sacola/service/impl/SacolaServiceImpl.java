package com.ericarodrigs.sacola.service.impl;

import com.ericarodrigs.sacola.enumeration.FormaPagamento;
import com.ericarodrigs.sacola.model.Item;
import com.ericarodrigs.sacola.model.Restaurante;
import com.ericarodrigs.sacola.model.Sacola;
import com.ericarodrigs.sacola.repository.ItemRepository;
import com.ericarodrigs.sacola.repository.ProdutoRepository;
import com.ericarodrigs.sacola.repository.SacolaRepository;
import com.ericarodrigs.sacola.resource.dto.ItemDto;
import com.ericarodrigs.sacola.service.SacolaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SacolaServiceImpl implements SacolaService {
    private final SacolaRepository sacolaRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemRepository itemRepository;

    @Override
    public Item incluirItemNaSacola(ItemDto itemDto) {
        Sacola sacola = verSacola(itemDto.getIdSacola());

        if(sacola.isFechada()){
            throw new RuntimeException("Esta sacola está fechada!");
        }

        Item itemParaInserir = Item.builder().quantidade(itemDto.getQuantidade())
                .sacola(sacola)
                .produto(produtoRepository.findById(itemDto.getProdutoId()).orElseThrow(
                        () -> {
                            throw new RuntimeException("Esse produto não existe!");
                        }
                ))
                .build();

        List<Item> itensDaSacola = sacola.getItens();
        if (itensDaSacola.isEmpty()) {
            itensDaSacola.add(itemParaInserir);
        } else {
            Restaurante restauranteAtual = itensDaSacola.get(0).getProduto().getRestaurante();
            Restaurante restauranteDoItemParaInserir = itemParaInserir.getProduto().getRestaurante();
            if(restauranteAtual.equals(restauranteDoItemParaInserir)){
                itensDaSacola.add(itemParaInserir);
            } else {
                throw new RuntimeException("Não é possível adicionar itens de restaurantes distintos!");
            }
        }

        sacolaRepository.save(sacola);
        return itemRepository.save(itemParaInserir);
    }

    @Override
    public Sacola verSacola(Long id) {
        return sacolaRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Essa sacola não existe!");
                }
        );
    }

    @Override
    public Sacola fecharSacola(Long id, int numeroFormaPagamento) {
        Sacola sacola = verSacola(id);

        if(sacola.getItens().isEmpty()){
            throw new RuntimeException("Inclua itens na sacola!");
        }

        FormaPagamento formaPagamento = numeroFormaPagamento == 0 ? FormaPagamento.DINHEIRO : FormaPagamento.MAQUINETA;

        sacola.setFormaPagamento(formaPagamento);
        sacola.setFechada(true);
        return sacolaRepository.save(sacola);
    }
}
