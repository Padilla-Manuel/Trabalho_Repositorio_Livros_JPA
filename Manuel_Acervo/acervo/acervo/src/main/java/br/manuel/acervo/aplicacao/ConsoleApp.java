package br.manuel.acervo.aplicacao;

import br.manuel.acervo.entidade.Livro;
import br.manuel.acervo.repositorio.LivroRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleApp {
    @Autowired
    private LivroRepository livroRepository;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleApp(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public void iniciar() {
        System.out.println("Método iniciar() foi chamado com sucesso!");

        while (true) {
            System.out.println("\n==== CADASTRO DO LIVRO ====");
            System.out.println("1 - Cadastrar Livro");
            System.out.println("2 - Listar Livros");
            System.out.println("3 - Buscar por Autor");
            System.out.println("4 - Buscar por Ano");
            System.out.println("5 - Buscar por Título");
            System.out.println("6 - Sair");
            System.out.print("Escolha uma opção: ");

            int opcao;
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida! Digite um número.");
                continue;
            }

            switch (opcao) {
                case 1 -> cadastrarLivro();
                case 2 -> listarLivros();
                case 3 -> buscarPorAutor();
                case 4 -> buscarPorAno();
                case 5 -> buscarPorTitulo();
                case 6 -> {
                    System.out.println("Saindo... Obrigado por usar o sistema!");
                    return;
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    private void cadastrarLivro() {
        System.out.print("Digite o título: ");
        String titulo = scanner.nextLine().trim();

        System.out.print("Digite o autor: ");
        String autor = scanner.nextLine().trim();

        System.out.print("Digite o ano de publicação: ");
        int ano;
        try {
            ano = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ano inválido! Digite um número.");
            return;
        }

        System.out.print("Digite a editora: ");
        String editora = scanner.nextLine().trim();

        if (livroRepository.existsByTituloAndAutor(titulo, autor)) {
            System.out.println("Livro já cadastrado!");
            return;
        }

        Livro livro = new Livro(titulo, autor, ano, editora);
        livroRepository.save(livro);
        System.out.println("Livro cadastrado com sucesso!");
    }

    private void listarLivros() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }
        System.out.println("\n==== LISTAGEM COMPLETA DO ACERVO ====");
        livros.forEach(l -> System.out.printf("%d | %s | %s | %d | %s%n",
                l.getId(), l.getTitulo(), l.getAutor(), l.getAnoPublicacao(), l.getEditora()));
    }

    private void buscarPorAutor() {
        System.out.print("Digite o nome do autor: ");
        String autor = scanner.nextLine().trim();
        
        List<Livro> livros = livroRepository.findByAutor(autor);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para esse autor.");
            return;
        }

        System.out.println("\nLivros encontrados:");
        livros.forEach(l -> System.out.printf("- %s (%d, %s)%n",
                l.getTitulo(), l.getAnoPublicacao(), l.getEditora()));
    }

    private void buscarPorAno() {
        System.out.print("Digite o ano: ");
        int ano;
        try {
            ano = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ano inválido! Digite um número.");
            return;
        }

        List<Livro> livros = livroRepository.findByAnoPublicacao(ano);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para esse ano.");
            return;
        }

        System.out.println("\nLivros publicados em " + ano + ":");
        livros.forEach(l -> System.out.printf("- %s, por %s (%s)%n",
                l.getTitulo(), l.getAutor(), l.getEditora()));
    }

    private void buscarPorTitulo() {
        System.out.print("Digite um termo no título: ");
        String termo = scanner.nextLine().trim();

        List<Livro> livros = livroRepository.findByTituloContainingIgnoreCase(termo);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado com esse título.");
            return;
        }

        System.out.println("\nResultados da busca:");
        livros.forEach(l -> System.out.printf("- %s, por %s (%d)%n",
                l.getTitulo(), l.getAutor(), l.getAnoPublicacao()));
    }
}
