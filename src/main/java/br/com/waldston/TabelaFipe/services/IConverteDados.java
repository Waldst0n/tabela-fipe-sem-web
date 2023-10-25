package br.com.waldston.TabelaFipe.services;

import java.util.List;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);

    <T> List<T> obterLists(String json, Class<T> classe);
}
