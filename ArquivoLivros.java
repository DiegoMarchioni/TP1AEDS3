

import java.io.RandomAccessFile;
import java.util.ArrayList;

import aeds3.Arquivo;
import aeds3.ListaInvertida;

public class ArquivoLivros {
    Arquivo<Livro> arqLivros;
    ListaInvertida listaInvertida;
    ArrayList<String> listaStopwords;

    public ArquivoLivros() throws Exception{
        //criamos o objeto de arquivo de livros
        arqLivros = new Arquivo<>("livros", Livro.class.getConstructor());
        //criamos o objeto de lista invertida
        listaInvertida = new ListaInvertida(4, "dados/dicionario.listainv.db", "dados/blocos.listainv.db");
        //funcao para criar arraylist a partir do arquivo de stopwords
        listaStopwords = getStopwords();
    }

    //Cria a lista de stopwords
    ArrayList<String> getStopwords() throws Exception{
        //abre o arquivo db que contém as stopwords
        RandomAccessFile arqStopWords = new RandomAccessFile("./dados/estaticos/stopwords.db", "r");
        //vai para o inicio do arquivo
        arqStopWords.seek(0);
        // cria um array list de strings
        ArrayList<String> result = new ArrayList<String>();
        // enquanto existirem registros no arquivo, adicionamos as strings ao arraylist
        while(arqStopWords.length() > arqStopWords.getFilePointer()){
            result.add(arqStopWords.readUTF());
        }

        //fechamos o arquivo e retornamos o arraylist resultante
        arqStopWords.close();
        return result;
    }

    //DEBUG
    public void DEBUG() throws Exception{
        //programa para printar todos os registros e registros da lista invertida (teste)
        for(int i = 1; i <= arqLivros.tamanho(); i++){
            Livro obj = arqLivros.read(i);
            if(obj != null){
                System.out.println(obj.toString());
            } else{
                System.out.println("Registro deletado");
            }
        }

        listaInvertida.print();
        
    }

    //Create
    public int create(Livro obj) throws Exception{


        //criamos o objeto no arquivo de livros
        int id = arqLivros.create(obj);

        //se a criação for um sucesso e nos retornar um id valido
        if(id > 0){
            //Separa as  palavras por espaço em branco
            String[] listaPalavras = obj.getTitulo().split(" ");

            //Para cada palavra na lista de palavras do titulo
            for( String palavra : listaPalavras){
                //Remove pontos e transforma a palavra em lowerCase (limpa as palavras)
                String limpa = palavra
                .replace(",", "")
                .replace(".", "")
                .replace(":", "")
                .replace(";", "")
                .replace("!", "")
                .replace("?", "")
                .toLowerCase();

                //Só criamos um indice na lista invertida se a palavra não for uma stopword
                if(listaStopwords.contains(limpa) == false){
                    listaInvertida.create(limpa, id);
                } 
            }
        }

        


        


        return id;
    }

    

    //Update
    public boolean update(Livro novoObj, String antigoIsbn)throws Exception{
        
        //Realizando a leitura sequencial no arquivo procurando com base no ISBN para retornar o id
        int id = arqLivros.readISBN(antigoIsbn);
        
        //atualizando o id do objeto recebido
        novoObj.setID(id);
        
        //buscamos o obj original no arquivo de livros
        Livro obj = arqLivros.read(novoObj.getID());

        // se o objeto lido for diferente de null, fazemos o update no arquivo principal e, se for um sucesso
        if( obj != null && arqLivros.update(novoObj) == true ){


            // ---------------------------------------------------------------
            // PARTE de delecao (DELETAMOS TODOS OS INDICES DO TITULO ANTIGO)
            // ---------------------------------------------------------------

            //Separa as  palavras por espaço em branco
            String[] listaPalavras = obj.getTitulo().split(" ");


            //Para cada palavra na lista de palavras do titulo
            for( String palavra : listaPalavras){
                //Remove pontos e transforma a palavra em lowerCase (limpa as palavras)
                String limpa = palavra
                .replace(",", "")
                .replace(".", "")
                .replace(":", "")
                .replace(";", "")
                .replace("!", "")
                .replace("?", "")
                .toLowerCase();

        

                //deletamos todas as palavras do titulo, com id 1 ( listainvertida trata as que nao existem)
                listaInvertida.delete(limpa, obj.getID());

                
            }

            // ---------------------------------------------------------------
            // PARTE de criacao (CRIAMOS OS INDICES DO TITULO NOVO)
            // ---------------------------------------------------------------

            //Separa as  palavras por espaço em branco
            listaPalavras = novoObj.getTitulo().split(" ");

            //Para cada palavra na lista de palavras do titulo
            for( String palavra : listaPalavras){
                //Remove pontos e transforma a palavra em lowerCase (limpa as palavras)
                String limpa = palavra
                .replace(",", "")
                .replace(".", "")
                .replace(":", "")
                .replace(";", "")
                .replace("!", "")
                .replace("?", "")
                .toLowerCase();

                //Só criamos um indice na lista invertida se a palavra não for uma stopword
                if(listaStopwords.contains(limpa) == false){
                    listaInvertida.create(limpa, novoObj.getID());
                } 
            }

            

            //retornamos true, se completar
            return true;
        }

        return false;
    }

