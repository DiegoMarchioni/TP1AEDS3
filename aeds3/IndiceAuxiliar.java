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
  final float PERCA_ACEITAVEL = 1.25f;

  public IndiceAuxiliar() throws Exception {

    arq = new RandomAccessFile("ArquivoDeExcluidos", "rw");
    // Se o diretório ou os cestos estiverem vazios, cria um novo diretório e lista
    // de cestos
    if (arq.length() == 0) {
      arq.seek(0);
      arq.writeInt(0);
    }
  }
  //
  public void create(short tam, long endereco) throws Exception {
    par = new ParTamEndereco(tam, endereco);
    // Carrega o diretório
    byte[] bd = par.toByteArray();
    arq.seek(0);
    int qtd = arq.readInt();
    arq.seek(0);
    arq.writeInt(++qtd);

    // Identifica a hash do diretório,
    arq.seek(arq.length());
    arq.writeByte(' ');
    arq.writeShort((short) bd.length);
    arq.write(bd);

    this.print();
  }
  
  public void print() throws Exception{

    arq.seek(TAM_CABECALHO);
    ParTamEndereco pte = new ParTamEndereco();

    System.out.println("------------- Printando Indice Auxliar -------------");

    while(arq.getFilePointer() < arq.length()){
      if(arq.readByte() == ' '){
        short tam = arq.readShort();
        byte[] ba = new byte[tam];
        arq.read(ba);
        pte.fromByteArray(ba);
        System.out.println("Tamanho: " + pte.getTam() + " Endereco: " + pte.getEndereco() );
      }
    }
    System.out.println("------------- Fim Indice Auxliar -------------");
  }


  public void close() throws Exception {
    arq.close();
  }
}
