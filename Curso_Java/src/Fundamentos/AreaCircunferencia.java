package Fundamentos;

public class AreaCircunferencia {
	public static void main(String[] args) {
		double raio = 5.7; //precisamos especificar o tipo de dado de uma variável
		final double pi = 3.14159; // "final" é para dizer que essa é um constante e que não pode ser atribuído nenhum valor novo
		
		double area_circulo = pi * raio * raio;
		
		System.out.println("Aréa do círculo: " + area_circulo + "m².");
	}
}
