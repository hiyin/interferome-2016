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

package edu.monash.merc.util.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

/**
 * ImageScale class provides the resize image function.
 * 
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
public class ImageScale {

	private int width;
	private int height;
	private int zoomWidth;
	private int zoomHeight;
	private File destFile;
	private BufferedImage srcBufferImage;

	private float alpha = 0.7f;

	public static ImageScale resizeFix(File srcFile, File destFile, int width, int height) throws IOException {
		return new ImageScale(srcFile, destFile, width, height);
	}

	public static ImageScale resizeFix(BufferedImage bufImg, File destFile, int width, int height) throws IOException {
		return new ImageScale(bufImg, destFile, width, height);
	}

	protected ImageScale(File srcFile, File destFile, int zoomWidth, int zoomHeight) throws IOException {
		this.destFile = destFile;
		this.zoomWidth = zoomWidth;
		this.zoomHeight = zoomHeight;
		this.srcBufferImage = ImageIO.read(srcFile);
		this.width = this.srcBufferImage.getWidth();
		this.height = this.srcBufferImage.getHeight();
		if (width <= zoomWidth && height <= zoomHeight) {
			FileUtils.copyFile(srcFile, destFile);
		} else {
			resizeFix();
		}
	}

	protected ImageScale(BufferedImage srcBufferImage, File destFile, int zoomWidth, int zoomHeight) throws IOException {
		this.destFile = destFile;
		this.zoomWidth = zoomWidth;
		this.zoomHeight = zoomHeight;
		this.srcBufferImage = srcBufferImage;
		this.width = this.srcBufferImage.getWidth();
		this.height = this.srcBufferImage.getHeight();
		resizeFix();
	}

	protected void resizeFix() throws IOException {
		if (width <= zoomWidth && height <= zoomHeight) {
			zoomWidth = width;
			zoomHeight = height;
			resize(width, height);
		} else if ((float) width / height > (float) zoomWidth / zoomHeight) {
			zoomHeight = Math.round((float) zoomWidth * height / width);
			resize(zoomWidth, zoomHeight);
		} else {
			zoomWidth = Math.round((float) zoomHeight * width / height);
			resize(zoomWidth, zoomHeight);
		}
	}

	private void resize(int w, int h) throws IOException {
		BufferedImage imgBuf = scaleImage(w, h);
		File parent = destFile.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		ImageIO.write(imgBuf, "jpeg", destFile);
	}

	private BufferedImage scaleImage(int outWidth, int outHeight) {
		int[] rgbArray = srcBufferImage.getRGB(0, 0, width, height, null, 0, width);
		BufferedImage bfImg = new BufferedImage(outWidth, outHeight, BufferedImage.TYPE_INT_RGB);
		double hScale = ((double) width) / ((double) outWidth);
		double vScale = ((double) height) / ((double) outHeight);

		int winX0, winY0, winX1, winY1;
		int valueRGB = 0;
		long R, G, B;
		int x, y, i, j;
		int n;
		for (y = 0; y < outHeight; y++) {
			winY0 = (int) (y * vScale + alpha);
			if (winY0 < 0) {
				winY0 = 0;
			}
			winY1 = (int) (winY0 + vScale + alpha);
			if (winY1 > height) {
				winY1 = height;
			}
			for (x = 0; x < outWidth; x++) {
				winX0 = (int) (x * hScale + alpha);
				if (winX0 < 0) {
					winX0 = 0;
				}
				winX1 = (int) (winX0 + hScale + alpha);
				if (winX1 > width) {
					winX1 = width;
				}
				R = 0;
				G = 0;
				B = 0;
				for (i = winX0; i < winX1; i++) {
					for (j = winY0; j < winY1; j++) {
						valueRGB = rgbArray[width * j + i];
						R += getRedValue(valueRGB);
						G += getGreenValue(valueRGB);
						B += getBlueValue(valueRGB);
					}
				}
				n = (winX1 - winX0) * (winY1 - winY0);
				R = (int) (((double) R) / n + alpha);
				G = (int) (((double) G) / n + alpha);
				B = (int) (((double) B) / n + alpha);
				valueRGB = combineRGB(clip((int) R), clip((int) G), clip((int) B));
				bfImg.setRGB(x, y, valueRGB);
			}
		}
		return bfImg;
	}

	private int clip(int x) {
		if (x < 0)
			return 0;
		if (x > 255)
			return 255;
		return x;
	}

	private int getRedValue(int rgbValue) {
		int temp = rgbValue & 0x00ff0000;
		return temp >> 16;
	}

	private int getGreenValue(int rgbValue) {
		int temp = rgbValue & 0x0000ff00;
		return temp >> 8;
	}

	private int getBlueValue(int rgbValue) {
		return rgbValue & 0x000000ff;
	}

	private int combineRGB(int redValue, int greenValue, int blueValue) {

		return (redValue << 16) + (greenValue << 8) + blueValue;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getZoomWidth() {
		return zoomWidth;
	}

	public void setZoomWidth(int zoomWidth) {
		this.zoomWidth = zoomWidth;
	}

	public int getZoomHeight() {
		return zoomHeight;
	}

	public void setZoomHeight(int zoomHeight) {
		this.zoomHeight = zoomHeight;
	}

	public BufferedImage getSrcBufferImage() {
		return srcBufferImage;
	}

	public void setSrcBufferImage(BufferedImage srcBufferImage) {
		this.srcBufferImage = srcBufferImage;
	}

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		String filename = "./testImage/HowardSprings_NT.jpg";
		ImageScale.resizeFix(new File(filename), new File("./testImage/big-n.jpg"), 250, 300);
		long end = System.currentTimeMillis();
		System.out.println("success:" + (end - start));
	}

}
