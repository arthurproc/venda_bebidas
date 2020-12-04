import java.io.*;

public class Bebida {

	char ativo;
	String codVenda;
	String nomeCliente;
	String codProduto;
	String descProduto;
	String dtVenda;
	String descCategoria;

	int quantidade;
	float precoUnitario;
	float vlrImposto;

	static String vetCodProdutos[] = { "WJW", "CSL", "CBL", "CCO", "GAN", "WCB", "WBL", "VCT", "VTM", "SLT" };

	static String vetDescProdutos[] = { "Whisky Johny Walker", "Cerveja Skol Lata", "Cerveja Brahma Lata",
			"Coca Cola 2lts", "Guarana Antartica 2lts", "Whisky Cavalo Branco", "Whisky Ballantines",
			"Vinho Concha Y Toro", "Vinho Tinto Miolo", "Suco Laranja Tropicana" };

	static String vetCategorias[] = { "destilado alcolico importado", "fermentado alcolico nacional",
			"fermentado alcolico nacional", "sem alcool nacional", "sem alcool nacional",
			"destilado alcolico importado", "destilado alcolico importado", "alcolico importado", "alcolico nacional",
			"sem alcool importado" };
	static String nomeArquivo = "VENDAS.DAT";

	// *********************** INCLUSAO *****************************
	public void incluir() {

		String codVendaChave;
		char confirmacao;
		long posicaoRegistro;

		do {
			do {

				System.out.println("\n ***************  INCLUSAO DE VENDAS  ***************** ");
				System.out.print("Digite o codigo da Venda ( FIM para encerrar): ");
				codVendaChave = Main.leia.nextLine();
				if (codVendaChave.equals("FIM")) {
					break;
				}
				posicaoRegistro = pesquisarVenda(codVendaChave);

				if (posicaoRegistro >= 0) {
					System.out.println("Venda ja cadastrada, digite outro valor\n");
				}
			} while (posicaoRegistro >= 0);

			if (codVendaChave.equals("FIM")) {
				break;
			}

			descProduto = " ";
			vlrImposto = 0;

			ativo = 'S';
			codVenda = codVendaChave;

			// COD CLIENTE e VALIDACAO
			do {
				System.out.print("Digite o nome do cliente.........................: ");
				nomeCliente = Main.leia.nextLine();
				if (nomeCliente.trim().length() == 0) {
					System.out.println("Campo obrigatorio");
				}
			} while (nomeCliente.trim().length() == 0);
			// COD PRODUTO e VALIDAÇÃO
			do {
				System.out.print("Digite o codigo do produto.........................: ");
				codProduto = Main.leia.next();
				validarCodProduto(codProduto);
				descProduto = validarCodProduto(codProduto);
				if (descProduto.contentEquals(" ")) {
					System.out.println("\nCódido incorreto, digite outro código");
				} else {
					System.out.println("Descricao do produto...........................: " + descProduto);
				}
			} while (descProduto.equals(" "));

			// DATA VENDA e VALIDACAO
			do {
				System.out.print("Data da venda..................................: ");
				dtVenda = Main.leia.next();
			} while (!validarData(dtVenda));
			
			// QUANTIDADE e VALIDACAO
			do {
				System.out.print("Quantidade Vendiada............................: ");
				quantidade = Main.leia.nextInt();
				if (quantidade < 0) {
					System.out.println("Quantidade deve ser maior que 0, digite novamente");
					System.out.print("Quantidade Vendiada............................: ");
					quantidade = Main.leia.nextInt();
				}
			} while (quantidade < 0);
			
			// PRECO e VALIDIACAO
			do {
				System.out.print("Preco unitario.................................: ");
				precoUnitario = Main.leia.nextFloat();
				if (precoUnitario < 0) {
					System.out.println("Quantidade deve ser maior que 0, digite novamente");
					System.out.print("Preco Unitario............................: ");
					precoUnitario = Main.leia.nextInt();
				}
			} while (precoUnitario < 0);

			// TESTE IMPOSTOS
			vlrImposto = calcularImpostos(codProduto, precoUnitario);
			System.out.println("Valor do imposto 1 unidade.....................: "
					+ Main.df.format(calcularImpostos(codProduto, precoUnitario)));
			System.out.println(
					"Valor do imposto total.........................: " + Main.df.format(vlrImposto * quantidade));
			System.out.println("Valor final da venda...........................: "
					+ Main.df.format(quantidade * precoUnitario + quantidade * vlrImposto));

			do {
				System.out.print("\nConfirma a gravacao dos dados (S/N) ? ");
				confirmacao = Main.leia.next().charAt(0);
				if (confirmacao == 'S') {
					salvarVenda();
				}
			} while (confirmacao != 'S' && confirmacao != 'N');

		} while (!codVenda.equals("FIM"));
	}

