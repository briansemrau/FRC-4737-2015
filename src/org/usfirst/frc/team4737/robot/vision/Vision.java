package org.usfirst.frc.team4737.robot.vision;

import com.ni.vision.NIVision.MeasurementType;
import com.ni.vision.NIVision.ParticleFilterCriteria2;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.image.RGBImage;
import edu.wpi.first.wpilibj.vision.AxisCamera;

public class Vision {

	private ColorImage image;

	ParticleFilterCriteria2[] pfc; // the criteria for doing the particle filter operation

	public void robotInit() {
		pfc = new ParticleFilterCriteria2[] {
				new ParticleFilterCriteria2(MeasurementType.MT_BOUNDING_RECT_WIDTH, 30, 400, 0, 0),
				new ParticleFilterCriteria2(MeasurementType.MT_BOUNDING_RECT_HEIGHT, 40, 400, 0, 0), };
	}

	public void findTotes(AxisCamera camera) {
		try {
			// ColorImage image = camera.getImage(); // comment if using stored images
			ColorImage image; // next 2 lines read test image
			image = new RGBImage("test/gray.png");

			ParticleAnalysisReport[] reports = getParticles(image, ColorThreshold.WHITE);
			
			for (int i = 0; i < reports.length; i++) { // print results
				ParticleAnalysisReport r = reports[i];
				System.out.println("Particle: " + i + ":  Center of mass x: " + r.center_mass_x);
			}
			
			image.free();

		} catch (NIVisionException ex) {
			ex.printStackTrace();
		}
	}
	
	private int[][] getRects(ColorImage image, ColorThreshold thresh) throws NIVisionException {
		ParticleAnalysisReport[] reports = getParticles(image, thresh);
		
		int[][] rects = new int[reports.length][];
		
		for (int i = 0; i < reports.length; i++) {
			rects[i] = new int[] {
					reports[i].boundingRectLeft,
					reports[i].boundingRectTop,
					reports[i].boundingRectWidth,
					reports[i].boundingRectHeight,
			};
		}
		
		return rects;
	}
	
	private ParticleAnalysisReport[] getParticles(ColorImage image, ColorThreshold thresh) throws NIVisionException {
		BinaryImage thresholdImage = image.thresholdRGB(thresh.redLow, thresh.redHigh, thresh.greenLow, thresh.greenHigh, thresh.blueLow, thresh.blueHigh); // keep only white objects
		BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(false, 2); // remove small artifacts
		BinaryImage convexHullImage = bigObjectsImage.convexHull(false); // fill in occluded rectangles
		BinaryImage filteredImage = convexHullImage.particleFilter(pfc); // find filled in rectangles
		
		ParticleAnalysisReport[] reports = filteredImage.getOrderedParticleAnalysisReports(); // get list of results
		
		filteredImage.free();
		convexHullImage.free();
		bigObjectsImage.free();
		thresholdImage.free();
		
		return reports;
	}

}
