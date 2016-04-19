/*
 * Copyright (c) 2010-2011, Monash e-Research Centre
 * (Monash University, Australia)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 	* Redistributions of source code must retain the above copyright
 * 	  notice, this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright
 * 	  notice, this list of conditions and the following disclaimer in the
 * 	  documentation and/or other materials provided with the distribution.
 * 	* Neither the name of the Monash University nor the names of its
 * 	  contributors may be used to endorse or promote products derived from
 * 	  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edu.monash.merc.util.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Render a word using the given color and from a randomly selected font chosen from a list of fonts given in the
 * constructor.
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class DefaultWordRenderer implements WordRenderer {

    private static final Color DEFAULT_COLOR = new Color(12, 48, 57);
    private static final List<Font> DEFAULT_FONTS = new ArrayList<Font>();

    private final Color _color;
    private final List<Font> _fonts;

    static {
        DEFAULT_FONTS.add(new Font("Arial", Font.ITALIC, 38));
        DEFAULT_FONTS.add(new Font("Courier", Font.ITALIC, 38));
    }

    /**
     * Will render the characters in black and in either 40pt Arial or Courier.
     */
    public DefaultWordRenderer() {
        this(DEFAULT_COLOR, DEFAULT_FONTS);
    }

    public DefaultWordRenderer(Color color, List<Font> fonts) {
        _color = color != null ? color : DEFAULT_COLOR;
        _fonts = fonts != null ? fonts : DEFAULT_FONTS;
    }

    /**
     * Render a word onto a BufferedImage.
     *
     * @param word  The word to be rendered.
     * @param image The BufferedImage onto which the word will be painted on to
     */
    @Override
    public void render(String word, BufferedImage image) {
        Graphics2D g = image.createGraphics();

        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g.setRenderingHints(hints);

        g.setColor(_color);
        FontRenderContext frc = g.getFontRenderContext();
        int startPosX = 25;
        char[] wc = word.toCharArray();
        Random generator = new Random();
        for (char element : wc) {
            char[] itchar = new char[]{element};
            int choiceFont = generator.nextInt(_fonts.size());
            Font itFont = _fonts.get(choiceFont);
            g.setFont(itFont);

            GlyphVector gv = itFont.createGlyphVector(frc, itchar);
            double charWitdth = gv.getVisualBounds().getWidth();

            g.drawChars(itchar, 0, itchar.length, startPosX, 35);
            startPosX = startPosX + (int) charWitdth + 3;
        }
    }
}
