package br.manuel.acervo.repositorio;

import br.manuel.acervo.entidade.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByAutor(String autor);
    List<Livro> findByAnoPublicacao(int anoPublicacao);
    List<Livro> findByTituloContainingIgnoreCase(String termo);
    boolean existsByTituloAndAutor(String titulo, String autor);
}