# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](240 Endpoints.svg)](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUmB0kOAAGsYAAhYAcCIoNEYw7FUQnUHFS7E0mo9GxACiAA8VNgCIlqdRKHTCtlzOUACxOJzdfqjdTAez3KZsqDeMowPRkyGU2J9A7oDj8qi006yeRKFTqcoKsAAVQ6sJud2xpsUyjUqhOhWM5TIbIAMmy4PUYAAqGAAMXCNQAsjAbjA0pRDDcXI7gKMYABeOMdO44vGEu2C5IZVI5obpwzABB4jhhFAc6EaF3m91CooiFTlItQF2G430gVqhQ1lCkyz1AnoTnc3loQ2C3KnEUYcVOADM0r6stU8sVfWVqvKaYz1drYXk+PQurMnEwLbd6nbYK7MDQPgQCH7Kk9Mm0rctEA8UyHsHQ6F1nX-R8PVOb0AEkADlfXCYNEPqGpyzhE8UFmF43g4WYfm2TEYAANQUP0bTZMgYDAitRlw-RXiWQjNmI79DBNKCLVUUogLHTIFB8MBUlhYBhNSCD724ttYJ0UobSqaQFHqNlMPINlg3EkT6ggK8kmzbTUl0-SYAAdRYNlwjU7Cs0wvMX0405AQuNUEWhZE1A-LAXOBZ8GTVa5c0rSZylWJ4jJM68YDC-YFxBZdkFFGAACZJS3bDQqmR4pkivTotim99VMLxfH8AJoHYBUYD9CBoiSAI0gyLIkryQojkZao6iaVoDHUBI0GlbDGIWJYDg6kFCl8wUrimTKJi+PClhWSYAXOPzByNV8EDqpA0FhWr6pZDFsVxMdCRJMljtiDj-KHcpLuusAZxQHkBvi9sVzANcpV6bd3T3cYlRVaByg1GAtVZMAirvRzfwfHirRQW17WwyCzWg39vQ0gMg1DCMo1jeNE0hTDU2CjNs2w-NzpgHsjAgNQ3wgZgG2haSMZ4u6tpQcp30-W6l3agLygQlnw18TgXrevkJs+1rvpgCUxS3HdAbCw9QfUtAWZgAAzSXlj1O8EdkzbwTp8DtBpgkYDgtAQGgSFwBgfiOHMJBqw0OGhY7Epu2CCToCQAAvFAOGlucPt9r61wARlVgGFSBg8QbVHxA5E4Ow6N28DR9wpTcA4CUCEkSxIkqSi5gr15MU5TVPU30tIkqKDJgPLTIsqybIpwwqb7wXnPWxlDr2yJVG8zBpoS4X7pgIL6LGGLsq+TuCtX8aAvlnJFbS37F9uEKV-C3LW-ytAstWY2DU8bw-ECLwUHQGq6t8ZhGvSTJMC+38JvKCo0h-SaTZM0FofVVADW6Oveccth5AhmgvM+OkL4rRgGtBBk0-YWx2vYd+B037CSeqdAsTIroUihkPOe-syFPUju9OBiVd4-UTnKZOGs05g1JBDChGIYb5xpD+LinN3RIzAGXUSMD0aui5nJH0wC8ZhkjDGdS3drId3PqZQymj0A20LB0BmTMdas0bLEFoe0qDViQGSGBHMZFm2oRbfmX4fabUZGLMAEsM4Ry5K9KOjDhQK3jqw3c7DgZHjjJnVI2dw78LsQBGC5tXwSL7AXP8IjLTu24IJCSFcRJVxkk+OR9cVK90FM3DRKCtFvg-Agcyll1EwLsk0hQCFpD2UrHZamaTq5iIkXkyS2hpEJKxvJX0uNgxKMJqohpakmnaKqbos6ttwy-HDkYFA2T3rV25hbFJ1tXHUMZPbGEwQECaygPQ2W28Y5BJgAAVnSn9NWYTU4RKycjAa+s1m52Kjs32jk+a1Ojkw5K+9pQ3xKvfcqkIyR+mhDAAA4pWD0n9mo-wVn-EWlREWaTAfYSs0CdGwJuVNEesBZp9BgWgjBrksWdl5jAZAsRkWygOtCVlahiGC0LoU3iMBrT9KMgUjJNcjBjIUZMgmKj4xqLmcS5pxK9F0yiTEg0-ykmMucVQv2jIbSqqgKHcOVySVDh3slCUCdnlJ33Bc48BqjW-JNny3ZyTK4HMEZxXloryjMrAJy1QsJhmYzkTjQMUrlGxgJaMeJIbNXlAUCqYAlgAA8nKXT5B1TPco8KWUoonlPGe3NGQ9CmNGtQQMKj9HLXBaQQM44pXXGKJ4TUQJ9xWk8HQCBQD4h7HcDtZbKwIUrAtPYMBGhbzNbc5hlR96NGlIO2Ulbq2VlrfWxtzapitpQH2kKC1O3dpAL29t+7F0oGHaMUd47IV3zKoEbAPgoDYG4PAEuSLOlou-r-NxaouoNHxYSqJbdpTlovSgSdxx4F0spaBvdfQ17EppdPcl9KeblH4nIFAnLYRwBLpy7lyyLrcKejq-+tDeHsl8TLU1kHQWriVk8mUNqU52vVNwyGfDIU9L5X03JwqhmxtkbXUo4zw340jTMnulTjIX0VYs+chGVVGTVYJhx2DXzasObqtU+rlOGpziakFgSZ2WpCercJWsM56cdXEjVji3X5I9Qy+GPHXZ4crLCctIr7FFOE3bJCVlUIIXQgKjzNbpCzG3buhi6pD3HqXrMWDMXy01B0AAKxQOAEi5FKLUVouF2YzjCu1MS0OyspXRipYy1lrN5Lyi4YElhysBaEA+RQwC7FABIUtfRwvrqbegoz8A7kVDnQu3rq663lAbQNm9pUH4BEsJsnayQYAACkIB7XfRmAIXae0Yt3qhzqVQbQ9RaOWol8npTPuTZQOAEAdpQAqygWtEHFxkswSvHrfXT3PG7UtqA93HsDqpYhhag2i3xpgGlzbaBsMw72vhijWJlWXXJNqLAWmyOPWR4ZgJw2TOMf+mw21nC2OamR3E7jPqBXIyFe6+QwahPipE5K8T0zZWzOk23OTMn9LKt00HfT4dVNFKh5pz1xadMOoM1R-xNy6OK1M9aknLGydWaFzZyFdn1OMv2fIHl6SfP8utNh8LTPZJ+dE4o6VUbOlypgNYuy4XlVi1CxmA23jReJPs1q4FWPsUeK8VLOXDCFfGYtU4K1THVccIicY-WhtbMuo6wy8o6braKZHOeGAl5dEB-ntnscdZJz6Tx+HgnkfNwq9CaTiJZ5i8XinJfLjkvhHG+PNgLQmRsMZ8Z970Z3YlKlPd4YCpUW+4nATF-Hdk-WntL20e6L-dYs9uXzAefo+quZeYNmFL6Wd-1Kk+F53k2hux1nZKedvQ5vQsflAZNw2xywGANgZ9hB4iJBSDPg75gju-qAQDFUjAWMGjg+1cnQ24DwCDWQ0+yhxACgLhGxGpw71dkQJgP+T80AJAVt0wgAG4ZBgFG4JNy08CwDK96NRsr8IU85MAgA)

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
