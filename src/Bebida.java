import java.io.*;

public class Bebida {

	char ativo;
	String codVenda;
	String nomeCliente;
	String codProduto;
	String descProduto;
	String dtVenda;
	int quantidade;
	float precoUnitario;
	float vlrImposto;

	static String vetCodProdutos[] = { "WJW", "CSL", "CBL", "CCO", "GAN", "WCB", "WBL", "VCT", "VTM", "SLT" };

	static String vetDescProdutos[] = { "Whisky Johny Walker", "Cerveja Skol Lata", "Cerveja Brahma Lata",
			"Coca Cola 2lts", "Guaraná Antártica 2lts", "Whisky Cavalo Branco", "Whisky Ballantines", "Vinho Concha Y Toro",
			"Vinho Tinto Miolo", "Suco Laranja Tropicana" };

	static String vetCategorias[] = { "destilado alcoólico importado", "fermentado alcoólico nacional",
			"fermentado alcoólico nacional", "sem álcool nacional", "sem álcool nacional", "destilado alcoólico importado",
			"destilado alcoólico importado", "alcoólico importado", "alcoólico nacional", "sem álcool importado" };

	public long pesquisarVenda(String codVendaPesq) {
		// metodo para localizar um registro no arquivo em disco
		long posicaoCursorArquivo = 0;
		try {
			RandomAccessFile arqVendas = new RandomAccessFile("VENDAS.DAT", "rw");
			while (true) {
				posicaoCursorArquivo = arqVendas.getFilePointer(); // posicao do inicio do registro no arquivo

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
			RandomAccessFile arqVendas = new RandomAccessFile("VENDAS.DAT", "rw");
			arqVendas.seek(arqVendas.length()); // posiciona o ponteiro no final do arquivo (EOF)
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
		} catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
		}
	}

