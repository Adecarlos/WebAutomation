# language: pt

@fluxopadrao @termos
Funcionalidade: Acessar Termos e Condições
  Como um usuario
  Eu quero visualizar os termos e condições
  Para saber sobre meus direitos

  Cenário: Deve acessar a página de termos e condições e visualizar as informações
    Dado que eu estou na tela inicial
    E não estou logado na minha conta
    Quando eu desço até o final da pagina
    E vejo o item de Termos e Condinçoes de Uso
    E clico
    Então sou direcionado para a página de Termos de Uso
    E visualizo o texto descrito dos Termos de Uso
