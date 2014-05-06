package pe1314.g11.pr3;

import java.util.Objects;

import pe1314.g11.util.XorShiftRandom;

/**
 * Representation of the state of a game.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 */
public final class GameState {

    /** Size of the board */
    public static final int SIZE = 20;

    /**
     * Coordinates of the elements of the game.
     * 
     * @author Daniel Escoz Solana
     * @author Pedro Morgado Alarcón
     */
    public static class Coord {
        public final int x;
        public final int y;

        private Coord (int x, int y) {
            this.x = x;
            this.y = y;
        }

        public static Coord of (int x, int y) {
            if (x < 0 || x >= SIZE) {
                throw new IndexOutOfBoundsException("x: " + x);
            }
            if (y < 0 || y >= SIZE) {
                throw new IndexOutOfBoundsException("y: " + y);
            }
            return new Coord(x, y);
        }

        @Override
        public String toString () {
            return "(" + x + "," + y + ")";
        }
    }

    public static enum Move {
        LEFT, RIGHT, SHOOT;
    }

    private Coord ship;

    private Coord alien;

    private Coord shot;

    public GameState (Coord ship, Coord alien, Coord shot) {
        Objects.requireNonNull(ship, "ship");
        Objects.requireNonNull(alien, "alien");

        if (ship.y != 0) {
            throw new IllegalArgumentException("ship.y != 0 (" + ship.y + ")");
        }

        this.ship = ship;
        this.alien = alien;
        this.shot = shot;
    }

    public void advance (Move move) {
        if (shot != null) {
            shot = shot.y == SIZE - 1 ? null : Coord.of(shot.x, shot.y + 1);
        }

        if (alien.y % 2 == 0) {
            // Go left!
            if (alien.x == 0) {
                alien = Coord.of(alien.x, alien.y - 1);
            } else {
                alien = Coord.of(alien.x - 1, alien.y);
            }

        } else {
            // Go right!
            if (alien.x == SIZE - 1) {
                alien = Coord.of(alien.x, alien.y - 1);
            } else {
                alien = Coord.of(alien.x + 1, alien.y);
            }
        }

        if (move != null) {
            switch (move) {
                case LEFT:
                    ship = Coord.of(Math.max(0, ship.x - 1), ship.y);
                    break;

                case RIGHT:
                    ship = Coord.of(Math.min(SIZE - 1, ship.x + 1), ship.y);
                    break;

                case SHOOT:
                    if (shot == null) {
                        shot = Coord.of(ship.x, ship.y + 1);
                    }
                    break;
            }
        }
    }

    public Coord getShipCoord () {
        return ship;
    }

    public Coord getAlienCoord () {
        return alien;
    }

    public Coord getShotCoord () {
        return shot;
    }

    public int getDistX () {
        int dist = Math.abs(alien.x - ship.x);

        if ((alien.y % 2 == 0 && alien.x > ship.x) || (alien.y % 2 == 1 && alien.x < ship.x)) {
            return dist;
        } else {
            return -dist;
        }
    }

    public int getDistY () {
        return alien.y;
    }

    public boolean finished () {
        return won() || lose();
    }

    public boolean won () {
        return (shot != null && (shot.x == alien.x && shot.y == alien.y));
    }

    public boolean lose () {
        return alien.y == 0;
    }

    @Override
    public String toString () {
        return "[ship=" + ship + "; alien=" + alien + "; shot=" + shot + "]";
    }

    public static GameState newRandom (XorShiftRandom random) {
        return new GameState(Coord.of(random.nextInt(SIZE), 0), Coord.of(
            random.nextInt(SIZE), SIZE - 1 - random.nextInt(6)), null);
    }
}