	public void desativarVenda(long posicao) {
		// metodo para alterar o valor do campo ATIVO para N, tornando assim o registro
		// excluido
		try {
			RandomAccessFile arqVendas = new RandomAccessFile("VENDAS.DAT", "rw");
			arqVendas.seek(posicao);
			arqVendas.writeChar('N'); // desativar o registro antigo
			arqVendas.close();
		} catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
		}
	}

	// *********************** INCLUSAO *****************************
	public void incluir() {
		String codVendaChave;
		char confirmacao;
		long posicaoRegistro;

		do {
			do {
				Main.leia.nextLine();
				System.out.println("\n ***************  INCLUSAO DE ALUNOS  ***************** ");
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
			System.out.print("Digite o nome do cliente.........................: ");
			nomeCliente = Main.leia.nextLine();
			System.out.print("Digite o c�digo da venda.........................: ");
			codVenda = Main.leia.next();
			System.out.println("Descri��o do produto...........................: " + descProduto);
			System.out.print("Data da venda..................................: ");
			dtVenda = Main.leia.next();
			System.out.print("Quantidade Vendiada............................: ");
			quantidade = Main.leia.nextInt();
			System.out.print("Pre�o unit�rio.................................: ");
			precoUnitario = Main.leia.nextFloat();
			System.out.println("Valor do imposto...............................: " + vlrImposto);
			System.out.println(
					"Valor final da venda...........................: " + (quantidade * precoUnitario + quantidade * vlrImposto));

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
				Main.leia.nextLine();
				System.out.println("\n ***************  ALTERACAO DE VENDAS  ***************** ");
				System.out.print("Digite a Matricula do Aluno que deseja alterar( FIM para encerrar ): ");
				codVendaChave = Main.leia.nextLine();
				if (codVendaChave.equals("FIM")) {
					break;
				}

				posicaoRegistro = pesquisarVenda(codVendaChave);
				if (posicaoRegistro == -1) {
					System.out.println("Venda n�o cadastrada no arquivo, digite outro valor\n");
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
				System.out.println("[ 4 ] Pre�o unit�rio.............: " + precoUnitario);

				do {
					System.out.println("Digite o numero do campo que deseja alterar (0 para finalizar as alterações): ");
					opcao = Main.leia.nextByte();
				} while (opcao < 0 || opcao > 4);

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
					case 4:
						System.out.print("Digite o NOVO PRE�O UNITARIO do produto............: ");
						precoUnitario = Main.leia.next().charAt(0);
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
		String matriculaChave;
		char confirmacao;
		long posicaoRegistro = 0;

		do {
			do {
				Main.leia.nextLine();
				System.out.println(" ***************  EXCLUSAO DE ALUNOS  ***************** ");
				System.out.print("Digite a Matricula do Aluno que deseja excluir ( FIM para encerrar ): ");
				matriculaChave = Main.leia.nextLine();
				if (matriculaChave.equals("FIM")) {
					break;
				}

				posicaoRegistro = pesquisarAluno(matriculaChave);
				if (posicaoRegistro == -1) {
					System.out.println("Matricula nao cadastrada no arquivo, digite outro valor\n");
				}
			} while (posicaoRegistro == -1);

			if (matriculaChave.equals("FIM")) {
				System.out.println("\n ************  PROGRAMA ENCERRADO  ************** \n");
				break;
			}

			System.out.println("Nome do aluno.......: " + nomeAluno);
			System.out.println("Data de nascimento..: " + dtNasc);
			System.out.println("Valor da mensalidade: " + mensalidade);
			System.out.println("Sexo do aluno.......: " + sexo);
			System.out.println();

			do {
				System.out.print("\nConfirma a exclusao deste aluno (S/N) ? ");
				confirmacao = Main.leia.next().charAt(0);
				if (confirmacao == 'S') {
					desativarAluno(posicaoRegistro);
				}
			} while (confirmacao != 'S' && confirmacao != 'N');

		} while (!matricula.equals("FIM"));
	}

	// ************************ CONSULTA *****************************
	public void consultar() {
		RandomAccessFile arqAluno;
		byte opcao;
		String matriculaChave;
		char sexoAux;
		long posicaoRegistro;

		do {
			do {
				System.out.println(" ***************  CONSULTA DE ALUNOS  ***************** ");
				System.out.println(" [1] CONSULTAR APENAS 1 ALUNO ");
				System.out.println(" [2] LISTA DE TODOS OS ALUNOS ");
				System.out.println(" [3] LISTA SOMENTE SEXO MASCULINO OU FEMININO ");
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

				case 1: // consulta de uma unica matricula
					Main.leia.nextLine(); // limpa buffer de memoria
					System.out.print("Digite a Matriocula do Aluno: ");
					matriculaChave = Main.leia.nextLine();

					posicaoRegistro = pesquisarAluno(matriculaChave);
					if (posicaoRegistro == -1) {
						System.out.println("Matricula nao cadastrada no arquivo \n");
					} else {
						imprimirCabecalho();
						imprimirAluno();
						System.out.println("\n FIM DE RELATORIO - ENTER para continuar...\n");
						Main.leia.nextLine();
					}

					break;

				case 2: // imprime todos os alunos
					try {
						arqAluno = new RandomAccessFile("ALUNO.DAT", "rw");
						imprimirCabecalho();
						while (true) {
							ativo = arqAluno.readChar();
							matricula = arqAluno.readUTF();
							nomeAluno = arqAluno.readUTF();
							dtNasc = arqAluno.readUTF();
							mensalidade = arqAluno.readFloat();
							sexo = arqAluno.readChar();
							if (ativo == 'S') {
								imprimirAluno();
							}
						}
						// arqAluno.close();
					} catch (EOFException e) {
						System.out.println("\n FIM DE RELATORIO - ENTER para continuar...\n");
						Main.leia.nextLine();
						matriculaChave = Main.leia.nextLine();
					} catch (IOException e) {
						System.out.println("Erro na abertura do arquivo - programa sera finalizado");
						System.exit(0);
					}
					break;

				case 3: // imprime alunos do sexo desejado
					do {
						System.out.print("Digite o Sexo desejado (M/F): ");
						sexoAux = Main.leia.next().charAt(0);
						if (sexoAux != 'F' && sexoAux != 'M') {
							System.out.println("Sexo Invalido, digite M ou F");
						}
					} while (sexoAux != 'F' && sexoAux != 'M');

					try {
						arqAluno = new RandomAccessFile("ALUNO.DAT", "rw");
						imprimirCabecalho();
						while (true) {
							ativo = arqAluno.readChar();
							matricula = arqAluno.readUTF();
							nomeAluno = arqAluno.readUTF();
							dtNasc = arqAluno.readUTF();
							mensalidade = arqAluno.readFloat();
							sexo = arqAluno.readChar();

							if (sexoAux == sexo && ativo == 'S') {
								imprimirAluno();
							}
						}
					} catch (EOFException e) {
						System.out.println("\n FIM DE RELATORIO - ENTER para continuar...\n");
						Main.leia.nextLine();
						matriculaChave = Main.leia.nextLine();
					} catch (IOException e) {
						System.out.println("Erro na abertura do arquivo - programa sera finalizado");
						System.exit(0);
					}

			}

		} while (opcao != 0);
	}

	public void imprimirCabecalho() {
		System.out.println("-MATRICULA-  -------- NOME ALUNO ----------  --DATA NASC--  -Mensalidade-  -sexo- ");
	}

	public void imprimirAluno() {
		System.out.println(
				formatarString(matricula, 11) + "  " + formatarString(nomeAluno, 30) + "  " + formatarString(dtNasc, 13) + "  "
						+ formatarString(String.valueOf(mensalidade), 13) + "  " + formatarString(Character.toString(sexo), 6));
	}

	public static String formatarString(String texto, int tamanho) {
		// retorna uma string com o numero de caracteres passado como parametro em
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
			if (ano % 4 == 0 && ano % 100 != 0 || ano % 400 == 0) { // ano bissexto
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

	// public static boolean validarCodProduto(String codProduto)
	// public void calcularImpostos()
}
