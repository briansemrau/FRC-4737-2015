package org.usfirst.frc.team4737.robot.vision;

import org.usfirst.frc.team4737.robot.Log;
import org.usfirst.frc.team4737.robot.vision.components.Bin;
import org.usfirst.frc.team4737.robot.vision.components.GamePiece;
import org.usfirst.frc.team4737.robot.vision.components.L;
import org.usfirst.frc.team4737.robot.vision.components.LGroup;
import org.usfirst.frc.team4737.robot.vision.components.Rect4i;
import org.usfirst.frc.team4737.robot.vision.components.Tote;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.MeasurementType;
import com.ni.vision.NIVision.ParticleFilterCriteria2;
import com.ni.vision.NIVision.RGBValue;
import com.ni.vision.NIVision.Rect;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.image.RGBImage;
import edu.wpi.first.wpilibj.vision.AxisCamera;

public class Vision {

	public GamePiece[] foundTotes;
	public GamePiece[] foundBins;

	private ColorImage image;

	ParticleFilterCriteria2[] pfcRect; // Criteria for rectangles
	ParticleFilterCriteria2[] pfcBin; // Criteria for rectangles

	Joystick debug;

	public Vision() {
		Log.println("Initializing vision...");
		try {
			image = new RGBImage();
		} catch (NIVisionException e) {
			e.printStackTrace();
		}
		pfcRect = new ParticleFilterCriteria2[] {
				new ParticleFilterCriteria2(MeasurementType.MT_BOUNDING_RECT_WIDTH, 30, 400, 0, 0), // TODO tune
				new ParticleFilterCriteria2(MeasurementType.MT_BOUNDING_RECT_HEIGHT, 30, 400, 0, 0), };
		pfcBin = new ParticleFilterCriteria2[] {
				new ParticleFilterCriteria2(MeasurementType.MT_BOUNDING_RECT_WIDTH, 30, 400, 0, 0), // TODO tune
				new ParticleFilterCriteria2(MeasurementType.MT_BOUNDING_RECT_HEIGHT, 30, 400, 0, 0), };
		foundTotes = new GamePiece[0];
		foundBins = new GamePiece[0];
		debug = new Joystick(0);
		Log.println("Initialized vision.");
	}

	public void update(AxisCamera camera) {
		camera.getImage(image);
		{
			// Update found game pieces
			try {
				ColorImage output = image;

				int d = (int) (debug.getRawAxis(Joystick.AxisType.kZ.value) * 255);
				Log.println("Vision: debug value=" + d);
				Rect4i[] whiteRects = getRects(image, new ColorThreshold(d, 255, d, 255, d, 255));
				Rect4i[] greenRects = getRects(image, ColorThreshold.GREEN); // ColorThreshold.BIN

				Tote[] totes = Tote.createTotes(LGroup.findGroups(L.findLs(whiteRects)));
				Bin[] bins = new Bin[0];// Bin.getBins(BinShape.findShapes(greenRects));

				// Record found pieces
				this.foundTotes = totes;
				this.foundBins = bins;

				// Draw rectangles on image
				for (int i = 0; i < whiteRects.length; i++) {
					Rect rect = new Rect(whiteRects[i].y, whiteRects[i].x, whiteRects[i].h, whiteRects[i].w);
					NIVision.imaqSetToolColor(new RGBValue(255, 255, 255, 0));
					NIVision.imaqDrawShapeOnImage(output.image, image.image, rect, DrawMode.DRAW_VALUE,
							ShapeMode.SHAPE_RECT, 1.0f);
				}

				for (int i = 0; i < greenRects.length; i++) {
					Rect rect = new Rect(greenRects[i].y, greenRects[i].x, greenRects[i].h, greenRects[i].w);
					NIVision.imaqSetToolColor(new RGBValue(0, 255, 0, 0));
					NIVision.imaqDrawShapeOnImage(output.image, image.image, rect, DrawMode.DRAW_VALUE,
							ShapeMode.SHAPE_RECT, 1.0f);
				}

				// Output to driver station
				CameraServer.getInstance().setImage(output.image);

				output.free();

			} catch (NIVisionException e) {
				e.printStackTrace();
			}
		}
	}

	public void testRects(ColorImage image) {
		try {
			Rect4i[] reports = getRects(image, ColorThreshold.WHITE);

			for (Rect4i r : reports) {
				NIVision.imaqDrawShapeOnImage(image.image, image.image, new Rect(r.y, r.x, r.h, r.w),
						DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 1.0f);
			}
		} catch (NIVisionException ex) {
			ex.printStackTrace();
		}
	}

	private Rect4i[] getRects(ColorImage image, ColorThreshold thresh) throws NIVisionException {
		ParticleAnalysisReport[] reports = getParticles(image, thresh, pfcRect);
		Rect4i[] rects = new Rect4i[reports.length];

		for (int i = 0; i < reports.length; i++) {
			rects[i] = new Rect4i(reports[i].boundingRectLeft, reports[i].boundingRectTop,
					reports[i].boundingRectWidth, reports[i].boundingRectHeight);
		}

		return rects;
	}

	private ParticleAnalysisReport[] getParticles(ColorImage image, ColorThreshold thresh,
			ParticleFilterCriteria2[] criteria) throws NIVisionException {
		BinaryImage thresholdImage = image.thresholdRGB(thresh.redLow, thresh.redHigh, thresh.greenLow,
				thresh.greenHigh, thresh.blueLow, thresh.blueHigh); // keep only white objects
		BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(false, 5); // remove small artifacts
		BinaryImage convexHullImage = bigObjectsImage.convexHull(false); // fill in occluded rectangles
		BinaryImage filteredImage = convexHullImage.particleFilter(criteria); // find filled in rectangles
		BinaryImage img = image
				.thresholdRGB(thresh.redLow, thresh.redHigh, thresh.greenLow, thresh.greenHigh, thresh.blueLow,
						thresh.blueHigh).removeSmallObjects(false, 2).convexHull(false).particleFilter(criteria);

		ParticleAnalysisReport[] reports = img.getOrderedParticleAnalysisReports(); // get list of results

		img.free();
		filteredImage.free();
		convexHullImage.free();
		bigObjectsImage.free();
		thresholdImage.free();

		return reports;
	}

}