	// ************************ ALTERACAO *****************************
	public void alterar() {
		String codVendaChave;
		char confirmacao;
		long posicaoRegistro = 0;
		byte opcao;

		do {
			do {

				System.out.println("\n ***************  ALTERACAO DE VENDAS  ***************** ");
				System.out.print("Digite o codigo da venda que deseja alterar( FIM para encerrar ): ");
				codVendaChave = Main.leia.nextLine();
				if (codVendaChave.equals("FIM")) {
					break;
				}

				posicaoRegistro = pesquisarVenda(codVendaChave);
				if (posicaoRegistro == -1) {
					System.out.println("Venda nao cadastrada no arquivo, digite outro valor\n");
				}
			} while (posicaoRegistro == -1);

			if (codVendaChave.equals("FIM")) {
				break;
			}

			ativo = 'S';

			do {
				System.out.println("[ 1 ] Nome do Cliente............: " + nomeCliente);
				System.out.println("[ 2 ] Data da venda..............: " + dtVenda);
				System.out.println("[ 3 ] Quantidade Vendida ........: " + quantidade);
				System.out.println("[ 4 ] Preco unitario.............: " + precoUnitario);
				System.out.println("[ 5 ] Codigo do Produto..........: " + codProduto);

				do {
					System.out
							.println("Digite o numero do campo que deseja alterar (0 para finalizar as alteracoes): ");
					opcao = Main.leia.nextByte();
				} while (opcao < 0 || opcao > 5);

				switch (opcao) {
				case 1:
					Main.leia.nextLine();
					System.out.print("Digite o NOVO NOME do Cliente......................: ");
					nomeCliente = Main.leia.nextLine();
					break;
				case 2:
					Main.leia.nextLine();
					System.out.print("Digite a NOVA DATA de venda (DD/MM/AAAA)...........: ");
					dtVenda = Main.leia.nextLine();
					break;
				case 3:
					System.out.print("Digite a NOVA QUANTIDADE de produtos vendidos......: ");
					quantidade = Main.leia.nextInt();
					break;
				case 5:
					Main.leia.nextLine();
					System.out.print("Digite o NOVO CODIGO do produto............: ");
					codProduto = Main.leia.nextLine();
					break;
				default:
					System.out.print("Opcao invalida, tente novamente");
					break;
				}
				System.out.println();
			} while (opcao != 0);

			do {
				System.out.print("\nConfirma a alteracao dos dados (S/N) ? ");
				confirmacao = Main.leia.next().charAt(0);
				if (confirmacao == 'S') {
					desativarVenda(posicaoRegistro);
					salvarVenda();
					System.out.println("Dados gravados com sucesso !\n");
				}
			} while (confirmacao != 'S' && confirmacao != 'N');

		} while (!codVenda.equals("FIM"));
	}

	// ************************ EXCLUSAO *****************************
	public void excluir() {
		String codVendaChave;
		char confirmacao;
		long posicaoRegistro = 0;

		do {
			do {
				
				System.out.println(" ***************  EXCLUSAO DE VENDAS  ***************** ");
				System.out.print("Digite o codigo da venda que deseja excluir ( FIM para encerrar ): ");
				codVendaChave = Main.leia.nextLine();
				if (codVendaChave.equals("FIM")) {
					break;
				}

				posicaoRegistro = pesquisarVenda(codVendaChave);
				if (posicaoRegistro == -1) {
					System.out.println("Venda nao cadastrada no arquivo, digite outro valor\n");
				}
			} while (posicaoRegistro == -1);

			if (codVendaChave.equals("FIM")) {
				System.out.println("\n ************  PROGRAMA ENCERRADO  ************** \n");
				break;
			}

			System.out.println("Nome do Cliente............: " + nomeCliente);
			System.out.println("Data da venda..............: " + dtVenda);
			System.out.println("Quantidade Vendida ........: " + quantidade);
			System.out.println("Preco unitario.............: " + precoUnitario);
			System.out.println();

			do {
				System.out.print("\nConfirma a exclusao desta venda (S/N) ? ");
				confirmacao = Main.leia.next().charAt(0);
				if (confirmacao == 'S') {
					desativarVenda(posicaoRegistro);
				}
			} while (confirmacao != 'S' && confirmacao != 'N');

		} while (!codVendaChave.equals("FIM"));
	}

