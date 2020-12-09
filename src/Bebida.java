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
			"Coca Cola 2lts", "Guarana Antartica 2lts", "Whisky Cavalo Branco", "Whisky Ballantines", "Vinho Concha Y Toro",
			"Vinho Tinto Miolo", "Suco Laranja Tropicana" };

	static String vetCategorias[] = { "destilado alcolico importado", "fermentado alcolico nacional",
			"fermentado alcolico nacional", "sem alcool nacional", "sem alcool nacional", "destilado alcolico importado",
			"destilado alcolico importado", "alcolico importado", "alcolico nacional", "sem alcool importado" };

	RandomAccessFile arqVendas;

	public Bebida() {
		try {
			arqVendas = new RandomAccessFile("VENDAS.DAT", "rw");
		} catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
		}
	}

	// *********************** INCLUSAO *****************************
	public void incluir() {

		String codVendaChave;
		String confirmacao;
		long posicaoRegistro;

		do {
			do {

				System.out.println(" ***************  INCLUSAO DE VENDAS  ***************** ");
				System.out.println("Digite o codigo da Venda ( FIM para encerrar): ");
				codVendaChave = Main.leia.nextLine();
				if (codVendaChave.equals("FIM")) {
					break;
				}
				posicaoRegistro = pesquisarVenda(codVendaChave);

				if (posicaoRegistro >= 0) {
					System.out.println("Venda ja cadastrada, digite outro valor");
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
				System.out.println("Digite o nome do cliente.........................: ");
				nomeCliente = Main.leia.nextLine();
				if (nomeCliente.trim().length() == 0) {
					System.out.println("Campo obrigatorio");
				}
			} while (nomeCliente.trim().length() == 0);
			// COD PRODUTO e VALIDACAO
			do {
				System.out.println("Digite o codigo do produto.........................: ");
				codProduto = Main.leia.nextLine();
				descProduto = validarCodProduto(codProduto);
				if (descProduto.equals(" ")) {
					System.out.println("Código incorreto, digite outro código");
				} else {
					System.out.println("Descricao do produto...........................: " + descProduto);
				}
			} while (descProduto.equals(" "));

			// DATA VENDA e VALIDACAO
			do {
				System.out.println("Data da venda..................................: ");
				dtVenda = Main.leia.nextLine();
			} while (!validarData(dtVenda));

			// QUANTIDADE e VALIDACAO
			do {
				System.out.println("Quantidade Vendiada............................: ");
				quantidade = Main.leia.nextInt();
				Main.leia.nextLine();
				if (quantidade <= 0) {
					System.out.println("Quantidade deve ser maior que 0, digite novamente");
				}
			} while (quantidade <= 0);

			// PRECO e VALIDIACAO
			do {
				System.out.println("Preco unitario.................................: ");
				precoUnitario = Main.leia.nextFloat();
				Main.leia.nextLine();
				if (precoUnitario <= 0) {
					System.out.println("Quantidade deve ser maior que 0, digite novamente");
				}
			} while (precoUnitario <= 0);

			vlrImposto = calcularImpostos(codProduto, precoUnitario);
			System.out.println("Valor do imposto 1 unidade.....................: "
					+ Main.df.format(calcularImpostos(codProduto, precoUnitario)));
			System.out.println("Valor do imposto total.........................: " + Main.df.format(vlrImposto * quantidade));
			System.out.println("Valor final da venda...........................: "
					+ Main.df.format(quantidade * precoUnitario + quantidade * vlrImposto));

			do {
				System.out.println("Confirma a gravacao dos dados (S/N) ? ");
				confirmacao = Main.leia.nextLine();
				if (confirmacao.equalsIgnoreCase("S")) {
					salvarVenda();
				}
			} while (!confirmacao.equalsIgnoreCase("S") && !confirmacao.equalsIgnoreCase("N"));

		} while (!codVenda.equalsIgnoreCase("FIM"));
	}

	// ************************ ALTERACAO *****************************
	public void alterar() {
		String codVendaChave;
		String confirmacao;
		long posicaoRegistro = 0;
		byte opcao;

		do {
			do {

				System.out.println(" ***************  ALTERACAO DE VENDAS  ***************** ");
				System.out.println("Digite o codigo da venda que deseja alterar( FIM para encerrar ): ");
				codVendaChave = Main.leia.nextLine();
				if (codVendaChave.equals("FIM")) {
					break;
				}

				posicaoRegistro = pesquisarVenda(codVendaChave);
				if (posicaoRegistro == -1) {
					System.out.println("Venda nao cadastrada no arquivo, digite outro valor");
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
					System.out.println("Digite o numero do campo que deseja alterar (0 para finalizar as alteracoes): ");
					opcao = Main.leia.nextByte();
					Main.leia.nextLine();
				} while (opcao < 0 || opcao > 5);

				switch (opcao) {
					case 1:
						do {
							System.out.println("Digite o nome do cliente.........................: ");
							nomeCliente = Main.leia.nextLine();
							if (nomeCliente.trim().length() == 0) {
								System.out.println("Campo obrigatorio");
							}
						} while (nomeCliente.trim().length() == 0);
						break;
					case 2:
						do {
							System.out.println("Data da venda..................................: ");
							dtVenda = Main.leia.nextLine();
						} while (!validarData(dtVenda));
						break;
					case 3:
						do {
							System.out.println("Quantidade Vendiada............................: ");
							quantidade = Main.leia.nextInt();
							Main.leia.nextLine();
							if (quantidade <= 0) {
								System.out.println("Quantidade deve ser maior que 0, digite novamente");
							}
						} while (quantidade <= 0);
						break;
					case 4:
						do {
							System.out.println("Preco unitario.................................: ");
							precoUnitario = Main.leia.nextFloat();
							Main.leia.nextLine();
							if (precoUnitario <= 0) {
								System.out.println("Quantidade deve ser maior que 0, digite novamente");
							}
						} while (precoUnitario <= 0);
						break;
					case 5:
						do {
							System.out.println("Digite o codigo do produto.........................: ");
							codProduto = Main.leia.nextLine();
							descProduto = validarCodProduto(codProduto);
							if (descProduto.equals(" ")) {
								System.out.println("Código incorreto, digite outro código");
							} else {
								System.out.println("Descricao do produto...........................: " + descProduto);
							}
						} while (descProduto.equals(" "));
						break;
					case 0:
						System.out.println("Saindo da alteração de dados.");
						break;
					default:
						System.out.println("Opcao invalida, tente novamente");
						break;
				}
				System.out.println();
			} while (opcao != 0);
			vlrImposto = calcularImpostos(codProduto, precoUnitario);
			System.out.println("Valor do imposto 1 unidade.....................: "
					+ Main.df.format(calcularImpostos(codProduto, precoUnitario)));
			System.out.println("Valor do imposto total.........................: " + Main.df.format(vlrImposto * quantidade));
			System.out.println("Valor final da venda...........................: "
					+ Main.df.format(quantidade * precoUnitario + quantidade * vlrImposto));
			do {
				System.out.println("Confirma a alteracao dos dados (S/N) ? ");
				confirmacao = Main.leia.nextLine();
				if (confirmacao.equalsIgnoreCase("S")) {
					desativarVenda(posicaoRegistro);
					salvarVenda();
				}
			} while (!confirmacao.equalsIgnoreCase("S") && confirmacao.equalsIgnoreCase("N"));

		} while (!codVenda.equalsIgnoreCase("FIM"));
	}

	// ************************ EXCLUSAO *****************************
	public void excluir() {
		String codVendaChave;
		char confirmacao;
		long posicaoRegistro = 0;

		do {
			do {

				System.out.println(" ***************  EXCLUSAO DE VENDAS  ***************** ");
				System.out.println("Digite o codigo da venda que deseja excluir ( FIM para encerrar ): ");
				codVendaChave = Main.leia.nextLine();
				if (codVendaChave.equals("FIM")) {
					break;
				}

				posicaoRegistro = pesquisarVenda(codVendaChave);
				if (posicaoRegistro == -1) {
					System.out.println("Venda nao cadastrada no arquivo, digite outro valor");
				}
			} while (posicaoRegistro == -1);

			if (codVendaChave.equals("FIM")) {
				System.out.println("************  PROGRAMA ENCERRADO  **************");
				break;
			}

			System.out.println("Nome do Cliente............: " + nomeCliente);
			System.out.println("Data da venda..............: " + dtVenda);
			System.out.println("Quantidade Vendida.........: " + quantidade);
			System.out.println("Preco unitario.............: " + precoUnitario);

			do {
				System.out.println("Confirma a exclusao desta venda (S/N) ? ");
				confirmacao = Main.leia.nextLine().charAt(0);
				if (confirmacao == 'S') {
					desativarVenda(posicaoRegistro);
				}
			} while (confirmacao != 'S' && confirmacao != 'N');

		} while (!codVendaChave.equals("FIM"));
	}

	// ************************ CONSULTA *****************************
	public void consultar() {
		byte opcao;

		do {
			do {
				System.out.println("***************  CONSULTA DE VENDAS  *****************");
				System.out.println(" [1] VENDAS POR MES ");
				System.out.println(" [2] VENDAS POR CLIENTE ");
				System.out.println(" [3] TODAS AS VENDAS ");
				System.out.println(" [0] SAIR");
				System.out.println("Digite a opcao desejada: ");
				opcao = Main.leia.nextByte();
				Main.leia.nextLine();
				if (opcao < 0 || opcao > 3) {
					System.out.println("Opção Invalida, digite novamente.");
				}
			} while (opcao < 0 || opcao > 3);

			switch (opcao) {
				case 0:
					System.out.println("************  PROGRAMA ENCERRADO  **************");
					break;

				case 1: // VENDAS POR MES
					System.out.println("Digite o Mes/Ano da venda: ");
					String mesAno = Main.leia.nextLine();
					while (!validarMesAno(mesAno))
					{
						System.out.println("Digite o Mes/Ano novamente: ");
						mesAno = Main.leia.nextLine();
					}
					imprimirMensal(mesAno);
					break;

				case 2: // VENDAS POR CLIENTE
					System.out.println("Digite o Nome do Cliente: ");
					nomeCliente = Main.leia.nextLine();
					imprimirPorNome(nomeCliente);
					break;

				case 3: // TODAS AS VENDAS
					imprimirTodos();
					break;

				default:
					System.out.println("Opcao invalida, tente novamente");
					break;
			}
		} while (opcao != 0);
	}

	public void imprimirCabecalho() {
		System.out.println("CODVENDA--CLIENTE---------DESCR PROD.---DT.VENDA---QUANT-PRC UNIT-IMPOSTO--VLRVENDA--");
	}

	public void imprimirVenda() {
		float valorVenda = quantidade * precoUnitario + vlrImposto * quantidade;
		System.out.println(formatarString(codVenda, 8) + "  " + formatarString(nomeCliente, 13) + "  "
				+ formatarString(descProduto, 13) + "  " + formatarString(dtVenda, 10) + "  "
				+ formatarString(Integer.toString(quantidade), 4) + "  " + formatarString(Float.toString(precoUnitario), 7)
				+ "  " + formatarString(Float.toString(vlrImposto), 7) + "  " + formatarString(Float.toString(valorVenda), 9));
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

	public static boolean validarMesAno(String data) {
		if (data.length() != 7) {
			System.out.println("Mês invalido, digite 7 caracteres no formato MM/AAAA");
			return false;
		}
		if (data.charAt(2) != '/') {
			System.out.println("Mês invalido, digite / na 3a posicao da data");
			return false;
		}

		int mes, ano;
		try {
			ano = Integer.parseInt(data.substring(3));
			mes = Integer.parseInt(data.substring(0, 2));
		} catch (NumberFormatException e) {
			System.out.println("Mês invalido, mes e ano numericos");
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
		return true;
	}

	public long pesquisarVenda(String codVendaPesq) {
		// metodo para localizar um registro no arquivo em disco
		long posicaoCursorArquivo = 0;
		try {
			arqVendas.seek(0);
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
			arqVendas.seek(arqVendas.length());
			arqVendas.writeChar(ativo);
			arqVendas.writeUTF(codVenda);
			arqVendas.writeUTF(nomeCliente);
			arqVendas.writeUTF(codProduto);
			arqVendas.writeUTF(descProduto);
			arqVendas.writeUTF(dtVenda);
			arqVendas.writeInt(quantidade);
			arqVendas.writeFloat(precoUnitario);
			arqVendas.writeFloat(vlrImposto);
			System.out.println("Dados gravados com sucesso !");
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
			arqVendas.seek(posicao);
			arqVendas.writeChar('N'); // desativar o registro antigo
		} catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
		}
	}

	public static String validarCodProduto(String codProduto) {
		for (int x = 0; x < vetCodProdutos.length; x++) {
			if (codProduto.equalsIgnoreCase(vetCodProdutos[x])) {
				return vetDescProdutos[x];
			}
		}
		return " ";
	}

	// encontre | return descricao produto caso encontre
	public static float calcularImpostos(String codProduto, float precoUnitario) {

		float imposto = 0;
		int posicao = 0;

		for (int x = 0; x < vetCodProdutos.length; x++) {
			if (codProduto.equalsIgnoreCase(vetCodProdutos[x])) {
				posicao = x;
				break;
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

		return imposto * precoUnitario;
	}

	// NAO ESTA VALIDADA A ENTRADA
	public void imprimirMensal(String codMesPesq) {
		// metodo para localizar um registro no arquivo em disco
		float total = 0;

		System.out.println("");
		imprimirCabecalho();
		try {
			arqVendas.seek(0);
			while (true) {
				arqVendas.getFilePointer();

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
			System.out.println("                                                          total de vendas: " + total);
		} catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
		}
	}

	public void imprimirTodos() {
		// metodo para localizar um registro no arquivo em disco
		float total = 0;

		System.out.println("");
		imprimirCabecalho();
		try {
			arqVendas.seek(0);
			while (true) {
				arqVendas.getFilePointer();

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
			System.out.println("                                                          total de vendas: " + total);
		} catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
		}
	}

	public void imprimirPorNome(String nomePesq) {
		// metodo para localizar um registro no arquivo em disco
		float total = 0;

		System.out.println("");
		imprimirCabecalho();
		try {
			arqVendas.seek(0);
			while (true) {
				arqVendas.getFilePointer();

				ativo = arqVendas.readChar();
				codVenda = arqVendas.readUTF();
				nomeCliente = arqVendas.readUTF();
				codProduto = arqVendas.readUTF();
				descProduto = arqVendas.readUTF();
				dtVenda = arqVendas.readUTF();
				quantidade = arqVendas.readInt();
				precoUnitario = arqVendas.readFloat();
				vlrImposto = arqVendas.readFloat();

				if (nomePesq.equalsIgnoreCase(nomeCliente) && ativo == 'S') {
					imprimirVenda();
					total += quantidade * precoUnitario + vlrImposto * quantidade;
				}
			}
		} catch (EOFException e) {
			System.out.println("                                                          total de vendas: " + total);
		} catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
		}
	}
}