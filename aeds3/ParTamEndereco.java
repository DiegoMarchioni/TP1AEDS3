package aeds3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParTamEndereco{

  private short tam;
  private long endereco;
  final private int TAMANHO = 10;

  public ParTamEndereco() {
    this(-1, -1);
  }

  public ParTamEndereco(int i, long e) {
    this.tam = (short)i;
    this.endereco = e;
  }

  public short getTam() {
    return tam;
  }

  public long getEndereco() {
    return endereco;
  }

  public short size() {
    return TAMANHO;
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream ba_out = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(ba_out);
    dos.writeShort(this.tam);
    dos.writeLong(this.endereco);
    return ba_out.toByteArray();
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream ba_in = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(ba_in);
    this.tam = dis.readShort();
    this.endereco = dis.readLong();
  }

}
