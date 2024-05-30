import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.text.NumberFormat;
import java.util.Locale;
import com.google.gson.Gson;

public class ConversorDeMoeda {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/9b3c427e83eab3635237e9e5/latest/";


    private static final Set<String> MOEDAS_FILTRADAS = Set.of("USD", "EUR", "BRL", "GBP");

    private static final List<String> historicoConversoes = new ArrayList<>();

    public static void main(String[] args) {
        try {
            String urlComMoedaBase = API_URL + URLEncoder.encode("USD", StandardCharsets.UTF_8);

            HttpClient cliente = HttpClient.newHttpClient();

            HttpRequest requisicao = HttpRequest.newBuilder()
                    .uri(URI.create(urlComMoedaBase))
                    .header("Accept", "application/json")
                    .header("User-Agent", "Java HttpClient")
                    .GET() // Método HTTP GET
                    .build();

            HttpResponse<String> resposta = cliente.send(requisicao, HttpResponse.BodyHandlers.ofString());

            int statusCode = resposta.statusCode();
            if (statusCode != 200) {
                throw new RuntimeException("Falha na solicitação: " + statusCode);
            }

            String respostaJson = resposta.body();
            Gson gson = new Gson();
            ApiResponse apiResponse = gson.fromJson(respostaJson, ApiResponse.class);

            Map<String, Double> taxasFiltradas = new HashMap<>();
            Map<String, Double> taxas = apiResponse.getConversion_rates();
            for (String moeda : MOEDAS_FILTRADAS) {
                if (taxas.containsKey(moeda)) {
                    taxasFiltradas.put(moeda, taxas.get(moeda));
                }
            }

            Scanner scanner = new Scanner(System.in);
            boolean continuar = true;

            while (continuar) {
                System.out.println("Escolha uma opção de conversão:");
                System.out.println("1. Dólar para Euro (USD -> EUR)");
                System.out.println("2. Dólar para Real (USD -> BRL)");
                System.out.println("3. Dólar para Libra Esterlina (USD -> GBP)");
                System.out.println("4. Real para Euro (BRL -> EUR)");
                System.out.println("5. Real para Dólar (BRL -> USD)");
                System.out.println("6. Real para Libra Esterlina (BRL -> GBP)");
                System.out.println("7. Euro para Real (EUR -> BRL)");
                System.out.println("8. Euro para Dólar (EUR -> USD)");
                System.out.println("9. Euro para Libra Esterlina (EUR -> GBP)");
                System.out.println("10. Ver Histórico de Conversões");
                System.out.println("11. Encerrar o programa");

                int opcao = scanner.nextInt();

                if (opcao == 11) {
                    continuar = false;
                    System.out.println("Encerrando o programa...");
                } else if (opcao == 10) {
                    exibirHistoricoConversoes();
                } else {
                    double valor;
                    String moedaOrigem = "";
                    String moedaDestino = "";

                    switch (opcao) {
                        case 1:
                            moedaOrigem = "USD";
                            moedaDestino = "EUR";
                            break;
                        case 2:
                            moedaOrigem = "USD";
                            moedaDestino = "BRL";
                            break;
                        case 3:
                            moedaOrigem = "USD";
                            moedaDestino = "GBP";
                            break;
                        case 4:
                            moedaOrigem = "BRL";
                            moedaDestino = "EUR";
                            break;
                        case 5:
                            moedaOrigem = "BRL";
                            moedaDestino = "USD";
                            break;
                        case 6:
                            moedaOrigem = "BRL";
                            moedaDestino = "GBP";
                            break;
                        case 7:
                            moedaOrigem = "EUR";
                            moedaDestino = "BRL";
                            break;
                        case 8:
                            moedaOrigem = "EUR";
                            moedaDestino = "USD";
                            break;
                        case 9:
                            moedaOrigem = "EUR";
                            moedaDestino = "GBP";
                            break;
                        default:
                            System.out.println("Opção inválida, tente novamente.");
                            continue;
                    }

                    System.out.print("Digite o valor a ser convertido: ");
                    valor = scanner.nextDouble();

                    double valorConvertido = converterMoeda(valor, moedaOrigem, moedaDestino, taxasFiltradas);
                    System.out.println(formatarValor(valor) + " " + moedaOrigem + " é equivalente a " + formatarValor(valorConvertido) + " " + moedaDestino);

                    registrarConversao(valor, moedaOrigem, valorConvertido, moedaDestino);
                }
            }

            scanner.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static double converterMoeda(double valor, String moedaOrigem, String moedaDestino, Map<String, Double> taxas) {
        if (!taxas.containsKey(moedaOrigem)) {
            throw new IllegalArgumentException("Moeda de origem não encontrada: " + moedaOrigem);
        }
        if (!taxas.containsKey(moedaDestino)) {
            throw new IllegalArgumentException("Moeda de destino não encontrada: " + moedaDestino);
        }
        double valorEmUSD;
        if (!moedaOrigem.equals("USD")) {
            valorEmUSD = valor / taxas.get(moedaOrigem);
        } else {
            valorEmUSD = valor;
        }
        if (!moedaDestino.equals("USD")) {
            return valorEmUSD * taxas.get(moedaDestino);
        } else {
            return valorEmUSD;
        }
    }

    public static void registrarConversao(double valorOrigem, String moedaOrigem, double valorDestino, String moedaDestino) {
        LocalDateTime dataHora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String registro = String.format("%s: %s %s -> %s %s", dataHora.format(formatter), formatarValor(valorOrigem), moedaOrigem, formatarValor(valorDestino), moedaDestino);
        historicoConversoes.add(registro);
    }

    public static void exibirHistoricoConversoes() {
        System.out.println("Histórico de Conversões:");
        for (String registro : historicoConversoes) {
            System.out.println(registro);
        }
    }

    public static String formatarValor(double valor) {
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(valor);
    }
}


