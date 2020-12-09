import java.util.*;
import java.text.DecimalFormat;

public class Main {
	static Scanner leia = new Scanner(System.in);
	static DecimalFormat df = new DecimalFormat();

	public static void main(String[] args) {
		df.applyPattern("R$ #,##0.00");

		Bebida bebida = new Bebida();
		byte opcao = -1;

		do {
			System.out.println("\n ***************  VENDA DE BEBIDAS  ***************** ");
			System.out.println(" [1] INCLUIR VENDA ");
			System.out.println(" [2] ALTERAR VENDA ");
			System.out.println(" [3] CONSULTAR VENDA ");
			System.out.println(" [4] EXCLUIR VENDA ");
			System.out.println(" [0] SAIR");
			System.out.print("\nDigite a opcao desejada: ");
			opcao = leia.nextByte();
			leia.nextLine();

			switch (opcao) {
				case 0:
					System.out.println("\n ************  PROGRAMA ENCERRADO  ************** \n");
					break;
				case 1:
					bebida.incluir();
					break;
				case 2:
					bebida.alterar();
					break;
				case 3:
					bebida.consultar();
					break;
				case 4:
					bebida.excluir();
					break;
				default:
					System.out.println("Opção inválida, digite um valor entre 0 e 4");
					break;
			}
		} while (opcao != 0);
	}
}
