/*
TABELA HASH EXTENSÍVEL

Os nomes dos métodos foram mantidos em inglês
apenas para manter a coerência com o resto da
disciplina:
- boolean create(T elemento)
- long read(int hashcode)
- boolean update(T novoElemento)   //  a chave (hashcode) deve ser a mesma
- boolean delete(int hashcode)

Implementado pelo Prof. Marcos Kutova
v1.1 - 2021
*/
package aeds3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.lang.reflect.Constructor;

public class IndiceAuxiliar {

  RandomAccessFile arq;
  RandomAccessFile tmp;
  ParTamEndereco par;
  final int TAM_CABECALHO = 4;
  final float PERDA_ACEITAVEL = 1.3f;

  public IndiceAuxiliar() throws IOException {

    arq = new RandomAccessFile("dados/ArquivoDeExcluidos.db", "rw");

    // Se o diretório ou os cestos estiverem vazios, cria um novo diretório e lista
    // de cestos
    if (arq.length() == 0) {
      arq.seek(0);
      arq.writeInt(0);
    }
  }

  //
  public void create(short tam, long endereco) throws IOException {
    // DEFINICAO DE VARIAVEIS
    par = new ParTamEndereco(tam, endereco);
    // ATUALIZA O CABECALHO
    byte[] bd = par.toByteArray();
    arq.seek(0);
    int qtd = arq.readInt();
    arq.seek(0);
    arq.writeInt(++qtd);

    // GRAVA REGISTRO NO FINAL DO ARQUIVO
    arq.seek(arq.length());
    arq.writeByte(' ');
    arq.write(bd);

    // LE O ARQUIVO PARA TESTES
    System.out.println("\nADICIONANDO NOVA LAPIDE\n");
    this.print();

  }

  public long read(short tamanho) throws IOException {

    // definicao de variaveis
    short tamAceitavel = (short) (tamanho * PERDA_ACEITAVEL);

    // pulando o cabecalho
    arq.seek(TAM_CABECALHO);

    // pesquisa propriamente dita
    while (arq.getFilePointer() < arq.length()) {

      // salvando o endereco da lapide
      long lapideEnd = arq.getFilePointer();

      if (arq.readByte() == ' ') {

        // lendo o registro
        short tam = arq.readShort();
        long end = arq.readLong();

        // checando TAM
        if (tam >= tamanho && tam <= tamAceitavel) {
          // deletando registro
          arq.seek(lapideEnd);
          arq.writeByte('*');

          // LE O ARQUIVO PARA TESTES
          System.out.println("\nREMOVENDO LAPIDE");
          System.out.println("TAMANHO DA LAPIDE: " + tam + " TAMANHO DO NOVO REGISTRO: " + tamanho);
          this.print();

          // --APAGAR E RECRIAR O ARQUIVO--
          // criar arquivo temporário
          tmp = new RandomAccessFile("dados/tmp.db", "rw");
          arq.seek(0);
          int tam_arq = arq.readInt();
          int tam_tmp = 0;
          tmp.seek(0);
          tmp.writeInt(tam_tmp);
          for (int i = 0; i < tam_arq; i++) {
            Byte b = arq.readByte();
            short tam2 = arq.readShort();
            long end2 = arq.readLong();
            if (b == ' ') {
              tmp.seek(tmp.length());
              tmp.writeByte(' ');
              ParTamEndereco par2;
              par2 = new ParTamEndereco(tam2, end2);
              byte[] bd2 = par2.toByteArray();
              tmp.write(bd2);
              tam_tmp++;
            }
          }
          tmp.seek(0);
          tmp.write(tam_tmp);

          // deletar e reabrir o principal
          arq.close();
          new File("dados/ArquivoDeExcluidos.db").delete();
          arq = new RandomAccessFile("dados/ArquivoDeExcluidos.db", "rw");

          // passar do arquivo temporário para o principal
          tmp.seek(0);
          int tam_arq2 = tmp.readByte();
          arq.seek(0);
          arq.writeInt(tam_arq2);
          tmp.seek(TAM_CABECALHO);
          for (int i = 0; i < tam_arq2; i++) {
            Byte b2 = tmp.readByte();
            short tam3 = tmp.readShort();
            long end3 = tmp.readLong();
            arq.seek(arq.length());
            arq.writeByte(b2);
            ParTamEndereco par3;
            par3 = new ParTamEndereco(tam3, end3);
            byte[] bd3 = par3.toByteArray();
            arq.write(bd3);
          }

          // apagar o temporário
          tmp.close();
          new File("dados/tmp.db").delete();

          return end;
        }

      }
    }

    return -1;
  }

  public void print() throws IOException {

    // PULANDO CABECALHO
    arq.seek(TAM_CABECALHO);

    System.out.println("------------- Printando Indice Auxliar -------------");

    // PESQUISA NO ARQUIVO
    while (arq.getFilePointer() < arq.length()) {
      if (arq.readByte() == ' ') {
        System.out.println("Tamanho: " + arq.readShort() + " Endereco: " + arq.readLong());
      }
    }
    System.out.println("------------- Fim Indice Auxliar -------------");
  }

  public void close() throws Exception {
    arq.close();
  }
}
