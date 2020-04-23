# Sistema de Mercado Distribuído para Hash Matching Colaborativo

## Introdução

Este projecto tem dois objectivos principais: i) enriquecer os conhecimentos e a
familiaridade dos alunos em relação aos vários aspectos e requisitos vulgarmente
associados a projectos de sistemas distribuídos; ii) melhorar o entendimento e a prática dos
alunos em relação à especificação e desenvolvimento de sistemas distribuídos.

Neste projecto irá utilizar-se o conceito de hashing, no qual uma função de hash recebe
tipicamente uma sequência de caracteres de comprimento variável e implementa um
algoritmo que mapeia essa sequência de entrada num código (digest) de comprimento fixo.
Os códigos de saída da função designam-se tipicamente por hash codes ou simplesmente
hashes. NB: no projecto proposto iremos utilizar apenas inputs de comprimento fixo e
previamente conhecido.


Os algoritmos de hashing têm várias aplicações como, por exemplo, a verificação de
integridade de software descarregado da Internet e a codificação de passwords em sistemas
de autenticação. Para que estes algoritmos sejam eficazes devem possuir algumas
propriedades, nomeadamente:

● Serem determinísticos: a mesma mensagem processada pela mesma função hash
deve produzir sempre o mesmo código hash;

● Serem não reversíveis: ser computacionalmente difícil/impraticável gerar uma
mensagem a partir de seu hash;

● Possuírem entropia elevada: qualquer pequena alteração numa mensagem deve
produzir um hash muito diferente;

● Serem resistentes a colisões: duas mensagens diferentes não devem produzir o
mesmo hash.

## Run
1. Use IntelliJ/NetBeans to compile the package *edu.ufp.inf.sd.helloworld*
2. Update de setenv (.sh/.bat) file according to your environment (Mac/windows)
3. Run the python http server (script *runpython*)
4. Run the rmi name service (script *runrmiregistry*)
5. Run the server/servant (script *runserver*)
6. Run the client (script *runclient*)

## License
[MIT](https://choosealicense.com/licenses/mit/)