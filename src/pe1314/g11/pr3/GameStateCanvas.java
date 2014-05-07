package pe1314.g11.pr3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
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

    private static final BufferedImage IMG_ALIEN = loadImage("alien");
    private static final BufferedImage IMG_SHIP = loadImage("ship");
    private static final BufferedImage IMG_SHOT = loadImage("shot");

    private static BufferedImage loadImage (String name) {
        try {
            return ImageIO.read(Object.class.getResource("/pe1314/g11/pr3/" + name + ".png"));

        } catch (IOException exc) {
            exc.printStackTrace();
            throw new RuntimeException(exc);
        }
    }

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
            g.drawImage(IMG_ALIEN, state.getAlienCoord().x * SPRSIZE, (GameState.SIZE - state.getAlienCoord().y - 1)
                * SPRSIZE, null);
            g.drawImage(IMG_SHIP, state.getShipCoord().x * SPRSIZE, (GameState.SIZE - state.getShipCoord().y - 1)
                * SPRSIZE, null);

            if (state.getShotCoord() != null) {
                g.drawImage(IMG_SHOT, state.getShotCoord().x * SPRSIZE, (GameState.SIZE - state.getShotCoord().y - 1)
                    * SPRSIZE, null);
            }
        }
    }
}
