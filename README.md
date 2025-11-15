# Informações Gerais

O Toma ai! é uma aplicação simples que tem como objetivo ajudar os usuários a se lembrarem de tomar o seu medicamento.

## Estrutura do Repositório

O projeto é dividido nos seguintes módulos e submódulos.

```kotlin
├── app/                            # Documentação
├── features/                       # Contém as principais features do aplicativo
├────── features/createmedicine     # Contém a tela de cadstro de medicamentos
├────── features/home               # Tela inicial do aplicativo após logar, exibe os medicamentos
├────── features/onboarding         # Tela inicial do aplicativo, contém o login e o cadastro do usuários
├────── features/updatemedicine     # Tela para cadastrar um novo medicamento
├── libraries/                      # Componentes de interface compartilhado entre as telas
├────── libraries/auth              # Módulo responsável por gerenciar a autenticação do usuário
├────── libraries/data              # Módulo responsável pelo gerenciamento de dados no app
├────── libraries/design_system     # Módulo pelo design system do aplicativo, contendo elementos comuns e padrões de design
├────── libraries/navigation        # Módulo responsável pela navegação entre telas
├── .gitignore                      # Arquivos a serem ignorados pelo Git
└── README.md                       # Este arquivo
```

## Arquitetura de referência

O Toma ai! foi fei desenvolvido utilizando Kotlin. A arquitetura escolhida foi MVI e para a construção de interface foi utilizado o Jetpack Compose.
Para a autenticação do usuário foi utilizado o Firebase Authentication e para gerenciamento de dadosfoi utilizado o Firebase FireStore.

## Executar localmente

Ter o Android Studio (AS) e Java configurado na máquina.

## Pré-requisitos

* JDK 11;
* Android Studio;
* Um dispositivo físico ou emulador;

## Instruções de Instalação e Execução

* Abrir o projeto com o Android Studio
* Se certificar que a versão do JDK utilizada pelo Android Studio é a 11
* Esperar o AS sincronizar
* Rodar o projeto (^R)
