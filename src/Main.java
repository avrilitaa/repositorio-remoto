import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

enum Suite {
    CORAZONES("Corazones", "♥"),
    DIAMANTES("Diamantes", "♦"),
    TREBOLES("Tréboles", "♣"),
    PICAS("Picas", "♠");

    final String suiteName;
    final String suiteSymbol;

    Suite(String suiteName, String suiteSymbol) {
        this.suiteName = suiteName;
        this.suiteSymbol = suiteSymbol;
    }

    String getSuiteName() {
        return suiteName;
    }

    String getSuiteSymbol() {
        return suiteSymbol;
    }
}

enum Figure {
    DOS("2", 2), TRES("3", 3), CUATRO("4", 4), CINCO("5", 5),
    SEIS("6", 6), SIETE("7", 7), OCHO("8", 8), NUEVE("9", 9),
    DIEZ("10", 10), JACK("J", 10), QUEEN("Q", 10), KING("K", 10), AS("A", 11);

    final String figureName;
    final int figureValue;

    Figure(String figureName, int figureValue) {
        this.figureName = figureName;
        this.figureValue = figureValue;
    }

    String getFigureName() {
        return figureName;
    }

    int getFigureValue() {
        return figureValue;
    }
}

class Card {
    final Suite suite;
    final Figure figure;
    boolean isTaken = false;

    Card(Suite suite, Figure figure) {
        this.suite = suite;
        this.figure = figure;
    }

    String getCardName() {
        return figure.getFigureName() + suite.getSuiteSymbol();
    }

    int getCardValue() {
        return figure.getFigureValue();
    }

    boolean isCardTaken() {
        return isTaken;
    }

    void takeCard() {
        isTaken = true;
    }
}

class Deck {
    Card[] cards;

    Deck() {
        cards = new Card[52];
        int index = 0;
        for (Suite suite : Suite.values()) {
            for (Figure figure : Figure.values()) {
                cards[index] = new Card(suite, figure);
                index++;
            }
        }
    }

    void shuffle() {
        Random random = new Random();
        for (int i = 0; i < cards.length; i++) {
            int randomIndex = random.nextInt(cards.length);
            Card temp = cards[i];
            cards[i] = cards[randomIndex];
            cards[randomIndex] = temp;
        }
    }

    Card drawCard(Player player) {
        for (Card card : cards) {
            if (!card.isCardTaken()) {
                card.takeCard();
                player.cards.add(card);
                return card;
            }
        }
        return null; // Si no hay más cartas disponibles
    }
}

class Player {
    String name;
    ArrayList<Card> cards = new ArrayList<>();

    Player(String name) {
        this.name = name;
    }

    void showHand() {
        System.out.println(name + ", tus cartas son:");
        for (Card card : cards) {
            System.out.println(card.getCardName());
        }
    }

    int getHandScore() {
        int score = 0;
        int aceCount = 0;
        for (Card card : cards) {
            score += card.getCardValue();
            if (card.figure == Figure.AS) {
                aceCount++;
            }
        }
        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }
        return score;
    }
}

class Blackjack {
    Deck deck;
    Player player1;
    Player player2;

    Blackjack(String player1Name) {
        deck = new Deck();
        deck.shuffle();
        player1 = new Player(player1Name);
        player2 = new Player("Jugador 2");
    }

    void startGame() {
        System.out.println("¡Bienvenido al juego de Blackjack!");
        deck.drawCard(player1);
        deck.drawCard(player1);
        deck.drawCard(player2);
        deck.drawCard(player2);

        player1.showHand();
        System.out.println(player1.name + " tu puntaje es: " + player1.getHandScore());

        System.out.println("La primera carta de " + player2.name + " es: " + player2.cards.get(0).getCardName());
    }

    void getWinner() {
        int player1Score = player1.getHandScore();
        int player2Score = player2.getHandScore();

        System.out.println(player1.name + " tiene un puntaje de: " + player1Score);
        System.out.println(player2.name + " tiene un puntaje de: " + player2Score);

        if (player1Score > 21) {
            System.out.println(player1.name + " ha perdido, puntaje mayor a 21.");
        } else if (player2Score > 21) {
            System.out.println(player2.name + " ha perdido, puntaje mayor a 21.");
        } else if (player1Score > player2Score) {
            System.out.println(player1.name + " ha ganado.");
        } else if (player2Score > player1Score) {
            System.out.println(player2.name + " ha ganado.");
        } else {
            System.out.println("¡Empate!");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduce el nombre del Jugador 1:");
        String player1Name = scanner.nextLine();

        Blackjack blackjack = new Blackjack(player1Name);
        blackjack.startGame();

        // Simulando pedir cartas y decidir el ganador
        String respuesta;
        while (blackjack.player1.getHandScore() < 21) {
            System.out.println("¿" + blackjack.player1.name + ", deseas una carta adicional? (si/no)");
            respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("si")) {
                blackjack.deck.drawCard(blackjack.player1);
                blackjack.player1.showHand();
                System.out.println(blackjack.player1.name + " tu puntaje es: " + blackjack.player1.getHandScore());
            } else {
                break;
            }
        }

        blackjack.getWinner();
    }
}
