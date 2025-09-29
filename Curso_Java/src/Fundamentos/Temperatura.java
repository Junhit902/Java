package Fundamentos;

// Conversão de temperaturas de °C(Celsius) para °F(Fahrenheit)
public class Temperatura {
	public static void main(String[] args) {
			double fahrenheit = 79; // lembrando que Fahrenheit pode ir de -459°F até 212°F
			double celsius = ((fahrenheit - 32) * (5.0/9)); // convertendo para celsius
	
			System.out.println(fahrenheit + "°F em celsius = " + celsius + "°C" );
			// Uma outra forma de escrever, mais fácil e não precisa concatenar.
			System.out.printf("%.2f°F em celsius = %.2f°C", fahrenheit, celsius);
	}
}
