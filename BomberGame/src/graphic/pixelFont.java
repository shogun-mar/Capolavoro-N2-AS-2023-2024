package graphic;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class pixelFont {

    Font pixelFont;

    public pixelFont(){
        try{
            File font_file = new File("graphics" + File.separator + "fonts" + File.separator + "pixelFont.ttf");
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, font_file).deriveFont(36f);
        } catch (FontFormatException | IOException ex){
            ex.printStackTrace();
        }
    }

    public Font getPixelFont(){
        return pixelFont;
    }
}
