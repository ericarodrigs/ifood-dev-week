package com.ericarodrigs.sacola.service;

import com.ericarodrigs.sacola.model.Item;
import com.ericarodrigs.sacola.model.Sacola;
import com.ericarodrigs.sacola.resource.dto.ItemDto;

public interface SacolaService {
    Item incluirItemNaSacola(ItemDto itemDto);
    Sacola verSacola(Long id);
    Sacola fecharSacola(Long id, int formaPagamento);
}
