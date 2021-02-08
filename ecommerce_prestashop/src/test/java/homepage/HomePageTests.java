package homepage;


import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import base.BaseTests;
import pages.CarrinhoPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ModalProdutoPage;
import pages.PedidoPage;
import pages.ProdutoPage;
import util.Funcoes;

public class HomePageTests extends BaseTests {

	@Test
	public void testContarProdutos_oitoProdutosDiferentes() {
		carregarPaginaInicial();
		assertThat(homePage.contarProdutos(), is(8));
	}
	
	@Test
	public void testValidarCarrinhoZerado_ZeroItensNoCarrinho() {
		int produtosNoCarrinho = homePage.obterQuantidadeProdutosNoCarrinho();
		assertThat(produtosNoCarrinho, is(0));
	}
	
	ProdutoPage produtoPage;
	String nomeProduto_ProdutoPage;
	
	@Test
	public void testValidarDetalhesDoProduto_DescricaoEValorIguais() {
		int indice = 0;
		String nomeProduto_HomePage = homePage.obterNomeProduto(indice);
		String precoProduto_HomePage = homePage.obterPrecoProduto(indice);
		
//		System.out.println(nomeProduto_HomePage);
//		System.out.println(precoProduto_HomePage);
		
		produtoPage = homePage.clicarProduto(indice);
		
		nomeProduto_ProdutoPage = produtoPage.obterNomeProduto();
		String precoProduto_ProdutoPage = produtoPage.obterPrecoProduto();
		
//		System.out.println(nomeProduto_ProdutoPage);
//		System.out.println(precoProduto_ProdutoPage);
		
		assertThat(nomeProduto_HomePage.toUpperCase() , is(nomeProduto_ProdutoPage.toUpperCase()));
		assertThat(precoProduto_HomePage, is(precoProduto_ProdutoPage));
	}
	
	LoginPage loginPage;
	
	@Test
	public void testLoginComSucesso_UsuarioLogado() {
		//Clicar no botão sign in na home page
		 loginPage = homePage.clicarBotaoSignIn();
		
		//Preencher usuário e senha
		loginPage.preencherEmail("ade@junior.com");
		loginPage.preencherSenha("13245768");
		
		//Clicar no botão SignIn
		loginPage.clicarBotaoSignIn();
		
		// validar se o usuário esta logado de fato
		assertThat(homePage.estaLogado("Adecarlos Junior"), is (true));
		
		carregarPaginaInicial();
	}
	
	@ParameterizedTest
	@CsvFileSource(resources = "/massaTeste_Login.csv", numLinesToSkip = 1, delimiter = ';')
	public void testLogin_UsuarioLogadoComDadosValidos(String nomeTeste, String email, String password, String nomeUsuario, String resultado) {
		//Clicar no botão sign in na home page
		 loginPage = homePage.clicarBotaoSignIn();
		
		//Preencher usuário e senha
		loginPage.preencherEmail(email);
		loginPage.preencherSenha(password);
		
		//Clicar no botão SignIn
		loginPage.clicarBotaoSignIn();
		
		boolean esperado_loginOK;
		if(resultado.equals("positivo"))
			esperado_loginOK = true;
		else
			esperado_loginOK = false;
		
		// validar se o usuário esta logado de fato
		assertThat(homePage.estaLogado(nomeUsuario), is (esperado_loginOK));
		
		capturarTela(nomeTeste, resultado);
		
		if (esperado_loginOK) 
			homePage.clicarBotaoSignOut();
		
		carregarPaginaInicial();
	}
	
	
	ModalProdutoPage modalProdutoPage;
	
