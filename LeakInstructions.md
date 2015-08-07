# PerformanceInstructions

Passos para executar:

1. Ative a tab 'Memory' da perspectiva 'Android' no Android Studio
2. Rode o projeto e execute varios orientation changes
3. Perceba o gráfico de memória subindo
4. Execute um garbage collection e verifique que a memória não voltou ao valor inicial (sinal de leak)
5. Dump Java Heap => será salvo um arquivo de dump na pasta '<raiz-do-projeto>/captures'
6. Use a ferramenta 'hprof-conv' do sdk do Android para converter o dump específico da VM Android para dump de VM Java
7. Abra o dump convertido com o MAT
8. Crie um 'Component Report' através da janela que irá aparecer e digite 'com.training.leakandperformanceissues.*' no filtro. Você também poderá criar este report através 'Step By Step -> Component Report'
9. Neste relatório, você será capaz de identificar os objetos retidos na heap através do link 'Retained Set'
10. Verifique que a Activity em si está sendo retida. Clique em 'First 10 of xx objects'
11. Clique com botão direito em uma das entradas e vá em 'Path to GC Roots => with all references'. Este comando irá mostrar o caminho de referências até chegar ao Root do GC
12. Perceba que tem um instância de MyLeakThread segurando esta instância da MainActivity. Isto acontece por que a classe MyLeakThread não foi declarada estática. Altere o código, transformando esta classe em estática e repita o procedimento.
13. Você verá que ainda haverá leak. Repita o procedimento para extração do dump e abra-o no MAT.
14. Veja que, dessa vez, haverá apenas uma instância da activity na memória. No entanto, há várias intâncias de MyLeakThread. Isso acontece por que a execução da thread nunca termina e, portanto, ela fica retida.
15. Implemente um mecanismo para cancelamento da thread, destravando a execução no onDestroy da Activity.
16. Repita o procedimento e você verá que não há mais leaks.
