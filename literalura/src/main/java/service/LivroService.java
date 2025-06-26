package service;


import dto.DadosBusca;
import dto.DadosLivro;
import model.Autor;
import model.Livro;
import repository.AutorRepository;
import repository.LivroRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final String ENDERECO_API = "https://gutendex.com/api/books/?search=";

    // Injeção de dependências dos repositórios e do serviço de consumo de API
    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private ConsumoApiService consumoApiService;

    // ObjectMapper para converter JSON em objetos Java
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void buscarEsalvarLivroPeloTitulo(String titulo) {
        // 1. Verifica se o livro já existe no banco para evitar duplicidade
        Optional<Livro> livroExistente = livroRepository.findByTituloContainingIgnoreCase(titulo);
        if (livroExistente.isPresent()) {
            System.out.println("\nLivro já cadastrado no banco de dados.");
            System.out.println(livroExistente.get());
            return;
        }

        // 2. Busca o livro na API Gutendex
        String enderecoBusca = ENDERECO_API + titulo.replace(" ", "+");
        String json = consumoApiService.obterDados(enderecoBusca);

        try {
            // 3. Converte o JSON da API para nossos objetos DTO
            service.DadosBusca dadosBusca = objectMapper.readValue(json, DadosBusca.class);

            // Pega o primeiro resultado da busca
            Optional<DadosLivro> dadosLivroOpt = dadosBusca.resultados().stream().findFirst();

            if (dadosLivroOpt.isPresent()) {
                DadosLivro dadosLivro = dadosLivroOpt.get();
                Livro livro = new Livro(dadosLivro);

                // 4. Verifica se o autor do livro já existe no banco
                if (!dadosLivro.autores().isEmpty()) {
                    String nomeAutor = dadosLivro.autores().get(0).nome();
                    Optional<Autor> autorExistente = autorRepository.findByNomeContainingIgnoreCase(nomeAutor);

                    Autor autor;
                    if (autorExistente.isPresent()) {
                        // Se o autor já existe, usa a referência dele
                        autor = autorExistente.get();
                    } else {
                        // Se não existe, cria um novo autor e salva
                        autor = new Autor(dadosLivro.autores().get(0));
                    }
                    livro.setAutor(autor);
                } else {
                    livro.setAutor(null);
                }

                // 5. Salva o livro (e o autor, se for novo, devido ao Cascade)
                livroRepository.save(livro);
                System.out.println("\nLivro salvo com sucesso!");
                System.out.println(livro);

            } else {
                System.out.println("\nNenhum livro encontrado com o título '" + titulo + "' na API.");
            }
        } catch (JsonProcessingException e) {
            System.out.println("Erro ao processar os dados da API: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Livro> listarTodosOsLivros() {
        return livroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Autor> listarTodosOsAutores() {
        return autorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Autor> listarAutoresVivosEmAno(int ano) {
        return autorRepository.findAutoresVivosEmAno(ano);
    }

    @Transactional(readOnly = true)
    public List<Livro> listarLivrosPorIdioma(String idioma) {
        return livroRepository.findByIdioma(idioma);
    }
}
