package homepage;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import base.BaseTests;

public class HomePageTests extends BaseTests {

	@Test
	public void testContarProdutos_oitProdutosDiferentes() {
		carregarPaginaInicial();
		assertThat(homePage.contarProdutos(), is(7));
	}
}
