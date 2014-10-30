/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomeControl;

import java.io.IOException;
import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author SONY
 */
public class Logo extends CustomItem{
    public Logo() {
        super("");
       
    }
    
    protected int getMinContentHeight() {
        return 100;
    }
    
    protected int getMinContentWidth() {
        return 120;
    }
    
    protected int getPrefContentHeight(int width) {
        return getMinContentHeight()*2;
    }
    
    protected int getPrefContentWidth(int height) {
        return getMinContentWidth()*2;
    }
    
    protected void paint(Graphics g, int w, int h) {
        try {
           
            Image logo = Image.createImage(getClass().getResourceAsStream("world.png"));
            g.drawImage(logo,getPreferredWidth()/2-getPreferredWidth()/3+10,getPreferredHeight()/4, 0);
            g.setColor(128);
            g.drawString("M-Foodie",getMinContentWidth()/2+getMinContentWidth()/5, getPreferredHeight()-getPreferredHeight()/5+10,0);
            
        } catch (IOException ex) {
        }
    }

}