	// ************************ CONSULTA *****************************
	public void consultar() {
		RandomAccessFile arqVendas;
		byte opcao;
		char sexoAux;
		long posicaoRegistro;

		do {
			do {
				System.out.println("\n ***************  CONSULTA DE VENDAS  ***************** ");
				System.out.println(" [1] VENDAS POR MES ");
				System.out.println(" [2] VENDAS POR CLIENTE ");
				System.out.println(" [3] TODAS AS VENDAS ");
				System.out.println(" [0] SAIR");
				System.out.print("\nDigite a opcao desejada: ");
				opcao = Main.leia.nextByte();
				if (opcao < 0 || opcao > 3) {
					System.out.println("opcao Invalida, digite novamente.\n");
				}
			} while (opcao < 0 || opcao > 3);

			switch (opcao) {
			case 0:
				System.out.println("\n ************  PROGRAMA ENCERRADO  ************** \n");
				break;

			case 1: // VENDAS POR MES
				Main.leia.nextLine(); // limpa buffer de memoria
				System.out.print("Digite o Mes/Ano da venda: ");
				dtVenda = Main.leia.nextLine();
				pesquisarMensal(dtVenda);
				break;

			case 2: // VENDAS POR CLIENTE
				
					Main.leia.nextLine(); // limpa buffer de memoria
					System.out.print("Digite o Nome do Cliente: ");
					nomeCliente = Main.leia.nextLine();
					pesquisarNome(nomeCliente);
					break;
				
			case 3: // TODAS AS VENDAS
				imprimirTodos();
				break;
				
			default:
				System.out.print("Opcao invalida, tente novamente");
				break;
			}
		} while (opcao != 0);
	}

	public void imprimirCabecalho() {
		System.out.println("CODVENDA--CLIENTE---------DESCR PROD.---DT.VENDA---QUANT-PRC UNIT-IMPOSTO--VLRVENDA--");
	}

	public void imprimirVenda() {
		float valorVenda = quantidade * precoUnitario + vlrImposto * quantidade;
		System.out.println(formatarString(codVenda, 8) + "  " 
				+ formatarString(nomeCliente, 13) + "  "
				+ formatarString(descProduto, 13) + "  " 
				+ formatarString(dtVenda, 10) + "  "
				+ formatarString(Integer.toString(quantidade), 4) + "  "
				+ formatarString(Float.toString(precoUnitario), 7) + "  "
				+ formatarString(Float.toString(vlrImposto), 7) + "  "
				+ formatarString(Float.toString(valorVenda), 9));
	}

	public static String formatarString(String texto, int tamanho) {
		// retorna uma string com o numero de caracteres passado como parametro
		// em
		// TAMANHO
		if (texto.length() > tamanho) {
			texto = texto.substring(0, tamanho);
		} else {
			while (texto.length() < tamanho) {
				texto = texto + " ";
			}
		}
		return texto;
	}

