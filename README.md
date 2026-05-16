# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

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

## Design
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+uB5afJCIJqTsXzQo8wHiVQSIwAgQnihignCQSRJgKSb6GLuNL7gyTJTspXI3l5d5LsKYoSm6MpymW7xKpgKrBhqABCIYwCihJqFg7kgdU-rJRwzkZVGMZxoUWlJpUolpnhBE5qoebzNBRYlvUejrmlLkNvRcWqhqbrarq+r+UYEBqDAaAQMwVpojA4pGNoegGIFvLBZZ1k9n224eZZ-pugAchNIq+JwRUoLGCnofCybIKmMAAKzVaMYy1fVBZjE10D1G8Y0TTAABmR27HRTbDg65UdlZLobu6W7xeqMAAOJIDQaAwMZALDaN43MMcYAgPqaPrrNWjyAtYhZWDoGutMl7QEgABeKAcCdZ3xmVV0lGAVUAIw1fyL2NcWH0KtTeq0wzgONgx5NUre-JjhOKDPvE56XteIOLpUD5rgGqtbuTH7mf6jnihkqgAZgOmXb6OVgYR+nzCRqHfBRVH1o7tEXZh13YTAuH4Y9-k0WRYwu4hbukZ1TaeN4fj+F4KDoDEcSJPHieOb4WCiYKlONNIEb8RG7QRt0PRyaoCnDKHSEXRUlv1BRYt5AU9QADxV+g5QW4bUAVO59S2fYGcOUJGcFSSm0VOro4wIyYBKyr8Fh2gc5BfyFTLjAYXrhRkXyu3hSwxq+-fcw-0+BKRPzfoYhT+Vfdjb2-buXfNtUw3UD04zzMlTXFSVTA6YeajGWM9fMAtmrC3fp-XYMAgYMVvr3Z0XZjS63kBPcoU96gcBQNwY8l4F6USXivZaa9ygbxRDMCANAUEvi3BPWu3d6jp1PKbc2lsX4STqCMTSNtyqVT9lmOBjEY4BBROufw2BxQan4tNeGSoNBZ22qWBo8NC4l3sEqSul5XalV4eUOuND4iNxKq3fend2HlHvsgHIDk0Rj1cugzBM8mTz33sQhcqh17Ck3uKbeqDgCyj3tope3UEqGJ0SfP6AMZoo2Jroa+S0PEcIhsg9aT8kHJJ2iLIxH9xbf3OqBPh3tOYAKcEAkYIC+ZgMLILUs58oHiyWEIhBlikH138Y42W9IbJojkTmDE7j9xePqFvSsCAZ7yJiQjJUHoWn3z6SgV8GSGGwiNnY-8CBALd0yVw5YGicwFgaOMfZKAACS0gCxc3CMEQIIJNjxF1CgN0nIvgrGGMsZIoA1TPMgoZPZSpdpKkhBcGAnQeGcKKRzWoAjgFjBOaoQ5xylTnMudc25yx7mPJ+QZGC7yxifJAN8oiDUYL-LmICuYwLQVCOjsxfwHAADsbgnAoCcDECMwQ4BcQAGzwAVtMuYMAijFOzq-RorQOjqM0dknRWYTkUpQOCxMlQDENKbmgUxwSkLlCzGSlACqviwK7qs+E1tOz1EPHIFAGI4AK3sW5JBgonGz1cVq9Agzgqa1Cr4wxu9wkhMPjrReSFIlnwvrEq+i0p7ZXNQ-Da+szU1DfjTXJX9oynR-mzCqxTua81zNUt6tTPrZLFozSO8CukCgpm031esMkYMrfLI8KAFkYnlUqD1oMvUrm1mgFAmwJmCsgAKxZW45k1oWUszs7YDG2ubawzZxqvwxqTTACpcLkUXPqFcm5Rqs3wBzb7B666Tkou3Wio1XUeg9GDHjY0gsB3ZGNH9c+4AFIwBACafstLY6WBwbZAdsQkAJDAH+vsEAB0ACkICzQWTEAlaphUc1FZw+ozRmQyR6CcrRwb0BZmwAgYAf6oBwAgLZKAaxT3SCVRhA2Jr2mi1yeqzVuG0A6thQRojlBSPkb2AAdRYKcouPREr8QUHAAA0t8KjqLd1mRNShlJ9QABWMG0AYmgybFA6Vx7SxkI25xc98FuMSfue83qJQ720IE-11dA1KwiVjKJ59CYRpJgk6N1bY1pM2iurJDS03FQKTbL2ULSnlKelUhqNSIH1JTdA8tpmVqJuslZtBz8G2r3pLPVtVHO0ayFPUDIFhUDUJOf1PUM8kaJyo0lteiDY2Tphj1VGhHiM8egJWM0NZwy6M7Ds+ogZuthiQvk1mvC-6HoaOmYI3R12gOi4WiBJpuswFrG7IRGWnF+GJi2pUGJOPtbI9ASjm78tVu7ZKbAu3B2pP7dFGAwxVPilUD0Y4dYBSUJoHV9QyTrJNfS46sGBjNNoAXVshTXnV3cM9mDf+MKYddR-QELwRGk7AZTlANHiBgywGANgAjhB1VCsUYmySecC5FxLsYGu+jGG9BpxY++IBuB4BtazqA9rOlZeniz3HAyfueLId4jIX3DBfuiqoU7OY1iPDW0vDQW2DN4xQISiK2gBfju84-bnJDukq8JUrHkmvK1-chj5pXPOLXxFV2qAHwATc87N6knXz8VlflqDjvA4Ol1wgGzDwpk2oUI6EUAA