	@Test
	public void testincluirProdutosNoCarrinho_ProdutoIncluidoComSucesso() {
		String tamanhoProduto = "M";
		String corProduto = "Black";
		int quantidadeProduto = 2;
		
		
		// pré condição 
		// usuário logado
		if(!homePage.estaLogado("Adecarlos Junior")) {
			testLoginComSucesso_UsuarioLogado();
		} 
		
		//Teste
		//selecionando produto
		testValidarDetalhesDoProduto_DescricaoEValorIguais();
		
		//Selecionar tamanho
		List<String> listaOpcoes = produtoPage.obterOpcoesSelecionadas();
		
//		System.out.println(listaOpcoes.get(0));
//		System.out.println("Tamanho da lista: " + listaOpcoes.size());
		
		produtoPage.selecionarOpcaoDropdown("M");
		
		listaOpcoes = produtoPage.obterOpcoesSelecionadas();
		
//		System.out.println(listaOpcoes.get(0));
//		System.out.println("Tamanho da lista: " + listaOpcoes.size());
		
		
		//selecionar cor
		
		produtoPage.selecionarCorPreta();
		
		//Selecionar Quantidade
		
		produtoPage.alterarQuantidade(2);
		
		//Adicionar no carrinho
		modalProdutoPage = produtoPage.clicarBotaoAddToCart();
		
		//Validações
		assertTrue(modalProdutoPage.obterMensagemProdutoAdicionado().endsWith("Product successfully added to your shopping cart"));
		
//		System.out.println(modalProdutoPage.obterDescricaoProduto());
		
		assertThat(modalProdutoPage.obterDescricaoProduto().toUpperCase(), is(nomeProduto_ProdutoPage.toUpperCase()));
		
		String precoProdutoString = modalProdutoPage.obterPrecoProduto();
		precoProdutoString = precoProdutoString.replace("$", "");
		Double precoProduto = Double.parseDouble(precoProdutoString);
		
		assertThat(modalProdutoPage.obterTamanhoProduto(), is(tamanhoProduto));
		assertThat(modalProdutoPage.obterCorProduto(), is(corProduto));
		assertThat(modalProdutoPage.obterQuantidadeProduto(), is(Integer.toString(quantidadeProduto)));
		
		String subtotalString = modalProdutoPage.obterSubtotal();
		subtotalString = subtotalString.replace("$", "");
		Double subtotal = Double.parseDouble(subtotalString);
		
		Double subtotalCalculado = quantidadeProduto * precoProduto;
		
		assertThat(subtotal, is(subtotalCalculado));
	}
	
	//Valores esperados
	String esperado_nomeProduto = "Hummingbird printed t-shirt";
	Double esperado_precoProduto = 19.12;
	String esperado_tamanhoProduto = "M";
	String esperado_corProduto = "Black";
	int esperado_input_quantidadeProduto = 2;
	Double esperado_subtotalProduto = esperado_precoProduto * esperado_input_quantidadeProduto;
	
	int esperado_numeroItensTotal = esperado_input_quantidadeProduto;
	Double esperado_subtotalTotal = esperado_subtotalProduto;
	Double esperado_shippingTotal = 7.00;
	Double esperado_summaryTotal = esperado_subtotalTotal + esperado_shippingTotal;
	Double esperado_totalTotal = esperado_summaryTotal;
	Double esperado_taxesTotal = 0.00;
	
	String esperado_nomeCliente = "Adecarlos Junior";
	
	
	CarrinhoPage carrinhoPage;
	