	public static boolean validarData(String data) {
		if (data.length() != 10) {
			System.out.println("Data invalida, digite 10 caracteres no formato DD/MM/AAAA");
			return false;
		}
		if (data.charAt(2) != '/' || data.charAt(5) != '/') {
			System.out.println("Data invalida, digite / na 3a e 6a posicoes da data");
			return false;
		}

		int ano, mes, dia;
		try {
			ano = Integer.parseInt(data.substring(6));
			mes = Integer.parseInt(data.substring(3, 5));
			dia = Integer.parseInt(data.substring(0, 2));
		} catch (NumberFormatException e) {
			System.out.println("Data invalida, digite dia, mes e ano numericos");
			return false;
		}

		if (ano > 2020) {
			System.out.println("Ano invalido, digite maximo 2020");
			return false;
		}
		if (mes < 1 || mes > 12) {
			System.out.println("Mes invalido, digite de 1 a 12");
			return false;
		}
		if (dia < 1 || dia > 31) {
			System.out.println("Data invalida, digite dia acima de zero e abaixo de 31");
			return false;
		}
		if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia > 30) {
			System.out.println("Data invalida, para este mes o dia deve ser no maximo 30");
			return false;
		}
		if (mes == 2) {
			if (ano % 4 == 0 && ano % 100 != 0 || ano % 400 == 0) { // ano
																	// bissexto
				if (dia > 29) {
					System.out.println("Data invalida, neste ano o mes Fevereiro pode ter maximo 29 dias");
					return false;
				}
			} else {
				if (dia > 28) {
					System.out.println("Data invalida, neste ano o mes Fevereiro pode ter maximo 28 dias");
					return false;
				}
			}
		}

