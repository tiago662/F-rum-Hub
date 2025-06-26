package view;

import model.Autor;
import model.Livro;
import service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Component
public class Menu {

    @Autowired
    private LivroService livroService;

    private final Scanner teclado = new Scanner(System.in);

    public void exibirMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    \n*** LiterAlura - Catálogo de Livros ***
                    Escolha uma das opções abaixo:
                    1 - Buscar livro pelo título na web
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    
                    0 - Sair
                    """;

            System.out.println(menu);
            try {
                opcao = teclado.nextInt();
                teclado.nextLine(); // Consome a nova linha pendente

                switch (opcao) {
                    case 1:
                        buscarLivroPeloTitulo();
                        break;
                    case 2:
                        listarLivrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosPorAno();
                        break;
                    case 5:
                        listarLivrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Encerrando a aplicação LiterAlura...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Erro: Por favor, digite um número válido para a opção.");
                teclado.nextLine(); // Limpa o buffer do scanner para evitar loop infinito
            }
        }
    }

    private void buscarLivroPeloTitulo() {
        System.out.println("Digite o título do livro que deseja buscar:");
        var nomeLivro = teclado.nextLine();
        if (nomeLivro.isBlank()) {
            System.out.println("Título não pode ser vazio.");
            return;
        }
        livroService.buscarEsalvarLivroPeloTitulo(nomeLivro);
    }

    private void listarLivrosRegistrados() {
        System.out.println("\n--- Livros Registrados ---");
        List<Livro> livros = livroService.listarTodosOsLivros();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
        } else {
            livros.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        System.out.println("\n--- Autores Registrados ---");
        List<Autor> autores = livroService.listarTodosOsAutores();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor cadastrado.");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresVivosPorAno() {
        System.out.println("Digite o ano para pesquisar autores vivos:");
        try {
            int ano = teclado.nextInt();
            teclado.nextLine(); // Consome a nova linha
            List<Autor> autoresVivos = livroService.listarAutoresVivosEmAno(ano);
            if (autoresVivos.isEmpty()) {
                System.out.println("Nenhum autor vivo encontrado para o ano de " + ano + ".");
            } else {
                System.out.println("\n--- Autores Vivos em " + ano + " ---");
                autoresVivos.forEach(System.out::println);
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um ano válido (número).");
            teclado.nextLine(); // Limpa o buffer
        }
    }

    private void listarLivrosPorIdioma() {
        System.out.println("""
        Digite o código do idioma para a busca:
        es - espanhol
        en - inglês
        fr - francês
        pt - português
        """);
        String idioma = teclado.nextLine().toLowerCase();

        // Validação simples do input
        if (!idioma.matches("es|en|fr|pt")) {
            System.out.println("Código de idioma inválido. Tente novamente.");
            return;
        }

        List<Livro> livrosPorIdioma = livroService.listarLivrosPorIdioma(idioma);
        if (livrosPorIdioma.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma '" + idioma + "'.");
        } else {
            System.out.println("\n--- Livros no Idioma '" + idioma + "' ---");
            livrosPorIdioma.forEach(System.out::println);
        }
    }
}
