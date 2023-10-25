package br.com.waldston.TabelaFipe.main;

import br.com.waldston.TabelaFipe.models.Dados;
import br.com.waldston.TabelaFipe.models.Modelos;
import br.com.waldston.TabelaFipe.models.Veiculo;
import br.com.waldston.TabelaFipe.services.ConsumoApi;
import br.com.waldston.TabelaFipe.services.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {
        var menu = """
                *** OPÇÕES ***
                Carro
                Moto
                Caminhão
                
                Digite uma das opções para consultar valores:
                
                """;

        System.out.println(menu);
        var opcao = scanner.nextLine();

        String endereco;

        if (opcao.toLowerCase().contains("carr")) {
            endereco = URL_BASE + "carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas";
        } else {
            endereco = URL_BASE + "caminhoes/marcas";
        }

        var json = consumo.obterDados(endereco);

        System.out.println(endereco);


        var marcas = conversor.obterLists(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca para consulta");
        var marcaVeiculo = scanner.nextLine();

        endereco = endereco + "/" + marcaVeiculo + "/modelos";
        System.out.println(endereco);

        json = consumo.obterDados(endereco);
        var modeloLista = conversor.obterDados(json, Modelos.class);

        System.out.println("\nModelos dessa marca ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\n Digite o nome do veículo a ser buscado");
        var nomeVeiculo = scanner.nextLine();
        
        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\n Modelos Filtrados");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite por favor o código do modelo desejado");
        var modeloDesejado = scanner.nextLine();
        
        endereco = endereco + "/" + modeloDesejado + "/anos";
        json = consumo.obterDados(endereco);
        List<Dados> anos = conversor.obterLists(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);

            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);

        }

        System.out.println("\nTodos os veículos filtrados com avaliações por ano ");
        veiculos.forEach(System.out::println);


        

    }
}
