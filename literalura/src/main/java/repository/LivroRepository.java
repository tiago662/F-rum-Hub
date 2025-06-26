package repository;

import br.com.literatura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    Livro findByTituloContainingIgnoreCase(String titulo);
    List<Livro> findByIdioma(String idioma);
}