		return true;
	}

	public long pesquisarVenda(String codVendaPesq) {
		// metodo para localizar um registro no arquivo em disco
		long posicaoCursorArquivo = 0;
		try {
			RandomAccessFile arqVendas = new RandomAccessFile(nomeArquivo, "rw");
			while (true) {
				posicaoCursorArquivo = arqVendas.getFilePointer(); 
																	 
				ativo = arqVendas.readChar();
				codVenda = arqVendas.readUTF();
				nomeCliente = arqVendas.readUTF();
				codProduto = arqVendas.readUTF();
				descProduto = arqVendas.readUTF();
				dtVenda = arqVendas.readUTF();
				quantidade = arqVendas.readInt();
				precoUnitario = arqVendas.readFloat();
				vlrImposto = arqVendas.readFloat();

				if (codVendaPesq.equals(codVenda) && ativo == 'S') {
					arqVendas.close();
					return posicaoCursorArquivo;
				}
			}
		} catch (EOFException e) {
			return -1; // registro nao foi encontrado
		} catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
			return -1;
		}
	}

	public void salvarVenda() {
		// metodo para incluir um novo registro no final do arquivo em disco
		try {
			RandomAccessFile arqVendas = new RandomAccessFile(nomeArquivo, "rw");
			arqVendas.seek(arqVendas.length()); // posiciona o ponteiro no final
												// do arquivo (EOF)
			arqVendas.writeChar(ativo);
			arqVendas.writeUTF(codVenda);
			arqVendas.writeUTF(nomeCliente);
			arqVendas.writeUTF(codProduto);
			arqVendas.writeUTF(descProduto);
			arqVendas.writeUTF(dtVenda);
			arqVendas.writeInt(quantidade);
			arqVendas.writeFloat(precoUnitario);
			arqVendas.writeFloat(vlrImposto);
			arqVendas.close();
			System.out.println("Dados gravados com sucesso !\n");
			arqVendas.close();
		} catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
		}
	}

	public void desativarVenda(long posicao) {
		// metodo para alterar o valor do campo ATIVO para N, tornando assim o
		// registro
		// excluido
		try {
			RandomAccessFile arqVendas = new RandomAccessFile(nomeArquivo, "rw");
			arqVendas.seek(posicao);
			arqVendas.writeChar('N'); // desativar o registro antigo
			arqVendas.close();
		} catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
		}
	}

	public static String validarCodProduto(String codProduto) {
		String validar = " ";
		for (int x = 0; x < vetCodProdutos.length; x++) {
			if (codProduto.equalsIgnoreCase(vetCodProdutos[x])) {
				validar = vetDescProdutos[x];
			}
		}
		return validar;
	}

	// encontre | return descricao produto caso encontre
	public static float calcularImpostos(String codProduto, float precoUnitario) {

		float impostoProd = 0;
		float imposto = 0;
		int posicao = 0;

		for (int x = 0; x < vetCodProdutos.length; x++) {
			if (codProduto.contains(vetCodProdutos[x])) {
				posicao = x;
			}
		}

		if (vetCategorias[posicao].contains("sem alcool")) {
			imposto += 0.15;
		}
		if (vetCategorias[posicao].contains("alcolico")) {
			imposto += 0.30;
		}
		if (vetCategorias[posicao].contains("nacional")) {
			imposto += 0.10;
		}
		if (vetCategorias[posicao].contains("importado")) {
			imposto += 0.25;
		}
		if (vetCategorias[posicao].contains("fermentado")) {
			imposto += 0.18;
		}
		if (vetCategorias[posicao].contains("destilado")) {
			imposto += 0.23;
		}

		impostoProd = imposto * precoUnitario;

		return impostoProd;
	}
	//NAO ESTA VALIDADA A ENTRADA
	public void pesquisarMensal(String codMesPesq) {
		// metodo para localizar um registro no arquivo em disco
		long posicaoCursorArquivo = 0;
		float total=0;
		
		System.out.println("\n");
		imprimirCabecalho();
		try {
			RandomAccessFile arqVendas = new RandomAccessFile(nomeArquivo, "rw");
			while (true) {
				posicaoCursorArquivo = arqVendas.getFilePointer(); 
																	 
				ativo = arqVendas.readChar();
				codVenda = arqVendas.readUTF();
				nomeCliente = arqVendas.readUTF();
				codProduto = arqVendas.readUTF();
				descProduto = arqVendas.readUTF();
				dtVenda = arqVendas.readUTF();
				quantidade = arqVendas.readInt();
				precoUnitario = arqVendas.readFloat();
				vlrImposto = arqVendas.readFloat();
				
				
				if (codMesPesq.equals(dtVenda.substring(3)) && ativo == 'S') {
					imprimirVenda();
					total += quantidade * precoUnitario + vlrImposto * quantidade;
				}
			}
			
		} catch (EOFException e) {
			System.out.println("                                                          total de vendas: "+total);
			return; // registro nao foi encontrado
		} catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
			return;
		}
	}
	public void imprimirTodos() {
		// metodo para localizar um registro no arquivo em disco
		long posicaoCursorArquivo = 0;
		float total=0;
		
		System.out.println("\n");
		imprimirCabecalho();
		try {
			RandomAccessFile arqVendas = new RandomAccessFile(nomeArquivo, "rw");
			while (true) {
				posicaoCursorArquivo = arqVendas.getFilePointer(); 
																	 
				ativo = arqVendas.readChar();
				codVenda = arqVendas.readUTF();
				nomeCliente = arqVendas.readUTF();
				codProduto = arqVendas.readUTF();
				descProduto = arqVendas.readUTF();
				dtVenda = arqVendas.readUTF();
				quantidade = arqVendas.readInt();
				precoUnitario = arqVendas.readFloat();
				vlrImposto = arqVendas.readFloat();
				
				
				if (ativo == 'S') {
					imprimirVenda();
					total += quantidade * precoUnitario + vlrImposto * quantidade;
				}
			}
		} catch (EOFException e) {
			System.out.println("                                                          total de vendas: "+total);
			return; // registro nao foi encontrado
		} catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
			return;
		}
	}
	public void pesquisarNome(String nomePesq) {
		// metodo para localizar um registro no arquivo em disco
		long posicaoCursorArquivo = 0;
		float total=0;
		
		System.out.println("\n");
		imprimirCabecalho();
		try {
			RandomAccessFile arqVendas = new RandomAccessFile(nomeArquivo, "rw");
			while (true) {
				posicaoCursorArquivo = arqVendas.getFilePointer(); 
																	 
				ativo = arqVendas.readChar();
				codVenda = arqVendas.readUTF();
				nomeCliente = arqVendas.readUTF();
				codProduto = arqVendas.readUTF();
				descProduto = arqVendas.readUTF();
				dtVenda = arqVendas.readUTF();
				quantidade = arqVendas.readInt();
				precoUnitario = arqVendas.readFloat();
				vlrImposto = arqVendas.readFloat();
				
				
				if (nomePesq.equals(nomeCliente) && ativo == 'S') {
					imprimirVenda();
					total += quantidade * precoUnitario + vlrImposto * quantidade;
				}
			}
		} catch (EOFException e) {
			System.out.println("                                                          total de vendas: "+total);
			return; // registro nao foi encontrado
		} catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
			return;
		}
	}
	// precoUnitario)
	// return valor imposto
}
