package steps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.common.io.Files;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import pages.HomePage;
import pages.TermosECondicoesPage;

public class AcessarTermosCondicoesSteps {

	private static WebDriver driver;
	private HomePage homePage = new HomePage(driver);

	@Before
	public static void inicializar() {
		System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver\\88\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@Dado("que eu estou na tela inicial")
	public void que_eu_estou_na_tela_inicial() {
		
		homePage.carregarPaginaInicial();
		assertThat(homePage.obterTituloPagina(), is("Loja de Teste"));
	}

	@Dado("não estou logado na minha conta")
	public void não_estou_logado_na_minha_conta() {
		assertThat(homePage.estaLogado("Adecarlos Junior"), is (false));
	}

	@Quando("eu desço até o final da pagina")
	public void eu_desço_até_o_final_da_pagina() {
		
	}

	@Quando("vejo o item de Termos e Condinçoes de Uso")
	public void vejo_o_item_de_termos_e_condinçoes_de_uso() {
		assertThat(homePage.obterTermoseCondicoes(), is("Terms and conditions of use"));
	}

	
	@Quando("clico")
	public void clico() {
		termosPage = homePage.clicarBotaoTermoseCondicoes();
	}
	
	TermosECondicoesPage termosPage;
	@Então("sou direcionado para a página de Termos de Uso")
	public void sou_direcionado_para_a_página_de_termos_de_uso() {
		assertThat(homePage.obterTituloPagina(), is("Terms and conditions of use"));
	}

	@Então("visualizo o texto descrito dos Termos de Uso")
	public void visualizo_o_texto_descrito_dos_termos_de_uso() {
		assertThat(termosPage.obterTextoTituloSessao(), is("Terms and conditions of use"));
		assertThat(termosPage.obterTextoPrimeiroSubtitulo(), is("Rule 1"));
		assertThat(termosPage.obterTextoPrimeiroParagrafo(), is("Lorem ipsum dolor sit amet conse ctetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
	}
	
	@After (order = 1)
	public void capturarTela(Scenario scenario) {
		TakesScreenshot camera = (TakesScreenshot) driver;
		File capturaDeTela = camera.getScreenshotAs(OutputType.FILE);
		try {
			String scenarioId = scenario.getId().substring(scenario.getId().lastIndexOf(".feature:") + 9);
			String nomeArquivo = "resources/screenshots/" + scenario.getName() + "_" + scenarioId + "_" + scenario.getStatus() + ".png";
			System.out.println(nomeArquivo);
			Files.move(capturaDeTela, new File(nomeArquivo));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@After (order = 0)
	public static void finalizar() {
		driver.quit();
	}
}
