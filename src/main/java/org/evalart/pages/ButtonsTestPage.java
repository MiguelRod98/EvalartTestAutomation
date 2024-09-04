package org.evalart.pages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

@Getter
public class ButtonsTestPage extends BasePage {

    @FindBy(css = "p[class=\"text-center text-xl font-bold\"]")
    private WebElement coordinatesText;

    @FindBy(css = "button.grid_button")
    private List<WebElement> buttons;

    @FindBy(css = "input.rounded-md ")
    private WebElement modalSumInput;

    @FindBy(css = "button[class=\"border-black p-2 border-2 rounded-md mx-auto hover:bg-blue-300\"]")
    private WebElement sendSumButton;

    public ButtonsTestPage(WebDriver webDriver) {
        super(webDriver);
    }

    public WebElement[][] crearMatrizBidimensional() {

        int filas = 12;
        int columnas = 12;
        WebElement[][] matriz = new WebElement[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int index = i * columnas + j;
                if (index < buttons.size()) {
                    matriz[i][j] = buttons.get(index);
                } else {
                    matriz[i][j] = null;
                }
            }
        }
        return matriz;
    }

    public String obtenerTextoCoordenada() {
        return coordinatesText.getText();
    }

    public static int[][] procesarCoordenadas(String input) {

        String textoLimpio = input.replaceAll("\\*", ";");
        textoLimpio = textoLimpio.replaceAll("_", "");
        textoLimpio = textoLimpio.replaceAll("\\.", "");
        textoLimpio = textoLimpio.replaceAll("\\),\\(", ");(");
        textoLimpio = textoLimpio.replaceAll("[()]", "");

        String[] partes = textoLimpio.split(";");

        int[][] coordenadas = new int[partes.length][2];

        for (int i = 0; i < partes.length; i++) {
            String[] xy = partes[i].split(",");
            coordenadas[i][0] = Integer.parseInt(xy[0].trim());
            coordenadas[i][1] = Integer.parseInt(xy[1].trim());
        }
        return coordenadas;
    }

    public WebElement obtenerBotonEnCoordenada(int x, int y) {

        WebElement[][] matriz = crearMatrizBidimensional();
        if (x >= 0 && x < 12 && y >= 0 && y < 12) {
            System.out.printf("matriz[%d][%d]", x, y);
            return matriz[x][y];
        } else {
            throw new IllegalArgumentException("Coordenadas fuera de los límites");
        }
    }

    public int obtenerValorEnCoordenada(WebElement[][] matriz, int x, int y) {

        WebElement boton = matriz[x][y];
        if (boton != null) {
            String valorTexto = boton.getAttribute("value").trim();
            if (valorTexto.isEmpty()) {
                System.out.println("El botón en la coordenada (" + x + ", " + y + ") tiene un valor vacío.");
                return 0;
            }
            try {
                return Integer.parseInt(valorTexto);
            } catch (NumberFormatException e) {
                System.out.println("Error al convertir el atributo value a número en (" + x + ", " + y + "): " + valorTexto);
                return 0;
            }
        } else {
            System.out.println("Botón no encontrado en la coordenada (" + x + ", " + y + ")");
            return 0;
        }
    }

    public int sumaValorConAlrededor(int x, int y) {

        WebElement[][] matriz = crearMatrizBidimensional();
        int suma = 0;

        int[][] desplazamientos = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        int valorCentral = obtenerValorEnCoordenada(matriz, x, y);
        suma += valorCentral;
        System.out.println("Valor del botón central en (" + x + ", " + y + "): " + valorCentral);

        for (int[] desplazamiento : desplazamientos) {
            int nuevoX = x + desplazamiento[0];
            int nuevoY = y + desplazamiento[1];
            if (nuevoX >= 0 && nuevoX < 12 && nuevoY >= 0 && nuevoY < 12) {
                int valorCircundante = obtenerValorEnCoordenada(matriz, nuevoX, nuevoY);
                suma += valorCircundante;
                System.out.println("Valor del botón en (" + nuevoX + ", " + nuevoY + "): " + valorCircundante);
            }
        }

        return suma;
    }

    public SuccessPage makeClick() {

        for (int ciclo = 0; ciclo < 14; ciclo++) {
            try {
                String textoCoordenadas = obtenerTextoCoordenada();
                System.out.println("Texto de coordenadas: " + textoCoordenadas);

                int[][] coordenadas = ButtonsTestPage.procesarCoordenadas(textoCoordenadas);

                int sumaX = 0;
                int sumaY = 0;

                for (int[] coordenada : coordenadas) {
                    sumaX += coordenada[0];
                    sumaY += coordenada[1];
                }

                System.out.println("Suma de X = " + sumaX + ", Suma de Y = " + sumaY);

                if (sumaX >= 0 && sumaX < 12 && sumaY >= 0 && sumaY < 12) {
                    WebElement boton = obtenerBotonEnCoordenada(sumaX, sumaY);

                    if (boton != null) {
                        System.out.println("Botón encontrado. Visible: " + boton.isDisplayed() + ", Habilitado: " + boton.isEnabled());

                        String valorBoton = boton.getAttribute("value").trim();
                        System.out.println("Valor del botón antes de hacer clic: " + valorBoton);

                        scrollToElement(boton);

                        System.out.println("Haciendo clic en el botón en la posición [" + sumaX + "][" + sumaY + "]");
                        clickJS(boton);

                        waitForElementVisible(modalSumInput);
                        if (modalSumInput.isDisplayed()) {
                            int sumaAlrededor = sumaValorConAlrededor(sumaX, sumaY);
                            modalSumInput.sendKeys(String.valueOf(sumaAlrededor));
                            clickJS(sendSumButton);
                        }
                        return new SuccessPage(getDriver());
                    } else {
                        System.out.println("Botón en la posición [" + sumaX + "][" + sumaY + "] no encontrado.");
                    }
                } else {
                    System.out.println("Coordenadas fuera de rango: [" + sumaX + "][" + sumaY + "]");
                }

            } catch (Exception e) {
                System.out.println("Error al procesar las coordenadas o al hacer clic en el botón: " + e.getMessage());
                ciclo = -1;
            }
        }
        return null;
    }
}
