package org.squirrelsql.session.graph;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

public class ArrowDrawer
{
   private static Rotate createRotation(double angle, double px, double py)
   {
      double angleInDeg = 360 / (2 * Math.PI) * angle;
      return new Rotate( angleInDeg, px, py);
   }

   public static void drawArrow(GraphicsContext gc, double angle, double x, double y)
   {
      Rotate r = createRotation(angle, 0, 0);
      gc.save();
      gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());

      Point2D p = createRotation(-angle,0,0).transform(x, y);

      gc.drawImage(GraphConstants.ARROW_RIGHT_IMAGE, p.getX() - GraphConstants.IMAGE_WIDTH, p.getY() - GraphConstants.IMAGE_HEIGHT / 2.0);

      //gc.fillOval(p.getX() - 2, p.getY() - 2, 4, 4);

      gc.restore();
   }
}