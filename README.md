# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](Endpoints.svg)]
(https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUmB0kOAAGsYAAhYAcCIoNEYw7FUQnUHFS7E0mo9GxACiAA8VNgCIlqdRKHTCtlzOUACxOJzdfqjdTAez3KZsqDeMowPRkyGU2J9A7oDj8qi006yeRKFTqcoKsAAVQ6sJud2xpsUyjUqhOhWM5TIbIAMmy4PUYAAqGAAMXCNQAsjAbjA0pRDDcXI7gKMYABeOMdO44vGEu2C5IZVI5obpwzABB4jhhFAc6EaF3m91CooiFTlItQF2G430gVqhQ1lCkyz1AnoTnc3loQ2C3KnEUYcVOADM0r6stU8sVfWVqvKaYz1drYXk+PQurMnEwLbd6nbYK7MDQPgQCH7Kk9Mm0rctEA8UyHsHQ6F1nX-R8PVOb0AEkADlfXCYNEPqGpyzhE8UFmF43g4WYfm2TEYAANQUP0bTZMgYDAitRlw-RXiWQjNmI79DBNKCLVUUogLHTIFB8MBUlhYBhNSCD724ttYJ0UpEOQ1CEPQmBxJEujbkrWZ1NSeoICvNASPIyjqNo7CdIk-TDOxF9ONOQELjVBFoWRNQPywRzgWfBk1WuXNK0mcpVieXTrOvGBgv2BcQWXZBRRgAAmSUt2woKpkeKYwoMiKopvfVTC8Xx-ACaB2AVGA-QgaIkgCNIMiyeK8kKI5GWqOomlaAx1ASNBpQsmA8KWA5WpBQovMFK4pjSiYviG94VkmAFzm8wcjVfBBqqQNBYSqmqWQxbFcTHQkSTJA7Yg4nyh3KM6LrAGcUB5XqYvbFcwDXKVem3d093GJUVWgcoNRgLVWTAfK7zs38Hx4q0UFte1sMgs1oN-b1yH9QNgzDSMY0whNUiTTDUwCjNs2w-MTpgHsjAgNQ3wgZgG2haTUZ4671pQcp30-K6lxa3zygQpnw18ThHuevlRrepqPpgCUxS3Hc-uCw8gYJtAmZgAAzcXlj1O9YdktbwRp8DtCpgkYDgtAQGgSFwBgfiOHMJBqw0aGBY7Epu2CCToCQAAvFAOEludXu9961wARmV36FX+g9AbVHx-ZEwOQ4N28DS9wpjcA4CUCEjTdKkguYK9eTFLZFCbZUjDdM0u5LJE8KjLIiiqJo5vtLUqycqM-mHJWxk9u2yJVA8zAJtiwWbpgfz6LGSKMq+bLDPS1YRt82Wcnl5KvqXrTRi3zK+g33K18Ng1PG8PxAi8FB0Eq6rfGYOr0kyTB3t-UbygqNILG9Q2TNBaN1VQvVuiX3nDLEeQJJqLyygPTes0YDLQQWNH2ZtNr2HfrtN+wl7pHQLEyc6FJwbD3nr7Mh91w4vTgXFfen145ykTmrFOwNSSgwoRiSGucaQ-i4uzd08MwAl1EjAlGroOZyR9FjIMoYIxRljLpGAAB1Fgtc2T9zboPLMui9KDytoWDodMGZa2Zo2WILRtpUGrEgMkMC2YyJNtQs2vMvxezWoyEWYAxZpzDlyJ6EdGHCjlrHVhu52EAyPHGdOqRM6h34S4gCMFTavgkX2POf4RGWldtwQSEkxISXLjJJ8cigEBhAco-GajNHaMMe3AxMCYAKAQtITCdwDGUxyRXMREiSkiTKXkyuRh5K+gDIo3GKjDEaK0eEHRrTszOOOtbcMvxQ5GBQIUl6FdOZmyyZbbx1DGS2xhMEBA6soD0OlrvKOESYAAFYUrfRVjE5OcSCkI16rrTZ2cCr7O9nZHmH4vH3KYQlQ+0ob6FXviVSEZI-TQhgAAcUrB6T+DUf5yz-kLSoqK2SdRaPYSs0CUHoEjuNUesApoXwpWgRa6CZ40rxZ2bmMBkCxHRbKXa0IeVqGIfzfO5TeIwGtIMsu2hpFpPRhMhRONamqIkvMxpyymn6PaZ07CPSyZiDWYWBJSSDRAoyRyzxVCfaMhtEaqAwdQ63NgRC8JzCFZODjm8hO+5rnHltfagFRtRUHMyaU45gjOIitGeULlYABWqFhDKtGcjMZTMVXjWMpLRipKTWa8oCgVTAEsAAHgFS6fIlrZ7lGRdyjFk9p6z05oyHoUxM1qH+hUfora4LSH+jHRK64xRPHqiBPVi0ng6AQKAfEPY7hjpbZWBClZZp7BgI0HeQ494JQqIfRo0p52ynbZ2ys3be39sHVMYdKAZ2BVmuOydIBp2jtvfulAi7RjLtXbCu+xVAjYB8FAbA3B4BFzRZWFIX9Gr7zZTQ9qDQwGtvJXowy0pW1vpQOu448CnKr2bX0VDN6+jrwZUyjB2Hc3OyLgK2EcBKOViFQash5JtRYBOVatUd1eHsmCVLJ1G6HmuolEfH6bDvWcPVNwsGfDYV9NFQM4pUr5CJtkVXUokzsZKPTXMhpiyNWGRaQytpHSulgYpnqkxNM-VZ2zbI8jFrWP-ws7pY1jrI6QtXG6j1MovVJx9fEpzdqs4pNNe4kNwyw3sphrJijAkUBUdbSM1xFSVP1yUvXVSrbYRdukLMS916GLqnvY+5esx8P5dbTUHQAArFA4BjJdzMpl492W3xgtmJ4krC6+7laqzVrEFaaXlBozFgVdaECeVZcC-FABIXDWXT0DuZWE+Ajzt2Sl3b0F9J7yh9oW1+oqD8AiWB2ZtZIMAABSEBtqgYzAECdU6cVQZ8WqaoNpiUIYSe3aUgHC2UDgBATaUAOujG7Rhxc1LME4c2z259zxJ1HagH9gHc76VIevGg0jq0Qscsq5dtAVGcfbRG5xvrDGzpMcofZ-FHHmMuaW9HN1Qn3mibiSDSTOppPhsi1G8VCNJWhsU9Z2SKm1PTKVVphZSyDMrIM1q4z5M5f6tITa-z-rBcVNs2Cy1DnlcBwCw67joTnXLYE+6qJqtYkazTirwLsLgvYNC5JcLXMueJbFdaOLTWlNC-GfI1NGnZmttVTpxxBisvmZFuKsDetAlq-SVj0FfNKcLz8QEiWBuGFG-pxKTzwnonM41pY3W+sgtBsm+y8opbLYMZHOeGAl5KVJ5oTXscdZJyGVp5nx5EpNyepEz5sTZ4W8XinIyjnEXhGu+PNgLQmQqOV4F0C5LNoqjSAUDUwPvpgy5b1ScQmDU8uGFl3dh9B+DHH6KyfQ-RnuvVadtmG-vWg86Ky6HprrmXVbp3TCnOcKf0BC8IWstmOLAMANgIBoQPEIkOBtir-E9gAlUkSqAq0MYFSmcJguUCANwHgAmiyhDuRpgcATgTJtzgQdgV7klj7jIMAjoppjcAANxUHVI0EB6VgMEIE1Kaa6R0GoH06rZODrY9CwpAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