	@Test
	public void testIrParaCarrinho_InformacoesPersistidas() {
		//pré condicoes 
		//produto incluido na tela ModalProdutoPage
		testincluirProdutosNoCarrinho_ProdutoIncluidoComSucesso();
		
		carrinhoPage = modalProdutoPage.clicarBotaoProceedToCheckout();
	/*	
		//teste
		
		//Validar todos elementos da tela
		System.out.println(carrinhoPage.obter_nomeProduto());
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_precoProduto()));
		System.out.println(carrinhoPage.obter_tamanhoProduto());
		System.out.println(carrinhoPage.obter_corProduto());
		System.out.println(carrinhoPage.obter_input_quantidadeProduto());
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalProduto()));
		
		System.out.println("** Itens de totais **");
		System.out.println(Funcoes.removeTextoItemsDevolveInt(carrinhoPage.obter_numerosItensTotal()));
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalTotal()));
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_shippingTotal()));
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_summaryTotal()));
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_total()));
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_taxesTotal()));
	*/
		//Asserções hamcrest
		assertThat(carrinhoPage.obter_nomeProduto(), is(esperado_nomeProduto));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_precoProduto()), is(esperado_precoProduto));
		assertThat(carrinhoPage.obter_tamanhoProduto(), is(esperado_tamanhoProduto));
		assertThat(carrinhoPage.obter_corProduto(), is(esperado_corProduto));
		assertThat(Integer.parseInt(carrinhoPage.obter_input_quantidadeProduto()), is(esperado_input_quantidadeProduto));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalProduto()), is(esperado_subtotalProduto));
		
		assertThat(Funcoes.removeTextoItemsDevolveInt(carrinhoPage.obter_numerosItensTotal()), is(esperado_numeroItensTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalTotal()), is(esperado_subtotalTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_shippingTotal()), is(esperado_shippingTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_summaryTotal()), is(esperado_summaryTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_total()), is(esperado_totalTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_taxesTotal()), is(esperado_taxesTotal));
		
		//assercoes junit
	}
	CheckoutPage checkoutPage;
	
	@Test
	public void testIrParaCheckout_FreteMeioPagamentoEnderecoListadoOk() {
		//Pré-condicoes
		
		//Produto disponivel no carrinho de compras
		testIrParaCarrinho_InformacoesPersistidas();
		
		//teste
		
		//clicar no botão
		checkoutPage = carrinhoPage.clicarBotaoProceedToCheckout();
		//preencher informacoes
		
		//validar informacoes na tela
		assertThat(Funcoes.removeCifraoDevolveDouble(checkoutPage.obter_totalTaxIncTotal()), is(esperado_totalTotal));
		//assertThat(checkoutPage.obter_nomeCliente(), is(esperado_nomeCliente));
		assertTrue(checkoutPage.obter_nomeCliente().startsWith(esperado_nomeCliente));
		checkoutPage.clicarBotaoContinueAdress();
		
		String encontrado_shippingValor = checkoutPage.obter_shippingValor();
		encontrado_shippingValor = Funcoes.removeTexto(encontrado_shippingValor, " tax excl.");
		Double encontrado_shippingValor_Double = Funcoes.removeCifraoDevolveDouble(encontrado_shippingValor);
		
		assertThat(encontrado_shippingValor_Double, is(esperado_shippingTotal));
		
		checkoutPage.clicarBotaoContinueShipping();
		
		//Selecionar opcao "Pay by check"
		checkoutPage.selecionarRadioPayByCheck();
		//Validar valor do cheque (amount)
		String encontrado_amountPayByCheck = checkoutPage.obter_amountPayByCheck();
		encontrado_amountPayByCheck = Funcoes.removeTexto(encontrado_amountPayByCheck, " (tax incl.)");
		Double encontrado_amountPayByCheck_Double = Funcoes.removeCifraoDevolveDouble(encontrado_amountPayByCheck);
		
		assertThat(encontrado_amountPayByCheck_Double, is(esperado_totalTotal));
		//clicar opcao I Agree
		checkoutPage.selecionarCheboxIAgree();
		
		assertTrue(checkoutPage.estaSelecionadoCheckboxIAgree());
	}
	
	@Test
	public void testFinalizarPedido_pedidoFinalizadoComSucesso() {
		//Pré-condições
		//Checkout completamente concluído
		testIrParaCheckout_FreteMeioPagamentoEnderecoListadoOk();
		
		//Teste
		//Clicar no botão para confirmar o pedido
		PedidoPage pedidoPage = checkoutPage.clcicarBotaoConfirmaPedido();
		
		//Validar valores na tela
		assertTrue(pedidoPage.obter_textoPedidoConfirmado().endsWith("YOUR ORDER IS CONFIRMED"));
//		assertThat(pedidoPage.obter_textoPedidoConfirmado().toUpperCase(), is("YOUR ORDER IS CONFIRMED"))
		
		assertThat(pedidoPage.obter_email(), is("ade@junior.com"));
		
		assertThat(pedidoPage.obter_totalProdutos(), is(esperado_subtotalProduto));
		
		assertThat(pedidoPage.obter_totalTaxIncl(), is(esperado_totalTotal));
		
		assertThat(pedidoPage.obter_metodoPagamento(), is("check"));
		
	}
}
