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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Random;

/**
 * CurvedLineNoiseProducer class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class CurvedLineNoiseProducer implements NoiseProducer {

	private static final Random RAND = new SecureRandom();

	private final Color _color;
	private final float _width;

	public CurvedLineNoiseProducer() {
		this(new Color(102, 204, 255), 2.0f);
	}

	public CurvedLineNoiseProducer(Color color, float width) {
		_color = color;
		_width = width;
	}

	@Override
	public void makeNoise(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();

		// the curve from where the points are taken
		CubicCurve2D cc = new CubicCurve2D.Float(width * .1f, height * RAND.nextFloat(), width * .05f, height * RAND.nextFloat(), width * .25f,
				height * RAND.nextFloat(), width * .9f, height * RAND.nextFloat());

		// creates an iterator to define the boundary of the flattened curve
		PathIterator pi = cc.getPathIterator(null, 2);
		Point2D tmp[] = new Point2D[200];
		int i = 0;

		// while pi is iterating the curve, adds points to tmp array
		while (!pi.isDone()) {
			float[] coords = new float[6];
			switch (pi.currentSegment(coords)) {
			case PathIterator.SEG_MOVETO:
			case PathIterator.SEG_LINETO:
				tmp[i] = new Point2D.Float(coords[0], coords[1]);
			}
			i++;
			pi.next();
		}

		// the points where the line changes the stroke and direction
		Point2D[] pts = new Point2D[i];
		// copies points from tmp to pts
		System.arraycopy(tmp, 0, pts, 0, i);

		Graphics2D graph = (Graphics2D) image.getGraphics();
		graph.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

		graph.setColor(_color);

		// for the maximum 3 point change the stroke and direction
		for (i = 0; i < pts.length - 1; i++) {
			if (i < 3) {
				graph.setStroke(new BasicStroke(_width));
			}
			graph.drawLine((int) pts[i].getX(), (int) pts[i].getY(), (int) pts[i + 1].getX(), (int) pts[i + 1].getY());
		}

		graph.dispose();
	}
}
