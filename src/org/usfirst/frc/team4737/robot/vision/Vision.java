package org.usfirst.frc.team4737.robot.vision;

import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.vision.components.GamePiece;
import org.usfirst.frc.team4737.robot.vision.components.Rect4i;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.MeasurementType;
import com.ni.vision.NIVision.ParticleFilterCriteria2;
import com.ni.vision.NIVision.Rect;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

public class Vision {

	public GamePiece[] currentPieces;
	
	private ColorImage image;

	ParticleFilterCriteria2[] pfcRect; // Criteria for rectangles
	ParticleFilterCriteria2[] pfcBin; // Criteria for rectangles

	public Vision() {
		pfcRect = new ParticleFilterCriteria2[] {
				new ParticleFilterCriteria2(MeasurementType.MT_BOUNDING_RECT_WIDTH, 30, 400, 0, 0), // TODO tune
				new ParticleFilterCriteria2(MeasurementType.MT_BOUNDING_RECT_HEIGHT, 30, 400, 0, 0), };
		pfcBin = new ParticleFilterCriteria2[] {
				new ParticleFilterCriteria2(MeasurementType.MT_BOUNDING_RECT_WIDTH, 30, 400, 0, 0), // TODO tune
				new ParticleFilterCriteria2(MeasurementType.MT_BOUNDING_RECT_HEIGHT, 30, 400, 0, 0), }; 
	}
	
	public void update(Robot robot) {
		ColorImage prev = image;
		robot.camera.getImage(image);
		if (image.equals(prev)) {
			// camera hasn't updated
		} else {
			// Update found game pieces
			
			
		}
	}
	
	public void testRects(ColorImage image) {
		try {
			Rect4i[] reports = getRects(image, ColorThreshold.WHITE);

			for (Rect4i r : reports) {
				NIVision.imaqDrawShapeOnImage(image.image, image.image, new Rect(r.x, r.y, r.w, r.h),
						DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 1.0f);
			}
		} catch (NIVisionException ex) {
			ex.printStackTrace();
		}
	}

	/*public void findTotes(AxisCamera camera) {
		try {
			camera.getImage(image);

			// ParticleAnalysisReport[] reports = getParticles(image, ColorThreshold.WHITE);
			// for (int i = 0; i < reports.length; i++) { // print results
			// ParticleAnalysisReport r = reports[i];
			// System.out.println("Particle: " + i + ":  Center of mass x: " + r.center_mass_x);
			// }

			Rect4i[] reports = getRects(image, ColorThreshold.WHITE);

			for (Rect4i r : reports) {
				NIVision.imaqDrawShapeOnImage(image.image, image.image, new Rect(r.x, r.y, r.w, r.h),
						DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 1.0f);
			}

			image.free();

		} catch (NIVisionException ex) {
			ex.printStackTrace();
		}
	}*/

	private Rect4i[] getRects(ColorImage image, ColorThreshold thresh) throws NIVisionException {
		ParticleAnalysisReport[] reports = getParticles(image, thresh, pfcRect);

		Rect4i[] rects = new Rect4i[reports.length];

		for (int i = 0; i < reports.length; i++) {
			rects[i] = new Rect4i(reports[i].boundingRectLeft, reports[i].boundingRectTop,
					reports[i].boundingRectWidth, reports[i].boundingRectHeight);
		}

		return rects;
	}

	private ParticleAnalysisReport[] getParticles(ColorImage image, ColorThreshold thresh, ParticleFilterCriteria2[] criteria) throws NIVisionException {
		BinaryImage thresholdImage = image.thresholdRGB(thresh.redLow, thresh.redHigh, thresh.greenLow,
				thresh.greenHigh, thresh.blueLow, thresh.blueHigh); // keep only white objects
		BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(false, 2); // remove small artifacts
		BinaryImage convexHullImage = bigObjectsImage.convexHull(false); // fill in occluded rectangles
		BinaryImage filteredImage = convexHullImage.particleFilter(criteria); // find filled in rectangles

		ParticleAnalysisReport[] reports = filteredImage.getOrderedParticleAnalysisReports(); // get list of results

		filteredImage.free();
		convexHullImage.free();
		bigObjectsImage.free();
		thresholdImage.free();

		return reports;
	}

}
