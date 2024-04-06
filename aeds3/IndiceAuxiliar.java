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
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.lang.reflect.Constructor;

public class IndiceAuxiliar {

  RandomAccessFile arq;
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
    //DEFINICAO DE VARIAVEIS
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

    //LE O ARQUIVO PARA TESTES
    System.out.println("\nADICIONANDO NOVA LAPIDE\n");
    this.print();

  }

  public long read (short tamanho) throws IOException{

    //definicao de variaveis
    short tamAceitavel = (short) (tamanho * PERDA_ACEITAVEL);

    //pulando o cabecalho
    arq.seek(TAM_CABECALHO);

    //pesquisa propriamente dita
    while(arq.getFilePointer() < arq.length()){
      
      //salvando o endereco da lapide
      long lapideEnd = arq.getFilePointer();

      if(arq.readByte() == ' '){

        //lendo o registro
        short tam = arq.readShort();
        long end = arq.readLong();

        //checando TAM
        if(tam >= tamanho && tam <= tamAceitavel){
          //deletando registro
          arq.seek(lapideEnd);
          arq.writeByte('*');



          //LE O ARQUIVO PARA TESTES
          System.out.println("\nREMOVENDO LAPIDE");
          System.out.println("TAMANHO DA LAPIDE: " + tam + " TAMANHO DO NOVO REGISTRO: " + tamanho);
          this.print();

          return end;
        }


      }
    }
    


    return -1;
  }
  
  public void print() throws IOException{

    //PULANDO CABECALHO
    arq.seek(TAM_CABECALHO);

    System.out.println("------------- Printando Indice Auxliar -------------");

    //PESQUISA NO ARQUIVO
    while(arq.getFilePointer() < arq.length()){
      if(arq.readByte() == ' '){
        System.out.println("Tamanho: " + arq.readShort() + " Endereco: " + arq.readLong() );
      }
    }
    System.out.println("------------- Fim Indice Auxliar -------------");
  }


  public void close() throws Exception {
    arq.close();
  }
}
