## Conversor de Moeda - Usando a API Exchanger Rate

Este projeto é um conversor de moeda que utiliza a API Exchanger Rate para obter informações atualizadas sobre as taxas de câmbio. O aplicativo é desenvolvido em Java e permite aos usuários converter valores entre diferentes moedas. Abaixo estão os principais pontos que você pode incluir no seu README:

### Funcionalidades

1. **Conversão de Moedas**: Os usuários podem escolher entre várias opções de conversão de moedas, incluindo:
    - Dólar para Euro
    - Dólar para Real
    - Dólar para Libra Esterlina
    - Real para Euro
    - Real para Dólar
    - Real para Libra Esterlina
    - Euro para Real
    - Euro para Dólar
    - Euro para Libra Esterlina

2. **Interface de Usuário Simples**: O aplicativo possui uma interface de usuário baseada em texto que guia os usuários através das opções de conversão.

3. **Encerramento do Programa**: Os usuários podem escolher a opção para encerrar o programa.

### Como Executar o Aplicativo

1. Clone o repositório para o seu ambiente local.
2. Certifique-se de ter o Java instalado em sua máquina.
3. Compile o código-fonte.
4. Execute o aplicativo.

### Exemplo de Uso

```java
// Exemplo de código para converter dólar para euro
double valorDolar = 100.0;
double taxaDolarParaEuro = 0.85; // Taxa de câmbio atual
double valorEuro = valorDolar * taxaDolarParaEuro;
System.out.println("100 dólares equivalem a " + valorEuro + " euros.");
```

### Agradecimentos

Agradeço à Oracle Next Generation ONE e à Alura por proporcionarem esta oportunidade de aprendizado.

## Referências
API de Taxas de Câmbio Gratuita: [ExchangeRate-API](https://shallbd.com/pt/existe-uma-api-de-moeda-gratuita-descubra-as-melhores-opcoes-para-acessar-as-taxas-de-cambio-de-moedas/)