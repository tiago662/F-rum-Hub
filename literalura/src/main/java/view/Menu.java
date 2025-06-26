package view;

import service.ConsumoApiService;
import service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Menu {

    @Autowired
    private LivroService livroService;

    @Autowired
    private ConsumoApiService consumoApiService;

    private Scanner teclado = new Scanner(System.in);

    public void exibirMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = teclado.nextInt();
            teclado.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivroPeloTitulo();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                // ... outros casos
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarLivroPeloTitulo() {
        System.out.println("Digite o nome do livro para busca:");
        var nomeLivro = teclado.nextLine();
        // Lógica para chamar o serviço, consumir API e salvar no banco
    }

    private void listarLivrosRegistrados() {
        // Lógica para buscar os livros no banco e exibir
    }

    // ... outros métodos para as demais opções
}
