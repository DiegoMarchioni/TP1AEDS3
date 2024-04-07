# TP31AEDS3

Criamos um índice auxiliar, que tem como objetivo guardar o endereço de um registro com base em seu tamanho. Quando deletamos um registro do arquivo principal, marcamos ele como lápide  e adicionamos seu endereço junto com o tamanho no arquivo auxiliar, servindo da mesma forma quando aumentamos o tamanho do registro. Após todo o processo, quando vamos criar ou aumentar o tamanho de um registro, antes de simplesmente adiciona-lo no final do arquivo principal, nós checamos os espaços vazios (lápides), armazenados no índice auxiliar, para um tentar encontrar espaço compatível. O novo registro tem que ter pelo menos 70% do tamanho do registro marcado como lápide, caso contrário procuramos outro local para guarda-lo, e caso não haja nenhum espaço para reaproveitar, o enviamos para o final do arquivo.

Vocês implementaram todos os requisitos? 
Nós implementamos todos os requisitos, como foi solicitado no enunciado do trabalho.

Houve alguma operação mais difícil? 
Todas as operações tiveram uma complexidade baixa, onde conseguimos desenvolver de forma objetiva.

Vocês enfrentaram algum desafio na implementação?
Durante a implementação não tivemos desafios, a dificuldade que tivemos foi durante o planejamento do trabalho. Tivemos diversas ideias, mas decidir quais eram, de fato, válidas foi bastante complexo.

Os resultados foram alcançados?
Todos os resultados foram alcançados, mesmo que não sejam da forma mais eficiente. No caso tentamos implementar a árvore B+, porém não tivemos êxito e por isso fizemos um índice auxiliar.


O que você considerou como perda aceitável para o reuso de espaços vazios, isto é, quais são os critérios para a gestão dos espaços vazios? 
Após fazer a marcação como lápide no registro principal, adicionamos o tamanho e o endereço em um arquivo auxiliar. Fazendo isso, quando vamos incluir um novo registro, primeiramente conferimos no arquivo se seu tamanho é de pelo menos 70% de um dos tamanhos alocados no registro, e caso seja, vamos para o endereço no registro principal e sobrescrevemos o arquivo que foi previamente marcado como lápide. Com isso concluimos que o espaço aceitável para reuso, é o novo registro, ter no mínimo 70% do tamanho do registro marcado como lápide.


O código do CRUD com arquivos de tipos genéricos está funcionando corretamente? SIM
O CRUD tem um índice direto implementado com a tabela hash extensível? SIM
A operação de inclusão busca o espaço vazio mais adequado para o novo registro antes de acrescentá-lo ao fim do arquivo? SIM, há a busca do local mais adequado. 
A operação de alteração busca o espaço vazio mais adequado para o registro quando ele cresce de tamanho antes de acrescentá-lo ao fim do arquivo? SIM
As operações de alteração (quando for o caso) e de exclusão estão gerenciando os espaços vazios para que possam ser reaproveitados? SIM
O trabalho está funcionando corretamente? SIM
O trabalho está completo? SIM 
O trabalho é original e não a cópia de um trabalho de um colega? SIM