    //Delete
    public boolean delete(String isbn)throws Exception{
        
        //Realizando a leitura sequencial no arquivo procurando com base no ISBN para retornar o id
        int id = arqLivros.readISBN(isbn);

        //Testando se algum id foi achado de acordo com o ISBN
        if(id == -1){
            return false;
        }

        //buscamos o obj original no arquivo de livros
        Livro obj = arqLivros.read(id);

        // se o objeto lido for diferente de null, fazemos a delecao no arquivo principal e, se for um sucesso
        if( obj != null && arqLivros.delete(id) == true ){


            //Separa as  palavras por espaço em branco
            String[] listaPalavras = obj.getTitulo().split(" ");


            //Para cada palavra na lista de palavras do titulo
            for( String palavra : listaPalavras){
                //Remove pontos e transforma a palavra em lowerCase (limpa as palavras)
                String limpa = palavra
                .replace(",", "")
                .replace(".", "")
                .replace(":", "")
                .replace(";", "")
                .replace("!", "")
                .replace("?", "")
                .toLowerCase();

        

                //deletamos todas as palavras do titulo, com id 1 ( listainvertida trata as que nao existem)
                listaInvertida.delete(limpa, id);

                
            }

            //retornamos true, se completar
            return true;

        }

        return false;
    }

    //Limpa a string de pesquisa
    private ArrayList<String> limpar(String frase){
        String[] lista = frase.split(" ");
        ArrayList<String> result = null;

        
        
        for(int i = 0; i < lista.length; i++){
            lista[i] = lista[i]
            .replace(",", "")
            .replace(".", "")
            .replace(":", "")
            .replace(";", "")
            .replace("!", "")
            .replace("?", "")
            .toLowerCase();

            if(listaStopwords.contains(lista[i]) == false){
                if(result == null){
                    result = new ArrayList<String>();
                }
                result.add(lista[i]);
            }

        }

        return result;
    }

    //realiza a união entre dois conjuntos de ints
    private int[] uniao(int[] primario, int[] secundario){
        int tamanho = 0;
        for(int i = 0; i < primario.length; i++){
            boolean found = false;
            for(int j = 0; j < secundario.length; j++){
                if(primario[i] == secundario[j]){
                    found = true;
                    j = secundario.length;
                }
            }
            if(found == false){
                primario[i] = -1;
            } else {
                tamanho++;
            }
        }

        if(tamanho > 0){
            int[] resultado = new int[tamanho];
            int index = 0;
            for(int i = 0; i < primario.length; i++){
                if(primario[i] != -1){
                    resultado[index++] = primario[i];
                }
            }
            return resultado;
        } else {
            return null;
        }

    }


    //Read
    public ArrayList<Livro> read(String pesquisa) throws Exception{

        //prepara o arrayList de livros para retorno
        ArrayList<Livro> result = null;


        //separa e limpa as palavras da string
        ArrayList<String> termos = limpar(pesquisa);

        if(termos.size() > 0){
            //realiza a pesquisa da string na listaInvertida e recebe os resultados
            int[] resultadopesq = listaInvertida.read(termos.get(0));


            //se forem encontrados resultados
            if(resultadopesq.length > 0){
                //se a pesquisa for de um termo apenas
                if(termos.size() == 1){
                    //criamos o arraylist de livros propriamente dito e salvamos na variavel de retorno
                    result = new ArrayList<Livro>();

                    //enquanto o resultado da pesquisa tiver objetos
                    for(int i = 0; i < resultadopesq.length; i++){
                        //pesquisamos o id do objeto encontrado no arquivo de livros
                        Livro l = arqLivros.read(resultadopesq[i]);
                        // se o livro for diferente de null
                        if(l != null){
                            //salvamos na variavel de retorno
                            result.add(l);
                        }
                    }
                }
                //se a pesquisa tiver mais de um termo
                else{
                    //enquanto tivermos termos disponiveis
                    for(int i = 1; i < termos.size() && resultadopesq!=null; i++){
                        //fazemos a pesquisa na listainvertida do termo secundario para a operacao 
                        int[] termosecundario = listaInvertida.read(termos.get(i));
                        //fazemos a uniao do termo primario com o termo secundario
                        resultadopesq = uniao(resultadopesq, termosecundario);
                    }

                    //se a união retornar ids
                    if(resultadopesq != null){
                        //criamos o arraylist de livros propriamente dito e salvamos na variavel de retorno
                        result = new ArrayList<Livro>();

                        //enquanto o resultado da pesquisa tiver objetos
                        for(int i = 0; i < resultadopesq.length; i++){
                            //pesquisamos o id do objeto encontrado no arquivo de livros
                            Livro l = arqLivros.read(resultadopesq[i]);
                            // se o livro for diferente de null
                            if(l != null){
                                //salvamos na variavel de retorno
                                result.add(l);
                            }
                        }
                    } 

                }
            }
        }

        //retorna a variavel result
        return result;

    }

    public void close() throws Exception{
        //fecha os arquivos abertos
        arqLivros.close();
        listaInvertida.close(); 
    }

    
}
