package Fundamentos;

public class inferencia {
	public static void main(String[] args) {
		String nome = "Thiago";
		double peso = 71.8;
		
		var idade = 23; // Está sendo inferido o tipo da variável atráves do valor atribuído, ness caso inteiro.
		
		// var altura;
		// altura = 1.85 Não pode ser inicializada depois que um valor é inferida
		
		peso = 72.4; // isso pode
	}
}
