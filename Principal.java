import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

import aeds3.Arquivo;

class Principal {

  public static void main(String args[]) {

    new File("dados/livros.db").delete();
    new File("dados/livros.hash_d.db").delete();
    new File("dados/livros.hash_c.db").delete();
    new File("dados/ArquivoDeExcluidos.db").delete();
    new File("dados/blocos.listainv.db").delete();
    new File("dados/dicionario.listainv.db").delete();

    try {

      // Abrindo arquivo para Livros
      ArquivoLivros arqLivros = new ArquivoLivros();

      Scanner console = new Scanner(System.in);
      int opcao;
      do {

        //arqLivros.DEBUG();

        // imprimir menu
        System.out.println("\n\n-------------------------------");
        System.out.println("              MENU");
        System.out.println("-------------------------------");
        System.out.println("1 - Inserir");
        System.out.println("2 - Buscar");
        System.out.println("3 - Excluir");
        System.out.println("4 - Atualizar");
        System.out.println("0 - Sair");
        try {
          opcao = Integer.valueOf(console.nextLine());
        } catch (NumberFormatException e) {
          opcao = -1;
        }

        switch (opcao) {
          case 1: {// inserir livro
            System.out.println("\nINCLUSÃO");
            try {
              // ler os atributos do novo livro
              System.out.print("ISBN: "); 
              String isbn = console.nextLine();
              System.out.print("Nome: ");
              String nome = console.nextLine();
              System.out.print("Preco: ");
              float preco = Float.valueOf(console.nextLine());
              // criar novo objeto e envia-lo para a funcao da lista
              Livro liv = new Livro(-1, isbn, nome, preco);
              int livro = arqLivros.create(liv);
            } catch (Exception e) {
            }
          }
          break;

          case 2: {// buscar livro
            System.out.println("\nBUSCA");
            System.out.print("Chave de busca: ");
            try {
              // ler a chave de busca e criar uma lista com os resultados da procura
              String busca = console.nextLine();
              ArrayList<Livro> lista = arqLivros.read(busca);
              // imprimir resultados da consulta
              if (lista != null) {
                for (Livro l : lista) {
                  System.out.println(l.toString());
                }
              }
            } catch (Exception e) {
            }
          }
            break;

          case 3: {// deletar livro
            System.out.println("\nEXCLUSÃO");
            System.out.print("ISBN do livro a ser deletado: ");
            try {
              // ler id do registro a ser deletado e chamar a funcao responsável pela deleção
              String isbn = console.nextLine();
              arqLivros.delete(isbn);
            } catch (Exception e) {
            }
          }
            break;

          case 4: {// atualizar registro de livro
          System.out.println("\nATUALIZACAO"); 
          try {
            // ler atributos a serem atualizados
            System.out.print("ISBN do livro a ser atualizado: ");
            String antigoIsbn = console.nextLine();
            System.out.print("novo ISBN: ");
            String isbn = console.nextLine(); 
            System.out.print("novo Nome: ");
            String nome = console.nextLine();
            System.out.print("novo Preco: ");
            float preco = Float.valueOf(console.nextLine());
            // criar novo objeto e envia-lo para a funcao da lista
            Livro liv = new Livro(-1, isbn, nome, preco);
            arqLivros.update(liv, antigoIsbn);
          } catch (Exception e) {
          }
        }
            break;

          default: {
            
          }  
          break;
        }

      } while (opcao != 0);

      // Fim do programa
      arqLivros.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  

}
