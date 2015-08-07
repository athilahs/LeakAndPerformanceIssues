# PerformanceInstructions

1. Rode o aplicativo e escolha a opção para o problema de performance
2. Clique no botão para executar a operação e veja que a UI travou. Tente dar um Back na tela e veja que ela não responde.
3. Explique que existe dois metodos para capturar o trace: usando o Android Device Monitor através da opção "Start Method Profiling". Abrirá um diálogo perguntando se você quer executar o profiling por amostragem ou tudo. Este método é menos preciso. O outro método é através de instruções de código. Vamos focar neste método.
4. Adicione a instrução Debug.startMethodTracing("myTraceFile", 30 * 1024 * 1024); no início do listener de clique no botão (o segundo parâmetro é o tamanho do arquivo de trace).
5. Adicione a instrução Debug.stopMethodTracing(); no final do método para parar o tracing.
6. No log irá aparecer o caminho para onde o arquivo de trace foi salvo.
7. Obter o arquivo de trace para a sua máquina: adb pull /storage/emulated/legacy/myTraceFile.trace ~/Desktop/
8. Através do Android Device Monitor, abrir o arquivo de trace: File => Open File...
9. Veja que há muita atividade na main thread. E que o método performInsert é o grande responsável.
10. Modifique o código movendo a chamada para este método para uma AsyncTask. Modifique os locais de captura de trace adequadamente.
11. Execute o app e veja que a main thread não trava mais. Obtenha o trace novamente
12. Veja que uma das threads está executando muito trabalho (o mesmo que antes era executado pela main thread)
13. No painel de Profiling, vá seguindo o método mais demorado até chegar no método bulkInsert do Provider. Você verá que os métodos beginTransaction, endTransaction e notifyChange estão ofendendo bastante a performance.
14. Role a barra lateral do TraceView e observe a coluna Calls+Recur. Veja que estes métodos estão sendo chamados 1000 vezes (o mesmo número de inserções executadas). Ou seja, a cada inserção, estamos abrindo e fechando uma conexão com o banco.
15. Altere este código para abrir apenas uma conexão com o banco.
16. Execute novamente o app e veja como a performance melhorou
