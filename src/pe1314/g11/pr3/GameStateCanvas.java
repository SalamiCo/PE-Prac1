package pe1314.g11.pr3;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * A Swing component that shows a <tt>GameState</tt> object.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 */
public class GameStateCanvas extends JComponent {
    private static final long serialVersionUID = -8699666715467480924L;

    /** Size of the sprites */
    private static final int SPRSIZE = 16;

    /** Game state to show */
    private GameState state;

    public GameStateCanvas () {
        this(null);
    }

    public GameStateCanvas (GameState state) {
        this.state = state;

        setSize(SPRSIZE * GameState.SIZE, SPRSIZE * GameState.SIZE);
        setPreferredSize(getSize());
    }

    @Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getSize().width, getSize().height);

        if (state != null) {
            g.setColor(Color.RED);
            g.fillRect(
                state.getAlienCoord().x * SPRSIZE, (GameState.SIZE - state.getAlienCoord().y - 1) * SPRSIZE, SPRSIZE,
                SPRSIZE);

            g.setColor(Color.GREEN);
            g.fillRect(
                state.getShipCoord().x * SPRSIZE, (GameState.SIZE - state.getShipCoord().y - 1) * SPRSIZE, SPRSIZE,
                SPRSIZE);

            if (state.getShotCoord() != null) {
                g.setColor(Color.YELLOW);
                g.fillRect(
                    state.getShotCoord().x * SPRSIZE, (GameState.SIZE - state.getShotCoord().y - 1) * SPRSIZE, SPRSIZE,
                    SPRSIZE);
            }
        }
    }
}
