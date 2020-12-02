 import java.util.*;
public class Main {
	static Scanner leia = new Scanner(System.in);
	
	public static void main(String[] args) {	
		Bebida bebida = new Bebida();
    	byte opcao = -1;
    	 
    	do {
			do {
    			System.out.println("\n ***************  VENDA DE BEBIDAS  ***************** ");
    			System.out.println(" [1] INCLUIR VENDA ");
    			System.out.println(" [2] ALTERAR VENDA ");
    			System.out.println(" [3] CONSULTAR VENDA ");
    			System.out.println(" [4] EXCLUIR VENDA ");
    			System.out.println(" [0] SAIR");
    			System.out.print("\nDigite a opcao desejada: ");
    			opcao = leia.nextByte();
    			if (opcao < 0 || opcao > 4) {
    				System.out.println("opcao Invalida, digite novamente.\n");
    			}
    		}while (opcao < 0 || opcao > 4);
			
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
			}
    	} while ( opcao != 0 );
    	//leia.close();
	}

}
